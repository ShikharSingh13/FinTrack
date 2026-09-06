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

@WebServlet("/AddCategory")
public class AddCategory extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession httpSession = request.getSession();

		User user = (User) httpSession.getAttribute("User");

		if (user == null) {
			response.sendRedirect("signin.jsp");
			return;
		}

		String categoryName = request.getParameter("categoryName");

		if (categoryName == null) {
			categoryName = "";
		}

		categoryName = categoryName.trim();

		// Check for empty category
		if (categoryName.isEmpty()) {
			httpSession.setAttribute("msg", "Category name cannot be empty.");
			httpSession.setAttribute("msgType", "danger");
			response.sendRedirect("ManageCategory");
			return;
		}

		Session session = HbUtility.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		try {

			// Check if category already exists for this user
			String hql = "FROM Category WHERE lower(name)=:name AND user.email=:email";

			Category existingCategory = session.createQuery(hql, Category.class)
					.setParameter("name", categoryName.toLowerCase())
					.setParameter("email", user.getEmail())
					.uniqueResult();

			if (existingCategory != null) {

				tx.rollback();

				httpSession.setAttribute("msg", "Category already exists.");
				httpSession.setAttribute("msgType", "warning");

				response.sendRedirect("ManageCategory");

				return;
			}

			Category category = new Category();
			category.setName(categoryName);
			category.setUser(user);

			session.persist(category);

			tx.commit();

			httpSession.setAttribute("msg", "Category added successfully.");
			httpSession.setAttribute("msgType", "success");

		} catch (Exception e) {

			if (tx != null) {
				tx.rollback();
			}

			httpSession.setAttribute("msg", "Unable to add category.");
			httpSession.setAttribute("msgType", "danger");

			e.printStackTrace();

		} finally {

			session.close();

		}

		response.sendRedirect("ManageCategory");
	}
}