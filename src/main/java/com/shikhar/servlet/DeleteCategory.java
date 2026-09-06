package com.shikhar.servlet;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.shikhar.HbUtility;
import com.shikhar.entity.Category;
import com.shikhar.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/DeleteCategory")
public class DeleteCategory extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession ses = request.getSession(false);

		if (ses == null || ses.getAttribute("User") == null) {

			response.sendRedirect("signin.jsp");
			return;
		}

		User user = (User) ses.getAttribute("User");

		int id = Integer.parseInt(request.getParameter("id"));

		Session session = HbUtility.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		try {

			Category category = session.get(Category.class, id);

			if (category == null || !category.getUser().getEmail().equals(user.getEmail())) {

				tx.rollback();

				ses.setAttribute("msg", "Category not found.");
				ses.setAttribute("msgType", "danger");

				response.sendRedirect("ManageCategory");
				return;
			}

			// Usage validation will be added here

			Long expenseCount = session
					.createQuery("SELECT COUNT(e.id) FROM Expense e WHERE e.category.id=:id", Long.class)
					.setParameter("id", id).uniqueResult();

			if (expenseCount > 0) {

				tx.rollback();

				ses.setAttribute("msg", "Cannot delete category. It is used by " + expenseCount + " expenses.");

				ses.setAttribute("msgType", "warning");

				response.sendRedirect("ManageCategory");
				return;
			}

			session.remove(category);

			tx.commit();

			ses.setAttribute("msg", "Category deleted successfully.");
			ses.setAttribute("msgType", "success");

		} catch (Exception e) {

			if (tx != null) {
				tx.rollback();
			}

			ses.setAttribute("msg", "Unable to delete category.");
			ses.setAttribute("msgType", "danger");

			e.printStackTrace();

		} finally {

			session.close();
		}

		response.sendRedirect("ManageCategory");

	}
}