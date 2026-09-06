package com.shikhar.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.shikhar.HbUtility;
import com.shikhar.entity.Budget;
import com.shikhar.entity.Expense;
import com.shikhar.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession httpSession = request.getSession(false);

		// User Login Check
		if (httpSession == null || httpSession.getAttribute("User") == null) {
			response.sendRedirect("signin.jsp");
			return;
		}

		User user = (User) httpSession.getAttribute("User");

		Session session = HbUtility.sessionFactory.openSession();

		LocalDate today = LocalDate.now();

		YearMonth currentMonth = YearMonth.now();

		LocalDate firstDay = currentMonth.atDay(1);

		LocalDate lastDay = currentMonth.atEndOfMonth();

		int currentMonthValue = currentMonth.getMonthValue();
		int currentYear = currentMonth.getYear();

		double totalExpense = 0;
		double todayExpense = 0;
		double monthExpense = 0;
		long totalRecords = 0;

		double monthlyBudget = 0;
		double remainingBudget = 0;

		// ================= Total Expense =================

		Query<Double> totalQuery = session.createQuery("select sum(price) from Expense where user=:user", Double.class);

		totalQuery.setParameter("user", user);

		Double total = totalQuery.uniqueResult();

		if (total != null) {
			totalExpense = total;
		}

		// ================= Today's Expense =================

		Query<Double> todayQuery = session
				.createQuery("select sum(price) from Expense where user=:user and date=:today", Double.class);

		todayQuery.setParameter("user", user);
		todayQuery.setParameter("today", today);

		Double todayTotal = todayQuery.uniqueResult();

		if (todayTotal != null) {
			todayExpense = todayTotal;
		}

		// ================= This Month Expense =================

		Query<Double> monthQuery = session.createQuery(
				"select sum(price) from Expense where user=:user and date between :start and :end", Double.class);

		monthQuery.setParameter("user", user);
		monthQuery.setParameter("start", firstDay);
		monthQuery.setParameter("end", lastDay);

		Double monthTotal = monthQuery.uniqueResult();

		if (monthTotal != null) {
			monthExpense = monthTotal;
		}

		// ================= Total Records =================

		Query<Long> countQuery = session.createQuery("select count(*) from Expense where user=:user", Long.class);

		countQuery.setParameter("user", user);

		Long count = countQuery.uniqueResult();

		if (count != null) {
			totalRecords = count;
		}

		// ================= Recent Expenses (Last 7 Days) =================

		LocalDate weekAgo = LocalDate.now().minusDays(7);

		Query<Expense> recentQuery = session.createQuery(
				"from Expense where user=:user and date>=:weekAgo order by date desc,time desc", Expense.class);

		recentQuery.setParameter("user", user);
		recentQuery.setParameter("weekAgo", weekAgo);

		List<Expense> recentExpenses = recentQuery.list();

		// ================= Expense Analytics =================

		// Fetch all expenses of logged-in user
		Query<Expense> analyticsQuery = session.createQuery("from Expense where user=:user order by date asc",
				Expense.class);

		analyticsQuery.setParameter("user", user);

		List<Expense> allExpenses = analyticsQuery.list();

		// ---------- Category Wise Expense ----------

		Map<String, Double> categoryExpenses = new LinkedHashMap<>();

		// ---------- Monthly Expense - Last 6 Months ----------

		Map<String, Double> monthlyExpenses = new LinkedHashMap<>();

		YearMonth startMonth = currentMonth.minusMonths(5);

		for (int i = 0; i < 6; i++) {

			YearMonth month = startMonth.plusMonths(i);

			String monthName = month.getMonth().toString();

			monthlyExpenses.put(monthName, 0.0);
		}

		// ---------- Average & Highest Expense ----------

		double averageExpense = 0;
		double highestExpense = 0;

		double analyticsTotalExpense = 0;

		// Process expenses
		for (Expense expense : allExpenses) {

			double price = expense.getPrice();

			analyticsTotalExpense += price;

			// Category-wise
			String categoryName = expense.getCategory().getName();

			categoryExpenses.put(categoryName, categoryExpenses.getOrDefault(categoryName, 0.0) + price);

			// Last 6 months
			YearMonth expenseMonth = YearMonth.from(expense.getDate());

			if (!expenseMonth.isBefore(startMonth) && !expenseMonth.isAfter(currentMonth)) {

				String monthName = expenseMonth.getMonth().toString();

				monthlyExpenses.put(monthName, monthlyExpenses.getOrDefault(monthName, 0.0) + price);
			}

			// Highest expense
			if (price > highestExpense) {
				highestExpense = price;
			}
		}

		// Average expense
		if (!allExpenses.isEmpty()) {
			averageExpense = analyticsTotalExpense / allExpenses.size();
		}

		// ================= Current Month Budget =================

		Query<Budget> budgetQuery = session.createQuery("from Budget where user=:user and month=:month and year=:year",
				Budget.class);

		budgetQuery.setParameter("user", user);
		budgetQuery.setParameter("month", currentMonthValue);
		budgetQuery.setParameter("year", currentYear);

		Budget budget = budgetQuery.uniqueResult();

		if (budget != null) {
			monthlyBudget = budget.getAmount();
		}

		// Calculate Remaining Budget
		remainingBudget = monthlyBudget - monthExpense;

		// Close Hibernate Session
		session.close();

		// Send data to UserHome.jsp

		request.setAttribute("totalExpense", totalExpense);
		request.setAttribute("todayExpense", todayExpense);
		request.setAttribute("monthExpense", monthExpense);
		request.setAttribute("totalRecords", totalRecords);

		request.setAttribute("monthlyBudget", monthlyBudget);
		request.setAttribute("remainingBudget", remainingBudget);

		request.setAttribute("recentExpenses", recentExpenses);

		// Analytics data
		request.setAttribute("categoryExpenses", categoryExpenses);
		request.setAttribute("monthlyExpenses", monthlyExpenses);
		request.setAttribute("averageExpense", averageExpense);
		request.setAttribute("highestExpense", highestExpense);

		// ================= Page Forwarding =================

		String view = request.getParameter("view");

		if ("analytics".equals(view)) {

		    request.getRequestDispatcher("ExpenseAnalytics.jsp")
		           .forward(request, response);

		} else {

		    request.getRequestDispatcher("UserHome.jsp")
		           .forward(request, response);
		}

	}

}