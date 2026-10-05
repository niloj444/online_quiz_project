-- Run once after assessment-migration.sql on an existing quizdb database.
USE quizdb;
ALTER TABLE quizzes ADD COLUMN subject VARCHAR(80) NOT NULL DEFAULT 'General' AFTER teacher_id;
CREATE TABLE notifications (
  id INT AUTO_INCREMENT PRIMARY KEY,
  teacher_id INT NOT NULL,
  quiz_id INT NOT NULL,
  title VARCHAR(150) NOT NULL,
  message VARCHAR(500) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);
