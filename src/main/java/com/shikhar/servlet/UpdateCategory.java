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

@WebServlet("/UpdateCategory")
public class UpdateCategory extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession ses = request.getSession(false);

		if (ses == null || ses.getAttribute("User") == null) {
			response.sendRedirect("signin.jsp");
			return;
		}

		User user = (User) ses.getAttribute("User");

		int id = Integer.parseInt(request.getParameter("id"));

		String categoryName = request.getParameter("categoryName");

		if (categoryName == null) {
			categoryName = "";
		}

		categoryName = categoryName.trim();

		if (categoryName.isEmpty()) {

			ses.setAttribute("msg", "Category name cannot be empty.");
			ses.setAttribute("msgType", "danger");

			response.sendRedirect("ManageCategory?id=" + id);
			return;
		}

		Session session = HbUtility.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		try {

			Category category = session.get(Category.class, id);

			// Security Check
			if (category == null || !category.getUser().getEmail().equals(user.getEmail())) {

				tx.rollback();

				ses.setAttribute("msg", "Category not found.");
				ses.setAttribute("msgType", "danger");

				response.sendRedirect("ManageCategory");
				return;
			}

			// Duplicate Check
			String hql = "FROM Category WHERE lower(name)=:name "
					+ "AND user.email=:email AND id<>:id";

			Category existingCategory = session.createQuery(hql, Category.class)
					.setParameter("name", categoryName.toLowerCase())
					.setParameter("email", user.getEmail())
					.setParameter("id", id)
					.uniqueResult();

			if (existingCategory != null) {

				tx.rollback();

				ses.setAttribute("msg", "Category already exists.");
				ses.setAttribute("msgType", "warning");

				response.sendRedirect("ManageCategory?id=" + id);
				return;
			}

			category.setName(categoryName);

			session.merge(category);

			tx.commit();

			ses.setAttribute("msg", "Category updated successfully.");
			ses.setAttribute("msgType", "success");

		} catch (Exception e) {

			if (tx != null) {
				tx.rollback();
			}

			ses.setAttribute("msg", "Unable to update category.");
			ses.setAttribute("msgType", "danger");

			e.printStackTrace();

		} finally {

			session.close();
		}

		response.sendRedirect("ManageCategory");
	}
}