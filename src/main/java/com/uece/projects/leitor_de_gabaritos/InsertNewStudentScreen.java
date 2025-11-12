package com.uece.projects.leitor_de_gabaritos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.uece.projects.leitor_de_gabaritos.classes.School;
import com.uece.projects.leitor_de_gabaritos.classes.Subject;
import com.uece.projects.leitor_de_gabaritos.classes.exceptions.FileAlreadyCreatedException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
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
            char[] answers = studentAnswersTextField.getText().toUpperCase().toCharArray();
            if (studentNameTextField.getText().equals("") || studentAnswersTextField.getText().equals("")) {
                showErrorWindow("Todos os campos deverão ser preenchidos.");
                return;
            }

            if (answers.length != 10) {
                showErrorWindow("A resposta deve conter exatamente 10 caracteres.");
                return;
            }
            boolean goodInput = true;
            for (char answer : answers) {
                goodInput = (answer == 'V' || answer == 'F');
            }

            if (!goodInput) {
                showErrorWindow("A resposta deve conter apenas V ou F.");
                return;
            }
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

    private void showErrorWindow(String error) {
        try {
            Stage stage = new Stage();
            Scene scene;
            scene = new Scene(new FXMLLoader(App.class.getResource("error_message.fxml")).load());
            stage.initModality(Modality.APPLICATION_MODAL);
            Label messageLabel = (Label) scene.lookup("#messageLabel");
            messageLabel.setText(error);
            Button okButton = (Button) scene.lookup("#cancelButton");
            okButton.setOnAction(eh -> {
                stage.close();
            });

            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void close() {
        Stage stage = (Stage) insertStudentScreenAnchorPane.getScene().getWindow();
        stage.close();
    }
}
