package com.uece.projects.leitor_de_gabaritos;

import java.io.IOException;

import java.awt.Desktop;
import java.io.File;

import com.uece.projects.leitor_de_gabaritos.classes.School;
import com.uece.projects.leitor_de_gabaritos.classes.Student;
import com.uece.projects.leitor_de_gabaritos.classes.Subject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SubjectStudentsListController {

    @FXML
    VBox studentsListVBox;
    @FXML
    Label subjectNameLabel;
    @FXML
    Label templateLabel;
    @FXML
    Button addNewStudentButton;
    @FXML
    Button openDirectoryButton;
    @FXML
    Button sortByAddButton;
    @FXML
    Button sortByNameButton;
    @FXML
    Button sortByScoreButton;

    @SuppressWarnings("exports")
    public void build(School school, Subject subject) {
        // Show answers by score
        // Show answers by name
        // Add confirmation window
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
        if (!subject.getStudents().isEmpty()) {
            sortByAdd(school, subject);
        } else {
            studentsListVBox.getChildren().add(new Label("Essa matéria ainda não possui alunos."));
        }

        addNewStudentButton.setOnAction(eh -> {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("insert_student_screen.fxml"));
                Parent root = loader.load();
                InsertNewStudentScreen controller = loader.getController();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                controller.build(school, subject, this);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.out.println(e);
            }
        });

        openDirectoryButton.setOnAction(eh -> {
            File subjectTemplateFile = new File(school.getSubjectHashMap().get(subject).getParentFile().getPath() + "/templates/");
            try {
                Desktop.getDesktop().open(subjectTemplateFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        sortByAddButton.setOnAction(eh -> {
            sortByAdd(school, subject);
        });

        sortByNameButton.setOnAction(eh -> {
            try {
                sortByName(school, subject);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        sortByScoreButton.setOnAction(eh -> {
            try {
                sortByScore(school, subject);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void closeWindow() {
        Stage stage = (Stage) addNewStudentButton.getScene().getWindow();
        stage.close();
    }

    @SuppressWarnings("exports")
    public void sortByAdd(School school, Subject subject) {
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
            deleteSubject.setOnAction(eh -> {
                try {
                    school.deleteAnswer(subject, student);
                    this.build(school, subject);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            container.getChildren().add(studentName);
            container.getChildren().add(studentAnswers);
            container.getChildren().add(studentScore);
            container.getChildren().add(deleteSubject);

            studentsListVBox.getChildren().add(container);
        }
    }

    @SuppressWarnings("exports")
    public void sortByScore(School school, Subject subject) throws IOException {
        studentsListVBox.getChildren().clear();
        for (Student student : school.sortSubjectResults(subject, "score")) {
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
            deleteSubject.setOnAction(eh -> {
                try {
                    school.deleteAnswer(subject, student);
                    this.build(school, subject);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            container.getChildren().add(studentName);
            container.getChildren().add(studentAnswers);
            container.getChildren().add(studentScore);
            container.getChildren().add(deleteSubject);

            studentsListVBox.getChildren().add(container);
        }
    }

    @SuppressWarnings("exports")
    public void sortByName(School school, Subject subject) throws IOException {
        studentsListVBox.getChildren().clear();
        for (Student student : school.sortSubjectResults(subject, "name")) {
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
            deleteSubject.setOnAction(eh -> {
                try {
                    school.deleteAnswer(subject, student);
                    this.build(school, subject);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            container.getChildren().add(studentName);
            container.getChildren().add(studentAnswers);
            container.getChildren().add(studentScore);
            container.getChildren().add(deleteSubject);

            studentsListVBox.getChildren().add(container);
        }
    }
}
