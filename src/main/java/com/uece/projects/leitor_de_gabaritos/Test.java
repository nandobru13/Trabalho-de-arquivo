package com.uece.projects.leitor_de_gabaritos;

import java.io.IOException;

import com.uece.projects.leitor_de_gabaritos.classes.School;
import com.uece.projects.leitor_de_gabaritos.classes.Subject;
import com.uece.projects.leitor_de_gabaritos.classes.exceptions.FileAlreadyCreatedException;


public class Test {

    public static void main(String[] args) {
        School school = new School();
        char[] pooCorrectAnswers = {'V', 'F', 'V', 'F', 'V', 'F', 'V', 'F', 'V', 'F'};
        Subject poo = new Subject("poo", pooCorrectAnswers);

        try {
            school.createSubject(poo);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileAlreadyCreatedException ex) {
            ex.toString();
        }
        school.showAllAnswers();
    }
}
