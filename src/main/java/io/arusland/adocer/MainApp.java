package io.arusland.adocer;

/**
 * Created by arusland on 24.02.2015.
 */

import org.apache.commons.io.FilenameUtils;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;

import java.io.*;

public class MainApp {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("USAGE: adoc input.adoc output.html");
            return;
        }

        final File inputFile = new File(args[0]);
        File outputFile;

        if (args.length < 2) {
            // if output filename not defined, make it from input file
            outputFile = new File(FilenameUtils.removeExtension(inputFile.getAbsolutePath()) + ".html");
        } else {
            outputFile = new File(args[1]);
        }

        renderToFile(inputFile, outputFile);
    }

    private static void renderToFile(File fileSource, File fileDest) throws IOException {
        renderToFileInternal(fileSource, fileDest, getOptionsBuilder(true, null));
    }

    private static void renderToFileInternal(final File fileSource, final File fileDest, final OptionsBuilder optionsBuilder) throws IOException {
        FileReader reader = new FileReader(fileSource);
        FileWriter writer = new FileWriter(fileDest);

        final Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        asciidoctor.convert(reader, writer, optionsBuilder.asMap());

        reader.close();
        writer.close();
    }

    private static void renderFile(File sourceFile, File outputDirectory, boolean embedAssets, String imagesDir) {
        final OptionsBuilder optionsBuilder = getOptionsBuilder(embedAssets, imagesDir);
        optionsBuilder.toDir(outputDirectory).destinationDir(outputDirectory);

        renderFileInternal(sourceFile, optionsBuilder);
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
