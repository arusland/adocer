package io.arusland.adocer;

/**
 * Created by arusland on 24.02.2015.
 */
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;

import java.io.*;

public class MainApp {
    public static void main(String[] args) throws IOException {
        if (args.length < 2){
            System.out.println("USAGE: adoc input.adoc output.html");
            return;
        }

        renderToFile(new File(args[0]), new File(args[1]));
    }

    private static void renderFile(File sourceFile, File outputDirectory, boolean embedAssets, String imagesDir)
    {
        final OptionsBuilder optionsBuilder = getOptionsBuilder(embedAssets, imagesDir);
        optionsBuilder.toDir(outputDirectory).destinationDir(outputDirectory);

        renderFileInternal(sourceFile, optionsBuilder);
    }

    private static void renderToFile(File fileSource, File fileDest) throws IOException {
        renderToFileInternal(fileSource, fileDest, getOptionsBuilder(true, null));
    }

    private static void renderToFileInternal(File fileSource, File fileDest, OptionsBuilder optionsBuilder) throws IOException {
        FileReader reader = new FileReader(fileSource);
        FileWriter writer = new FileWriter(fileDest);

        final Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        asciidoctor.convert(reader, writer, optionsBuilder.asMap());

        reader.close();
        writer.close();
    }

    protected static void renderFileInternal(File fileSource, OptionsBuilder optionsBuilder) {
        final Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        asciidoctor.renderFile(fileSource, optionsBuilder.asMap());
    }

    private static OptionsBuilder getOptionsBuilder(boolean embedAssets, String imagesDir) {
        final OptionsBuilder optionsBuilder = OptionsBuilder.options().compact(false).safe(SafeMode.UNSAFE)
                .eruby("").backend("html5").docType("article").headerFooter(true).mkDirs(true);
        final AttributesBuilder attributesBuilder = AttributesBuilder.attributes().icons("font")
                .sourceHighlighter("prettify");

        if (embedAssets) {
            attributesBuilder.linkCss(false);
            attributesBuilder.dataUri(true);
        }
        if (imagesDir != null) {
            attributesBuilder.imagesDir(imagesDir);
        }
        optionsBuilder.attributes(attributesBuilder.get());
        return optionsBuilder;
    }
}
