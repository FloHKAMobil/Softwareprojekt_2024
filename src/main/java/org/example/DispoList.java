package org.example;

import java.text.ParseException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.FileWriter;

public class DispoList {

    //Möglichkeit auch Liste nur mit Fahrten ohne Güteprüfung auszugeben, aus der aktuellen Version herausgenommen, da akt. keine Verwendung!!!
    /*public static List<FahrtDaten.Fahrt> filterFahrtenOhneErfolgreicheGuetepruefung(Map<String, List<FahrtDaten.Fahrt>> fahrtenMap) {
        List<FahrtDaten.Fahrt> fahrtenOhneGuetepruefung = new ArrayList<>();

        // Liste mit Fahrten ohne Güteprüfung
        for (List<FahrtDaten.Fahrt> fahrtenListe : fahrtenMap.values()) {
            for (FahrtDaten.Fahrt fahrt : fahrtenListe) {
                if (fahrt.getGuetepruefungOk() == 0) {
                    fahrtenOhneGuetepruefung.add(fahrt);
                }
            }
        }

        return fahrtenOhneGuetepruefung;
    }*/

    //letzter Datumseintrag aus der Fahrt auslesen
    private static Date extractFahrtDatum(List<String> daten) {
        if (daten.isEmpty()) {
            return null; // Keine Daten vorhanden
        }
        String FahrtString = daten.get(daten.size() - 1); // Letzter Eintrag in der Liste
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return sdf.parse(FahrtString);
        } catch (ParseException e) {
            return null; // Ungültiges Datum
        }
    }


    //Erstellung der Dispositionsliste
    public static List<FahrtDaten.Fahrt> priorisierteFahrten(Map<String, List<FahrtDaten.Fahrt>> fahrtenMap, String aktuelleTagesgruppe) {
        List<FahrtDaten.Fahrt> allFahrten = new ArrayList<>();

        //Sammeln aller Fahrten der aktuellen Tagesgruppe
        for (List<FahrtDaten.Fahrt> fahrtenListe : fahrtenMap.values()) {
            for (FahrtDaten.Fahrt fahrt : fahrtenListe) {
                if (fahrt.getTagesgruppe().equals(aktuelleTagesgruppe)) {
                    allFahrten.add(fahrt);
                }
            }
        }

        //Sortierung der Fahrten
        allFahrten.sort(new Comparator<FahrtDaten.Fahrt>() {
            @Override
            public int compare(FahrtDaten.Fahrt fahrt1, FahrtDaten.Fahrt fahrt2) {
                // Vergleich nach Priorität der Tagesgruppe
                int tagesgruppePrioComparison = Integer.compare(getTagesgruppePriority(fahrt1.getTagesgruppe()), getTagesgruppePriority(fahrt2.getTagesgruppe()));
                if (tagesgruppePrioComparison != 0) {
                    return tagesgruppePrioComparison;
                }

                // Vergleich nach Fortschritt
                double fortschrittF1 = (double) fahrt1.getGuetepruefungOk() / fahrt1.getGeplanteFahrten();
                double fortschrittFahrt2 = (double) fahrt2.getGuetepruefungOk() / fahrt2.getGeplanteFahrten();
                int fortschrittComparison = Double.compare(fortschrittF1, fortschrittFahrt2); // Sortiere aufsteigend nach Fortschritt
                if (fortschrittComparison != 0) {
                    return fortschrittComparison;
                }

                // Vergleich nach Datum der letzten Fahrt
                Date datumFahrt1 = extractFahrtDatum(fahrt1.getDaten());
                Date datumFahrt2 = extractFahrtDatum(fahrt2.getDaten());

                if (datumFahrt1 == null && datumFahrt2 != null) {
                    return -1; // fahrt1 hat noch keine Fahrt, wird höher gewichtet
                } else if (datumFahrt1 != null && datumFahrt2 == null) {
                    return 1; // fahrt2 hat noch keine Fahrt, wird höher gewichtet
                } else if (datumFahrt1 != null && datumFahrt2 != null) {
                    int FahrtComparison = datumFahrt1.compareTo(datumFahrt2);
                    if (FahrtComparison != 0) {
                        return FahrtComparison; // Sortiere aufsteigend nach Datum der letzten Fahrt
                    }
                }

                // Wenn das Datum der letzten Fahrt gleich ist, Vergleich nach Abfahrtszeit
                return fahrt1.getAbfahrtszeit().compareTo(fahrt2.getAbfahrtszeit());
            }

            //Bestimmung Priorität einer Tagesgruppe (für Sortierung)
            private int getTagesgruppePriority(String tagesgruppe) {
                switch (tagesgruppe) {
                    case "Montag - Freitag Schule":
                        return 1;
                    case "Montag - Freitag Ferien":
                        return 2;
                    case "Samstag":
                        return 3;
                    case "Sonn-/Feiertag":
                        return 4;
                    default:
                        return Integer.MAX_VALUE; // Standardpriorität für unerwartete Werte
                }
            }
        });

        return allFahrten;
    }


    //Bestimmung aktueller Tagesgruppe auf Basis des Datums
    public static String getAktuelleTagesgruppe(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == Calendar.SATURDAY) {
            return "Samstag";
        } else if (dayOfWeek == Calendar.SUNDAY || isFeiertag(calendar)) {
            return "Sonn-/Feiertag";
        } else {
            boolean isSchoolDay = isSchultag(calendar);
            if (isSchoolDay) {
                return "Montag - Freitag Schule";
            } else {
                return "Montag - Freitag Ferien";
            }
        }
    }

    //Überprüfung ob Tag ein Schultag ist
    private static boolean isSchultag(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // Freitag ist ein Ferientag
        return dayOfWeek != Calendar.FRIDAY;
    }

    //Überprüfung ob Tag ein Feiertag ist
    private static boolean isFeiertag(Calendar calendar) {
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        // Der erste Tag des Monats ist ein Feiertag
        return dayOfMonth == 1;
    }

    //Bestimmung des zu verwendenden Datums
    public static Calendar getUserDatum() {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();

        System.out.print("\nSoll das aktuelle Datum verwendet werden? (y/n): ");
        String useAktuellesDatum = scanner.nextLine().trim().toLowerCase();

        if (!useAktuellesDatum.equals("y")) {
            boolean validDate = false;
            while (!validDate) {
                System.out.print("\nBitte geben Sie das gewünschte Datum im Format DD.MM.YYYY ein: ");
                String dateString = scanner.nextLine().trim();
                try {
                    Date date = sdf.parse(dateString);
                    calendar.setTime(date);
                    validDate = true;
                } catch (ParseException e) {
                    System.out.println("Ungültiges Datum. Bitte versuchen Sie es erneut.");
                }
            }
        }

        return calendar;
    }

    //Durchführung Priorisierung und Schreiben in Log-Datei
    public static void main(String[] args) {
        Evaluation.main(args);

 /*       List<FahrtDaten.Fahrt> result = filterFahrtenOhneErfolgreicheGuetepruefung(Evaluation.fahrtenMap);

        System.out.println("LISTE FAHRTEN MIT FEHLENDER GÜTEPRÜFUNG: ");
        for (FahrtDaten.Fahrt fahrt : result) {
            System.out.println("Linie: " + fahrt.getLinie());
            System.out.println("Richtung: " + fahrt.getRichtung());
            System.out.println("Tagesgruppe: " + fahrt.getTagesgruppe());
            System.out.println("Starthaltestelle: " + fahrt.getStarthaltestelle());
            System.out.println("Abfahrtszeit: " + fahrt.getAbfahrtszeit());
            System.out.println("Geplante Fahrten: " + fahrt.getGeplanteFahrten());
            System.out.println("Güteprüfung: " + fahrt.getGuetepruefungOk());
            System.out.println("Daten: " + String.join(", ", fahrt.getDaten()));
            System.out.println();
        }
*/
        Calendar calendar = getUserDatum();
        String aktuelleTagesgruppe = getAktuelleTagesgruppe(calendar);
        List<FahrtDaten.Fahrt> prioritizedFahrten = priorisierteFahrten(Evaluation.fahrtenMap, aktuelleTagesgruppe);

        System.out.println("\nPriorisierte Liste aller Fahrten (Dispositionsliste): ");

        // Benutzername abrufen
        String userName = System.getProperty("user.name");
        // Pfad zur Log-Datei festlegen
        String filePath = "C:/Users/" + userName + "/AppData/Roaming/Dispositionssoftware/dispolist_log.txt";

        try (FileWriter writer = new FileWriter(filePath, false)) { // 'false' means overwrite mode
            for (FahrtDaten.Fahrt fahrt : prioritizedFahrten) {
                int nochDurchzufuehrendeFahrten = fahrt.getGeplanteFahrten() - fahrt.getGuetepruefungOk();
                double fortschritt = (double) fahrt.getGuetepruefungOk() / fahrt.getGeplanteFahrten() * 100;
                Date letzteFahrtDatum = extractFahrtDatum(fahrt.getDaten());
                long differenzInTagen = (calendar.getTime().getTime() - (letzteFahrtDatum != null ? letzteFahrtDatum.getTime() : calendar.getTime().getTime())) / (1000 * 60 * 60 * 24);

                String output = String.format(
                        "Linie: %s\nRichtung: %s\nTagesgruppe: %s\nStarthaltestelle: %s\nAbfahrtszeit: %s\nNoch durchzuführende Fahrten: %d\nFortschritt: %.2f%%\nLetzte Fahrt Datum: %s\nTage seit der letzten Fahrt: %s\nDaten: %s\n\n",
                        fahrt.getLinie(),
                        fahrt.getRichtung(),
                        fahrt.getTagesgruppe(),
                        fahrt.getStarthaltestelle(),
                        fahrt.getAbfahrtszeit(),
                        nochDurchzufuehrendeFahrten,
                        fortschritt,
                        (letzteFahrtDatum != null ? letzteFahrtDatum : "Keine Fahrten durchgeführt"),
                        (letzteFahrtDatum != null ? differenzInTagen : "Noch keine Fahrt durchgeführt"),
                        String.join(", ", fahrt.getDaten())
                );


                // In die Datei schreiben
                writer.write(output);
            }
            System.out.println("Erfolgreich in die Log-Datei geschrieben: '" + filePath + "'");
        } catch (IOException e) {
            System.out.println("Ein Fehler ist beim Schreiben in die Datei aufgetreten.");
            e.printStackTrace();
        }
    }
}
