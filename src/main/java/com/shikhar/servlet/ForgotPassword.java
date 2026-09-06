package com.shikhar.servlet;

import java.io.IOException;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.shikhar.EmailService;
import com.shikhar.HbUtility;
import com.shikhar.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ForgotPassword")
public class ForgotPassword extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public ForgotPassword() {
		// Default constructor
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");

		Session session = HbUtility.sessionFactory.openSession();

		User user = session.get(User.class, email);

		HttpSession ses = request.getSession();

		if (user == null) {

			ses.setAttribute("msg", "No account found with this email.");
			ses.setAttribute("msgType", "danger");

			session.close();

			response.sendRedirect("forgotpassword.jsp");

		} else {

			String resetToken = UUID.randomUUID().toString();

			long expiryTime = System.currentTimeMillis() + (30 * 60 * 1000);

			user.setResetToken(resetToken);
			user.setResetTokenExpiry(expiryTime);

			Transaction transaction = session.beginTransaction();

			session.merge(user);

			transaction.commit();

			session.close();

			String resetLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath() + "/resetpassword.jsp?token=" + resetToken;

			EmailService.sendResetEmail(email, resetLink);

			ses.setAttribute("msg", "Password reset link has been sent to your email.");

			ses.setAttribute("msgType", "success");

			response.sendRedirect("forgotpassword.jsp");
		}
	}
}