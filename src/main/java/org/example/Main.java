package org.example;

import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Scanner-Objekt zum Lesen der Benutzereingabe erstellen
        Scanner scanner = new Scanner(System.in);
        String userName = System.getProperty("user.name");

        // Path to the configuration file
        String configFilePath = "C:/Users/" + userName + "/AppData/Roaming/dispolist_config.txt";
        File configFile = new File(configFilePath);

        String basePath;

        // Method to get valid base path
        basePath = getValidBasePath(scanner, configFile);

        // Stitch together the file paths for the CSV files
        String filePathErhebungsstand = basePath + "Erhebungsstand.csv";
        String filePathZaehlfahrten = basePath + "Zaehlfahrten.csv";

        // Save the file paths in the Config class
        Config.setCsvFilePathErhebungsstand(filePathErhebungsstand);
        Config.setCsvFilePathZaehlfahrten(filePathZaehlfahrten);

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



            // Liste Fahrten ohne Guetepruefung
            DispoList.main(args);
            System.out.println("^^Erstellung einer Dispositionsliste, Fortsetzen? -> Eingabe");
            String userInput4 = scanner.nextLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getValidBasePath(Scanner scanner, File configFile) {
        String userName = System.getProperty("user.name");
        String basePath;
        while (true) {
            if (!configFile.exists()) {
                basePath = promptUserForBasePath(scanner, configFile);
            } else {
                try {
                    basePath = new String(Files.readAllBytes(Paths.get(configFile.getPath()))).trim();
                    System.out.println("Konfigurationsdatei lokalisiert.");

                    // Ask user if they want to edit the path
                    System.out.print("Möchten Sie den Pfad in der Konfigurationsdatei ändern? (y/n): ");
                    String editChoice = scanner.nextLine();
                    if (editChoice.equalsIgnoreCase("y")) {
                        System.out.println("Konfigurationsdatei gefunden.");
                        basePath = promptUserForBasePath(scanner, configFile);
                    }

                } catch (IOException e) {
                    throw new RuntimeException("Fehler beim Lesen der Konfigurationsdatei: ", e);
                }
            }

            // Check if the constructed file paths exist
            String filePathErhebungsstand = basePath + "Erhebungsstand.csv";
            String filePathZaehlfahrten = basePath + "Zaehlfahrten.csv";
            if (new File(filePathErhebungsstand).exists() && new File(filePathZaehlfahrten).exists()) {
                System.out.println("Dateipfad automatisch eingelesen und validiert.\n");
                break;
            } else {
                System.out.println("Ungültiger Pfad hinterlegt oder Dateien 'Erhebungsstand.csv' und 'Zaehlfahrten.csv' existieren nicht im angegebenen Pfad. Bitte erneut eingeben.\n");
                basePath = promptUserForBasePath(scanner, configFile);
            }
        }
        return basePath;
    }

    private static String promptUserForBasePath(Scanner scanner, File configFile) {
        try {
            // Prompt user to input the base file path
            System.out.print("Eingabe des Pfades zu den Dateien 'Erhebungsstand.csv' und 'Zaehlfahrten.csv' (bspw. E:\\Daten\\Disposition\\): ");
            String basePath = scanner.nextLine();

            // Save the base path to the configuration file
            try (FileWriter writer = new FileWriter(configFile)) {
                writer.write(basePath);
            }

            return basePath;
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Schreiben der Konfigurationsdatei: ", e);
        }
    }
}
