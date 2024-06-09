package org.example;

import java.util.ArrayList;
import java.util.List;

class FahrtDaten {
    int geplanteFahrten;
    int erhobeneFahrten;
    int guetepruefungOk;


    FahrtDaten() {
        this.geplanteFahrten = 0;
        this.erhobeneFahrten = 0;
        this.guetepruefungOk = 0;
    }

    void addGeplanteFahrt(int count) {
        this.geplanteFahrten += count;
    }

    void addErhobeneFahrt(int count) {
        this.erhobeneFahrten += count;
    }

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

        public void addDatum(String datum) {
            this.daten.add(datum);
        }
    }
}

