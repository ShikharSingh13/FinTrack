package com.shikhar.servlet;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import com.shikhar.HbUtility;
import com.shikhar.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public Register() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name=request.getParameter("name");
		String email=request.getParameter("email");
		String password=request.getParameter("password");

		if (password == null || password.length() < 8
		        || !password.matches(".*[A-Z].*")
		        || !password.matches(".*[a-z].*")
		        || !password.matches(".*[0-9].*")
		        || !password.matches(".*[^a-zA-Z0-9].*")) {

		    HttpSession ses = request.getSession();

		    ses.setAttribute("msg",
		            "Password must be at least 8 characters and contain uppercase, lowercase, number and special character.");

		    ses.setAttribute("msgType", "danger");

		    response.sendRedirect("signup.jsp");
		    return;
		}

		User u=new User();
		u.setName(name);
		u.setEmail(email);

		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		u.setPassword(hashedPassword);

		Session session=HbUtility.sessionFactory.openSession();
		User user=session.get(User.class, email);
		HttpSession ses=request.getSession();
		if(user==null) {
			Transaction txn=session.beginTransaction();
			session.persist(u);
			txn.commit();
			session.close();
			ses.setAttribute("msg", "Registered Successfully! Kindly Login");
			ses.setAttribute("msgType", "success");
			response.sendRedirect("signin.jsp");
		}
		else {
			ses.setAttribute("msg", "User ALready Exist!");
			ses.setAttribute("msgType", "danger");
			response.sendRedirect("signup.jsp");
		}
	}

}
