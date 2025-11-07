package com.uece.projects.leitor_de_gabaritos;

import java.io.IOException;

import com.uece.projects.leitor_de_gabaritos.classes.School;
import com.uece.projects.leitor_de_gabaritos.classes.Subject;
import com.uece.projects.leitor_de_gabaritos.classes.exceptions.FileAlreadyCreatedException;


public class Test {

    public static void main(String[] args) {
        School school = new School();
        char[] calculoCorrectAnswers = {'V', 'F', 'V', 'F', 'V', 'F', 'V', 'F', 'V', 'F'};
        Subject calculo = new Subject("algebra_linear", calculoCorrectAnswers);

        try {
            school.createSubject(calculo);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileAlreadyCreatedException ex) {
            ex.toString();
        }
        
        char[] raimundoAnswers = {'V', 'F', 'V', 'F', 'V', 'F', 'V', 'F', 'V', 'F'};
        try {
            school.addStudentAnswer("algebra_linear", "Raimundo", raimundoAnswers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        school.showAllAnswers();
    }
}
