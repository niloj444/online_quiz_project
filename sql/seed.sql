USE quizdb;

-- Clear existing questions and seed clean categories
DELETE FROM questions;

INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option, category) VALUES
-- === Servlets & JSP ===
('Which object stores data for one user across multiple requests?', 'HttpServletRequest', 'ServletContext', 'HttpSession', 'RequestDispatcher', 'C', 'Servlets & JSP'),
('Which method sends the browser to a new URL via HTTP redirect?', 'forward()', 'include()', 'sendRedirect()', 'getRequestDispatcher()', 'C', 'Servlets & JSP'),
('Which component can intercept and manipulate a request before it reaches a servlet?', 'Filter', 'Listener', 'Cookie', 'Taglib', 'A', 'Servlets & JSP'),
('Which JSP directory is shielded from direct URL access by web browsers?', 'webapp/css/', 'WEB-INF/', 'META-INF/resources/', 'webapp/images/', 'B', 'Servlets & JSP'),
('Which lifecycle method of a Servlet is called only once when the servlet is first instantiated?', 'service()', 'doGet()', 'init()', 'destroy()', 'C', 'Servlets & JSP'),
('How are form parameter values retrieved from an HTTP request inside a Servlet?', 'request.getAttribute()', 'request.getParameter()', 'request.getSession()', 'request.getDispatcher()', 'B', 'Servlets & JSP'),
('Which JSTL core tag is used to escape HTML characters and prevent Cross-Site Scripting (XSS)?', '<c:set>', '<c:out>', '<c:if>', '<c:forEach>', 'B', 'Servlets & JSP'),
('What is the default inactive timeout interval for an HttpSession in web.xml if not specified?', '15 minutes', '30 minutes', '60 minutes', '24 hours', 'B', 'Servlets & JSP'),
('Which method is called on an HttpSession to log out a user and destroy the session?', 'session.clear()', 'session.remove()', 'session.invalidate()', 'session.close()', 'C', 'Servlets & JSP'),
('Which method of FilterChain must be called to pass the request to the next filter or servlet?', 'chain.next()', 'chain.doFilter()', 'chain.proceed()', 'chain.forward()', 'B', 'Servlets & JSP'),

-- === Database & JDBC ===
('Which interface is used to run a precompiled SQL statement safely with parameters?', 'Statement', 'PreparedStatement', 'ResultSet', 'Connection', 'B', 'Database & JDBC'),
('Which JDBC method is used to execute queries like INSERT, UPDATE, and DELETE?', 'executeQuery()', 'executeUpdate()', 'executeBatch()', 'executeRead()', 'B', 'Database & JDBC'),
('Which interface represents the active physical database connection in standard JDBC?', 'java.sql.Driver', 'java.sql.Connection', 'java.sql.Statement', 'java.sql.DriverManager', 'B', 'Database & JDBC'),
('What does ACID stand for in relational database transaction management?', 'Atomicity, Consistency, Isolation, Durability', 'Access, Control, Integrity, Data', 'Application, Client, Interface, Driver', 'Async, Callback, Interval, Dispatch', 'A', 'Database & JDBC'),
('Which SQL clause is used to eliminate duplicate rows in query results?', 'ORDER BY', 'GROUP BY', 'DISTINCT', 'HAVING', 'C', 'Database & JDBC'),
('What is the main purpose of connection pooling in enterprise applications?', 'Encrypt SQL traffic', 'Reuse existing DB connections to avoid creation overhead', 'Backup the database automatically', 'Validate user passwords', 'B', 'Database & JDBC'),
('Which JDBC method returns an auto-generated primary key after an INSERT?', 'ps.getPrimaryKeys()', 'ps.getGeneratedKeys()', 'ps.getAutoIncrement()', 'ps.getLastId()', 'B', 'Database & JDBC'),
('To manage database transactions manually in JDBC, which method should be called on Connection?', 'con.setAutoCommit(false)', 'con.startTransaction()', 'con.beginTransaction()', 'con.disableCommit()', 'A', 'Database & JDBC'),

