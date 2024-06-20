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



/*
        //Einfaches Auslesen der Erhebungsstand.CSV
        try {
            Erhebungsstand_CsvParser.main(args);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("^^Auslesen der Erhebungsstand.csv Datei^^, Fortsetzen? -> Eingabe");
        String userInput = scanner.nextLine();
        System.out.println("Benutzereingabe: " + userInput);


        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        //Einfaches Auslesen der Zaehlfahrten.CSV
        try {
            Zaehlfahrten_CsvParser.main(args);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("^^Auslesen der Zaehlfahrten.csv Datei^^, Fortsetzen? -> Eingabe");
        String userInput2 = scanner.nextLine();



        //Bewertung
        Evaluation.main(args);
        System.out.println("^^Durchführung der Bewertung der Daten nach Tagesgruppe, Linie, Quartal^^, Fortsetzen? -> Eingabe");
        String userInput3 = scanner.nextLine();*/

        //Liste Fahrten ohne Guetepruefung
        DispoList.main(args);
        System.out.println("^^Erstellung einer Dispositionsliste, Fortsetzen? -> Eingabe");
        String userInput4 = scanner.nextLine();
        }


    }
