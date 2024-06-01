package org.example;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Erhebungsstand_CsvParser {

    public static void main(String[] args) throws IOException {

        String fileName = Config.getCsvFilePathErhebungsstand();

        List<Erhebungsstand> erhebungsstandList = new CsvToBeanBuilder(new FileReader(fileName))
                .withType(Erhebungsstand.class)
                .withSeparator(';')  // Setze das Trennzeichen auf Semikolon
                .build()
                .parse();

        erhebungsstandList.forEach(System.out::println);

    }

}