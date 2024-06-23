package org.example;

import java.text.ParseException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.FileWriter;

public class DispoList {

    public static List<FahrtDaten.Fahrt> filterFahrtenOhneErfolgreicheGuetepruefung(Map<String, List<FahrtDaten.Fahrt>> fahrtenMap) {
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
    }

    private static Date extractLetzteFahrtDatum(List<String> daten) {
        if (daten.isEmpty()) {
            return null; // Keine Daten vorhanden
        }
        String letzteFahrtString = daten.get(daten.size() - 1); // Letzter Eintrag in der Liste
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return sdf.parse(letzteFahrtString);
        } catch (ParseException e) {
            return null; // Ungültiges Datum
        }
    }


    public static List<FahrtDaten.Fahrt> priorisierteFahrten(Map<String, List<FahrtDaten.Fahrt>> fahrtenMap, String aktuelleTagesgruppe) {
        List<FahrtDaten.Fahrt> allFahrten = new ArrayList<>();

        for (List<FahrtDaten.Fahrt> fahrtenListe : fahrtenMap.values()) {
            for (FahrtDaten.Fahrt fahrt : fahrtenListe) {
                if (fahrt.getTagesgruppe().equals(aktuelleTagesgruppe)) {
                    allFahrten.add(fahrt);
                }
            }
        }

        Collections.sort(allFahrten, new Comparator<FahrtDaten.Fahrt>() {
            @Override
            public int compare(FahrtDaten.Fahrt f1, FahrtDaten.Fahrt f2) {
                // Vergleich nach Priorität der Tagesgruppe
                int tagesgruppePriorityComparison = Integer.compare(getTagesgruppePriority(f1.getTagesgruppe()), getTagesgruppePriority(f2.getTagesgruppe()));
                if (tagesgruppePriorityComparison != 0) {
                    return tagesgruppePriorityComparison;
                }

                // Vergleich nach Fortschritt
                double fortschrittF1 = (double) f1.getGuetepruefungOk() / f1.getGeplanteFahrten();
                double fortschrittF2 = (double) f2.getGuetepruefungOk() / f2.getGeplanteFahrten();
                int fortschrittComparison = Double.compare(fortschrittF1, fortschrittF2); // Sortiere aufsteigend nach Fortschritt
                if (fortschrittComparison != 0) {
                    return fortschrittComparison;
                }

                // Vergleich nach Datum der letzten Fahrt
                Date letzteFahrtDatumF1 = extractLetzteFahrtDatum(f1.getDaten());
                Date letzteFahrtDatumF2 = extractLetzteFahrtDatum(f2.getDaten());

                if (letzteFahrtDatumF1 == null && letzteFahrtDatumF2 != null) {
                    return -1; // f1 hat noch keine Fahrt, wird höher gewichtet
                } else if (letzteFahrtDatumF1 != null && letzteFahrtDatumF2 == null) {
                    return 1; // f2 hat noch keine Fahrt, wird höher gewichtet
                } else if (letzteFahrtDatumF1 != null && letzteFahrtDatumF2 != null) {
                    int letzteFahrtComparison = letzteFahrtDatumF1.compareTo(letzteFahrtDatumF2);
                    if (letzteFahrtComparison != 0) {
                        return letzteFahrtComparison; // Sortiere aufsteigend nach Datum der letzten Fahrt
                    }
                }

                // Wenn das Datum der letzten Fahrt gleich ist, Vergleich nach Abfahrtszeit
                return f1.getAbfahrtszeit().compareTo(f2.getAbfahrtszeit());
            }

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


    public static String getAktuelleTagesgruppe(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == Calendar.SATURDAY) {
            return "Samstag";
        } else if (dayOfWeek == Calendar.SUNDAY || isHoliday(calendar)) {
            return "Sonn-/Feiertag";
        } else {
            boolean isSchoolDay = isSchoolDay(calendar);
            if (isSchoolDay) {
                return "Montag - Freitag Schule";
            } else {
                return "Montag - Freitag Ferien";
            }
        }
    }

    private static boolean isSchoolDay(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // Freitag ist ein Ferientag
        return dayOfWeek != Calendar.FRIDAY;
    }

    private static boolean isHoliday(Calendar calendar) {
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        // Der erste Tag des Monats ist ein Feiertag
        return dayOfMonth == 1;
    }

    public static Calendar getUserSpecifiedDate() {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();

        System.out.print("Soll das aktuelle Datum verwendet werden? (y/n): ");
        String useCurrentDate = scanner.nextLine().trim().toLowerCase();

        if (!useCurrentDate.equals("y")) {
            boolean validDate = false;
            while (!validDate) {
                System.out.print("Bitte geben Sie das gewünschte Datum im Format DD.MM.YYYY ein: ");
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

    public static void main(String[] args) {
        Evaluation.main(args);

        List<FahrtDaten.Fahrt> result = filterFahrtenOhneErfolgreicheGuetepruefung(Evaluation.fahrtenMap);

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

        Calendar calendar = getUserSpecifiedDate();
        String aktuelleTagesgruppe = getAktuelleTagesgruppe(calendar);
        List<FahrtDaten.Fahrt> prioritizedFahrten = priorisierteFahrten(Evaluation.fahrtenMap, aktuelleTagesgruppe);

        System.out.println("PRIORISIERTE LISTE ALLER FAHRTEN: ");

        // Get the username of the currently logged-in user
        String userName = System.getProperty("user.name");
        // Specify the path where you want to create the file, including the username
        String filePath = "C:/Users/" + userName + "/AppData/Roaming/Dispositionssoftware/dispolist_log.txt";

        try (FileWriter writer = new FileWriter(filePath, false)) { // 'false' means overwrite mode
            for (FahrtDaten.Fahrt fahrt : prioritizedFahrten) {
                int nochDurchzufuehrendeFahrten = fahrt.getGeplanteFahrten() - fahrt.getGuetepruefungOk();
                double fortschritt = (double) fahrt.getGuetepruefungOk() / fahrt.getGeplanteFahrten() * 100;
                Date letzteFahrtDatum = extractLetzteFahrtDatum(fahrt.getDaten());
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

                // Print to console
                System.out.print(output);

                // Write to file
                writer.write(output);
            }
            System.out.println("Erfolgreich in die Log-Datei geschrieben: '" + filePath + "'");
        } catch (IOException e) {
            System.out.println("Ein Fehler ist beim Schreiben in die Datei aufgetreten.");
            e.printStackTrace();
        }
    }
}
