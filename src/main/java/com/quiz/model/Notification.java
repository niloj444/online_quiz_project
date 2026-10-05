package com.quiz.model;
import java.sql.Timestamp;
public class Notification {
 private int id, quizId; private String title, message; private Timestamp createdAt;
 public int getId(){return id;} public void setId(int v){id=v;} public int getQuizId(){return quizId;} public void setQuizId(int v){quizId=v;}
 public String getTitle(){return title;} public void setTitle(String v){title=v;} public String getMessage(){return message;} public void setMessage(String v){message=v;}
 public Timestamp getCreatedAt(){return createdAt;} public void setCreatedAt(Timestamp v){createdAt=v;}
}
