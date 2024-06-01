package org.example;

import com.opencsv.bean.CsvBindByName;


public class Erhebungsstand {

    @CsvBindByName(column = "Tagesgruppe")
    private String tagesgruppe;

    @CsvBindByName(column = "Linie")
    private String linie;

    @CsvBindByName(column = "Richtung")
    private String richtung;

    @CsvBindByName(column = "Abfahrtszeit")
    private String abfahrtszeit;

    @CsvBindByName(column = "Starthaltestelle")
    private String starthaltestelle;

    @CsvBindByName(column = "Geplant")
    private String geplant;

    @CsvBindByName(column = "Erhoben")
    private String erhoben;

    @CsvBindByName(column = "Güteprüfung ok")
    private String guetepruefung;

    // Getter und Setter

    public String getTagesgruppe() {
        return tagesgruppe;
    }

    public String getLinie() {
        return linie;
    }

    public String getRichtung() {
        return richtung;
    }

    public String getAbfahrtszeit() {
        return abfahrtszeit;
    }

    public String getStarthaltestelle() {
        return starthaltestelle;
    }

    public String getGeplant() {
        return geplant;
    }

    public String getErhoben() {
        return erhoben;
    }

    public String getGuetepruefung() {
        return guetepruefung;
    }

    public void setTagesgruppe(String tagesgruppe) {
        this.tagesgruppe = tagesgruppe;
    }

    public void setLinie(String linie) {
        this.linie = linie;
    }

    public void setRichtung(String richtung) {
        this.richtung = richtung;
    }

    public void setAbfahrtszeit(String abfahrtszeit) {
        this.abfahrtszeit = abfahrtszeit;
    }

    public void setStarthaltestelle(String starthaltestelle) {
        this.starthaltestelle = starthaltestelle;
    }

    public void setGeplant(String geplant) {
        this.geplant = geplant;
    }

    public void setErhoben(String erhoben) {
        this.erhoben = erhoben;
    }

    public void setGuetepruefung(String guetepruefung) {
        this.guetepruefung = guetepruefung;
    }

    @Override
    public String toString() {
        return "Erhebungsstand{" +
                "tagesgruppe='" + tagesgruppe + '\'' +
                ", linie='" + linie + '\'' +
                ", richtung='" + richtung + '\'' +
                ", abfahrtszeit='" + abfahrtszeit + '\'' +
                ", starthaltestelle='" + starthaltestelle + '\'' +
                ", geplant='" + geplant + '\'' +
                ", erhoben='" + erhoben + '\'' +
                ", guetepruefung='" + guetepruefung + '\'' +
                '}';
    }


}