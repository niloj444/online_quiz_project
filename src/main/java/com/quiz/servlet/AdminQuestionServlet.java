package com.quiz.servlet;

import com.quiz.dao.QuestionDAO;
import com.quiz.model.Question;
import com.quiz.model.User;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Admin CRUD for questions (guarded by AuthFilter + AdminFilter).
 *   GET  /admin/questions            list + empty form
 *   GET  /admin/questions?edit=7     list + form filled with question 7
 *   POST action=save                 insert (no id) or update (with id)
 *   POST action=delete               delete by id
 */
@WebServlet("/admin/questions")
public class AdminQuestionServlet extends HttpServlet {
    private final QuestionDAO questionDAO = new QuestionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String edit = req.getParameter("edit");
            if (edit != null) {
                try { req.setAttribute("editing", questionDAO.findById(Integer.parseInt(edit))); }
                catch (NumberFormatException ignored) { }
            }
            req.setAttribute("questions", questionDAO.findAll());
            req.getRequestDispatcher("/WEB-INF/views/admin-questions.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Could not load questions", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        User user = (User) req.getSession().getAttribute("user");
        try {
            if ("delete".equals(action)) {
                questionDAO.delete(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect(req.getContextPath() + "/admin/questions?msg=Question+deleted");
                return;
            }

            Question q = new Question();
            q.setQuestionText(clean(req.getParameter("questionText")));
            q.setOptionA(clean(req.getParameter("optionA")));
            q.setOptionB(clean(req.getParameter("optionB")));
            q.setOptionC(clean(req.getParameter("optionC")));
            q.setOptionD(clean(req.getParameter("optionD")));
            q.setCorrectOption(clean(req.getParameter("correctOption")));
            String idParam = req.getParameter("id");
            boolean isEdit = idParam != null && !idParam.isEmpty();
            if (isEdit) q.setId(Integer.parseInt(idParam));

            boolean valid = !q.getQuestionText().isEmpty() && !q.getOptionA().isEmpty() && !q.getOptionB().isEmpty()
                    && !q.getOptionC().isEmpty() && !q.getOptionD().isEmpty()
                    && q.getCorrectOption().matches("[ABCD]");
            if (!valid) {
                req.setAttribute("error", "Fill in the question, all four options, and choose the correct one.");
                req.setAttribute("editing", q);
                req.setAttribute("questions", questionDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/views/admin-questions.jsp").forward(req, resp);
                return;
            }

            if (isEdit) {
                questionDAO.update(q);
                resp.sendRedirect(req.getContextPath() + "/admin/questions?msg=Question+updated");
            } else {
                questionDAO.insert(q);
                resp.sendRedirect(req.getContextPath() + "/admin/questions?msg=Question+added");
            }
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Could not save question", e);
        }
    }

    private static String clean(String s) { return s == null ? "" : s.trim(); }
}
