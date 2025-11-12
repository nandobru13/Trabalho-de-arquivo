package com.uece.projects.leitor_de_gabaritos;

import java.io.IOException;

import java.awt.Desktop;
import java.io.File;
import java.util.List;

import com.uece.projects.leitor_de_gabaritos.classes.School;
import com.uece.projects.leitor_de_gabaritos.classes.Student;
import com.uece.projects.leitor_de_gabaritos.classes.Subject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

        try {
            sort(school, subject, "add");
        } catch (IOException e) {
            e.printStackTrace();
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
            try {
                sort(school, subject, "add");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        sortByNameButton.setOnAction(eh -> {
            try {
                sort(school, subject, "name");
            } catch (IOException e) {
                studentsListVBox.getChildren().clear();
                studentsListVBox.getChildren().add(new Label("Não há respostas no momento."));
            }
        });

        sortByScoreButton.setOnAction(eh -> {
            try {
                sort(school, subject, "score");
            } catch (IOException e) {
            studentsListVBox.getChildren().clear();
                studentsListVBox.getChildren().add(new Label("Não há respostas no momento."));
            }
        });
    }

    public void closeWindow() {
        Stage stage = (Stage) addNewStudentButton.getScene().getWindow();
        stage.close();
    }

    @SuppressWarnings("exports")
    public void sort(School school, Subject subject, String mode) throws IOException {
        studentsListVBox.getChildren().clear();
        List<Student> studentsList;
        switch (mode) {
            case "add" ->
                studentsList = subject.getStudents();
            default ->
                studentsList = school.sortSubjectResults(subject, mode);
        }
        if (!studentsList.isEmpty()) {
            for (Student student : studentsList) {
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
                deleteSubject.setOnAction(e -> openConfirmationWindow(eh -> {
                    try {
                        school.deleteAnswer(subject, student);
                        this.build(school, subject);
                    } catch (IOException er) {
                        er.printStackTrace();
                    }
                }));

                container.getChildren().add(studentName);
                container.getChildren().add(studentAnswers);
                container.getChildren().add(studentScore);
                container.getChildren().add(deleteSubject);

                studentsListVBox.getChildren().add(container);
            }
        } else {
            studentsListVBox.getChildren().add(new Label("Não há respostas no momento."));
        }
    }

    private void openConfirmationWindow(EventHandler<ActionEvent> e) {
        try {
            Stage stage = new Stage();
            Scene scene;
            scene = new Scene(new FXMLLoader(App.class.getResource("confirmation_screen.fxml")).load());
            stage.initModality(Modality.APPLICATION_MODAL);
            Button confirmButton = (Button) scene.lookup("#confirmButton");
            Button cancelButton = (Button) scene.lookup("#cancelButton");

            confirmButton.setOnAction(eh -> {
                e.handle(eh);
                stage.close();
            });
            cancelButton.setOnAction(eh -> {
                stage.close();
            });

            stage.setScene(scene);
            stage.show();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
