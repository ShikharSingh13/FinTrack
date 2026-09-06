package com.shikhar.servlet;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.shikhar.HbUtility;
import com.shikhar.entity.Expense;
import com.shikhar.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/DeleteExpense")
public class DeleteExpense extends HttpServlet {

    @Override
	protected void service(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession httpSession = request.getSession(false);

        if (httpSession == null || httpSession.getAttribute("User") == null) {

            response.sendRedirect("signin.jsp");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        Session session = HbUtility.sessionFactory.openSession();

        Expense expense = session.get(Expense.class, id);

        if (expense != null) {

            User user = (User) httpSession.getAttribute("User");

            // Security check
            if(expense.getUser().getEmail().equals(user.getEmail())){

                Transaction tx = session.beginTransaction();

                session.remove(expense);

                tx.commit();

                httpSession.setAttribute("msg",
                        "Expense Deleted Successfully");

                httpSession.setAttribute("msgType",
                        "success");

            }

        }

        session.close();

        response.sendRedirect("ViewExpense.jsp");

    }

}