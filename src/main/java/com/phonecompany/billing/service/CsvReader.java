package com.phonecompany.billing.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CsvReader {
    public static void main(String[] args) {

        Path filePath = Paths.get("phonelog.csv");
        List<String> lines;
        List<String[]> values = new ArrayList<>();
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            lines = Files.readAllLines(filePath);
            for (int i = 0; i < lines.size(); i++) {
//                System.out.println(lines.get(i).split(","));
                values.add(lines.get(i).split(","));
            }
            for (int i = 0; i < values.size(); i++) {
//                System.out.println("TEST" + Arrays.toString(values.get(i)));
                Date startDate = sdf.parse(values.get(i)[1]);
                Date endDate = sdf.parse(values.get(i)[2]);
                gc.setTime(startDate);
                System.out.println("Start hour: "+gc.get(Calendar.HOUR_OF_DAY));
                gc.setTime(endDate);
                System.out.println("End hour: "+gc.get(Calendar.HOUR_OF_DAY));

                long diffInMillies = Math.abs(startDate.getTime() - endDate.getTime());
                long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
//                System.out.println("HOUR "+ TimeUnit.HOURS.convert(startDate.getTime(), TimeUnit.HOURS));
//                System.out.println(startDate.getTime());
               // System.out.println(TimeUnit.HOURS(10,TimeUnit.MILLISECONDS));
//                System.out.println(diff);
                System.out.println("Phone number " + values.get(i)[0] + " start call " + values.get(i)[1] + " " +
                        "end " +
                        "call " + values.get(i)[2] + " duration " + diff + " price " + calculate((int) diff, 8,16));
//                    calculate((int) diff);
//                    System.out.println("PRICE" + calculate(50));
//                    System.out.println(calculate(5));

            }
        } catch (IOException e) {
            System.out.println("File is incorrect!");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static double calculate(int duration, int startHour, int endHour) {
        double price = 0;

        if (duration > 5) {
            price = 5 + (duration - 5) * 0.2;
            return price;
        }
        return duration;
    }
}







