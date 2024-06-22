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
        System.out.print("Eingabe des Pfades zur Erhebungsstand.CSV (bspw. E:\\Daten\\Erhebungsstand.csv: ");
        String filePathErhebungsstand = scanner.nextLine();

        // Benutzer nach dem Pfad zur zweiten CSV-Datei fragen
        System.out.print("Eingabe des Pfades zur Zaehlfahrten.CSV (bspw. E:\\Daten\\Zaehlfahrten.csv: ");
        String filePathZaehlfahrten = scanner.nextLine();

        // Dateipfade in der Config-Klasse speichern
        Config.setCsvFilePathErhebungsstand(filePathErhebungsstand);
        Config.setCsvFilePathZaehlfahrten(filePathZaehlfahrten);

        String userName = System.getProperty("user.name");

        // Specify the path where you want to create the file, including the username
        String filePath = "C:/Users/" + userName + "/AppData/Roaming/dispolist_log.txt";

        try {
            // Create a File object with the specified path
            File file = new File(filePath);

            // Check if the file's parent directory exists; if not, create it
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Create the file
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
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