package com.shikhar.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.shikhar.HbUtility;
import com.shikhar.entity.Expense;
import com.shikhar.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/UserHome")
public class UserHome extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession httpSession = request.getSession(false);

        if (httpSession == null || httpSession.getAttribute("User") == null) {

            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("msg", "Please Login First");
            newSession.setAttribute("msgType", "warning");

            response.sendRedirect("signin.jsp");
            return;
        }

        User user = (User) httpSession.getAttribute("User");

        Session session = HbUtility.sessionFactory.openSession();

        String filter = request.getParameter("filter");
        String keyword = request.getParameter("keyword");

        List<Expense> expenses;

        // ===========================
        // SEARCH BY TITLE
        // ===========================

        if ("title".equals(filter) && keyword != null && !keyword.trim().isEmpty()) {

            Query<Expense> query = session.createQuery(
                    "from Expense where user=:user and lower(title) like :title order by date desc,time desc",
                    Expense.class);

            query.setParameter("user", user);
            query.setParameter("title", "%" + keyword.toLowerCase() + "%");

            expenses = query.list();
        }

        // ===========================
        // SEARCH BY DATE
        // ===========================

        else if ("date".equals(filter) && keyword != null && !keyword.isEmpty()) {

            LocalDate searchDate = LocalDate.parse(keyword);

            Query<Expense> query = session.createQuery(
                    "from Expense where user=:user and date=:date order by time desc",
                    Expense.class);

            query.setParameter("user", user);
            query.setParameter("date", searchDate);

            expenses = query.list();
        }

        // ===========================
        // RECENT EXPENSES
        // ===========================

        else {

            LocalDate lastWeek = LocalDate.now().minusDays(7);

            Query<Expense> query = session.createQuery(
                    "from Expense where user=:user and date>=:date order by date desc,time desc",
                    Expense.class);

            query.setParameter("user", user);
            query.setParameter("date", lastWeek);

            expenses = query.list();
        }

        session.close();

        request.setAttribute("expenses", expenses);

        request.getRequestDispatcher("UserHome.jsp").forward(request, response);
    }
}