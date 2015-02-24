package io.arusland.adocer;

/**
 * Created by arusland on 24.02.2015.
 */
import org.asciidoctor.Asciidoctor;
import static org.asciidoctor.OptionsBuilder.options;

import java.io.*;

public class MainApp {
    public static void main(String[] args) throws IOException {
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        FileReader reader = new FileReader(args[0]);
        FileWriter writer = new FileWriter(args[1]);

        asciidoctor.convert(reader, writer, options().backend("html5").headerFooter(true).asMap());
    }
}
