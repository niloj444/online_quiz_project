<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="pageTitle" value="Quiz"/>
<%@ include file="_header.jspf" %>

<c:if test="${not empty availableQuizzes}">
  <c:if test="${not empty notifications}"><h2>Notifications</h2><c:forEach var="n" items="${notifications}"><section class="card notification"><strong><c:out value="${n.title}"/></strong><p><c:out value="${n.message}"/></p><a href="${pageContext.request.contextPath}/assessment/start?id=${n.quizId}">Open quiz</a></section></c:forEach></c:if>
  <h1>Available Quizzes</h1>
  <div class="quiz-list">
    <c:forEach var="aq" items="${availableQuizzes}">
      <section class="card quiz-summary">
        <div><p class="muted"><c:out value="${aq.subject}"/></p><h2><c:out value="${aq.title}"/></h2><p class="muted"><c:out value="${aq.description}"/></p></div>
        <p><span class="status-tag tag-type">${aq.quizType}</span> &nbsp; ${aq.questionCount} questions &nbsp; ${aq.durationMinutes} minutes &nbsp; Pass: ${aq.passingPercentage}%</p>
        <a class="btn" href="${pageContext.request.contextPath}/assessment/start?id=${aq.id}">Start</a>
      </section>
    </c:forEach>
  </div>
  <h2>Quick practice</h2>
</c:if>

<c:choose>
  <c:when test="${empty questions}">
    <section class="card">
      <h1>No questions yet</h1>
      <p>The quiz has no questions. Ask an admin to add some under Manage questions.</p>
    </section>
  </c:when>
  <c:otherwise>
    <div class="quiz-sticky-bar">
      <div>
        <h1 style="display:inline; margin:0; font-size:1.35rem;"><c:out value="${empty assessment ? 'Online Quiz' : assessment.title}"/></h1>
        <span class="count" id="answeredCount">0 of ${questions.size()} answered</span>
      </div>
      <div class="timer-box" id="timerBox">
        ⏱️ <span id="timerDisplay">10:00</span>
      </div>
    </div>

    <form id="quizForm" method="post" action="${pageContext.request.contextPath}${submitPath}" class="quiz">
      <c:if test="${not empty assessment}"><input type="hidden" name="quizId" value="${assessment.id}"></c:if>
      <c:forEach var="q" items="${questions}" varStatus="st">
        <fieldset class="question" id="card_q_${q.id}">
          <legend><span class="qnum">${st.count}</span> <c:out value="${q.questionText}"/></legend>
          <input type="hidden" name="qid" value="${q.id}">

          <label class="option" for="opt_${q.id}_A">
            <input type="radio" id="opt_${q.id}_A" name="q_${q.id}" value="A">
            <span class="bubble">A</span>
            <span class="opt-text"><c:out value="${q.optionA}"/></span>
          </label>
          <label class="option" for="opt_${q.id}_B">
            <input type="radio" id="opt_${q.id}_B" name="q_${q.id}" value="B">
            <span class="bubble">B</span>
            <span class="opt-text"><c:out value="${q.optionB}"/></span>
          </label>
          <label class="option" for="opt_${q.id}_C">
            <input type="radio" id="opt_${q.id}_C" name="q_${q.id}" value="C">
            <span class="bubble">C</span>
            <span class="opt-text"><c:out value="${q.optionC}"/></span>
          </label>
          <label class="option" for="opt_${q.id}_D">
            <input type="radio" id="opt_${q.id}_D" name="q_${q.id}" value="D">
            <span class="bubble">D</span>
            <span class="opt-text"><c:out value="${q.optionD}"/></span>
          </label>
        </fieldset>
      </c:forEach>

      <div class="quiz-footer-actions">
        <button type="submit" class="btn" id="submitBtn">Submit answers</button>
      </div>
    </form>

    <script>
      const totalQuestions = ${questions.size()};
      let timeRemainingSeconds = ${durationSeconds};
      let timerInterval = null;

      function updateProgress() {
        const fieldsets = document.querySelectorAll('fieldset.question');
        let answered = 0;
        fieldsets.forEach(fs => {
          const checked = fs.querySelector('input[type="radio"]:checked');
          if (checked) {
            answered++;
            fs.querySelectorAll('.option').forEach(o => o.classList.remove('selected'));
            const parentLabel = checked.closest('.option');
            if (parentLabel) parentLabel.classList.add('selected');
          }
        });
        const countSpan = document.getElementById('answeredCount');
        if (countSpan) {
          countSpan.textContent = answered + ' of ' + totalQuestions + ' answered';
        }
      }

      function startTimer() {
        const display = document.getElementById('timerDisplay');
        const box = document.getElementById('timerBox');

        timerInterval = setInterval(() => {
          timeRemainingSeconds--;
          if (timeRemainingSeconds <= 0) {
            clearInterval(timerInterval);
            display.textContent = '00:00';
            box.classList.add('timer-urgent');
            document.getElementById('quizForm').submit();
            return;
          }

          const mins = Math.floor(timeRemainingSeconds / 60);
          const secs = timeRemainingSeconds % 60;
          display.textContent = (mins < 10 ? '0' : '') + mins + ':' + (secs < 10 ? '0' : '') + secs;

          if (timeRemainingSeconds <= 60) {
            box.classList.add('timer-urgent');
          }
        }, 1000);
      }

      // Handle radio changes cleanly
      document.querySelectorAll('.option input[type="radio"]').forEach(radio => {
        radio.addEventListener('change', updateProgress);
      });

      // Allow clicking anywhere on the option row
      document.querySelectorAll('.option').forEach(opt => {
        opt.addEventListener('click', function(e) {
          const radio = this.querySelector('input[type="radio"]');
          if (radio && e.target !== radio) {
            radio.checked = true;
            updateProgress();
          }
        });
      });

      // Clear timer on submit
      document.getElementById('quizForm').addEventListener('submit', function() {
        clearInterval(timerInterval);
      });

      window.addEventListener('DOMContentLoaded', () => {
        updateProgress();
        startTimer();
      });
    </script>
  </c:otherwise>
</c:choose>

<%@ include file="_footer.jspf" %>
