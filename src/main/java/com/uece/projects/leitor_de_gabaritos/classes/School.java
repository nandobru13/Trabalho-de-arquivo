package com.uece.projects.leitor_de_gabaritos.classes;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class School {

    private final List<Subject> subjects;
    private final HashMap<Subject, File> subjectHashMap;

    public School() {
        this.subjects = new CopyOnWriteArrayList<>();
        this.subjectHashMap = new HashMap<>();
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void createSubject(Subject newSubject) {
        subjects.add(newSubject);
        File newFile = new File("src/main/resources/com/uece/projects/leitor_de_gabaritos/school_files/"
                + newSubject.getName() + ".txt");
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        subjectHashMap.put(newSubject, newFile);
    }
}
