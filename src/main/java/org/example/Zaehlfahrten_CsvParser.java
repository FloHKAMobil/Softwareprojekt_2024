package org.example;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Zaehlfahrten_CsvParser {

    public static void main(String[] args) throws IOException {

        String fileName = Config.getCsvFilePathZaehlfahrten();

        List<Zaehlfahrten> zaehlfahrtenList = new CsvToBeanBuilder(new FileReader(fileName))
                .withType(Zaehlfahrten.class)
                .withSeparator(';')  // Setze das Trennzeichen auf Semikolon
                .build()
                .parse();

        zaehlfahrtenList.forEach(System.out::println);

    }

}