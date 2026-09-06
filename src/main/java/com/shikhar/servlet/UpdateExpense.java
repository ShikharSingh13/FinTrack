package com.shikhar.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.shikhar.HbUtility;
import com.shikhar.entity.Expense;
import com.shikhar.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/UpdateExpense")
public class UpdateExpense extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession httpSession = request.getSession(false);

		if (httpSession == null || httpSession.getAttribute("User") == null) {

			response.sendRedirect("signin.jsp");
			return;
		}

		User user = (User) httpSession.getAttribute("User");

		int id = Integer.parseInt(request.getParameter("id"));

		String title = request.getParameter("title");
		LocalDate date = LocalDate.parse(request.getParameter("date"));
		LocalTime time = LocalTime.parse(request.getParameter("time"));
		String description = request.getParameter("description");
		double price = Double.parseDouble(request.getParameter("price"));

		Session session = HbUtility.sessionFactory.openSession();

		Expense expense = session.get(Expense.class, id);

		// Security Check
		if (expense == null || !expense.getUser().getEmail().equals(user.getEmail())) {

			session.close();

			httpSession.setAttribute("msg", "Invalid Expense");
			httpSession.setAttribute("msgType", "danger");

			response.sendRedirect("ViewExpense.jsp");
			return;
		}

		Transaction txn= session.beginTransaction();

		expense.setTitle(title);
		expense.setDate(date);
		expense.setTime(time);
		expense.setDescription(description);
		expense.setPrice(price);



		txn.commit();

		session.close();

		httpSession.setAttribute("msg", "Expense Updated Successfully");
		httpSession.setAttribute("msgType", "success");

		response.sendRedirect("ViewExpense.jsp");
	}
}