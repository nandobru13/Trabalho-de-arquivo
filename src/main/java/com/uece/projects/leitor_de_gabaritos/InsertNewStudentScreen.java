package com.uece.projects.leitor_de_gabaritos;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.uece.projects.leitor_de_gabaritos.classes.School;
import com.uece.projects.leitor_de_gabaritos.classes.Subject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class InsertNewStudentScreen {

    @FXML
    TextField studentNameTextField;
    @FXML
    TextField studentAnswersTextField;
    @FXML
    Button saveButton;
    @FXML
    AnchorPane insertStudentScreenAnchorPane;

    public void build(@SuppressWarnings("exports") School school, @SuppressWarnings("exports") Subject subject, SubjectStudentsListController listController) {
        saveButton.setOnAction(eh -> {
            char[] answers = studentAnswersTextField.getText().toCharArray();
            try {
                school.addStudentAnswer(subject.getName(), studentNameTextField.getText(), answers);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            listController.build(school, subject);
            close();
        });
    }

    public void close() {
        Stage stage = (Stage) insertStudentScreenAnchorPane.getScene().getWindow();
        stage.close();
    }
}
