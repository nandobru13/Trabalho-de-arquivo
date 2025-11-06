package com.uece.projects.leitor_de_gabaritos.classes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Student {

    private final int id;
    private final String name;
    private final List<Boolean> answers;
    private final boolean allSameAnswer;
    private int score;

    public Student(int id, String name, char[] answers) {
        this.id = id;
        this.name = name;
        this.answers = new CopyOnWriteArrayList<>();

        int answersCount = 0;
        for (char answer : answers) {
            if (answer == 'V') {
                this.answers.add(true);
                answersCount++;
            } else {
                this.answers.add(false);
                answersCount--;
            }
        }
        
        allSameAnswer = answersCount == 10 || answersCount == -10;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public List<Boolean> getAnswers() {
        return answers;
    }

    public boolean isAllSameAnswer() {
        return allSameAnswer;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
