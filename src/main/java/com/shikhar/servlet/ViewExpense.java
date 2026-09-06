package com.shikhar.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.shikhar.HbUtility;
import com.shikhar.entity.Category;
import com.shikhar.entity.Expense;
import com.shikhar.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ViewExpense")
public class ViewExpense extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession httpSession = request.getSession(false);

		if (httpSession == null || httpSession.getAttribute("User") == null) {
			response.sendRedirect("signin.jsp");
			return;
		}

		User user = (User) httpSession.getAttribute("User");
		String filter = request.getParameter("filter");

		Session session = HbUtility.sessionFactory.openSession();

		String hql = "FROM Expense WHERE user=:user";

		if ("today".equals(filter)) {
			hql += " AND date=:today";
		} else if ("month".equals(filter)) {
			hql += " AND date BETWEEN :startDate AND :endDate";
		}

		hql += " ORDER BY date DESC,time DESC";

		Query<Expense> query = session.createQuery(hql, Expense.class);

		query.setParameter("user", user);

		if ("today".equals(filter)) {
			query.setParameter("today", LocalDate.now());
		}

		else if ("month".equals(filter)) {

			YearMonth currentMonth = YearMonth.now();

			query.setParameter("startDate", currentMonth.atDay(1));

			query.setParameter("endDate", currentMonth.atEndOfMonth());
		}

		List<Expense> expenses = query.getResultList();

		List<Category> categories = session
				.createQuery("FROM Category WHERE user.email=:email ORDER BY name", Category.class)
				.setParameter("email", user.getEmail()).getResultList();
		request.setAttribute("categories", categories);

		session.close();

		request.setAttribute("expenses", expenses);

		String pageTitle = "All Expenses";

		if ("today".equals(filter)) {
			pageTitle = "Today's Expenses";
		} else if ("month".equals(filter)) {
			pageTitle = "This Month's Expenses";
		}

		request.setAttribute("pageTitle", pageTitle);
		request.setAttribute("filter", filter);

		request.getRequestDispatcher("ViewExpense.jsp").forward(request, response);

	}

}