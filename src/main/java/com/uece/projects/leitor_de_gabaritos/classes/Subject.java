package com.uece.projects.leitor_de_gabaritos.classes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Subject {

    private final String name;
    private final List<Student> students;
    private final List<Boolean> correctAnswers;

    public Subject(String name, char[] correctAnswers) throws  IllegalArgumentException {
        this.name = name;
        this.students = new CopyOnWriteArrayList<>();
        this.correctAnswers = new CopyOnWriteArrayList<>();
        for (char correctAnswer : correctAnswers) {
            if (Character.toUpperCase(correctAnswer) != 'V' && Character.toUpperCase(correctAnswer) != 'F') {
                throw new IllegalArgumentException("As questões deverão ser respondidas apenas com V ou F");
            }
        }

        for (int i = 0; i < correctAnswers.length; i++) {
            this.correctAnswers.add(Character.toUpperCase(correctAnswers[i]) == 'V');
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
