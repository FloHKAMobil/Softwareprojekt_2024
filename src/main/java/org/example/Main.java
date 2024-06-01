package org.example;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Scanner-Objekt zum Lesen der Benutzereingabe erstellen
        Scanner scanner = new Scanner(System.in);

        // Benutzer nach dem Pfad zur Erhebungsstand CSV-Datei fragen
        System.out.print("Eingabe des Pfades zur Erhebungsstand.CSV (bspw. E:\\Daten\\Erhebungsstand.csv: ");
        String filePathErhebungsstand = scanner.nextLine();

        // Benutzer nach dem Pfad zur zweiten CSV-Datei fragen
        System.out.print("Eingabe des Pfades zur Zaehlfahrten.CSV (bspw. E:\\Daten\\Zaehlfahrten.csv: ");
        String filePathZaehlfahrten = scanner.nextLine();

        // Dateipfade in der Config-Klasse speichern
        Config.setCsvFilePathErhebungsstand(filePathErhebungsstand);
        Config.setCsvFilePathZaehlfahrten(filePathZaehlfahrten);

        // Scanner schließen
        scanner.close();


        //Einfaches Auslesen der Erhebungsstand.CSV
        try {
            Erhebungsstand_CsvParser.main(args);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/


        //Einfaches Auslesen der Zaehlfahrten.CSV
        try {
            Zaehlfahrten_CsvParser.main(args);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Bewertung
        Evaluation.main(args);
        }
    }
