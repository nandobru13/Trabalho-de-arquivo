package com.uece.projects.leitor_de_gabaritos.classes.exceptions;

public class FileAlreadyCreatedException extends Exception {

    private final String fileName;

    public FileAlreadyCreatedException(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "A disciplina " + fileName + " já existe.";
    }

}
