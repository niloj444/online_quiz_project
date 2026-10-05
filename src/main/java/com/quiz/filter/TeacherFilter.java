package com.quiz.filter;
import com.quiz.model.User;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
@WebFilter(urlPatterns={"/teacher/*"})
public class TeacherFilter implements Filter {
 public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain)throws IOException,ServletException {
  HttpServletRequest r=(HttpServletRequest)req; HttpServletResponse p=(HttpServletResponse)res; HttpSession s=r.getSession(false); User u=s==null?null:(User)s.getAttribute("user");
  if(u==null)p.sendRedirect(r.getContextPath()+"/login"); else if(!u.canManageAssessments())p.sendRedirect(r.getContextPath()+"/quiz"); else chain.doFilter(req,res);
 }
}
