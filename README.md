# 1. Online Quiz & Assessment System

A college-level Java web application for teachers to publish assessments and for students to complete and review quizzes.

## 2. Project Overview

This system provides a small role-based quiz workflow using one account system. Teachers create subject-labelled PRACTICE or EXAM assessments from the question bank, publish them, and view their own assessment results. Students browse published assessments, filter them by subject, attempt each assessment once, and view their personal performance history. Roles keep teacher pages separate from ordinary student pages; the existing ADMIN role remains available for global question/result administration.

## 3. Key Features

### Authentication & Authorization

- Registration with **Student** (`USER`) or **Teacher** role selection; `ADMIN` cannot be self-selected.
- PBKDF2 password hashing, login, logout, and a 30-minute session.
- Role-based post-login routing: Teacher → Teacher Dashboard; Student → Student Dashboard; Admin → Admin question management.
- `AuthFilter`, `TeacherFilter`, and `AdminFilter` protect application routes.

### Teacher Features

- Teacher Dashboard to create and edit assessments.
- Assessment subject, title, description, PRACTICE/EXAM type, duration, passing percentage, published state, and selected question-bank questions.
- Draft/publish/unpublish/delete actions are supported by the dashboard servlet/DAO; a published assessment is visible to students only when it has questions.
- In-app notifications linked to one of the teacher's assessments.
- Assessment Results page scoped in SQL to quizzes created by the logged-in teacher, with attempts, average, highest score, and pass rate.
- The existing Question Bank lets authorized teacher/admin users add, edit, and delete questions with a category (used as the question subject/topic).

### Student Features

- Separate Student Dashboard with a subject filter for published assessments.
- Quiz cards show subject, type, question count, duration, pass mark, completion state, and due date when a due date exists.
- JavaScript countdown timer and server-side scoring on submission.
- One saved assessment attempt per student/quiz is enforced before start and submission.
- PRACTICE attempts retain the in-session answer review; EXAM attempts show score/pass-fail without an answer key.
- Dashboard summary and subject-wise performance derived from saved results: attempts, average, highest score, pass, and fail counts.
- Database-backed in-app notification list with direct quiz links. Notifications are broadcast to students for published linked quizzes; they are not per-student read/unread notifications.

## 4. User Roles

| Role | Responsibilities and access |
| --- | --- |
| Student (`USER`) | Uses `/student/dashboard`, views published quizzes, attempts them once, sees own result history and performance. Cannot use teacher routes. |
| Teacher | Creates/manages assessments, opens the question bank, sends linked in-app notifications, and views results for own quizzes. |
| Admin | Retains legacy global question and result pages. |

## 5. Application Workflow

### Teacher Workflow

```text
Register as Teacher → Login → Teacher Dashboard → choose subject and questions
→ configure PRACTICE/EXAM quiz → publish → create in-app notification
→ select quiz in Assessment Results → view student attempts
```

### Student Workflow

```text
Register as Student → Login → Student Dashboard → filter published quizzes by subject
→ start one available quiz → timer and answers → submit
→ server evaluation → saved result → result/performance record updates
```

## 6. System Architecture

```text
Browser
  ↓
JSP / HTML / CSS / JavaScript
  ↓
Servlet controllers + Filters
  ↓
Model and DAO layer
  ↓
JDBC
  ↓
MySQL
```

- **JSP/UI:** pages under `WEB-INF/views` render request data with JSTL and `<c:out>`.
- **Servlets:** handle page requests, form submission, scoring, and redirects.
- **Models:** represent users, questions, assessments, results, notifications, and performance rows.
- **DAOs:** use `PreparedStatement` for MySQL access.
- **Filters:** require a session and restrict teacher/admin paths.
- **Utilities:** `DBConnection` obtains JDBC connections; `PasswordUtil` hashes/verifies passwords.

## 7. Project Structure

```text
quiz/
├── pom.xml
├── README.md
├── docs/
│   ├── architecture.md
│   └── design.md
├── sql/
│   ├── schema.sql
│   ├── seed.sql
│   ├── assessment-migration.sql
│   ├── subject-notification-migration.sql
│   └── student-dashboard-migration.sql
└── src/main/
    ├── java/com/quiz/
    │   ├── dao/
    │   ├── filter/
    │   ├── model/
    │   ├── servlet/
    │   └── util/
    └── webapp/
        ├── css/style.css
        ├── index.jsp
        └── WEB-INF/views/
```

## 8. Technology Stack

