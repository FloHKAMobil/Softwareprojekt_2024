package org.example;

import com.opencsv.bean.CsvBindByName;

public class Zaehlfahrten {

    //CSV-Bindings für die entsprechenden Spaltennamen
    @CsvBindByName(column = "Linie")
    private String linie;

    @CsvBindByName(column = "Richtung")
    private String richtung;

    @CsvBindByName(column = "Starthaltenstelle")
    private String starthaltestelle;

    @CsvBindByName(column = "Abfahrtzeit")
    private String abfahrtszeit;

    @CsvBindByName(column = "Datum")
    private String datum;

    @CsvBindByName(column = "tagesgruppe")
    private String tagesgruppe;

    @CsvBindByName(column = "Fahrzeug")
    private String fahrzeug;


    // Getter und Setter
    public String getLinie() {
        return linie;
    }

    public String getRichtung() {
        return richtung;
    }

    public String getStarthaltestelle() {
        return starthaltestelle;
    }

    public String getAbfahrtszeit() {
        return abfahrtszeit;
    }

    public String getDatum() {
        return datum;
    }

    public String getTagesgruppe() {
        return tagesgruppe;
    }

    public String getFahrzeug() {
        return fahrzeug;
    }


    public void setLinie(String linie) {
        this.linie = linie;
    }

    public void setRichtung(String richtung) {
        this.richtung = richtung;
    }

    public void setStarthaltestelle(String starthaltestelle) {
        this.starthaltestelle = starthaltestelle;
    }

    public void setAbfahrtszeit(String abfahrtszeit) {
        this.abfahrtszeit = abfahrtszeit;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public void setTagesgruppe(String tagesgruppe) {
        this.tagesgruppe = tagesgruppe;
    }

    public void setFahrzeug(String fahrzeug) {
        this.fahrzeug = fahrzeug;
    }


    //Gibt String-Darstellung des Objektes zurück
    @Override
    public String toString() {
        return "Zaehlfahrten{" +
                "Linie='" + linie + '\'' +
                ", Richtung='" + richtung + '\'' +
                ", Starthaltestelle='" + starthaltestelle + '\'' +
                ", Abfahrtszeit='" + abfahrtszeit + '\'' +
                ", Datum='" + datum + '\'' +
                ", Tagesgruppe='" + tagesgruppe + '\'' +
                ", Fahrzeug='" + fahrzeug + '\'' +
                '}';
    }
}