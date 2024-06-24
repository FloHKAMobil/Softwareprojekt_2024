package org.example;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

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

        // Path to the log file
        String logFilePath = "C:/Users/" + userName + "/AppData/Roaming/dispolist_log.txt";

        try {
            File logFile = new File(logFilePath);

            // Create parent directories if they don't exist
            if (logFile.getParentFile() != null && !logFile.getParentFile().exists()) {
                logFile.getParentFile().mkdirs();
            }

            // Create the log file
            if (logFile.createNewFile()) {
                System.out.println("Log-Datei erstellt: '" + logFilePath + "'. Auswertungsergebnisse werden in dieser Datei gespeichert.");
            } else {
                System.out.println("Die Logdatei '" + logFile.getName() + "' existiert bereits: '" + logFilePath + "'. Die Datei wird mit den neuen Auswertungsergebnissen überschrieben. \nWenn sie die alten Ergebnisse behalten möchten, speichern sie die Datei bitte jetzt an anderer Stelle ab.");
            }

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
                System.out.println("Dateipfad automatisch eingelesen und validiert. Zur Veränderung des Pfades bitte die Datei 'C:/Users/" + userName + "/AppData/Roaming/dispolist_config.txt' löschen.");
                break;
            } else {
                System.out.println("Ungültiger Pfad hinterlegt oder Dateien 'Erhebungsstand.csv' und 'Zaehlfahrten.csv' existieren nicht im angegebenen Pfad. Bitte erneut eingeben.");
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
