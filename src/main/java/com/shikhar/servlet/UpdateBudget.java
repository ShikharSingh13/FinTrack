package com.shikhar.servlet;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.shikhar.HbUtility;
import com.shikhar.entity.Budget;
import com.shikhar.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/UpdateBudget")
public class UpdateBudget extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession ses = request.getSession(false);

		if (ses == null || ses.getAttribute("User") == null) {

			HttpSession newSession = request.getSession(true);

			newSession.setAttribute("msg", "Please login first!");
			newSession.setAttribute("msgType", "danger");

			response.sendRedirect("signin.jsp");

			return;
		}

		User user = (User) ses.getAttribute("User");

		int id = Integer.parseInt(request.getParameter("id"));

		int month = Integer.parseInt(request.getParameter("month"));

		int year = Integer.parseInt(request.getParameter("year"));

		double amount = Double.parseDouble(request.getParameter("amount"));

		Session session = HbUtility.sessionFactory.openSession();

		Transaction tx = session.beginTransaction();

		try {

			Budget budget = session.get(Budget.class, id);

			String hql = "FROM Budget WHERE month=:month AND year=:year AND user.email=:email AND id!=:id";

			Budget existingBudget = session.createQuery(hql, Budget.class).setParameter("month", month)
					.setParameter("year", year).setParameter("email", user.getEmail()).setParameter("id", id)
					.uniqueResult();

			if (existingBudget != null) {

				tx.rollback();

				ses.setAttribute("msg", "Budget already exists for this month.");

				ses.setAttribute("msgType", "warning");

				response.sendRedirect("ManageBudget");

				return;

			}

			if (budget != null && budget.getUser().getEmail().equals(user.getEmail())) {

				budget.setMonth(month);

				budget.setYear(year);

				budget.setAmount(amount);

				session.merge(budget);

				tx.commit();

				ses.setAttribute("msg", "Budget updated successfully.");

				ses.setAttribute("msgType", "success");

			} else {

				tx.rollback();

				ses.setAttribute("msg", "Invalid budget.");

				ses.setAttribute("msgType", "danger");

			}

		} catch (Exception e) {

			if (tx != null) {

				tx.rollback();

			}

			ses.setAttribute("msg", "Unable to update budget.");

			ses.setAttribute("msgType", "danger");

			e.printStackTrace();

		} finally {

			session.close();

		}

		response.sendRedirect("ManageBudget");

	}

}