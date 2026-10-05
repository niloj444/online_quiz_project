package com.quiz.model;

import java.io.Serializable;

/** One multiple-choice question with four options. */
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String questionText;
    private String optionA, optionB, optionC, optionD;
    private String correctOption; // "A", "B", "C" or "D"
    private String category;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String q) { this.questionText = q; }
    public String getOptionA() { return optionA; }
    public void setOptionA(String s) { this.optionA = s; }
    public String getOptionB() { return optionB; }
    public void setOptionB(String s) { this.optionB = s; }
    public String getOptionC() { return optionC; }
    public void setOptionC(String s) { this.optionC = s; }
    public String getOptionD() { return optionD; }
    public void setOptionD(String s) { this.optionD = s; }
    public String getCorrectOption() { return correctOption; }
    public void setCorrectOption(String c) { this.correctOption = c; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
