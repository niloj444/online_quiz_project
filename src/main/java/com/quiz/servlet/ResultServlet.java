package com.quiz.servlet;

import com.quiz.dao.ResultDAO;
import com.quiz.model.User;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/** GET /result?id=5 shows one attempt plus the user's history. GET /result shows history only. */
@WebServlet("/result")
public class ResultServlet extends HttpServlet {
    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        try {
            String idParam = req.getParameter("id");
            if (idParam != null) {
                try {
                    int rId = Integer.parseInt(idParam);
                    req.setAttribute("result", resultDAO.findByIdAndUser(rId, user.getId()));
                    HttpSession session = req.getSession(false);
                    com.quiz.model.Result loaded = (com.quiz.model.Result) req.getAttribute("result");
                    if (loaded != null && session != null && Integer.valueOf(rId).equals(session.getAttribute("lastReviewResultId"))
                            && loaded.isPracticeAssessment()) {
                        req.setAttribute("reviews", session.getAttribute("lastReviews"));
                    }
                } catch (NumberFormatException ignored) { }
            }
            req.setAttribute("history", resultDAO.findByUser(user.getId()));
            req.getRequestDispatcher("/WEB-INF/views/result.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Could not load results", e);
        }
    }
}
