package com.phonecompany.billing.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CsvReader {
    Path filePath = Paths.get("phonelog.csv");
    try {
        List<String> lines = Files.readAllLines(filePath);
        System.out.println(lines.get(0));
    } catch (IOException e) {
        System.out.println("Sorry file is not correct");
    }
}
