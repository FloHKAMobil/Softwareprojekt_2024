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

        // Pfad zur Konfigurationsdatei
        String configFilePath = "C:/Users/" + userName + "/AppData/Roaming/dispolist_config.txt";
        File configFile = new File(configFilePath);

        String basePath;

        // Abruf gültiger Basispfad
        basePath = getValidBasePath(scanner, configFile);

        // Ergänzung .csv-Datei
        String filePathErhebungsstand = basePath + "Erhebungsstand.csv";
        String filePathZaehlfahrten = basePath + "Zaehlfahrten.csv";

        // Dateipfade in Config-Klasse speichern
        Config.setCsvFilePathErhebungsstand(filePathErhebungsstand);
        Config.setCsvFilePathZaehlfahrten(filePathZaehlfahrten);

        // Erzeugung Log-Dateien
        String filePath = "C:/Users/" + userName + "/AppData/Roaming/Dispositionssoftware/dispolist_log.txt";
        String filePath2 = "C:/Users/" + userName + "/AppData/Roaming/Dispositionssoftware/tagesgruppelist_log.txt";
        String filePath3 = "C:/Users/" + userName + "/AppData/Roaming/Dispositionssoftware/linienlist_log.txt";
        String filePath4 = "C:/Users/" + userName + "/AppData/Roaming/Dispositionssoftware/fahrtverteilunglist_log.txt";

        try {
            // Dispositionsliste als Datei
            File file = new File(filePath);

            // Überprüfung, ob das übergeordnete Verzeichnis der Datei vorhanden ist, ansonsten wird es erstellt
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Erzeugung File
            if (file.createNewFile()) {
                System.out.println("Dispositionsliste erstellt: '" + filePath + "'");
            } else {
                System.out.println("Datei '" + file.getName() + "' existiert bereits: '" + filePath + "'");
            }



            // Tagesgruppenliste als Datei
            File file2 = new File(filePath2);

            // Überprüfung, ob das übergeordnete Verzeichnis der Datei vorhanden ist, ansonsten wird es erstellt
            if (file2.getParentFile() != null && !file2.getParentFile().exists()) {
                file2.getParentFile().mkdirs();
            }

            // Erzeugung File
            if (file2.createNewFile()) {
                System.out.println("Tagesgruppenliste erstellt: '" + filePath2 + "'");
            } else {
                System.out.println("Datei '" + file2.getName() + "' existiert bereits: '" + filePath2 + "'");
            }



            // Linienliste als Datei
            File file3 = new File(filePath3);

            // Überprüfung, ob das übergeordnete Verzeichnis der Datei vorhanden ist, ansonsten wird es erstellt
            if (file3.getParentFile() != null && !file3.getParentFile().exists()) {
                file3.getParentFile().mkdirs();
            }

            // Erzeugung File
            if (file3.createNewFile()) {
                System.out.println("Tagesgruppenliste erstellt: '" + filePath3 + "'");
            } else {
                System.out.println("Datei '" + file3.getName() + "' existiert bereits: '" + filePath3 + "'");
            }



            // Erzeugung Dispositionsliste
            DispoList.main(args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Auslesen des gültigen Pfades aus der Konfigurationsdatei bzw. wenn nicht vorhanden Eingabe des Pfades
    private static String getValidBasePath(Scanner scanner, File configFile) {
        String userName = System.getProperty("user.name");
        String basePath;
        while (true) {
            if (!configFile.exists()) {
                //Falls Konfigurationsdatei NICHT existiert muss Nutzer Pfad eingeben
                basePath = promptUserForBasePath(scanner, configFile);
            } else {
                try {
                    //Fals Konfigurationsdatei existiert, wird Pfad aus Datei gelesen
                    basePath = new String(Files.readAllBytes(Paths.get(configFile.getPath()))).trim();
                    System.out.println("Konfigurationsdatei lokalisiert.");

                    // Möglichkeit zur Änderung des Pfades der in Konfigurationsdatei vorhanden ist
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

            // Überprüfung ob Dateipfade existieren
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

    //Eingabe des Pfades durch Nutzer und Abspeichern des Pfades in Konfigurationsdatei
    private static String promptUserForBasePath(Scanner scanner, File configFile) {
        try {
            // Eingabe Dateipfad durch Nutzer
            System.out.print("Eingabe des Pfades zu den Dateien 'Erhebungsstand.csv' und 'Zaehlfahrten.csv' (bspw. E:\\Daten\\Disposition\\): ");
            String basePath = scanner.nextLine();

            // Speichern des Pfades in Konfigurationsdatei
            try (FileWriter writer = new FileWriter(configFile)) {
                writer.write(basePath);
            }

            return basePath;
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Schreiben der Konfigurationsdatei: ", e);
        }
    }
}
