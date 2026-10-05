package com.quiz.model;

import java.sql.Timestamp;

/** One assessment attempt shown only to its quiz owner. */
public class TeacherResult {
    private String studentName, quizName;
    private int score, total, passingPercentage;
    private Timestamp takenAt;
    public String getStudentName() { return studentName; } public void setStudentName(String v) { studentName = v; }
    public String getQuizName() { return quizName; } public void setQuizName(String v) { quizName = v; }
    public int getScore() { return score; } public void setScore(int v) { score = v; }
    public int getTotal() { return total; } public void setTotal(int v) { total = v; }
    public int getPassingPercentage() { return passingPercentage; } public void setPassingPercentage(int v) { passingPercentage = v; }
    public Timestamp getTakenAt() { return takenAt; } public void setTakenAt(Timestamp v) { takenAt = v; }
    public int getPercent() { return total == 0 ? 0 : score * 100 / total; }
    public boolean isPassed() { return getPercent() >= passingPercentage; }
}
