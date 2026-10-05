<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="pageTitle" value="Results"/>
<%@ include file="_header.jspf" %>

<c:if test="${not empty result}">
  <section class="card score">
    <c:choose>
      <c:when test="${result.passed}">
        <div class="badge-pill badge-success">🎉 Excellent Performance</div>
      </c:when>
      <c:otherwise>
        <div class="badge-pill badge-alert">⚠️ ${result.assessment ? 'Failed' : 'Needs Practice'}</div>
      </c:otherwise>
    </c:choose>

    <p class="score-line"><strong>${result.score}</strong> out of ${result.total}</p>
    <div class="progress-container">
      <div class="progress-bar" style="width: ${result.percent}%;"></div>
    </div>
    <p class="percent">${result.percent}% score</p>
    <div style="margin-top: 1.25rem;">
      <a class="btn" href="${pageContext.request.contextPath}/quiz">${result.assessment ? 'Available Quizzes' : 'Take Another Quiz'}</a>
    </div>
  </section>
</c:if>

<c:if test="${not empty reviews}">
  <section class="review-section">
    <h2>Detailed Answer Review</h2>
    <div class="reviews-list">
      <c:forEach var="rev" items="${reviews}" varStatus="st">
        <div class="review-card ${rev.correct ? 'rev-correct' : 'rev-wrong'}">
          <div class="rev-header">
            <span class="qnum">${st.count}</span>
            <span class="rev-title"><c:out value="${rev.question.questionText}"/></span>
            <span class="rev-badge ${rev.correct ? 'badge-ok' : 'badge-bad'}">
              ${rev.correct ? '✓ Correct' : '✗ Incorrect'}
            </span>
          </div>
          <div class="rev-details">
            <p><strong>Your answer:</strong>
              <span class="${rev.correct ? 'text-ok' : 'text-bad'}">
                <c:choose>
                  <c:when test="${empty rev.chosenOption}">
                    <em>Not answered</em>
                  </c:when>
                  <c:otherwise>
                    (${rev.chosenOption}) <c:out value="${rev.chosenText}"/>
                  </c:otherwise>
                </c:choose>
              </span>
            </p>
            <c:if test="${not rev.correct}">
              <p><strong>Correct answer:</strong>
                <span class="text-ok">(${rev.question.correctOption}) <c:out value="${rev.correctText}"/></span>
              </p>
            </c:if>
          </div>
        </div>
      </c:forEach>
    </div>
  </section>
</c:if>

<h2>Your Past Attempts</h2>
<c:choose>
  <c:when test="${empty history}">
    <p class="muted">You haven't taken a quiz yet. <a href="${pageContext.request.contextPath}/quiz">Start one</a>.</p>
  </c:when>
  <c:otherwise>
    <table>
      <thead>
        <tr>
          <th>Attempt #</th>
          <th>Date & Time</th>
          <th>Score</th>
          <th>Percentage</th>
          <th>Result</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="r" items="${history}" varStatus="st">
          <tr>
            <td>#${history.size() - st.index}</td>
            <td>${r.takenAt}</td>
            <td><strong>${r.score}</strong> / ${r.total}</td>
            <td>${r.percent}%</td>
            <td>
              <span class="status-tag ${r.passed ? 'tag-pass' : 'tag-fail'}">
                ${r.passed ? 'Pass' : 'Fail'}
              </span>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </c:otherwise>
</c:choose>

<%@ include file="_footer.jspf" %>
