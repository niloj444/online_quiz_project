-- Run once:  mysql -u root -p < sql/schema.sql
CREATE DATABASE IF NOT EXISTS quizdb CHARACTER SET utf8mb4;
USE quizdb;

CREATE TABLE IF NOT EXISTS users (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  name          VARCHAR(80)  NOT NULL,
  email         VARCHAR(120) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role          ENUM('USER','TEACHER','ADMIN') NOT NULL DEFAULT 'USER'
);

CREATE TABLE IF NOT EXISTS questions (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  question_text  VARCHAR(500) NOT NULL,
  option_a       VARCHAR(200) NOT NULL,
  option_b       VARCHAR(200) NOT NULL,
  option_c       VARCHAR(200) NOT NULL,
  option_d       VARCHAR(200) NOT NULL,
  correct_option CHAR(1)      NOT NULL CHECK (correct_option IN ('A','B','C','D')),
  category       VARCHAR(80)  NOT NULL DEFAULT 'Core Java',
  created_by     INT NULL,
  FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS quizzes (
  id                 INT AUTO_INCREMENT PRIMARY KEY,
  teacher_id         INT NOT NULL,
  subject            VARCHAR(80) NOT NULL DEFAULT 'General',
  title              VARCHAR(150) NOT NULL,
  description        VARCHAR(500) NULL,
  quiz_type          ENUM('PRACTICE','EXAM') NOT NULL DEFAULT 'PRACTICE',
  duration_minutes   INT NOT NULL DEFAULT 10,
  passing_percentage INT NOT NULL DEFAULT 50,
  published          BOOLEAN NOT NULL DEFAULT FALSE,
  due_date           DATETIME NULL,
  created_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS notifications (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  teacher_id INT NOT NULL,
  quiz_id    INT NOT NULL,
  title      VARCHAR(150) NOT NULL,
  message    VARCHAR(500) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz_questions (
  quiz_id     INT NOT NULL,
  question_id INT NOT NULL,
  position    INT NOT NULL DEFAULT 0,
  PRIMARY KEY (quiz_id, question_id),
  FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
  FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS results (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  user_id    INT NOT NULL,
  category   VARCHAR(80) NOT NULL DEFAULT 'All Topics',
  score      INT NOT NULL,
  total      INT NOT NULL,
  quiz_id    INT NULL,
  passing_percentage INT NULL,
  quiz_type  ENUM('PRACTICE','EXAM') NULL,
  taken_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE SET NULL
);
