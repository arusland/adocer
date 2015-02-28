package io.arusland.adocer;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruslan A. on 27.02.2015.
 */
public class ArgumentHelper {
    private JCommander commander;

    @Parameter(description = "input.adoc [output.html]")
    public List<String> files = new ArrayList<String>();

    @Parameter(names = "-linkcss", description = "Css is linked, otherwise css is embedded")
    public boolean linkCss;

    @Parameter(names = "-compact", description = "Compact the output removing blank lines")
    public boolean compact;

    @Parameter(names = "-headerfooter", description = "Sets header footer attribute")
    public boolean headerFooter = true;

    @Parameter(names = "-datauri", description = "Images should be embedded")
    public boolean datauri;

    @Parameter(names = "-imgdir", description = "Sets image directory")
    public String imagesDir;

    @Parameter(names = "-backend", description = "Sets backend option (e.g., html5, pdf, docbook)")
    public String backend = "html5";

    @Parameter(names = "-doctype", description = "Sets doctype option (e.g., article, book)")
    public String doctype = "article";

    @Parameter(names = "-icons", description = "Sets which admonition icons to use")
    public String icons = "font";

    @Parameter(names = "-highlighter", description = "Name of the source highlighting library (e.g., coderay).")
    public String highlighter = "prettify";

    public void setCommander(JCommander commander) {
        this.commander = commander;
    }

    private ArgumentHelper() {
    }

    public static ArgumentHelper parse(String[] args) {
        ArgumentHelper arguments = new ArgumentHelper();
        arguments.setCommander(new JCommander(arguments, args));

        return arguments;
    }

    public void usage() {
        commander.usage();
    }
}
