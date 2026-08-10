package com;

import java.util.*;
import java.util.stream.*;

// Custom functional interface (Java 8 allows default/static methods too)
@FunctionalInterface
interface GradeCalculator {
    char calculate(double average);

    // default method combining behavior
    default String describe(double average) {
        return "Average " + average + " => Grade " + calculate(average);
    }
}
class Student {
    private String name;
    private List<Double> scores;

    public Student(String name, List<Double> scores) {
        this.name = name;
        this.scores = scores;
    }

    public String getName() { return name; }
    public List<Double> getScores() { return scores; }

    public double getAverage() {
        return scores.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public String toString() {
        return String.format("%-10s | Avg: %.2f", name, getAverage());
    }
}

public class StudentGradeAnalyzer {

    private List<Student> students;
    // Lambda implementation of the custom functional interface
    private GradeCalculator gradeCalculator = average -> {
        if (average >= 90) return 'A';
        else if (average >= 75) return 'B';
        else if (average >= 60) return 'C';
        else if (average >= 40) return 'D';
        else return 'F';
    };

    public StudentGradeAnalyzer() {
        students = new ArrayList<>();
        loadSampleData();
    }

    private void loadSampleData() {
        students.add(new Student("Aarav", Arrays.asList(88.0, 92.0, 79.0, 95.0)));
        students.add(new Student("Bina", Arrays.asList(45.0, 60.0, 55.0, 50.0)));
        students.add(new Student("Chirag", Arrays.asList(72.0, 68.0, 74.0, 71.0)));
        students.add(new Student("Deepa", Arrays.asList(95.0, 98.0, 91.0, 99.0)));
        students.add(new Student("Esha", Arrays.asList(33.0, 40.0, 38.0, 45.0)));
        students.add(new Student("Farhan", Arrays.asList(61.0, 64.0, 58.0, 66.0)));
    }
    // Print statistics using DoubleSummaryStatistics
    public void printClassStatistics() {
        DoubleSummaryStatistics stats = students.stream()
                .mapToDouble(Student::getAverage)
                .summaryStatistics();

        System.out.printf("Count: %d, Min: %.2f, Max: %.2f, Average: %.2f, Sum: %.2f%n",
                stats.getCount(), stats.getMin(), stats.getMax(), stats.getAverage(), stats.getSum());
    }
    // Group students by grade letter using Collectors.groupingBy
    public Map<Character, List<String>> groupByGrade() {
        return students.stream()
                .collect(Collectors.groupingBy(
                        s -> gradeCalculator.calculate(s.getAverage()),
                        Collectors.mapping(Student::getName, Collectors.toList())
                ));
    }
    // Top N students sorted by average descending
    public List<Student> topStudents(int n) {
        return students.stream()
                .sorted((s1, s2) -> Double.compare(s2.getAverage(), s1.getAverage()))
                .limit(n)
                .collect(Collectors.toList());
    }
    // Students who need improvement (average below passing threshold)
    public List<Student> studentsNeedingSupport(double passMark) {
        return students.stream()
                .filter(s -> s.getAverage() < passMark)
                .collect(Collectors.toList());
    }

    public void printAllGradeDescriptions() {
        students.forEach(s ->
                System.out.println(s.getName() + " -> " + gradeCalculator.describe(s.getAverage())));
    }

    public static void main(String[] args) {
        StudentGradeAnalyzer analyzer = new StudentGradeAnalyzer();

        System.out.println("=== Class Statistics ===");
        analyzer.printClassStatistics();

        System.out.println("\n=== Grade Descriptions (Functional Interface + default method) ===");
        analyzer.printAllGradeDescriptions();

        System.out.println("\n=== Students Grouped by Grade ===");
        analyzer.groupByGrade().forEach((grade, names) ->
                System.out.println("Grade " + grade + ": " + names));

        System.out.println("\n=== Top 3 Students ===");
        analyzer.topStudents(3).forEach(System.out::println);

        System.out.println("\n=== Students Needing Support (avg < 60) ===");
        List<Student> support = analyzer.studentsNeedingSupport(60);
        if (support.isEmpty()) {
            System.out.println("None! Everyone is passing.");
        } else {
            support.forEach(System.out::println);
        }
    }

}

