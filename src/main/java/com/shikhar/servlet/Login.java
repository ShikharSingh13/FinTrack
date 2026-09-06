package com.shikhar.servlet;

import java.io.IOException;

import org.hibernate.Session;
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
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public Login() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email=request.getParameter("email");
		String password=request.getParameter("password");

		Session session=HbUtility.sessionFactory.openSession();
		User u=session.get(User.class, email);
		HttpSession ses=request.getSession();

		if(u==null) {

		    ses.setAttribute("msg", "Invalid Credentials!");
		    ses.setAttribute("msgType", "danger");

		    response.sendRedirect("signin.jsp");

		}
		else {

			if(BCrypt.checkpw(password, u.getPassword()))  {

		        ses.setAttribute("User", u);

		        response.sendRedirect("DashboardServlet");

		    }
		    else {

		        ses.setAttribute("msg", "Invalid Credentials!");
		        ses.setAttribute("msgType", "danger");

		        response.sendRedirect("signin.jsp");

		    }

		}

	}

}