| Technology | Purpose |
| --- | --- |
| Java 11 source/target | Application code |
| Servlet API 4.0.1 | HTTP controllers and filters |
| JSP API 2.3.3 / JSTL 1.2 | Server-rendered UI |
| JDBC / MySQL Connector/J 8.3.0 | Database access |
| MySQL 8 | Relational database |
| Maven | WAR build and dependency management |
| Apache Tomcat 9 | Servlet container (`javax.servlet`) |
| HTML/CSS/JavaScript | UI and client timer |

## 9. Database Design

| Table | Purpose and key relationships |
| --- | --- |
| `users` | Accounts: `id`, name, email, password hash, `USER`/`TEACHER`/`ADMIN` role. |
| `questions` | Four-option question bank; optional `created_by` references `users`. |
| `quizzes` | Teacher assessment: teacher, subject, configuration, publication state, optional `due_date`. |
| `quiz_questions` | Many-to-many assessment/question selection, with display position. |
| `results` | Attempt score and assessment context; `user_id` and optional `quiz_id` are foreign keys. |
| `notifications` | Teacher-created message linked to a teacher and quiz. |

```text
Teacher (users) ──< quizzes ──< quiz_questions >── questions
                       ├──< notifications
Student (users) ──< results >── quizzes
```

## 10. Quiz Lifecycle

```text
Question bank → Teacher selects questions → Assessment configuration
→ Draft/Published → Student dashboard → Attempt and server scoring
→ Result storage → Student performance / Teacher results
```

## 11. Notification System

Teachers create a title and message linked to one of their quizzes through `/teacher/notifications`. `NotificationDAO` stores the message in MySQL. Student Dashboard and the legacy quiz page show notifications only when the linked quiz is currently published and provide an **Open quiz** link. This is database-backed in-app messaging—not real-time, email, SMS, or push notification delivery.

## 12. Student Performance Tracking

Assessment results store score, total, passing percentage, quiz type, attempt time, and quiz reference. Subject performance uses aggregate SQL over the student's saved assessment results joined to `quizzes`; it calculates count, average percentage, highest percentage, passes, and failures. Legacy quick-practice results remain in the existing results history.

## 13. Security & Access Control

- Login creates a fresh session and stores the authenticated `User` object.
- Passwords use PBKDF2 hashing (`PasswordUtil`).
- Servlet filters require authentication; teacher and admin filters restrict their route groups.
- Teacher assessment updates/deletes and teacher result queries include the current teacher ID.
- Student assessment submission obtains the user from the session, not a form user ID.
- Prepared statements are used for DAO parameters; JSP output uses JSTL escaping in displayed user-provided text.
- Server checks published state, overdue state, and previous assessment attempt before starting/submitting an assessment.

### Sensitive Information

Do not commit database passwords or local connection details. `DB_URL`, `DB_USER`, and `DB_PASS` can be supplied through environment variables or Java system properties; use private local values in development and production.

## 14. Prerequisites

- JDK 11+
- Maven 3.6+
- MySQL 8+
- Apache Tomcat **9** (Tomcat 10 uses incompatible `jakarta.servlet` packages)
- Git and a modern browser

## 15. Installation & Setup

```powershell
git clone <repository-url>
cd quiz
```

1. Create an empty MySQL server/database environment.
2. Run the schema and optional seed scripts shown below.
3. Configure database connection values privately through environment variables/system properties.
4. Build the WAR with Maven.
5. Copy `target/quiz-app.war` into Tomcat's `webapps` folder.
6. Start Tomcat and open the context URL.

## 16. Database Setup

For a fresh database:

```powershell
mysql -u <user> -p < sql/schema.sql
mysql -u <user> -p < sql/seed.sql
```

For a database created before role-based assessments, run each migration once in order:

```powershell
mysql -u <user> -p < sql/assessment-migration.sql
mysql -u <user> -p < sql/subject-notification-migration.sql
mysql -u <user> -p < sql/student-dashboard-migration.sql
```

`schema.sql` creates `quizdb`; do not rerun additive migrations on a database where their columns/tables already exist.

## 17. Build & Run

```powershell
mvn clean package -DskipTests
Copy-Item .\target\quiz-app.war <TOMCAT_HOME>\webapps\quiz-app.war
```

Tomcat expands the WAR under `webapps/quiz-app`. The usual local URL is `http://localhost:<tomcat-port>/quiz-app/`; the actual port depends on the local Tomcat connector configuration.

## 18. Configuration

`src/main/java/com/quiz/util/DBConnection.java` reads `DB_URL`, `DB_USER`, and `DB_PASS` from environment variables first, then Java system properties, before using local defaults. Set those variables outside source control. `WEB-INF/web.xml` supplies the welcome file, session timeout, and generic error page.

## 19. Application URLs

