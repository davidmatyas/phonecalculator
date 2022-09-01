package com.phonecompany.billing.service;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


import static com.phonecompany.billing.Prices.LOW_PRICE;

@Getter

public class CsvReader {
    public static void main(String[] args) {
        Path filePath = Paths.get("phonelog.csv");
        List<String> lines;
        List<String[]> values = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        List<Integer> callTimeline = new ArrayList<>();
        double price = 0;
        double lowPrice = 0.5;
        double highPrice = 1;
        double afterLimit = 0.2;
        int startHighRange = 8;
        int endHighRange = 16;
        int limit = 5; // in minutes
        int countedDuration = 0;

        try {
            lines = Files.readAllLines(filePath);
            for (String line : lines) {

                values.add(line.split(","));
            }
            promoNumber(values);
            System.out.println(promoNumber(values));
            for (int i = 0; i < values.size(); i++) {
                LocalDateTime callStart = LocalDateTime.parse(values.get(i)[1], formatter);
                LocalDateTime callEnd = LocalDateTime.parse(values.get(i)[2], formatter);
                long duration = (Duration.between(callStart, callEnd).getSeconds()) / 60;

                long durationLow =
                        (Duration.between(callStart,
                                LocalDateTime.of(callStart.getYear(), callStart.getMonth(), callStart.getDayOfMonth(),
                                        startHighRange, 0, 0)).getSeconds()) / 60;
                System.out.println(durationLow);
                System.out.println("Phone number " + values.get(i)[0] + " start call " + values.get(i)[1] + " " +
                        "end " +
                        "call " + values.get(i)[2] + " duration " + duration);
                calculate(callStart, callEnd);


//                System.out.println(callStart.getHour());
//                if (diff < 24 * 60 * 60 * 60 * 100) { // hovor kratsi nez den
//                    if (startHour == endHour) { // hovor v ramci jedne hodiny
//                        if (startHour < 8 || startHour > 16) { // hovor v nizkem pasmu
//                            callTimeline.add(endMinute - startMinute);
//                        } else {
//                            callTimeline.add((endMinute - startMinute) * -1);// hovor ve vysokem pasmu
//                        }
//                    } else if (endHour - startHour < 2) // hovor do hodiny
//                    {
//                        if((startHour < 8 && endHour < 8) || (startHour > 16 && endHour > 16)) {
//                            // hovor neprekracuje pasma a je v low price
//                            callTimeline.add((60-startMinute) + endMinute);
//                        } else if ((startHour >= 8 && endHour >= 8) || (startHour <= 16 && endHour <= 16)){
//                            // hovor neprekracuje pasma a je v high price
//                            callTimeline.add(((60-startMinute) + endMinute)*-1);
//                        }
//                    }
//                }
//                System.out.println("End hour: " + gc.get(Calendar.HOUR_OF_DAY) + "End minute: " + gc.get(Calendar.MINUTE));


//                System.out.println("HOUR "+ TimeUnit.HOURS.convert(startDate.getTime(), TimeUnit.HOURS));
//                System.out.println(startDate.getTime());
                // System.out.println(TimeUnit.HOURS(10,TimeUnit.MILLISECONDS));
//                System.out.println(diff);
//                System.out.println("Phone number " + values.get(i)[0] + " start call " + values.get(i)[1] + " " +
//                        "end " +
//                        "call " + values.get(i)[2] + " duration " + diff + " price " + calculate((int) diff, callTimeline));
//                    calculate((int) diff);
//                    System.out.println("PRICE" + calculate(50));
//                    System.out.println(calculate(5));

            }
        } catch (IOException e) {
            System.out.println("File is incorrect!");
        }

    }
// Vyber promo cisla, pro zjednoduseni pouzita jen cislo bez predvolby, aby to nemuselo byt Double. Nefungovalo by to
// tedy na zahranicni cisla. Nejprve si udelam HashMapu, kde si dam pocet opakovani cisla a nasledne jako return to
// nejprve seradim podle hodnot (tedy poctu pouziti) a nasledne podle telefonniho cisla

    public static String promoNumber(List<String[]> phoneLog) {
        HashMap<Integer, Integer> phoneUsages = new HashMap<>();
        Integer phone;
        for (int i = 0; i < phoneLog.size(); i++) {
            phone = Integer.valueOf(phoneLog.get(i)[0].substring(2));
            if (phoneUsages.containsKey(phone)) {
                phoneUsages.put(phone, phoneUsages.get(phone) + 1);
            } else {
                phoneUsages.put(phone, 1);
            }
        }
        return String.valueOf("420" + phoneUsages.entrySet()
                .stream()
                .max(Map.Entry.<Integer, Integer>comparingByValue()
                        .thenComparing(Map.Entry::getKey)).get());
    }

    public static double calculate(LocalDateTime callStart, LocalDateTime callEnd) {
//        double price = 0;
//        double lowPrice = 0.5;
//        double highPrice = 1;
//        double afterLimit = 0.2;
//        int startHighRange = 8;
//        int endHighRange = 16;
//        int limit = 5; // in minutes
//        int countedDuration = 0;
//        long duration = (Duration.between(callStart, callEnd).getSeconds()) / 60;
//        System.out.println(duration);

        if ((callStart.getHour() < 8 || callStart.getHour() > 16) && (callEnd.getHour() < 8 || callEnd.getHour() > 16)) {

            System.out.println("LOW price");
        }

        // pokud je v callSchedule zaporna hodnota jde o cas v dobe vyssi ceny, pokud je kladna jde o cas v dobe
        // nizsi ceny

//        for (int i = 0; i < callSchedule.size(); i++) {
//            if (callSchedule.get(i) < 0) {
//                if ((Math.abs(callSchedule.get(i)) - countedDuration) > limit) {
//                    price += highPrice * (limit - countedDuration) + afterLimit * (Math.abs(callSchedule.get(i)) - (limit - countedDuration));
//                    countedDuration += Math.abs(callSchedule.get(i));
//                } else if (countedDuration > limit) {
//                    price += afterLimit * Math.abs(callSchedule.get(i));
//                    countedDuration += Math.abs(callSchedule.get(i));
//                } else {
//                    price += highPrice * Math.abs(callSchedule.get(i));
//                    countedDuration += Math.abs(callSchedule.get(i));
//                }
//            } else {
//                if (callSchedule.get(i) - countedDuration > limit) {
//                    price += lowPrice * (limit - countedDuration) + afterLimit * (callSchedule.get(i) - (limit - countedDuration));
//                    countedDuration += callSchedule.get(i);
//                } else if (countedDuration > limit) {
//                    price += afterLimit * callSchedule.get(i);
//                    countedDuration += callSchedule.get(i);
//                } else {
//                    price += lowPrice * callSchedule.get(i);
//                    countedDuration += callSchedule.get(i);
//                }
//            }
//        }
        return 1;
    }
}








