<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="pageTitle" value="Student Results"/>
<%@ include file="_header.jspf" %>

<h1>Student Quiz Results</h1>

<div class="stats-grid">
  <div class="stat-card">
    <div class="stat-num">${totalAttempts}</div>
    <div class="stat-label">Total Attempts</div>
  </div>
  <div class="stat-card">
    <div class="stat-num">${avgScore}%</div>
    <div class="stat-label">Average Score</div>
  </div>
  <div class="stat-card">
    <div class="stat-num">${passRate}%</div>
    <div class="stat-label">Passing Rate</div>
  </div>
</div>

<h2>All Completed Quizzes (${results.size()})</h2>

<c:choose>
  <c:when test="${empty results}">
    <section class="card">
      <p class="muted">No student attempts have been submitted yet.</p>
    </section>
  </c:when>
  <c:otherwise>
    <table>
      <thead>
        <tr>
          <th>Student</th>
          <th>Email</th>
          <th>Date Taken</th>
          <th>Score</th>
          <th>Percentage</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="r" items="${results}">
          <tr>
            <td><strong><c:out value="${r.userName}"/></strong></td>
            <td><c:out value="${r.userEmail}"/></td>
            <td>${r.takenAt}</td>
            <td><strong>${r.score}</strong> / ${r.total}</td>
            <td>${r.percent}%</td>
            <td>
              <span class="status-tag ${r.percent >= 50 ? 'tag-pass' : 'tag-fail'}">
                ${r.percent >= 50 ? 'Passed' : 'Failed'}
              </span>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </c:otherwise>
</c:choose>

<%@ include file="_footer.jspf" %>
