package com.phonecompany.billing.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.phonecompany.billing.Prices.LOW_PRICE;

public class CsvReader {
    public static void main(String[] args) {
        Path filePath = Paths.get("phonelog.csv");
        List<String> lines;
        List<String[]> values = new ArrayList<>();
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        List<Integer> callTimeline = new ArrayList<>();
        callTimeline.add(-1);
        callTimeline.add(20);
        callTimeline.add(-5);
        callTimeline.add(20);
        System.out.println(callTimeline);


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
                System.out.println("Start hour: " + gc.get(Calendar.HOUR_OF_DAY));
                gc.setTime(endDate);
                System.out.println("End hour: " + gc.get(Calendar.HOUR_OF_DAY));

                long diffInMillies = Math.abs(startDate.getTime() - endDate.getTime());
                long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);

//                System.out.println("HOUR "+ TimeUnit.HOURS.convert(startDate.getTime(), TimeUnit.HOURS));
//                System.out.println(startDate.getTime());
                // System.out.println(TimeUnit.HOURS(10,TimeUnit.MILLISECONDS));
//                System.out.println(diff);
                System.out.println("Phone number " + values.get(i)[0] + " start call " + values.get(i)[1] + " " +
                        "end " +
                        "call " + values.get(i)[2] + " duration " + diff + " price " + calculate((int) diff, callTimeline));
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

    public static double calculate(int duration, List<Integer> callSchedule) {
        double price = 0;
        double lowPrice = 0.5;
        double highPrice = 1;
        double afterLimit = 0.2;
        int limit = 5; // in minutes
        int countedDuration = 0;
        // pokud je v callSchedule zaporna hodnota jde o cas v dobe vyssi ceny, pokud je kladna jde o cas v dobe
        // nizsi ceny

        for (int i = 0; i < callSchedule.size(); i++) {
            if (callSchedule.get(i) < 0) {
                if (Math.abs(callSchedule.get(i)) > limit) {
                    price += highPrice * limit + afterLimit * (Math.abs(callSchedule.get(i)) - limit);
                    countedDuration += Math.abs(callSchedule.get(i);
                } else {
                    price += highPrice * limit + afterLimit * (Math.abs(callSchedule.get(i)) - limit);
                    countedDuration += Math.abs(callSchedule.get(i);
                }
            } else {
                price += lowPrice * callSchedule.get(i);
            }
        }


        if (duration > limit) {
            price = limit * lowPrice + (duration - limit) * afterLimit;
            return price;
        }
        return duration;
    }
}