-- === Core Java ===
('Which Java collection class guarantees unique elements and does not allow duplicates?', 'ArrayList', 'LinkedList', 'HashSet', 'Vector', 'C', 'Core Java'),
('Which keyword in Java prevents a class from being subclassed or a method from being overridden?', 'static', 'abstract', 'final', 'synchronized', 'C', 'Core Java'),
('Which interface should a class implement to allow its instances to be stored in an HttpSession safely across restarts?', 'Cloneable', 'Comparable', 'Serializable', 'Runnable', 'C', 'Core Java'),
('What is the default initial capacity of a standard ArrayList in Java?', '5', '10', '16', '32', 'B', 'Core Java'),
('Which area of JVM memory stores object instances created with the new keyword?', 'Stack', 'Heap', 'Program Counter', 'Native Method Stack', 'B', 'Core Java'),
('Can an abstract class in Java have constructor methods?', 'No, abstract classes cannot have constructors', 'Yes, called during subclass instantiation via super()', 'Only if the constructor is declared private', 'Only in Java 17 and later', 'B', 'Core Java'),
('Which Java feature introduced in Java 8 allows passing behavior/functions as method arguments?', 'Generics', 'Lambda Expressions', 'Annotations', 'Enums', 'B', 'Core Java'),
('What happens when two distinct objects produce the same hashCode() in a HashMap?', 'NullPointerException', 'Hash collision, resolved using linked nodes or balanced trees', 'The previous entry is immediately overwritten', 'OutOfMemoryError', 'B', 'Core Java'),

-- === Web Fundamentals ===
('In MVC, which layer is responsible for database access and business data models?', 'View', 'Controller', 'Model (DAO)', 'web.xml', 'C', 'Web Fundamentals'),
('What HTTP status code represents a resource redirect in the Post/Redirect/Get pattern?', '200 OK', '404 Not Found', '302 Found', '500 Server Error', 'C', 'Web Fundamentals'),
('Which HTTP method is idempotent and intended for retrieving resources without side effects?', 'POST', 'GET', 'DELETE', 'PATCH', 'B', 'Web Fundamentals'),
('What is the main difference between an HTTP Cookie and an HttpSession?', 'Cookies are stored on server, sessions on client', 'Cookies are stored in browser, session data is stored on server', 'Cookies are encrypted, sessions are always plain text', 'There is no difference', 'B', 'Web Fundamentals'),
('Which HTTP response code indicates that a client lacks valid authentication credentials?', '400 Bad Request', '401 Unauthorized', '403 Forbidden', '404 Not Found', 'B', 'Web Fundamentals'),
('What type of security vulnerability occurs when unescaped user input is rendered into HTML?', 'SQL Injection', 'Cross-Site Scripting (XSS)', 'Cross-Site Request Forgery (CSRF)', 'Man-in-the-Middle', 'B', 'Web Fundamentals'),

-- === Python Basics ===
('What is the primary difference between a Python list and a tuple?', 'Lists are immutable; tuples are mutable', 'Lists are mutable; tuples are immutable', 'Tuples can only hold numbers', 'Lists cannot be indexed', 'B', 'Python Basics'),
('Which Python built-in function returns both the index and value during loop iteration?', 'range()', 'zip()', 'enumerate()', 'items()', 'C', 'Python Basics'),
('What is the output of bool([]) in Python?', 'True', 'False', 'None', 'Error', 'B', 'Python Basics'),
('Which keyword is used in Python to define an anonymous inline function?', 'def', 'anon', 'lambda', 'func', 'C', 'Python Basics'),
('What data type is returned by {1, 2, 3} in Python?', 'Dictionary', 'List', 'Set', 'Tuple', 'C', 'Python Basics'),
('How are code blocks indicated in Python syntax?', 'Curly braces {}', 'Indentation with whitespace', 'Square brackets []', 'begin / end keywords', 'B', 'Python Basics');
