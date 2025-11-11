package com.uece.projects.leitor_de_gabaritos;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import com.uece.projects.leitor_de_gabaritos.classes.School;
import com.uece.projects.leitor_de_gabaritos.classes.Student;
import com.uece.projects.leitor_de_gabaritos.classes.Subject;
import com.uece.projects.leitor_de_gabaritos.classes.exceptions.FileAlreadyCreatedException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainScreenController {

    School school = new School();

    @FXML
    TextField subjectTemplateTextField;
    @FXML
    TextField subjectNameTextField;
    @FXML
    VBox averageListVBox;
    @FXML
    VBox subjectsListVBox;
    @FXML
    Button addFileButton;
    @FXML
    Button addSubjectButton;

    public void addSubject() {
        // Input Errors
        // Choose file option
        String subjectName = subjectNameTextField.getText();
        char[] subjectTemplate = subjectTemplateTextField.getText().toCharArray();
        Subject newSubject = new Subject(subjectName, subjectTemplate);

        try {
            school.createSubject(newSubject);
        } catch (IOException | FileAlreadyCreatedException e) {
            System.out.println(e);
        }
    }

    public void showSubjectsList() {
        subjectsListVBox.getChildren().clear();
        for (Subject subject : school.getSubjects()) {
            HBox container = new HBox(10);
            Label subjectName = new Label(subject.getName());
            Button view = new Button("Visualizar");
            Button deleteSubject = new Button("Excluir");

            view.setOnAction(eh -> {
                try {
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("student_list_screen.fxml"));
                    Parent root = loader.load();
                    SubjectStudentsListController controller = loader.getController();
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    controller.build(school, subject);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    System.out.println(e);
                }
            });
            
            deleteSubject.setOnAction(eh -> {
            // Add confirmation window
                try {
                    school.deleteSubject(subject);
                    showSubjectsList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            container.getChildren().add(view);
            container.getChildren().add(subjectName);
            container.getChildren().add(deleteSubject);

            subjectsListVBox.getChildren().add(container);
        }
    }

    public void showAverageList() {
        // Show in score order
        averageListVBox.getChildren().clear();
        if(school.getSujectsAverages().isEmpty()) {
            averageListVBox.getChildren().add(new Label("Não há matérias no momento."));
        } else {
            for (Subject subject : school.getSubjects()) {
                HBox container = new HBox(10);
                Label subjectName = new Label(subject.getName());
                Label subjectAverage;
                if (!school.getSujectsAverages().get(subject).equals("N/A")) {
                    subjectAverage = new Label(String.valueOf(school.getSujectsAverages().get(subject)));
                } else {
                    subjectAverage = new Label("Sem respostas.");
                }

                container.getChildren().add(subjectName);
                container.getChildren().add(subjectAverage);

                averageListVBox.getChildren().add(container);
            }
        }
    }

}
