package org.example;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evaluation {

    public static Map<String, List<FahrtDaten.Fahrt>> fahrtenMap = new HashMap<>();

        //Bewertung des Erhebungsstandes nach Tagesgruppe und Linie:

    public static void main(String[] args) {
        String csvFile = Config.getCsvFilePathErhebungsstand();
        Map<String, FahrtDaten> tagesgruppenMap = new HashMap<>();
        Map<String, FahrtDaten> linienMap = new HashMap<>();

        //Lesen der Erhebungsstand CSV-Datei und Parsing der Daten
        try (FileReader reader = new FileReader(csvFile)) {
            HeaderColumnNameMappingStrategy<Erhebungsstand> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(Erhebungsstand.class);

            CsvToBean<Erhebungsstand> csvToBean = new CsvToBeanBuilder<Erhebungsstand>(reader)
                    .withMappingStrategy(strategy)
                    .withSeparator(';') // Set the separator to semicolon
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Erhebungsstand> erhebungsstandList = csvToBean.parse();

            //Verarbeiten der Erhebungsstand-Daten
            for (Erhebungsstand stand : erhebungsstandList) {
                String tagesgruppe = stand.getTagesgruppe();
                String linie = stand.getLinie();
                int geplant = Integer.parseInt(stand.getGeplant());
                int erhoben = Integer.parseInt(stand.getErhoben());
                int guetepruefungOk = Integer.parseInt(stand.getGuetepruefung());

                // Update tagesgruppenMap
                FahrtDaten tagesgruppeFahrtDaten = tagesgruppenMap.getOrDefault(tagesgruppe, new FahrtDaten());
                tagesgruppeFahrtDaten.addGeplanteFahrt(geplant);
                tagesgruppeFahrtDaten.addErhobeneFahrt(erhoben);
                tagesgruppeFahrtDaten.addGuetepruefungOk(guetepruefungOk);
                tagesgruppenMap.put(tagesgruppe, tagesgruppeFahrtDaten);

                // Update linienMap
                FahrtDaten linieFahrtDaten = linienMap.getOrDefault(linie, new FahrtDaten());
                linieFahrtDaten.addGeplanteFahrt(geplant);
                linieFahrtDaten.addErhobeneFahrt(erhoben);
                linieFahrtDaten.addGuetepruefungOk(guetepruefungOk);
                linienMap.put(linie, linieFahrtDaten);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String userName = System.getProperty("user.name");
        String filePath2 = "C:/Users/" + userName + "/AppData/Roaming/Dispositionssoftware/tagesgruppelist_log.txt";
        String filePath3 = "C:/Users/" + userName + "/AppData/Roaming/Dispositionssoftware/linienlist_log.txt";
        String filePath4 = "C:/Users/" + userName + "/AppData/Roaming/Dispositionssoftware/fahrtverteilunglist_log.txt";

        //Schreiben der Tagesgruppen- und Liniendaten in Dateien
        writeToFile(filePath2, tagesgruppenMap, "Tagesgruppe");
        writeToFile(filePath3, linienMap, "Linie");



        //Bewertung der Fahrtverteilung über ein Quartal:

        String erhebungsstandCsvFile = Config.getCsvFilePathErhebungsstand();
        String zaehlfahrtenCsvFile = Config.getCsvFilePathZaehlfahrten();
        List<FahrtDaten.Fahrt> fahrtenListe = new ArrayList<>();


        // Mapping von Tagesgruppen
        Map<String, String> differentTagesgruppenCompareMap = new HashMap<>();
        differentTagesgruppenCompareMap.put("Schule", "Montag - Freitag Schule");
        differentTagesgruppenCompareMap.put("Ferien inkl. Brückentag", "Montag - Freitag Ferien");
        differentTagesgruppenCompareMap.put("Sonntag", "Sonn-/Feiertag");
        differentTagesgruppenCompareMap.put("Feiertag", "Sonn-/Feiertag");

        //Lesen der Erhebungsstand- und Zählfahrten-CSV-Dateien und Verarbeitung der Daten
        try (FileReader erhebungsstandReader = new FileReader(erhebungsstandCsvFile);
             FileReader zaehlfahrtenReader = new FileReader(zaehlfahrtenCsvFile)) {

            HeaderColumnNameMappingStrategy<Erhebungsstand> erhebungsstandStrategy = new HeaderColumnNameMappingStrategy<>();
            erhebungsstandStrategy.setType(Erhebungsstand.class);

            CsvToBean<Erhebungsstand> erhebungsstandCsvToBean = new CsvToBeanBuilder<Erhebungsstand>(erhebungsstandReader)
                    .withMappingStrategy(erhebungsstandStrategy)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Erhebungsstand> erhebungsstandList2 = erhebungsstandCsvToBean.parse();

            HeaderColumnNameMappingStrategy<Zaehlfahrten> zaehlfahrtenStrategy = new HeaderColumnNameMappingStrategy<>();
            zaehlfahrtenStrategy.setType(Zaehlfahrten.class);

            CsvToBean<Zaehlfahrten> zaehlfahrtenCsvToBean = new CsvToBeanBuilder<Zaehlfahrten>(zaehlfahrtenReader)
                    .withMappingStrategy(zaehlfahrtenStrategy)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Zaehlfahrten> zaehlfahrten = zaehlfahrtenCsvToBean.parse();

            //Verarbeiten der Erhebungsstand-Daten
            for (Erhebungsstand stand : erhebungsstandList2) {
                String tagesgruppe = stand.getTagesgruppe();
                String abfahrtszeit = stand.getAbfahrtszeit().substring(0, 5); // Extrahiere nur die ersten 5 Zeichen (hh:mm)

                int geplanteFahrten = Integer.parseInt(stand.getGeplant());
                int guetepruefungOk = Integer.parseInt(stand.getGuetepruefung());

                FahrtDaten.Fahrt fahrt = new FahrtDaten.Fahrt(
                        stand.getLinie(),
                        stand.getRichtung(),
                        tagesgruppe,
                        stand.getStarthaltestelle(),
                        abfahrtszeit, // Verkürzte Abfahrtszeit (nur noch hh:mm)
                        geplanteFahrten,
                        guetepruefungOk

                );
                fahrtenListe.add(fahrt);
            }

            //Verarbeiten der Zählfahrten-Daten
            for (Zaehlfahrten zfahrt : zaehlfahrten) {
                String mappedTagesgruppe = differentTagesgruppenCompareMap.getOrDefault(zfahrt.getTagesgruppe(), zfahrt.getTagesgruppe());
                String zaehlfahrtenAbfahrtszeit = zfahrt.getAbfahrtszeit().substring(0, 5); // Extrahiere nur die ersten 5 Zeichen (hh:mm)

                for (FahrtDaten.Fahrt fahrt : fahrtenListe) {
                    if (fahrt.getLinie().equals(zfahrt.getLinie())
                            && fahrt.getRichtung().equals(zfahrt.getRichtung())
                            && fahrt.getTagesgruppe().equals(mappedTagesgruppe)
                            && fahrt.getStarthaltestelle().equals(zfahrt.getStarthaltestelle())
                            && fahrt.getAbfahrtszeit().equals(zaehlfahrtenAbfahrtszeit)) {
                        fahrt.addDatum(zfahrt.getDatum());
                    }
                }
            }

            // Schreiben der Fahrtverteilung in die Datei
            writeFahrtverteilungToFile(filePath4, fahrtenListe);


            // Abspeichern der Fahrtenliste in der fahrtenMap
            for (FahrtDaten.Fahrt f : fahrtenListe) {
                String key = f.getLinie() + "_" + f.getRichtung() + "_" + f.getTagesgruppe() + "_" + f.getStarthaltestelle() + "_" + f.getAbfahrtszeit();
                fahrtenMap.computeIfAbsent(key, k -> new ArrayList<>()).add(f);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Schreibt die Fahrt-Daten in eine Datei (für Tagesgruppen-Liste und Linien-Liste !!!)
    private static void writeToFile(String filePath, Map<String, FahrtDaten> dataMap, String type) {
        try (FileWriter writer = new FileWriter(filePath, false)) { // 'false' means overwrite mode
            for (Map.Entry<String, FahrtDaten> entry : dataMap.entrySet()) {
                String key = entry.getKey();
                FahrtDaten daten = entry.getValue();
                String output = String.format(
                        "%s: %s\nGeplante Fahrten: %d\nErhobene Fahrten: %d\nGüteprüfung ok: %d\n\n",
                        type, key, daten.geplanteFahrten, daten.erhobeneFahrten, daten.guetepruefungOk
                );

                // Write to file
                writer.write(output);
            }
            System.out.println("Erfolgreich in die Datei geschrieben: '" + filePath + "'");
        } catch (IOException e) {
            System.out.println("Ein Fehler ist beim Schreiben in die Datei aufgetreten.");
            e.printStackTrace();
        }
    }

    //Schreibt die Fahrt-Daten für die Fahrtverteilung in eine Datei (NUR für Fahrtverteilugngs-Liste !!!)
    private static void writeFahrtverteilungToFile(String filePath, List<FahrtDaten.Fahrt> fahrtenListe) {
        try (FileWriter writer = new FileWriter(filePath, false)) { // 'false' means overwrite mode
            for (FahrtDaten.Fahrt f : fahrtenListe) {
                String output = String.format(
                        "Linie: %s\nRichtung: %s\nTagesgruppe: %s\nStarthaltestelle: %s\nAbfahrtszeit: %s\nAnzahl geplanter Fahrten: %d\nAnzahl Fahrten mit erfolgreicher Güteprüfung: %d\nDaten: %s\n\n",
                        f.getLinie(),
                        f.getRichtung(),
                        f.getTagesgruppe(),
                        f.getStarthaltestelle(),
                        f.getAbfahrtszeit(),
                        f.getGeplanteFahrten(),
                        f.getGuetepruefungOk(),
                        String.join(", ", f.getDaten())
                );

                // Write to file
                writer.write(output);
            }
            System.out.println("Erfolgreich in die Datei geschrieben: '" + filePath + "'");
        } catch (IOException e) {
            System.out.println("Ein Fehler ist beim Schreiben in die Datei aufgetreten.");
            e.printStackTrace();
        }
    }
}