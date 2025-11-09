package com.uece.projects.leitor_de_gabaritos;

import java.io.IOException;

import com.uece.projects.leitor_de_gabaritos.classes.School;
import com.uece.projects.leitor_de_gabaritos.classes.Subject;
import com.uece.projects.leitor_de_gabaritos.classes.exceptions.FileAlreadyCreatedException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

public class MainScreenController {

    School school = new School();

    @FXML
    TextField subjectTemplateTextField;
    @FXML
    TextField subjectNameTextField;
    @FXML
    ScrollPane averageListScrollPane;
    @FXML
    ScrollPane subjectsListScrollPane;
    @FXML
    Button addFileButton;
    @FXML
    Button addSubjectButton;

    public void addSubject() {
        String subjectName = subjectNameTextField.getText();
        char[] subjectTemplate = subjectTemplateTextField.getText().toCharArray();
        Subject newSubject = new Subject(subjectName, subjectTemplate);

        try {
            school.createSubject(newSubject);
        } catch (IOException | FileAlreadyCreatedException e) {
            System.out.println(e);
        }
    }

}
