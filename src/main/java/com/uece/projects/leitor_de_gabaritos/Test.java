package com.uece.projects.leitor_de_gabaritos;

import com.uece.projects.leitor_de_gabaritos.classes.School;
import com.uece.projects.leitor_de_gabaritos.classes.Subject;

public class Test {

    public static void main(String[] args) {
        School school = new School();
        char[] pooCorrectAnswers = {'V', 'F', 'V', 'F', 'V', 'F', 'V', 'F', 'V', 'F'};
        Subject poo = new Subject("poo", pooCorrectAnswers);
        
        school.createSubject(poo);
    }
}
