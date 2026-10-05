<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isErrorPage="true" %>
<c:set var="pageTitle" value="Something went wrong"/>
<%@ include file="_header.jspf" %>
<section class="card narrow">
  <h1>Something went wrong</h1>
  <p>The request could not be completed. Check that MySQL is running and the database settings in DBConnection.java are correct, then try again.</p>
  <a class="btn" href="${pageContext.request.contextPath}/login">Back to login</a>
</section>
<%@ include file="_footer.jspf" %>
