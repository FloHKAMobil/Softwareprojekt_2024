package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class FahrtDaten {
    int geplanteFahrten;
    int erhobeneFahrten;
    int guetepruefungOk;

    //Konstruktor zur Initialisierung der geplanten, erhobenen und erfolgreichen (Erfüllung Gütekriterien) Fahrten
    FahrtDaten() {
        this.geplanteFahrten = 0;
        this.erhobeneFahrten = 0;
        this.guetepruefungOk = 0;
    }

    //Hinzufügen der Anzahl der geplanten Fahrten
    void addGeplanteFahrt(int count) {
        this.geplanteFahrten += count;
    }

    //Hinzufügen der Anzahl der erhobenen Fahrten
    void addErhobeneFahrt(int count) {
        this.erhobeneFahrten += count;
    }

    //Hinzufügen der Anzahl der erfolgreichen (Gütekriterien) Fahrten
    void addGuetepruefungOk(int count) {
        this.guetepruefungOk += count;
    }


    public static class Fahrt {
        private String linie;
        private String richtung;
        private String tagesgruppe;
        private String starthaltestelle;
        private String abfahrtszeit;
        private int geplanteFahrten;
        private int guetepruefungOk;
        private List<String> daten;
        private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

        //Konstruktor zur Initialisierung einer Fahrt mit allen relevanten Parametern
        public Fahrt(String linie, String richtung, String tagesgruppe, String starthaltestelle, String abfahrtszeit, int geplanteFahrten, int guetepruefungOk) {
            this.linie = linie;
            this.richtung = richtung;
            this.tagesgruppe = tagesgruppe;
            this.starthaltestelle = starthaltestelle;
            this.abfahrtszeit = abfahrtszeit;
            this.geplanteFahrten = geplanteFahrten;
            this.guetepruefungOk = guetepruefungOk;
            this.daten = new ArrayList<>();
        }

        public String getLinie() {
            return linie;
        }

        public String getRichtung() {
            return richtung;
        }

        public String getTagesgruppe() {
            return tagesgruppe;
        }

        public String getStarthaltestelle() {
            return starthaltestelle;
        }

        public String getAbfahrtszeit() {
            return abfahrtszeit;
        }

        public int getGeplanteFahrten() {
            return geplanteFahrten;
        }

        public int getGuetepruefungOk() {
            return guetepruefungOk;
        }

        public List<String> getDaten() {
            return daten;
        }

        //Fügt Datum zur Liste hinzu
        public void addDatum(String datum) {
            this.daten.add(datum);
            sortDaten();
        }

        //Sortiert Liste nach Datum
        private void sortDaten() {
            this.daten.sort((date1, date2) -> {
                try {
                    Date d1 = DATE_FORMAT.parse(date1);
                    Date d2 = DATE_FORMAT.parse(date2);
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    throw new RuntimeException("Error parsing date: " + e.getMessage());
                }
            });
        }
    }
}
