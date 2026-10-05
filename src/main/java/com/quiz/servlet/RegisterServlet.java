package com.quiz.servlet;

import com.quiz.dao.UserDAO;
import com.quiz.util.PasswordUtil;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = trim(req.getParameter("name"));
        String email = trim(req.getParameter("email")).toLowerCase();
        String password = req.getParameter("password") == null ? "" : req.getParameter("password");
        String role = "TEACHER".equals(req.getParameter("role")) ? "TEACHER" : "USER";

        String error = null;
        if (name.isEmpty() || email.isEmpty()) error = "Enter your name and email.";
        else if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) error = "Enter a valid email address.";
        else if (password.length() < 6) error = "Password must be at least 6 characters.";

        try {
            if (error == null && !userDAO.create(name, email, PasswordUtil.hash(password), role)) {
                error = "That email is already registered. Try logging in.";
            }
        } catch (SQLException e) {
            throw new ServletException("Registration failed", e);
        }

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.setAttribute("role", role);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login?registered=1");
        }
    }

    private static String trim(String s) { return s == null ? "" : s.trim(); }
}
