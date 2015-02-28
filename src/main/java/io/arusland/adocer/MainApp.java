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
        ArgumentHelper arguments = ArgumentHelper.parse(args);

        mainInternal(arguments);
    }

    private static void mainInternal(final ArgumentHelper arguments) throws IOException {
        if (arguments.files.size() < 1) {
            arguments.usage();
            return;
        }

        final File inputFile = new File(arguments.files.get(0));
        File outputFile;

        if (arguments.files.size() < 2) {
            // if output filename not defined, make it from input file
            outputFile = new File(FilenameUtils.removeExtension(inputFile.getAbsolutePath())
                    + getExtensionByBackend(arguments.backend));
        } else {
            outputFile = new File(arguments.files.get(1));
        }

        System.out.println("Generating file " + outputFile);
        renderToFile(inputFile, outputFile, arguments);
    }

    private static void renderToFile(File fileSource, File fileDest, ArgumentHelper argumentHelper) throws IOException {
        renderToFileInternal(fileSource, fileDest, getOptionsBuilder(argumentHelper));
    }

    private static OptionsBuilder getOptionsBuilder(ArgumentHelper arguments) {
        final OptionsBuilder optionsBuilder = OptionsBuilder.options()
                .compact(arguments.compact)
                .safe(SafeMode.UNSAFE)
                .eruby("")
                .backend(arguments.backend)
                .docType(arguments.doctype)
                .headerFooter(arguments.headerFooter)
                .mkDirs(true);
        final AttributesBuilder attributesBuilder = AttributesBuilder.attributes()
                .icons(arguments.icons)
                .sourceHighlighter(arguments.highlighter)
                .linkCss(arguments.linkCss)
                .dataUri(arguments.datauri);

        if (arguments.imagesDir != null) {
            attributesBuilder.imagesDir(arguments.imagesDir);
        }

        optionsBuilder.attributes(attributesBuilder.get());

        return optionsBuilder;
    }

    private static String getExtensionByBackend(String backend) {
        if ("html5".equals(backend)) {
            return ".html";
        } else if ("pdf".equals(backend)) {
            return ".pdf";
        } else if ("docbook".equals(backend)) {
            return ".dbk";
        } else {
            throw new IllegalStateException("Unknown backend: " + backend);
        }
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
            attributesBuilder.dataUri(false);
        }
        if (imagesDir != null) {
            attributesBuilder.imagesDir(imagesDir);
        }
        optionsBuilder.attributes(attributesBuilder.get());
        return optionsBuilder;
    }
}
