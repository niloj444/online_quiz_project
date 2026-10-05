package com.quiz.model;

import java.io.Serializable;
import java.sql.Timestamp;

/** DTO combining Result and User information for Admin overview. */
public class UserResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int userId;
    private String userName;
    private String userEmail;
    private int score;
    private int total;
    private Timestamp takenAt;
    private String category;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public Timestamp getTakenAt() { return takenAt; }
    public void setTakenAt(Timestamp takenAt) { this.takenAt = takenAt; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getPercent() { return total == 0 ? 0 : (score * 100) / total; }
}
