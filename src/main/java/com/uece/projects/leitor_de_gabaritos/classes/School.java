package com.uece.projects.leitor_de_gabaritos.classes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.uece.projects.leitor_de_gabaritos.classes.exceptions.FileAlreadyCreatedException;

public class School {

    private final List<Subject> subjects;
    private final List<File> subjectFiles;
    private final HashMap<Subject, File> subjectHashMap;

    public School() {
        this.subjects = new CopyOnWriteArrayList<>();
        this.subjectFiles = new CopyOnWriteArrayList<>();
        this.subjectHashMap = new HashMap<>();
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void createSubject(Subject newSubject) throws IOException, FileAlreadyCreatedException {
        findSubjectFiles();

        for (File subjectFile : subjectFiles) {
            if (subjectFile.getName().equals(newSubject.getName() + ".txt")) {
                throw new FileAlreadyCreatedException(newSubject.getName());
            }
        }

        subjects.add(newSubject);
        File newFile = new File("src/main/resources/com/uece/projects/leitor_de_gabaritos/school_files/"
                + newSubject.getName() + ".txt");

        if (!newFile.exists()) {
            newFile.createNewFile();

            try (FileWriter writer = new FileWriter(newFile, false)) {
                StringBuilder sb = new StringBuilder();
                for (boolean answer : newSubject.getCorrectAnswers()) {
                    sb.append(answer ? 'V' : 'F');
                }
                sb.append(",GABARITO").append(System.lineSeparator());
                writer.write(sb.toString());
            }
        }

        subjectHashMap.put(newSubject, newFile);
        for (Subject subject : subjects) {
            System.out.println(subject.getName());
        }
    }

    private void findSubjectFiles() throws IOException {
        File folder = new File("src/main/resources/com/uece/projects/leitor_de_gabaritos/school_files/");

        if (folder.exists() && folder.isDirectory()) {
            subjectFiles.clear();
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
            if (files != null) {
                subjectFiles.addAll(Arrays.asList(files));
            }
        }

        loadSubjectsFromFiles();
    }

    private void loadSubjectsFromFiles() throws IOException {

        for (File file : subjectFiles) {
            String subjectName = file.getName().replace(".txt", "");
            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.isEmpty()) {
                continue;
            }

            String[] firstLine = lines.get(0).split(",");
            char[] correctAnswers = firstLine[0].toCharArray();

            Subject subject = new Subject(subjectName, correctAnswers);

            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts.length < 2) {
                    continue;
                }

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

    public void showAllAnswers() {
        for (Subject subject : subjects) {
            subject.updateScores();
            System.out.println("---- " + subject.getName() + " ------");
            System.out.print("GABARITO: ");
            for (Boolean answer : subject.getCorrectAnswers()) {
                System.out.print(answer ? 'V' : 'F');
            }
            System.out.println("");
            for (Student student : subject.getStudents()) {
                System.out.println("Resposta: " + student.getAnswers() + ", " + student.getScore());
            }
        }
    }

    private void addStudentAnswer(Subject subject, String studentName, char[] answers) throws IOException {
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
