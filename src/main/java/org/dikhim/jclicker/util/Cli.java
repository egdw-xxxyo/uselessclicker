package org.dikhim.jclicker.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.*;

public class Cli {
    private static final Logger log = Logger.getLogger(Cli.class.getName());
    private String[] args = null;
    private Options options = new Options();

    // args
    private boolean console = false;
    private String filePath = "";


    public boolean isConsole() {
        return console;
    }

    public void setConsole(boolean console) {
        this.console = console;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public Cli(String[] args) {

        this.args = args;

        Option file = Option.builder("f").argName("file").required(false).desc("open file. ").hasArg().build();
        options.addOption("h", "help", false, "show help.");
        options.addOption("c", "console", false, "show help.");
        options.addOption(file);
        parse();
    }

    public void parse() {
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);

            // help
            if (cmd.hasOption("h"))
                help();

            // console
            if (cmd.hasOption("c")) {
                console = true;
            }

            // file
            if (cmd.hasOption("f")) {
                String value = cmd.getOptionValue("f");

                if (value == null) {
                    System.out.println("Missed the file path parameter");
                } else {
                    filePath = value;
                }

                log.log(Level.INFO, "Open file '" + value + "'");
            }

        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse command line properties", e);
            help();
        }
    }

    private void help() {
        // This prints out some help
        HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("Main", options);
        System.exit(0);
    }

}