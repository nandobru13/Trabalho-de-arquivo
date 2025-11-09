package com.uece.projects.leitor_de_gabaritos;

import com.uece.projects.leitor_de_gabaritos.classes.Student;
import com.uece.projects.leitor_de_gabaritos.classes.Subject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SubjectStudentsListController {

    @FXML
    VBox studentsListVBox;
    @FXML
    Label subjectNameLabel;
    @FXML
    Label templateLabel;

    public void build(Subject subject) {
        // Delete student
        // Show answers by score
        // Show answers by name
        // Add new Student
        // Close window
        subjectNameLabel.setText(subject.getName());
        String template = "Gabarito: ";
        for (Boolean answer : subject.getCorrectAnswers()) {
            if (answer) {
                template = template.concat("V");
            } else {
                template = template.concat("F");
            }
        }
        templateLabel.setText(template);

        studentsListVBox.getChildren().clear();
        for (Student student : subject.getStudents()) {
            HBox container = new HBox(10);
            Label studentName = new Label(student.getName());
            String answers = "Respostas: ";
            for (Boolean answer : student.getAnswers()) {
                if (answer) {
                    answers = answers.concat("V");
                } else {
                    answers = answers.concat("F");
                }
            }
            answers = answers.concat(".");
            Label studentAnswers = new Label(answers);
            Label studentScore = new Label("Nota: " + String.valueOf(student.getScore()));

            Button deleteSubject = new Button("Excluir");

            container.getChildren().add(studentName);
            container.getChildren().add(studentAnswers);
            container.getChildren().add(studentScore);
            container.getChildren().add(deleteSubject);

            studentsListVBox.getChildren().add(container);
        }
    }
}
