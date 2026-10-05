package com.quiz.servlet;

import com.quiz.dao.QuestionDAO;
import com.quiz.model.Question;
import com.quiz.dao.QuizDAO;
import com.quiz.dao.NotificationDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/** GET /quiz : picks random questions and shows the quiz page. */
@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {
    private static final int QUESTIONS_PER_QUIZ = 10;
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final QuizDAO quizDAO = new QuizDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Question> questions = questionDAO.findRandom(QUESTIONS_PER_QUIZ);
            req.setAttribute("questions", questions);
            req.setAttribute("availableQuizzes", quizDAO.findPublished());
            req.setAttribute("notifications", notificationDAO.findPublished());
            req.setAttribute("durationSeconds", 600);
            req.setAttribute("submitPath", "/submit");
            req.getRequestDispatcher("/WEB-INF/views/quiz.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Could not load questions", e);
        }
    }
}
