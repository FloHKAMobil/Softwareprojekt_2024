package org.example;

public class Config {
    private static String csvFilePathErhebungsstand;
    private static String csvFilePathZaehlfahrten;

    // Getter-Methoden zum Abrufen der Dateipfade
    public static String getCsvFilePathErhebungsstand() {
        return csvFilePathErhebungsstand;
    }

    public static String getCsvFilePathZaehlfahrten() {
        return csvFilePathZaehlfahrten;
    }

    // Setter-Methoden zum Setzen der Dateipfade
    public static void setCsvFilePathErhebungsstand(String filePath) {
        csvFilePathErhebungsstand = filePath;
    }

    public static void setCsvFilePathZaehlfahrten(String filePath) {
        csvFilePathZaehlfahrten = filePath;
    }

}
