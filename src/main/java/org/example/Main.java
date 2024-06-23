package org.example;

import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;


public class Main {

    public static void main(String[] args) {

        // Scanner-Objekt zum Lesen der Benutzereingabe erstellen
        Scanner scanner = new Scanner(System.in);

        // Benutzer nach dem Pfad zur Erhebungsstand CSV-Datei fragen
        System.out.print("Eingabe des Pfades zur Erhebungsstand.CSV (bspw. E:\\Daten\\Erhebungsstand.csv): ");
        String filePathErhebungsstand = scanner.nextLine();

        // Benutzer nach dem Pfad zur zweiten CSV-Datei fragen
        System.out.print("Eingabe des Pfades zur Zaehlfahrten.CSV (bspw. E:\\Daten\\Zaehlfahrten.csv): ");
        String filePathZaehlfahrten = scanner.nextLine();

        // Dateipfade in der Config-Klasse speichern
        Config.setCsvFilePathErhebungsstand(filePathErhebungsstand);
        Config.setCsvFilePathZaehlfahrten(filePathZaehlfahrten);

        String userName = System.getProperty("user.name");

        // Specify the path where you want to create the file, including the username
        String filePath = "C:/Users/" + userName + "/AppData/Roaming/Dispositionssoftware/dispolist_log.txt";
        String filePath2 = "C:/Users/" + userName + "/AppData/Roaming/Dispositionssoftware/tagesgruppelist_log.txt";
        String filePath3 = "C:/Users/" + userName + "/AppData/Roaming/Dispositionssoftware/linienlist_log.txt";
        String filePath4 = "C:/Users/" + userName + "/AppData/Roaming/Dispositionssoftware/fahrtverteilunglist_log.txt";

        try {
            // Dispositionsliste als Datei
            File file = new File(filePath);

            // Check if the file's parent directory exists; if not, create it
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Create the file
            if (file.createNewFile()) {
                System.out.println("Dispositionsliste erstellt: '" + filePath + "'");
            } else {
                System.out.println("Datei '" + file.getName() + "' existiert bereits: '" + filePath + "'");
            }

            // Tagesgruppenliste als Datei
            File file2 = new File(filePath2);

            // Check if the file's parent directory exists; if not, create it
            if (file2.getParentFile() != null && !file2.getParentFile().exists()) {
                file2.getParentFile().mkdirs();
            }

            // Create the file
            if (file2.createNewFile()) {
                System.out.println("Tagesgruppenliste erstellt: '" + filePath2 + "'");
            } else {
                System.out.println("Datei '" + file2.getName() + "' existiert bereits: '" + filePath2 + "'");
            }

            // Linienliste als Datei
            File file3 = new File(filePath3);

            // Check if the file's parent directory exists; if not, create it
            if (file3.getParentFile() != null && !file3.getParentFile().exists()) {
                file3.getParentFile().mkdirs();
            }

            // Create the file
            if (file3.createNewFile()) {
                System.out.println("Tagesgruppenliste erstellt: '" + filePath3 + "'");
            } else {
                System.out.println("Datei '" + file3.getName() + "' existiert bereits: '" + filePath3 + "'");
            }

            // Fahrtverteilungsliste als Datei
            File file4 = new File(filePath4);

            // Check if the file's parent directory exists; if not, create it
            if (file4.getParentFile() != null && !file4.getParentFile().exists()) {
                file4.getParentFile().mkdirs();
            }

            // Create the file
            if (file4.createNewFile()) {
                System.out.println("Tagesgruppenliste erstellt: '" + filePath4 + "'");
            } else {
                System.out.println("Datei '" + file4.getName() + "' existiert bereits: '" + filePath4 + "'");
            }






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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}