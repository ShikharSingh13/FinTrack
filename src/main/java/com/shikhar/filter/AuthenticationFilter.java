package com.shikhar.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter({ "/DashboardServlet",
    "/UserHome.jsp",
    "/AddExpense.jsp",
    "/ViewExpense.jsp",
    "/EditExpense.jsp",
    "/UpdateExpense",
    "/DeleteExpense",
    "/SearchExpense",
    "/AddExpense",
    "/Logout"})
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("User") == null) {

            HttpSession newSession = req.getSession(true);
            newSession.setAttribute("msg", "Please Login First");
            newSession.setAttribute("msgType", "warning");

            res.sendRedirect("signin.jsp");
            return;
        }

        chain.doFilter(request, response);
    }
}