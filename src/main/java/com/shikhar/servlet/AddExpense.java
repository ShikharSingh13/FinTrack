package com.shikhar.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.Session;
import org.hibernate.Transaction;

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
 * Servlet implementation class AddExpense
 */
@WebServlet("/AddExpense")
public class AddExpense extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public AddExpense() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		 HttpSession ses = request.getSession(false);

		    if (ses == null || ses.getAttribute("User") == null) {
		    	 HttpSession newSession = request.getSession(true);

		    	    newSession.setAttribute("msg", "Please login first!");
		    	    newSession.setAttribute("msgType", "danger");

		    	    response.sendRedirect("signin.jsp");
		    	    return;
		    }

		    User user = (User) ses.getAttribute("User");

			String title=request.getParameter("title");
			LocalDate date=LocalDate.parse(request.getParameter("date"));
			LocalTime time=LocalTime.parse(request.getParameter("time"));
			String description=request.getParameter("description");
			double price=Double.parseDouble(request.getParameter("price"));

			int categoryId = Integer.parseInt(request.getParameter("categoryId"));

			Session session=HbUtility.sessionFactory.openSession();
			Transaction txn=session.beginTransaction();

			Category category = session.get(Category.class, categoryId);

			Expense expense=new Expense();

			expense.setTitle(title);
			expense.setDate(date);
			expense.setTime(time);
			expense.setDescription(description);
			expense.setPrice(price);
			expense.setCategory(category);
			expense.setUser(user);


			session.persist(expense);
			txn.commit();
			session.close();

			ses.setAttribute("msg", "Expense Added Successfully");
			ses.setAttribute("msgType", "success");

			response.sendRedirect("AddExpense.jsp");
	}

}