| Page | URL | Access |
| --- | --- | --- |
| Login / Register | `/login`, `/register` | Public |
| Student Dashboard | `/student/dashboard` | Authenticated student workflow |
| Legacy quick quiz | `/quiz` | Authenticated |
| My Results | `/result` | Authenticated, own history |
| Start assessment | `/assessment/start?id={id}` | Authenticated published quiz |
| Teacher Dashboard | `/teacher/dashboard` | Teacher/Admin |
| Teacher Results | `/teacher/results` | Teacher/Admin |
| Teacher Notifications | `/teacher/notifications` | Teacher/Admin |
| Question Bank | `/admin/questions` | Admin; teacher route permitted by current filter implementation |
| Admin Results | `/admin/results` | Admin |

## 20. API / Servlet Endpoints

This is a server-rendered servlet application, not a REST API.

| Method | Endpoint | Purpose |
| --- | --- | --- |
| GET/POST | `/login`, `/register` | Render/submit authentication forms |
| POST | `/logout` | Invalidates session |
| GET/POST | `/teacher/dashboard` | Render/save assessment form; query `edit` loads an owned quiz |
| GET/POST | `/teacher/notifications` | Render/save a linked notification |
| GET | `/assessment/start?id=` | Loads an available assessment |
| POST | `/assessment/submit` | Scores submitted answers and redirects to saved result |
| GET | `/student/dashboard?subject=` | Renders filtered student dashboard |
| GET | `/result?id=` | Displays the authenticated user's result/history |

## 21. Testing

### Teacher test

1. Register with **Teacher** role and log in.
2. Add questions through Question Bank.
3. Create a subject-labelled PRACTICE assessment, select questions, and publish it.
4. Create a notification linked to that assessment.
5. Open Teacher Results after a student submits.

### Student test

1. Register with **Student** role and log in.
2. Open Student Dashboard; verify notification and subject filter.
3. Start the published assessment, answer, and submit.
4. Verify result is saved, dashboard performance updates, and a second start is blocked.
5. Verify PRACTICE shows review in the immediate result flow; EXAM does not.

Handled edge cases include invalid login, unauthenticated routes, unpublished/missing assessment, expired assessment when a due date is set, and duplicate assessment attempt.

## 22. Screenshots

### Login
[Add screenshot]

### Teacher Dashboard
[Add screenshot]

### Student Dashboard
[Add screenshot]

### Quiz Attempt
[Add screenshot]

## 23. Current Implementation Status

- [x] Student and teacher registration
- [x] Role-aware login redirects
- [x] Teacher assessment dashboard
- [x] Subject-labelled PRACTICE and EXAM quizzes
- [x] Publish state and selected questions
- [x] In-app quiz-linked notifications
- [x] Student dashboard and subject filter
- [x] One assessment attempt per student/quiz
- [x] Student subject performance aggregates
- [x] Teacher-owned assessment results
- [ ] Teacher UI entry for setting a due date (database/model/server checks exist, but the current form does not provide date input)
- [ ] Per-student notification read/dismiss state

## 24. Known Limitations

- Notifications are broadcast for published quizzes; no read/unread tracking or real-time delivery exists.
- Due dates are supported by schema/model/start-submit validation but are not set from the current Teacher Dashboard form.
- Practice answer reviews are kept in the active session after submission; there is no persisted per-answer review table for later history viewing.
- No email/SMS/push notifications, advanced analytics, proctoring, scheduling UI, or multiple-attempt policy is implemented.
- A repository `.gitignore` file is not currently present; add one before publishing to avoid committing generated `target/` files, IDE settings, logs, and local secrets.

## 25. Future Enhancements

- Teacher due-date input and due-soon labels.
- Per-student read/dismissed notifications.
- Persisted practice-answer review history.
- Teacher-owned question enforcement at DAO level.
- Question randomization, difficulty, multiple practice attempts, exports, and email/push delivery.

## 26. Deployment

For local development, build the WAR and deploy to Tomcat 9 as described above. In production, use environment-provided database credentials, a secured MySQL account, HTTPS, a configured Tomcat connector, and a backup policy. The context path is determined by the WAR name (`quiz-app.war` → `/quiz-app`).

## 27. Git & GitHub

```bash
git clone <repository-url>
git add .
git commit -m "Describe change"
git push
```

Before pushing, add a `.gitignore` for Maven output, IDE files, logs, and local configuration. Do not commit credentials or tokens.

## 28. Contribution Guidelines

```text
Fork → Create branch → Make focused changes → Build/test → Commit → Push → Pull Request
```

Keep servlet/JSP/JDBC architecture, update SQL migrations for schema changes, and update this README when behaviour changes.

