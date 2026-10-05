package com.quiz.model;

import java.io.Serializable;
import java.sql.Timestamp;

/** A saved quiz attempt. */
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int userId;
    private int score;
    private int total;
    private Timestamp takenAt;
    private String category;
    private Integer quizId;
    private Integer passingPercentage;
    private String quizType;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    public Timestamp getTakenAt() { return takenAt; }
    public void setTakenAt(Timestamp takenAt) { this.takenAt = takenAt; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getQuizId() { return quizId; }
    public void setQuizId(Integer quizId) { this.quizId = quizId; }
    public Integer getPassingPercentage() { return passingPercentage; }
    public void setPassingPercentage(Integer v) { passingPercentage = v; }
    public String getQuizType() { return quizType; }
    public void setQuizType(String v) { quizType = v; }
    public boolean isAssessment() { return quizId != null; }
    public boolean isPracticeAssessment() { return isAssessment() && "PRACTICE".equals(quizType); }
    public boolean isPassed() { return getPercent() >= (passingPercentage == null ? 50 : passingPercentage); }

    public int getPercent() { return total == 0 ? 0 : (score * 100) / total; }
}
