-- Run once after earlier assessment migrations for an existing database.
USE quizdb;
ALTER TABLE quizzes ADD COLUMN due_date DATETIME NULL AFTER published;
