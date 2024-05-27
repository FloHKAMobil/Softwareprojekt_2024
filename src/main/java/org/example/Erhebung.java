package com.example;

public class Erhebung {
    private String tagesgruppe;
    private String linie;
    private String richtung;
    private String abfahrtszeitStarthaltestelle;
    private String geplant;
    private String erhoben;
    private String guetepruefungOk;

    // Getter und Setter
    public String getTagesgruppe() {
        return tagesgruppe;
    }

    public void setTagesgruppe(String tagesgruppe) {
        this.tagesgruppe = tagesgruppe;
    }

    public String getLinie() {
        return linie;
    }

    public void setLinie(String linie) {
        this.linie = linie;
    }

    public String getRichtung() {
        return richtung;
    }

    public void setRichtung(String richtung) {
        this.richtung = richtung;
    }

    public String getAbfahrtszeitStarthaltestelle() {
        return abfahrtszeitStarthaltestelle;
    }

    public void setAbfahrtszeitStarthaltestelle(String abfahrtszeitStarthaltestelle) {
        this.abfahrtszeitStarthaltestelle = abfahrtszeitStarthaltestelle;
    }

    public String getGeplant() {
        return geplant;
    }

    public void setGeplant(String geplant) {
        this.geplant = geplant;
    }

    public String getErhoben() {
        return erhoben;
    }

    public void setErhoben(String erhoben) {
        this.erhoben = erhoben;
    }

    public String getGuetepruefungOk() {
        return guetepruefungOk;
    }

    public void setGuetepruefungOk(String guetepruefungOk) {
        this.guetepruefungOk = guetepruefungOk;
    }

    @Override
    public String toString() {
        return "Erhebung{" +
                "tagesgruppe='" + tagesgruppe + '\'' +
                ", linie='" + linie + '\'' +
                ", richtung='" + richtung + '\'' +
                ", abfahrtszeitStarthaltestelle='" + abfahrtszeitStarthaltestelle + '\'' +
                ", geplant='" + geplant + '\'' +
                ", erhoben='" + erhoben + '\'' +
                ", guetepruefungOk='" + guetepruefungOk + '\'' +
                '}';
    }
}
