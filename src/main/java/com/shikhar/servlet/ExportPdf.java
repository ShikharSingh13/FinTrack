package com.shikhar.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import com.shikhar.HbUtility;
import com.shikhar.PdfGenerator;
import com.shikhar.entity.Budget;
import com.shikhar.entity.Expense;
import com.shikhar.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ExportPdf")
public class ExportPdf extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ==========================================
		// 1. CHECK USER SESSION
		// ==========================================

		HttpSession session = request.getSession(false);

		if (session == null) {
			response.sendRedirect("signin.jsp");
			return;
		}

		User user = (User) session.getAttribute("User");

		if (user == null) {
			response.sendRedirect("signin.jsp");
			return;
		}

		// ==========================================
		// 2. GET MONTH AND YEAR
		// ==========================================

		String monthParameter = request.getParameter("month");

		String yearParameter = request.getParameter("year");

		LocalDate today = LocalDate.now();

		int month;
		int year;

		try {

			if (monthParameter == null || monthParameter.isEmpty()) {

				month = today.getMonthValue();

			} else {

				month = Integer.parseInt(monthParameter);
			}

			if (yearParameter == null || yearParameter.isEmpty()) {

				year = today.getYear();

			} else {

				year = Integer.parseInt(yearParameter);
			}

		} catch (NumberFormatException e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid month or year.");

			return;
		}

		// ==========================================
		// 3. VALIDATE MONTH
		// ==========================================

		if (month < 1 || month > 12) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid month.");

			return;
		}

		Session hibernateSession = null;

		try {

			// ==========================================
			// 4. OPEN HIBERNATE SESSION
			// ==========================================

			hibernateSession = HbUtility.sessionFactory.openSession();

			// ==========================================
			// 5. CREATE DATE RANGE
			// ==========================================

			LocalDate startDate = LocalDate.of(year, month, 1);

			LocalDate endDate = startDate.plusMonths(1);

			// ==========================================
			// 6. FETCH USER'S EXPENSES
			// ==========================================

			String expenseHql = "FROM Expense e " + "WHERE e.user.email = :email " + "AND e.date >= :startDate "
					+ "AND e.date < :endDate " + "ORDER BY e.date ASC, e.time ASC";

			List<Expense> expenses = hibernateSession.createQuery(expenseHql, Expense.class)
					.setParameter("email", user.getEmail()).setParameter("startDate", startDate)
					.setParameter("endDate", endDate).getResultList();

			// ==========================================
			// 7. FETCH MONTHLY BUDGET
			// ==========================================

			String budgetHql = "FROM Budget b " + "WHERE b.user.email = :email " + "AND b.month = :month "
					+ "AND b.year = :year";

			List<Budget> budgets = hibernateSession.createQuery(budgetHql, Budget.class)
					.setParameter("email", user.getEmail()).setParameter("month", month).setParameter("year", year)
					.getResultList();

			Budget budget = null;

			if (!budgets.isEmpty()) {
				budget = budgets.get(0);
			}

			// ==========================================
			// 8. SET PDF RESPONSE
			// ==========================================

			response.setContentType("application/pdf");

			String monthName = java.time.Month.of(month).toString();

			response.setHeader("Content-Disposition",
					"attachment; filename=\"Expense_Report_" + monthName + "_" + year + ".pdf\"");

			// ==========================================
			// 9. CREATE PDF DOCUMENT
			// ==========================================

			Document document = new Document();

			PdfWriter.getInstance(document, response.getOutputStream());

			// ==========================================
			// 10. OPEN PDF
			// ==========================================

			document.open();

			// ==========================================
			// 11. GENERATE REPORT
			// ==========================================

			PdfGenerator.generateReport(document, user, month, year, expenses, budget);

			// ==========================================
			// 12. CLOSE PDF
			// ==========================================

			document.close();

		} catch (Exception e) {

			e.printStackTrace();

			if (!response.isCommitted()) {

				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to generate PDF report.");
			}

		} finally {

			// ==========================================
			// 13. CLOSE HIBERNATE SESSION
			// ==========================================

			if (hibernateSession != null && hibernateSession.isOpen()) {

				hibernateSession.close();
			}
		}
	}
}