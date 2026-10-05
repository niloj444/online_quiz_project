package com.quiz.servlet;

import com.quiz.dao.UserDAO;
import com.quiz.model.User;
import com.quiz.util.PasswordUtil;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = (email == null || password == null) ? null : userDAO.findByEmail(email.trim().toLowerCase());
            if (user == null || !PasswordUtil.verify(password, user.getPasswordHash())) {
                req.setAttribute("error", "Email or password is incorrect.");
                req.setAttribute("email", email);
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;
            }
            // Fresh session on login (prevents session fixation), then remember the user.
            HttpSession old = req.getSession(false);
            if (old != null) old.invalidate();
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(30 * 60);               // 30 minutes
            resp.sendRedirect(req.getContextPath() + (user.isTeacher() ? "/teacher/dashboard" : user.isAdmin() ? "/admin/questions" : "/student/dashboard"));
        } catch (SQLException e) {
            throw new ServletException("Login failed", e);
        }
    }
}
