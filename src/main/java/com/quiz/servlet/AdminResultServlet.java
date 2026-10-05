package com.quiz.servlet;

import com.quiz.dao.ResultDAO;
import com.quiz.model.UserResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Admin view of all quiz attempts across all students.
 * Guarded by AuthFilter and AdminFilter.
 */
@WebServlet("/admin/results")
public class AdminResultServlet extends HttpServlet {
    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<UserResult> results = resultDAO.findAllWithUsers();

            int totalAttempts = results.size();
            double avgScore = 0;
            int passCount = 0;

            if (totalAttempts > 0) {
                int scoreSum = 0;
                for (UserResult ur : results) {
                    scoreSum += ur.getPercent();
                    if (ur.getPercent() >= 50) passCount++;
                }
                avgScore = (double) scoreSum / totalAttempts;
            }

            req.setAttribute("results", results);
            req.setAttribute("totalAttempts", totalAttempts);
            req.setAttribute("avgScore", String.format("%.1f", avgScore));
            req.setAttribute("passRate", totalAttempts == 0 ? 0 : (passCount * 100) / totalAttempts);

            req.getRequestDispatcher("/WEB-INF/views/admin-results.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Could not load student results", e);
        }
    }
}
