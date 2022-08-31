package com.phonecompany.billing.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CsvReader {
    public static void main(String[] args) {

        Path filePath = Paths.get("phonelog.csv");
        List<String> lines;
        List<String[]> values = new ArrayList<>();
        try {
            lines = Files.readAllLines(filePath);
            for (int i = 0; i < lines.size(); i++) {
                System.out.println(lines.get(i).split(","));
                values.add(lines.get(i).split(","));
            }
            for (int i = 0; i < values.size(); i++) {
                System.out.println("TEST" + Arrays.toString(values.get(i)));
            }
            System.out.println(lines.get(0));
        } catch (IOException e) {
            System.out.println("File is incorrect!");
        }
    }
}







