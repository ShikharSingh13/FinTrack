package com.shikhar.servlet;

import java.io.IOException;
import java.util.List;

import org.hibernate.Session;

import com.shikhar.HbUtility;
import com.shikhar.entity.Budget;
import com.shikhar.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ManageBudget")
public class ManageBudget extends HttpServlet {

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

        String hql = "FROM Budget WHERE user.email = :email ORDER BY year DESC, month DESC";

        List<Budget> budgetList = session.createQuery(hql, Budget.class)
                .setParameter("email", user.getEmail())
                .getResultList();

        // Edit Mode
        Budget editBudget = null;

        String id = request.getParameter("id");

        if (id != null) {

            editBudget = session.get(Budget.class, Long.parseLong(id));

            if (editBudget != null &&
                    !editBudget.getUser().getEmail().equals(user.getEmail())) {

                editBudget = null;
            }
        }

        session.close();

        request.setAttribute("budgetList", budgetList);
        request.setAttribute("editBudget", editBudget);

        request.getRequestDispatcher("Budget.jsp").forward(request, response);
    }
}