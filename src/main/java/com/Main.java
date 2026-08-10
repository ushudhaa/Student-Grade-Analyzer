package com;

import java.util.*;
import java.util.stream.*;

// Custom functional interface (Java 8 allows default/static methods too)
@FunctionalInterface
interface GradeCalculator {
    char calculate(double average);
