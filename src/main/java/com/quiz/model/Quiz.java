package com.quiz.model;

import java.io.Serializable;
import java.sql.Timestamp;

/** A teacher-created assessment. */
public class Quiz implements Serializable {
    private int id, teacherId, durationMinutes, passingPercentage, questionCount;
    private String title, description, quizType, subject;
    private boolean published, completed;
    private Timestamp createdAt;
    private Timestamp dueDate;
    private Integer lastScore;
    public int getId() { return id; } public void setId(int v) { id = v; }
    public int getTeacherId() { return teacherId; } public void setTeacherId(int v) { teacherId = v; }
    public int getDurationMinutes() { return durationMinutes; } public void setDurationMinutes(int v) { durationMinutes = v; }
    public int getPassingPercentage() { return passingPercentage; } public void setPassingPercentage(int v) { passingPercentage = v; }
    public int getQuestionCount() { return questionCount; } public void setQuestionCount(int v) { questionCount = v; }
    public String getTitle() { return title; } public void setTitle(String v) { title = v; }
    public String getDescription() { return description; } public void setDescription(String v) { description = v; }
    public String getQuizType() { return quizType; } public void setQuizType(String v) { quizType = v; }
    public String getSubject() { return subject; } public void setSubject(String v) { subject = v; }
    public boolean isPublished() { return published; } public void setPublished(boolean v) { published = v; }
    public Timestamp getCreatedAt() { return createdAt; } public void setCreatedAt(Timestamp v) { createdAt = v; }
    public boolean isPractice() { return "PRACTICE".equals(quizType); }
    public boolean isCompleted() { return completed; } public void setCompleted(boolean v) { completed=v; }
    public Integer getLastScore() { return lastScore; } public void setLastScore(Integer v) { lastScore=v; }
    public Timestamp getDueDate() { return dueDate; } public void setDueDate(Timestamp v) { dueDate=v; }
    public boolean isOverdue() { return dueDate != null && dueDate.before(new java.util.Date()); }
}
