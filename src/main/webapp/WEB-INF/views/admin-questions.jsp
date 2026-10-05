<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="pageTitle" value="Manage questions"/>
<%@ include file="_header.jspf" %>
<h1>Manage questions</h1>
<c:if test="${not empty param.msg}"><p class="notice ok"><c:out value="${param.msg}"/></p></c:if>
<c:if test="${not empty error}"><p class="notice bad"><c:out value="${error}"/></p></c:if>

<section class="card">
  <h2><c:choose><c:when test="${not empty editing.id and editing.id > 0}">Edit question</c:when><c:otherwise>Add a question</c:otherwise></c:choose></h2>
  <form method="post" action="${pageContext.request.contextPath}/admin/questions">
    <input type="hidden" name="action" value="save">
    <c:if test="${not empty editing.id and editing.id > 0}"><input type="hidden" name="id" value="${editing.id}"></c:if>

    <label for="questionText">Question</label>
    <textarea id="questionText" name="questionText" rows="2" required><c:out value="${editing.questionText}"/></textarea>

    <div class="grid2">
      <div><label for="optionA">Option A</label><input id="optionA" name="optionA" required value="<c:out value='${editing.optionA}'/>"></div>
      <div><label for="optionB">Option B</label><input id="optionB" name="optionB" required value="<c:out value='${editing.optionB}'/>"></div>
      <div><label for="optionC">Option C</label><input id="optionC" name="optionC" required value="<c:out value='${editing.optionC}'/>"></div>
      <div><label for="optionD">Option D</label><input id="optionD" name="optionD" required value="<c:out value='${editing.optionD}'/>"></div>
    </div>

    <label for="correctOption">Correct answer</label>
    <select id="correctOption" name="correctOption" required>
      <option value="" disabled ${empty editing.correctOption ? 'selected' : ''}>Choose one</option>
      <option value="A" ${editing.correctOption == 'A' ? 'selected' : ''}>A</option>
      <option value="B" ${editing.correctOption == 'B' ? 'selected' : ''}>B</option>
      <option value="C" ${editing.correctOption == 'C' ? 'selected' : ''}>C</option>
      <option value="D" ${editing.correctOption == 'D' ? 'selected' : ''}>D</option>
    </select>

    <button type="submit" class="btn">Save question</button>
    <c:if test="${not empty editing}"><a class="btn ghost" href="${pageContext.request.contextPath}/admin/questions">Cancel</a></c:if>
  </form>
</section>

<h2>All questions (${questions.size()})</h2>
<table>
  <thead><tr><th>Question</th><th>Answer</th><th></th></tr></thead>
  <tbody>
    <c:forEach var="q" items="${questions}">
      <tr>
        <td><c:out value="${q.questionText}"/></td>
        <td>${q.correctOption}</td>
        <td class="actions">
          <a href="${pageContext.request.contextPath}/admin/questions?edit=${q.id}">Edit</a>
          <form method="post" action="${pageContext.request.contextPath}/admin/questions" onsubmit="return confirm('Delete this question?');">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="id" value="${q.id}">
            <button type="submit" class="link-button danger">Delete</button>
          </form>
        </td>
      </tr>
    </c:forEach>
  </tbody>
</table>
<%@ include file="_footer.jspf" %>
