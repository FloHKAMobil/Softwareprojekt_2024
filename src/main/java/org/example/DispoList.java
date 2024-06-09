package org.example;

import java.util.*;

public class DispoList {

    public static List<FahrtDaten.Fahrt> filterFahrtenOhneErfolgreicheGuetepruefung(Map<String, List<FahrtDaten.Fahrt>> fahrtenMap) {
        List<FahrtDaten.Fahrt> fahrtenOhneGuetepruefung = new ArrayList<>();

        //Liste mit Fahrten ohne Güteprüfung
        for (List<FahrtDaten.Fahrt> fahrtenListe : fahrtenMap.values()) {
            for (FahrtDaten.Fahrt fahrt : fahrtenListe) {
                if (fahrt.getGuetepruefungOk() == 0) {
                    fahrtenOhneGuetepruefung.add(fahrt);
                }
            }
        }

        return fahrtenOhneGuetepruefung;
    }

    /*Liste mit Priorisierten Fahrten
    Priorisierung abhängig von:
    1. Sortiert nach Tagesgruppe (Mo-Fr Schule, Mo-Fr Ferien, Sa, Sonn/Feiertag)
    2. Anzahl der noch durchzuführenden Fahrten (Geplante Fahrten - Güteprüfung ok)
    3. Uhrzeit

     */
    public static List<FahrtDaten.Fahrt> priorisierteFahrten(Map<String, List<FahrtDaten.Fahrt>> fahrtenMap) {
        List<FahrtDaten.Fahrt> allFahrten = new ArrayList<>();

        for (List<FahrtDaten.Fahrt> fahrtenListe : fahrtenMap.values()) {
            allFahrten.addAll(fahrtenListe);
        }

        Collections.sort(allFahrten, new Comparator<FahrtDaten.Fahrt>() {
            @Override
            public int compare(FahrtDaten.Fahrt f1, FahrtDaten.Fahrt f2) {
                // Compare by Tagesgruppe priority
                int tagesgruppePriorityComparison = Integer.compare(getTagesgruppePriority(f1.getTagesgruppe()), getTagesgruppePriority(f2.getTagesgruppe()));
                if (tagesgruppePriorityComparison != 0) {
                    return tagesgruppePriorityComparison;
                }

                // If Tagesgruppe priority is equal, compare by remaining Fahrten
                int remainingFahrtenF1 = f1.getGeplanteFahrten() - f1.getGuetepruefungOk();
                int remainingFahrtenF2 = f2.getGeplanteFahrten() - f2.getGuetepruefungOk();
                int remainingFahrtenComparison = Integer.compare(remainingFahrtenF2, remainingFahrtenF1);
                if (remainingFahrtenComparison != 0) {
                    return remainingFahrtenComparison;
                }

                // If remaining Fahrten are equal, compare by Uhrzeit
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
                        return Integer.MAX_VALUE; // Default priority for unexpected values
                }
            }
        });

        return allFahrten;
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



        List<FahrtDaten.Fahrt> prioritizedFahrten = priorisierteFahrten(Evaluation.fahrtenMap);

        System.out.println("PRIORISIERTE LISTE ALLER FAHRTEN: ");
        for (FahrtDaten.Fahrt fahrt : prioritizedFahrten) {
            System.out.println("Linie: " + fahrt.getLinie());
            System.out.println("Richtung: " + fahrt.getRichtung());
            System.out.println("Tagesgruppe: " + fahrt.getTagesgruppe());
            System.out.println("Starthaltestelle: " + fahrt.getStarthaltestelle());
            System.out.println("Abfahrtszeit: " + fahrt.getAbfahrtszeit());
            System.out.println("Noch durchzuführende Fahrten: " + (fahrt.getGeplanteFahrten() - fahrt.getGuetepruefungOk()));
            System.out.println("Daten: " + String.join(", ", fahrt.getDaten()));
            System.out.println();
        }
    }
}
