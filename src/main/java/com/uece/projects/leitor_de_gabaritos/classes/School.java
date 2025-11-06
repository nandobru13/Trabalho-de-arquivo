package com.uece.projects.leitor_de_gabaritos.classes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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

    public void createSubject(Subject newSubject) throws IOException {
        subjects.add(newSubject);
        File newFile = new File("src/main/resources/com/uece/projects/leitor_de_gabaritos/school_files/"
                + newSubject.getName() + ".txt");

        if (!newFile.exists()) {
            newFile.createNewFile();
        }

        subjectHashMap.put(newSubject, newFile);

        
        try (FileWriter writer = new FileWriter(newFile, false)) {
            StringBuilder sb = new StringBuilder();
            for (boolean answer : newSubject.getCorrectAnswers()) {
                sb.append(answer ? 'V' : 'F');
            }
            sb.append("\tGABARITO").append(System.lineSeparator());
            writer.write(sb.toString());
        }
    }


    public List<File> findSubjectFiles(String folderPath) {
        File folder = new File(folderPath);
        List<File> subjectFiles = new CopyOnWriteArrayList<>();

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
            if (files != null) {
                for (File file : files) {
                    subjectFiles.add(file);
                }
            }
        }
        return subjectFiles;
    }

    public void loadSubjectsFromFiles(String folderPath) throws IOException {
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("A pasta " + folderPath + " não existe ou não é uma pasta válida.");
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) {
            System.out.println("Nenhum arquivo encontrado em " + folderPath);
            return;
        }

        for (File file : files) {
            String subjectName = file.getName().replace(".txt", "");
            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.isEmpty()) continue;

        
            String[] firstLine = lines.get(0).split("\t");
            char[] correctAnswers = firstLine[0].toCharArray();

            Subject subject = new Subject(subjectName, correctAnswers);

       
            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i).split("\t");
                if (parts.length < 2) continue;

                char[] studentAnswers = parts[0].toCharArray();
                String studentName = parts[1];

                Student student = new Student(i, studentName, studentAnswers);
                subject.getStudents().add(student);
            }

            subject.updateScores();
            subjects.add(subject);
            subjectHashMap.put(subject, file);
        }

        System.out.println("Matérias carregadas: " + subjects.size());
    }


    public void addStudentAnswer(Subject subject, String studentName, char[] answers) throws IOException {
        if (!subjectHashMap.containsKey(subject)) {
            System.out.println("Matéria não encontrada nos arquivos.");
            return;
        }

        File subjectFile = subjectHashMap.get(subject);
        int newId = subject.getStudents().size() + 1;

        Student newStudent = new Student(newId, studentName, answers);
        subject.getStudents().add(newStudent);

        subject.updateScores();

     
        try (FileWriter writer = new FileWriter(subjectFile, true)) {
            StringBuilder sb = new StringBuilder();
            for (char c : answers) {
                sb.append(c);
            }
            sb.append("\t").append(studentName).append(System.lineSeparator());
            writer.write(sb.toString());
        }

        System.out.println("Aluno \"" + studentName + "\" adicionado à matéria \"" + subject.getName() + "\".");
    }
}