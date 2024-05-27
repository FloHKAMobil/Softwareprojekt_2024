package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {

    public List<com.example.Erhebung> parseCsv(String filePath) {
        List<com.example.Erhebung> erhebungen = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = csvReader.readAll();
            for (String[] record : records) {
                if (record.length == 7) { // Überprüfen Sie, ob die Zeile die erwartete Anzahl von Spalten hat
                    com.example.Erhebung erhebung = new com.example.Erhebung();
                    erhebung.setTagesgruppe(record[0]);
                    erhebung.setLinie(record[1]);
                    erhebung.setRichtung(record[2]);
                    erhebung.setAbfahrtszeitStarthaltestelle(record[3]);
                    erhebung.setGeplant(record[4]);
                    erhebung.setErhoben(record[5]);
                    erhebung.setGuetepruefungOk(record[6]);

                    erhebungen.add(erhebung);
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return erhebungen;
    }

    public static void main(String[] args) {
        CsvParser parser = new CsvParser();
        List<com.example.Erhebung> erhebungen = parser.parseCsv("E:/Eigene Dateien/Daten Florian/Studium\\4.Semester\\VSMB430 Softwareentwicklung\\Projekt\\Erhebungsstand.csv");

        for (com.example.Erhebung erhebung : erhebungen) {
            System.out.println(erhebung);
        }
    }
}
