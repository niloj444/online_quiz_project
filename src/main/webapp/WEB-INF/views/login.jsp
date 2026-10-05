<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="pageTitle" value="Log in"/>
<%@ include file="_header.jspf" %>
<section class="card narrow">
  <h1>Log in</h1>
  <c:if test="${param.registered == '1'}"><p class="notice ok">Account created. Log in to start.</p></c:if>
  <c:if test="${not empty error}"><p class="notice bad"><c:out value="${error}"/></p></c:if>

  <form method="post" action="${pageContext.request.contextPath}/login">
    <label for="email">Email</label>
    <input id="email" name="email" type="email" required autofocus value="<c:out value='${email}'/>">

    <label for="password">Password</label>
    <input id="password" name="password" type="password" required>

    <button type="submit" class="btn">Log in</button>
  </form>
  <p class="muted">New here? <a href="${pageContext.request.contextPath}/register">Create an account</a></p>
</section>
<%@ include file="_footer.jspf" %>
