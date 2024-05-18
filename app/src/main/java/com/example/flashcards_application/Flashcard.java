package com.example.flashcards_application;

public class Flashcard {
    private String question;
    private String answer;
    private final long id;

    public Flashcard(long id,String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public long getId() {
        return id;
    }
    public String getQuestion() {
        return question;
    }
    public String getAnswer() {
        return answer;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
