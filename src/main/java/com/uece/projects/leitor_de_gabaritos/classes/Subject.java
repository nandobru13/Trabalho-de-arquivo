package com.uece.projects.leitor_de_gabaritos.classes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Subject {

    private final String name;
    private final List<Student> students;
    private final List<Boolean> correctAnswers;

    // tratar erro caso correctAnswers.length > 10
    public Subject(String name, char[] correctAnswers) {
        this.name = name;
        this.students = new CopyOnWriteArrayList<>();
        this.correctAnswers = new CopyOnWriteArrayList<>();

        for (int i = 0; i < correctAnswers.length; i++) {
            this.correctAnswers.add(correctAnswers[i] == 'V');
        }
    }

    public String getName() {
        return name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Boolean> getCorrectAnswers() {
        return correctAnswers;
    }

    public void updateScores() {
        for (Student student : students) {
            student.setScore(0);
            if (!student.isAllSameAnswer()) {
                for (int i = 0; i < correctAnswers.size(); i++) {
                    if (student.getAnswers().get(i).equals(correctAnswers.get(i))) {
                        student.setScore(student.getScore() + 1);
                    }
                }
            }
        }
    }
}
