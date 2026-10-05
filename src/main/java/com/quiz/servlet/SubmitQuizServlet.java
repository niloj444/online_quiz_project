package com.quiz.servlet;

import com.quiz.dao.QuestionDAO;
import com.quiz.dao.ResultDAO;
import com.quiz.model.Question;
import com.quiz.model.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * POST /submit : scores the answers on the server (the browser never sees correct answers),
 * saves the result with JDBC, then redirects to the result page (Post/Redirect/Get).
 */
@WebServlet("/submit")
public class SubmitQuizServlet extends HttpServlet {
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Integer> ids = new ArrayList<>();
        String[] rawIds = req.getParameterValues("qid");
        if (rawIds != null) {
            for (String s : rawIds) {
                try { ids.add(Integer.parseInt(s)); } catch (NumberFormatException ignored) { }
            }
        }
        if (ids.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/quiz");
            return;
        }

        try {
            int score = 0;
            List<Question> questions = questionDAO.findByIds(ids);
            List<com.quiz.model.QuestionReview> reviews = new ArrayList<>();
            for (Question q : questions) {
                String chosen = req.getParameter("q_" + q.getId());
                com.quiz.model.QuestionReview r = new com.quiz.model.QuestionReview(q, chosen);
                if (r.isCorrect()) score++;
                reviews.add(r);
            }
            int resultId = resultDAO.save(user.getId(), score, questions.size());
            if (session == null) session = req.getSession(true);
            session.setAttribute("lastReviewResultId", resultId);
            session.setAttribute("lastReviews", reviews);
            resp.sendRedirect(req.getContextPath() + "/result?id=" + resultId);
        } catch (SQLException e) {
            throw new ServletException("Could not save result", e);
        }
    }
}
