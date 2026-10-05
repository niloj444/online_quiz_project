<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="pageTitle" value="Create account"/>
<%@ include file="_header.jspf" %>
<section class="card narrow">
  <h1>Create account</h1>
  <c:if test="${not empty error}"><p class="notice bad"><c:out value="${error}"/></p></c:if>

  <form method="post" action="${pageContext.request.contextPath}/register">
    <label for="name">Full name</label>
    <input id="name" name="name" type="text" required autofocus value="<c:out value='${name}'/>">

    <label for="email">Email</label>
    <input id="email" name="email" type="email" required value="<c:out value='${email}'/>">

    <label for="password">Password (6 or more characters)</label>
    <input id="password" name="password" type="password" required minlength="6">

    <label for="role">Register as</label>
    <select id="role" name="role">
      <option value="USER" ${role != 'TEACHER' ? 'selected' : ''}>Student</option>
      <option value="TEACHER" ${role == 'TEACHER' ? 'selected' : ''}>Teacher</option>
    </select>
    <p class="muted">Teacher accounts can create and manage assessments. Admin access cannot be selected here.</p>

    <button type="submit" class="btn">Create account</button>
  </form>
  <p class="muted">Already registered? <a href="${pageContext.request.contextPath}/login">Log in</a></p>
</section>
<%@ include file="_footer.jspf" %>
