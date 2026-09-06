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

@WebServlet("/ResetPassword")
public class ResetPassword extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public ResetPassword() {
        // Default constructor
    }

    @Override
	protected void service(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        HttpSession ses = request.getSession();

        // Check token
        if (token == null || token.trim().isEmpty()) {

            ses.setAttribute("msg", "Invalid password reset link.");
            ses.setAttribute("msgType", "danger");

            response.sendRedirect("signin.jsp");
            return;
        }

        // Check password match
        if (!password.equals(confirmPassword)) {

            ses.setAttribute("msg", "Passwords do not match.");
            ses.setAttribute("msgType", "danger");

            response.sendRedirect("resetpassword.jsp?token=" + token);
            return;
        }

        Session session = HbUtility.sessionFactory.openSession();

        User user = session.createQuery(
                "FROM User WHERE resetToken = :token",
                User.class)
                .setParameter("token", token)
                .uniqueResult();

        if (user == null) {

            session.close();

            ses.setAttribute("msg", "Invalid or expired reset link.");
            ses.setAttribute("msgType", "danger");

            response.sendRedirect("signin.jsp");
            return;
        }

        // Check token expiry
        if (user.getResetTokenExpiry() == null ||
            System.currentTimeMillis() > user.getResetTokenExpiry()) {

            session.close();

            ses.setAttribute("msg", "Reset link has expired.");
            ses.setAttribute("msgType", "danger");

            response.sendRedirect("signin.jsp");
            return;
        }

        // Hash new password
        String hashedPassword =
                BCrypt.hashpw(password, BCrypt.gensalt());

        user.setPassword(hashedPassword);

        // Invalidate token after successful password reset
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        Transaction transaction = session.beginTransaction();

        session.merge(user);

        transaction.commit();

        session.close();

        ses.setAttribute("msg",
                "Password reset successfully. Please login.");

        ses.setAttribute("msgType", "success");

        response.sendRedirect("signin.jsp");
    }
}