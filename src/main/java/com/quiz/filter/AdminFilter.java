package com.quiz.filter;

import com.quiz.model.User;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

/** Only ADMIN users may open /admin/*. Normal users are sent back to the quiz. */
@WebFilter(urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
        } else if (!user.isAdmin() && !(user.isTeacher() && request.getRequestURI().endsWith("/admin/questions"))) {
            response.sendRedirect(request.getContextPath() + "/quiz");
        } else {
            chain.doFilter(req, res);
        }
    }
}
