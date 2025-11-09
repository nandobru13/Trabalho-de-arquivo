package com.uece.projects.leitor_de_gabaritos;

import java.io.IOException;

import com.uece.projects.leitor_de_gabaritos.classes.School;
import com.uece.projects.leitor_de_gabaritos.classes.Subject;
import com.uece.projects.leitor_de_gabaritos.classes.exceptions.FileAlreadyCreatedException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
        // Tratar erros de input
        // Adicionar escolha de arquivo
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
        // Button view, Button delete
        for (Subject subject : school.getSubjects()) {
            HBox container = new HBox(10);
            Label subjectName = new Label(subject.getName());
            Button view = new Button("Visualizar");
            Button deleteSubject = new Button("Excluir");

            container.getChildren().add(view);
            container.getChildren().add(subjectName);
            container.getChildren().add(deleteSubject);

            subjectsListVBox.getChildren().add(container);
        }
    }

    public void showAverageList() {
        for (Subject subject : school.getSubjects()) {
            HBox container = new HBox(10);
            Label subjectName = new Label(subject.getName());
            Label subjectAverage;
            if (school.getSujectsAverages().get(subject) != -1) {
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
