package com.uece.projects.leitor_de_gabaritos.classes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
        File templateFile = new File("src/main/resources/com/uece/projects/leitor_de_gabaritos/school_files/templates/"
                + newSubject.getName() + "_template.txt");
        File newFile = new File("src/main/resources/com/uece/projects/leitor_de_gabaritos/school_files/"
                + newSubject.getName() + ".txt");

        if (!templateFile.exists()) {
            templateFile.createNewFile();

            try (FileWriter writer = new FileWriter(templateFile, false)) {
                StringBuilder sb = new StringBuilder();
                for (boolean answer : newSubject.getCorrectAnswers()) {
                    sb.append(answer ? 'V' : 'F');
                }
                sb.append(",GABARITO").append(System.lineSeparator());
                writer.write(sb.toString());
                writer.close();
            }

        }
        if (!newFile.exists()) {
            newFile.createNewFile();
        }
        subjectHashMap.put(newSubject, newFile);
    }

    private void createSubjectResultsFiles(Subject subject) throws IOException {
        File resultFolder = new File("src/main/resources/com/uece/projects/leitor_de_gabaritos/school_files/results/"
                + subject.getName() + "/");
        File resultFile = new File("src/main/resources/com/uece/projects/leitor_de_gabaritos/school_files/results/"
                + subject.getName() + "/" + subject.getName() + "_results.txt");

        if (!resultFolder.exists()) {
            resultFolder.mkdir();
        }

        if (!resultFile.exists()) {
            resultFile.createNewFile();
        }

        try (FileWriter fw = new FileWriter(resultFile, false); BufferedWriter bw = new BufferedWriter(fw)) {
            for (Student student : subject.getStudents()) {
                bw.write(student.getName() + "," + student.getScore());
                bw.newLine();
            }
        }
        
        sortSubjectResults(subject, "score");
        sortSubjectResults(subject, "name");
    }

    private void sortSubjectResults(Subject subject, String chooseMode) throws IOException {
        File resultFile = new File("src/main/resources/com/uece/projects/leitor_de_gabaritos/school_files/results/"
                + subject.getName() + "/" + subject.getName() + "_results_by_" + chooseMode +".txt");

        if (!resultFile.exists()) {
            resultFile.createNewFile();
        }
        
        List<Student> sortedList = new CopyOnWriteArrayList<>(subject.getStudents());
        
        if(chooseMode.equals("name")) {
            sortedList.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
        } else if (chooseMode.equals("score")) {
            Collections.sort(sortedList, Comparator.comparing(Student::getName).reversed());
        }

        try (FileWriter fw = new FileWriter(resultFile, false); BufferedWriter bw = new BufferedWriter(fw)) {
            for (Student student : sortedList) {
                bw.write(student.getName() + "," + student.getScore());
                bw.newLine();
            }
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
        subjects.clear();
        for (File file : subjectFiles) {
            String subjectName = file.getName().replace(".txt", "");
            List<String> lines = Files.readAllLines(file.toPath());
            List<String> template = Files.readAllLines(Path.of(file.toPath().getParent().toString() + "/templates/" + subjectName + "_template.txt"));

            String[] templateLine = template.get(0).split(",");
            char[] correctAnswers = templateLine[0].toCharArray();

            Subject subject = new Subject(subjectName, correctAnswers);

            if (!lines.isEmpty()) {
                for (int i = 0; i < lines.size(); i++) {
                    String[] parts = lines.get(i).split(",");
                    if (parts.length < 2) {
                        continue;
                    }

                    char[] studentAnswers = parts[0].toCharArray();
                    String studentName = parts[1];

                    Student student = new Student(i, studentName, studentAnswers);
                    subject.getStudents().add(student);
                }
            }

            subject.updateScores();
            subjects.add(subject);
            subjectHashMap.put(subject, file);
        }
    }

    public void showAllAnswers() throws IOException {
        for (Subject subject : subjects) {
            subject.updateScores();
            createSubjectResultsFiles(subject);
        }
    }

    public void addStudentAnswer(String subjectName, String studentName, char[] answers) throws IOException, FileNotFoundException {

        if (!subjectHashMap.containsKey(searchSubject(subjectName))) {
            throw new FileNotFoundException();
        }

        int newId = searchSubject(subjectName).getStudents().size() + 1;
        Student newStudent = new Student(newId, studentName, answers);
        searchSubject(subjectName).getStudents().add(newStudent);
        searchSubject(subjectName).updateScores();

        addStudentToFile(searchSubject(subjectName), studentName, answers);
    }

    private void addStudentToFile(Subject subject, String studentName, char[] answers) throws IOException {
        File subjectFile = subjectHashMap.get(subject);

        try (FileWriter fw = new FileWriter(subjectFile, true)) {
            StringBuilder sb = new StringBuilder();
            for (char c : answers) {
                sb.append(c);
            }
            sb.append(",").append(studentName).append(System.lineSeparator());
            fw.write(sb.toString());
            fw.close();
        }
    }

    private Subject searchSubject(String subjectName) {
        for (Subject subject : subjects) {
            if (subjectName.equals(subject.getName())) {
                return subject;
            }
        }
        return null;
    }
}
