package com.phonecompany.billing.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
                for (int j = 0; j < 3; j++) {
                    System.out.println(values.get(i)[j]);
                    System.out.println("Phone number " + values.get(i)[0] + " start call " + values.get(i)[1]  + " " +
                            "end" +
                            " " +
                            "call " + values.get(i)[2]);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Date startDate = sdf.parse(values.get(i)[1]);
                    Date endDate = sdf.parse(values.get(i)[2]);

                    long diffInMillies = Math.abs(startDate.getTime() - endDate.getTime());
                    long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    System.out.println(diff);
                    calculate((int) diff);
                    System.out.println("PRICE" + calculate(50));
                    System.out.println(calculate(5));

                }
            }
            System.out.println(lines.get(0));
        } catch (IOException e) {
            System.out.println("File is incorrect!");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static double calculate(int duration)
    {
        double price = 0;
        if(duration > 5){
            price = 5 + (duration -5) * 0.2;
        return price;
        }
        return duration;
    }
}







