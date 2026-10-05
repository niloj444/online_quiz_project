-- Run this once for an already-created quizdb database before deploying this update.
USE quizdb;
ALTER TABLE users MODIFY role ENUM('USER','TEACHER','ADMIN') NOT NULL DEFAULT 'USER';
ALTER TABLE questions ADD COLUMN created_by INT NULL,
  ADD CONSTRAINT fk_questions_creator FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL;
CREATE TABLE quizzes (
  id INT AUTO_INCREMENT PRIMARY KEY, teacher_id INT NOT NULL, title VARCHAR(150) NOT NULL,
  description VARCHAR(500) NULL, quiz_type ENUM('PRACTICE','EXAM') NOT NULL DEFAULT 'PRACTICE',
  duration_minutes INT NOT NULL DEFAULT 10, passing_percentage INT NOT NULL DEFAULT 50,
  published BOOLEAN NOT NULL DEFAULT FALSE, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE quiz_questions (
  quiz_id INT NOT NULL, question_id INT NOT NULL, position INT NOT NULL DEFAULT 0,
  PRIMARY KEY (quiz_id, question_id), FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
  FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);
ALTER TABLE results ADD COLUMN quiz_id INT NULL, ADD COLUMN passing_percentage INT NULL,
  ADD COLUMN quiz_type ENUM('PRACTICE','EXAM') NULL,
  ADD CONSTRAINT fk_results_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE SET NULL;

-- Promote a registered user when needed:
-- UPDATE users SET role='TEACHER' WHERE email='teacher@example.com';
