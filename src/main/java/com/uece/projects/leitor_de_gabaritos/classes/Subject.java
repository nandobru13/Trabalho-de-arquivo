package com.uece.projects.leitor_de_gabaritos.classes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Subject {

    private final String name;
    private final List<Student> students;
    private final boolean[] correctAnswers;

    // tratar erro caso correctAnswers.length > 10
    public Subject(String name, char[] correctAnswers) {
        this.name = name;
        this.students = new CopyOnWriteArrayList<>();
        this.correctAnswers = new boolean[10];

        for (int i = 0; i < this.correctAnswers.length; i++) {
            this.correctAnswers[i] = correctAnswers[i] == 'V';
        }
    }

    public String getName() {
        return name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public boolean[] getCorrectAnswers() {
        return correctAnswers;
    }

    public void updateScores() {
        for (Student student : students) {
            if (student.isAllSameAnswer()) {
                student.setScore(0);
                continue;
            }
            for (boolean answer : student.getAnswers()) {
                if (answer == correctAnswers[student.getAnswers().indexOf(answer)]) {
                    student.setScore(student.getScore() + 1);
                } else {
                    if (student.getScore() > 0) {
                        student.setScore(student.getScore() - 1);
                    }
                }
            }
        }
    }
}
