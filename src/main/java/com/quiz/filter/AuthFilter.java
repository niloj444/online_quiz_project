package com.quiz.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

/** Module IV: blocks every protected page unless a user is stored in the HttpSession. */
@WebFilter(urlPatterns = {"/quiz", "/submit", "/result", "/admin/*", "/teacher/*", "/assessment/*", "/student/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);          // don't create one just to check
        boolean loggedIn = session != null && session.getAttribute("user") != null;

        if (loggedIn) {
            chain.doFilter(req, res);                              // let the request through
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
