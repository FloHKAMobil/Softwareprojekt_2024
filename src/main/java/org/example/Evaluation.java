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

    //ERHEBUNGSSTAND:

    //Bewertung des Erhebungsstandes nach Tagesgruppe und Linie (!!!Fahrt aktuell außenvorgelassen weil eig. schon in CSV-Datei beschrieben!!!)
    public static void main(String[] args) {
        String csvFile = Config.getCsvFilePathErhebungsstand();
        Map<String, FahrtDaten> tagesgruppenMap = new HashMap<>();
        Map<String, FahrtDaten> linienMap = new HashMap<>();

        try (FileReader reader = new FileReader(csvFile)) {
            HeaderColumnNameMappingStrategy<Erhebungsstand> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(Erhebungsstand.class);

            CsvToBean<Erhebungsstand> csvToBean = new CsvToBeanBuilder<Erhebungsstand>(reader)
                    .withMappingStrategy(strategy)
                    .withSeparator(';') // Set the separator to semicolon
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Erhebungsstand> erhobeneStands = csvToBean.parse();

            for (Erhebungsstand stand : erhobeneStands) {
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

        writeToFile(filePath2, tagesgruppenMap, "Tagesgruppe");
        writeToFile(filePath3, linienMap, "Linie");

        System.out.println(" ");
        System.out.println(" ");
        System.out.println("Nach Tagesgruppe:");
        for (Map.Entry<String, FahrtDaten> entry : tagesgruppenMap.entrySet()) {
            String tagesgruppe = entry.getKey();
            FahrtDaten daten = entry.getValue();
            System.out.println("Tagesgruppe: " + tagesgruppe);
            System.out.println("Geplante Fahrten: " + daten.geplanteFahrten);
            System.out.println("Erhobene Fahrten: " + daten.erhobeneFahrten);
            System.out.println("Güteprüfung ok: " + daten.guetepruefungOk);
            System.out.println();
        }

        System.out.println("Nach Linie:");
        for (Map.Entry<String, FahrtDaten> entry : linienMap.entrySet()) {
            String linie = entry.getKey();
            FahrtDaten daten = entry.getValue();
            System.out.println("Linie: " + linie);
            System.out.println("Geplante Fahrten: " + daten.geplanteFahrten);
            System.out.println("Erhobene Fahrten: " + daten.erhobeneFahrten);
            System.out.println("Güteprüfung ok: " + daten.guetepruefungOk);
            System.out.println();
        }

        // Bewertung der Fahrtverteilung über ein Quartal
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("Fahrtverteilung:");
        String erhebungsstandCsvFile = Config.getCsvFilePathErhebungsstand();
        String zaehlfahrtenCsvFile = Config.getCsvFilePathZaehlfahrten();
        List<FahrtDaten.Fahrt> fahrtenListe = new ArrayList<>();

        // Mapping von Tagesgruppen
        Map<String, String> tagesgruppenMap2 = new HashMap<>();
        tagesgruppenMap2.put("Schule", "Montag - Freitag Schule");
        tagesgruppenMap2.put("Ferien inkl. Brückentag", "Montag - Freitag Ferien");
        tagesgruppenMap2.put("Sonntag", "Sonn-/Feiertag");
        tagesgruppenMap2.put("Feiertag", "Sonn-/Feiertag");

        try (FileReader erhReader = new FileReader(erhebungsstandCsvFile);
             FileReader zaehlReader = new FileReader(zaehlfahrtenCsvFile)) {

            HeaderColumnNameMappingStrategy<Erhebungsstand> erhStrategy = new HeaderColumnNameMappingStrategy<>();
            erhStrategy.setType(Erhebungsstand.class);

            CsvToBean<Erhebungsstand> erhCsvToBean = new CsvToBeanBuilder<Erhebungsstand>(erhReader)
                    .withMappingStrategy(erhStrategy)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Erhebungsstand> erhobeneStands = erhCsvToBean.parse();

            HeaderColumnNameMappingStrategy<Zaehlfahrten> zaehlStrategy = new HeaderColumnNameMappingStrategy<>();
            zaehlStrategy.setType(Zaehlfahrten.class);

            CsvToBean<Zaehlfahrten> zaehlCsvToBean = new CsvToBeanBuilder<Zaehlfahrten>(zaehlReader)
                    .withMappingStrategy(zaehlStrategy)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Zaehlfahrten> zaehlfahrten = zaehlCsvToBean.parse();

            for (Erhebungsstand stand : erhobeneStands) {
                String tagesgruppe = stand.getTagesgruppe();
                String abfahrtszeit = stand.getAbfahrtszeit().substring(0, 5); // Extrahiere nur die ersten 5 Zeichen (HH:mm)

                int geplanteFahrten = Integer.parseInt(stand.getGeplant());
                int guetepruefungOk = Integer.parseInt(stand.getGuetepruefung());

                FahrtDaten.Fahrt fahrt = new FahrtDaten.Fahrt(
                        stand.getLinie(),
                        stand.getRichtung(),
                        tagesgruppe,
                        stand.getStarthaltestelle(),
                        abfahrtszeit, // Verkürzte Abfahrtszeit
                        geplanteFahrten,
                        guetepruefungOk

                );
                fahrtenListe.add(fahrt);
            }

            for (Zaehlfahrten fahrt : zaehlfahrten) {
                String mappedTagesgruppe = tagesgruppenMap2.getOrDefault(fahrt.getTagesgruppe(), fahrt.getTagesgruppe());
                String zaehlAbfahrtszeit = fahrt.getAbfahrtszeit().substring(0, 5); // Extrahiere nur die ersten 5 Zeichen (HH:mm)

                for (FahrtDaten.Fahrt f : fahrtenListe) {
                    if (f.getLinie().equals(fahrt.getLinie())
                            && f.getRichtung().equals(fahrt.getRichtung())
                            && f.getTagesgruppe().equals(mappedTagesgruppe)
                            && f.getStarthaltestelle().equals(fahrt.getStarthaltestelle())
                            && f.getAbfahrtszeit().equals(zaehlAbfahrtszeit)) {
                        f.addDatum(fahrt.getDatum());
                    }
                }
            }

            // Schreiben der Fahrtverteilung in die Datei
            writeFahrtverteilungToFile(filePath4, fahrtenListe);

            // Optional: Ausgabe der Fahrtverteilung auf der Konsole
            for (FahrtDaten.Fahrt f : fahrtenListe) {
                System.out.println("Linie: " + f.getLinie());
                System.out.println("Richtung: " + f.getRichtung());
                System.out.println("Tagesgruppe: " + f.getTagesgruppe());
                System.out.println("Starthaltestelle: " + f.getStarthaltestelle());
                System.out.println("Abfahrtszeit: " + f.getAbfahrtszeit());
                System.out.println("Anzahl geplanter Fahrten: " + f.getGeplanteFahrten());
                System.out.println("Anzahl Fahrten mit erfolgreicher Güteprüfung: " + f.getGuetepruefungOk());
                System.out.println("Daten: " + String.join(", ", f.getDaten()));
                System.out.println();
            }

            // Abspeichern der Fahrtenliste
            for (FahrtDaten.Fahrt f : fahrtenListe) {
                String key = f.getLinie() + "_" + f.getRichtung() + "_" + f.getTagesgruppe() + "_" + f.getStarthaltestelle() + "_" + f.getAbfahrtszeit();
                fahrtenMap.computeIfAbsent(key, k -> new ArrayList<>()).add(f);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToFile(String filePath, Map<String, FahrtDaten> dataMap, String type) {
        try (FileWriter writer = new FileWriter(filePath, false)) { // 'false' means overwrite mode
            for (Map.Entry<String, FahrtDaten> entry : dataMap.entrySet()) {
                String key = entry.getKey();
                FahrtDaten daten = entry.getValue();
                String output = String.format(
                        "%s: %s\nGeplante Fahrten: %d\nErhobene Fahrten: %d\nGüteprüfung ok: %d\n\n",
                        type, key, daten.geplanteFahrten, daten.erhobeneFahrten, daten.guetepruefungOk
                );

                // Print to console
                System.out.print(output);

                // Write to file
                writer.write(output);
            }
            System.out.println("Erfolgreich in die Datei geschrieben: '" + filePath + "'");
        } catch (IOException e) {
            System.out.println("Ein Fehler ist beim Schreiben in die Datei aufgetreten.");
            e.printStackTrace();
        }
    }

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

                // Print to console
                System.out.print(output);

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