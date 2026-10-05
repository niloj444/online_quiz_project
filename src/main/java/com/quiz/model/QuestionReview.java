package com.quiz.model;

import java.io.Serializable;

/** Represents a single question's review after submission. */
public class QuestionReview implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Question question;
    private final String chosenOption;
    private final boolean correct;

    public QuestionReview(Question question, String chosenOption) {
        this.question = question;
        this.chosenOption = chosenOption;
        this.correct = question != null && question.getCorrectOption() != null
                && question.getCorrectOption().equalsIgnoreCase(chosenOption);
    }

    public Question getQuestion() { return question; }
    public String getChosenOption() { return chosenOption; }
    public boolean isCorrect() { return correct; }

    public String getChosenText() {
        if (chosenOption == null) return "Not answered";
        switch (chosenOption.toUpperCase()) {
            case "A": return question.getOptionA();
            case "B": return question.getOptionB();
            case "C": return question.getOptionC();
            case "D": return question.getOptionD();
            default: return "Not answered";
        }
    }

    public String getCorrectText() {
        if (question == null || question.getCorrectOption() == null) return "";
        switch (question.getCorrectOption().toUpperCase()) {
            case "A": return question.getOptionA();
            case "B": return question.getOptionB();
            case "C": return question.getOptionC();
            case "D": return question.getOptionD();
            default: return "";
        }
    }
}
