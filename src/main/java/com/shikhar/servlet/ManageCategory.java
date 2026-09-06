package com.shikhar.servlet;

import java.io.IOException;
import java.util.List;

import org.hibernate.Session;

import com.shikhar.HbUtility;
import com.shikhar.entity.Category;
import com.shikhar.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ManageCategory")
public class ManageCategory extends HttpServlet {

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

		Session session = HbUtility.sessionFactory.openSession();

		// Fetch all categories
		String hql = "FROM Category WHERE user.email=:email ORDER BY id DESC";

		List<Category> categories = session.createQuery(hql, Category.class)
		        .setParameter("email", user.getEmail())
		        .getResultList();

		for(Category c : categories){

		    Long count = session.createQuery(
		            "SELECT COUNT(e) FROM Expense e WHERE e.category.id=:id",
		            Long.class)
		            .setParameter("id", c.getId())
		            .uniqueResult();

		    request.setAttribute("count_" + c.getId(), count);
		}
		// For Edit Mode
		Category editCategory = null;

		String id = request.getParameter("id");

		if (id != null) {

			editCategory = session.get(Category.class, Integer.parseInt(id));

			if (editCategory != null &&
				!editCategory.getUser().getEmail().equals(user.getEmail())) {

				editCategory = null;
			}
		}

		session.close();

		request.setAttribute("categories", categories);
		request.setAttribute("editCategory", editCategory);

		request.getRequestDispatcher("Category.jsp").forward(request, response);
	}
}