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

