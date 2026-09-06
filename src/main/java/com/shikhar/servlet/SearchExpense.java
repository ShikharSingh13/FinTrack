package com.shikhar.servlet;

import java.io.IOException;
import java.time.LocalDate;
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

/**
 * Servlet implementation class SearchExpense
 */
@WebServlet("/SearchExpense")
public class SearchExpense extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public SearchExpense() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession httpSession = request.getSession(false);

		if (httpSession == null || httpSession.getAttribute("User") == null) {
		    response.sendRedirect("signin.jsp");
		    return;
		}

		User user = (User) httpSession.getAttribute("User");

		String categoryId=request.getParameter("categoryId");
		String date = request.getParameter("date");

		Session session = HbUtility.sessionFactory.openSession();

		List<Expense> expenses;

		String hql = "FROM Expense WHERE user=:user";

		if(categoryId != null && !categoryId.isEmpty()){
		    hql += " AND category.id=:categoryId";
		}

		if(date != null && !date.isEmpty()){
		    hql += " AND date=:date";
		}

		hql += " ORDER BY date DESC,time DESC";

		Query<Expense> query = session.createQuery(hql, Expense.class);

		query.setParameter("user", user);

		if(categoryId != null && !categoryId.isEmpty()){
		    query.setParameter("categoryId",
		            Integer.parseInt(categoryId));
		}

		if(date != null && !date.isEmpty()){
		    query.setParameter("date",
		            LocalDate.parse(date));
		}

		expenses = query.list();

		List<Category> categories = session.createQuery(
		        "FROM Category WHERE user.email=:email ORDER BY name",
		        Category.class)
		        .setParameter("email", user.getEmail())
		        .getResultList();
		request.setAttribute("categories", categories);

		session.close();

		request.setAttribute("selectedCategory", categoryId);
		request.setAttribute("searchDate", date);

		request.setAttribute("expenses", expenses);

		request.getRequestDispatcher("ViewExpense.jsp").forward(request, response);
	}

}
