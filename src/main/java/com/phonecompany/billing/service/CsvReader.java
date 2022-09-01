package com.phonecompany.billing.service;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Getter
@Setter
public class CsvReader {
    public static void main(String[] args) {
        Path filePath = Paths.get("phonelog.csv");
        List<String> lines;
        List<String[]> values = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        double callPrice;
        int promoPrice = 0;

        try {
            lines = Files.readAllLines(filePath);
            for (String line : lines) {
                values.add(line.split(","));
            }
            String promoNumber = promoNumberSelect(values); // vybere promo cislo - cislo na ktere bylo nejvice volano a pokud
            // je jich vice tak to, ktere ma vyssi aritmetickou hodnotu

            for (int i = 0; i < values.size(); i++) {
                LocalDateTime callStart = LocalDateTime.parse(values.get(i)[1], formatter);
                LocalDateTime callEnd = LocalDateTime.parse(values.get(i)[2], formatter);
                long duration = (Duration.between(callStart, callEnd).getSeconds()) / 60;
                System.out.println(promoNumber);
                if (values.get(i)[0].equals(promoNumber.substring(0,12))) {
                    callPrice = calculate(callStart, callEnd) * promoPrice;
                } else {
                    callPrice = calculate(callStart, callEnd);
                }
                System.out.println("Phone number " + values.get(i)[0] + " start call " + values.get(i)[1] + " " +
                        "end " +
                        "call " + values.get(i)[2] + " duration " + duration + " price " + callPrice);
            }
        } catch (IOException e) {
            System.out.println("File is incorrect!");
        }

    }
// Vyber promo cisla, pro zjednoduseni pouzita jen cislo bez predvolby, aby to nemuselo byt Double. Nefungovalo by to
// tedy na zahranicni cisla. Nejprve si udelam HashMapu, kde si dam pocet opakovani cisla a nasledne jako return to
// nejprve seradim podle hodnot (tedy poctu pouziti) a nasledne podle telefonniho cisla

    public static String promoNumberSelect(List<String[]> phoneLog) {
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
        return "420" + phoneUsages.entrySet()
                .stream()
                .max(Map.Entry.<Integer, Integer>comparingByValue()
                        .thenComparing(Map.Entry::getKey)).get();
    }

    public static double calculate(LocalDateTime callStart, LocalDateTime callEnd) {
        double price = 0;
        double lowPrice = 0.5;
        double highPrice = 1;
        double afterLimit = 0.2;
        int startHighRange = 8;
        int endHighRange = 16;
        int limit = 5; // in minutes
        long duration = (Duration.between(callStart, callEnd).getSeconds()) / 60;
        // hovor jen v low pasmu
        if ((callStart.getHour() < startHighRange || (callStart.getHour() >= endHighRange && callStart.getMinute() > 0))
                && (callEnd.getHour() < startHighRange || (callEnd.getHour() > endHighRange && callEnd.getMinute() > 0))) {
            if (duration < limit) {
                price = lowPrice * duration;
            } else {
                price = (lowPrice * limit) + (afterLimit * (duration - limit));
            }
            System.out.println("LOW price " + price);
//            hovor jen v high pasmu
        } else if ((callStart.getHour() >= startHighRange && callStart.getHour() <= endHighRange
                && (callEnd.getHour() >= startHighRange && (callEnd.getHour() < endHighRange)))) {
            if (duration < limit) {
                price = highPrice * duration;
            } else {
                price = (highPrice * limit) + (afterLimit * (duration - limit));
            }
            System.out.println("HIGH price " + price);
        } else if (callStart.getHour() < startHighRange && callEnd.getHour() > startHighRange) {
            // zacne v low a pokracuje v high
            long durationLow = startHighRange * 60 - (callStart.getHour() * 60 + callStart.getMinute());
            long durationHigh = duration - durationLow;
            if (duration < limit) {
                price = lowPrice * durationLow + highPrice * durationHigh;
            } else if (durationLow >= limit) {
                price = lowPrice * limit + afterLimit * (duration - limit);
            } else if (durationLow < limit) {
                price = durationLow * lowPrice + (limit-durationLow) * highPrice + (duration - limit) * afterLimit;
            }
            System.out.println("LOW duration  " + durationLow + "HIGH duration  " + durationHigh + "Duration  " + duration);
        } else if (callStart.getHour() < endHighRange && callEnd.getHour() >= endHighRange) {
            // zacne v high a pokracuje v low
            long durationHigh = endHighRange * 60 - (callStart.getHour() * 60 + callStart.getMinute());
            long durationLow = duration - durationHigh;
            if (duration < limit) {
                price = lowPrice * durationLow + highPrice * durationHigh;
            } else if (durationHigh >= limit) {
                price = highPrice * limit + afterLimit * (duration - limit);
            } else if (durationHigh < limit) {
                price = durationHigh * highPrice + (limit-durationHigh) * lowPrice + (duration - limit) * afterLimit;
            }
            System.out.println("HIGH duration  " + durationHigh + "LOW duration  " + durationLow + "Duration  " + duration);
        }
        return price;
    }
}








