package org.dikhim.jclicker.util;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.*;

public class Cli {
    private static final Logger log = Logger.getLogger(Cli.class.getName());
    private String[] args;
    private Options options = new Options();

    // args
    private boolean guiApplication = true;
    private boolean eventRecording = true;
    private File file;
    private boolean openFile = false;
    private boolean runFile = false;
    private int httpPort;
    private int socketPort;
    private boolean runHttpServer = false;
    private boolean runSocketServer = false;

    public Cli(String[] args) {

        this.args = args;

        Option help = Option.builder("h")
                .longOpt("help")
                .desc("show help ")
                .build();
        Option openFile = Option.builder("o")
                .longOpt("open-file")
                .hasArg()
                .argName("file path")
                .desc("open file ")
                .build();
        Option runFile = Option.builder("r")
                .longOpt("run-file")
                .hasArg()
                .argName("file path")
                .desc("run file ")
                .build();
        Option guiApplication = Option.builder("g")
                .longOpt("gui")
                .hasArg()
                .argName("enable")
                .desc("true - gui, false - cli application ")
                .build();
        Option httpServer = Option.builder("H")
                .longOpt("http-server")
                .hasArg()
                .argName("port")
                .desc("run http server ")
                .build();
        Option socketServer = Option.builder("S")
                .longOpt("socket-server")
                .hasArg()
                .argName("port")
                .desc("run socket server ")
                .build();
        Option recording = Option.builder("R")
                .longOpt("recording")
                .hasArg()
                .argName("enable")
                .desc("true - activate, false - disable mouse and keyboard recording ")
                .build();


        options.addOption(help);
        options.addOption(openFile);
        options.addOption(runFile);
        options.addOption(guiApplication);
        options.addOption(httpServer);
        options.addOption(socketServer);
        options.addOption(recording);
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

            // gui
            if (cmd.hasOption("g")) {
                String arg = cmd.getOptionValue("nogui");
                try {
                    guiApplication = Boolean.parseBoolean(arg);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Illegal argument in option '-gui'");
                }
            }

            // open
            if (cmd.hasOption("o")) {
                String arg = cmd.getOptionValue("o");
                if (arg == null)
                    throw new IllegalArgumentException("Illegal argument in option '-o'");

                file = new File(arg);
                if (!file.exists() || file.isDirectory())
                    throw new IllegalArgumentException("file doesn't exist -o='" + file.getAbsolutePath() + "'");
                if (!isGuiApplication())
                    throw new IllegalArgumentException("you can open file only in gui application");

                openFile = true;
            }


            // run
            if (cmd.hasOption("r")) {
                String arg = cmd.getOptionValue("r");
                if (arg == null)
                    throw new IllegalArgumentException("Illegal argument in option '-r'");

                file = new File(arg);
                if (!file.exists() || file.isDirectory())
                    throw new IllegalArgumentException("file doesn't exist -r='" + file.getAbsolutePath() + "'");

                openFile = false;
                runFile = true;
            }


            // run
            if (cmd.hasOption("H")) {
                String arg = cmd.getOptionValue("H");

                try {
                    httpPort = Integer.parseInt(arg);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Illegal argument in option '-http'");
                }

                runHttpServer = true;
            }

            // run
            if (cmd.hasOption("S")) {

                String arg = cmd.getOptionValue("S");

                try {
                    socketPort = Integer.parseInt(arg);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Illegal argument in option '-socket'");
                }

                runSocketServer = true;
            }

            // rec
            if (cmd.hasOption("R")) {
                String arg = cmd.getOptionValue("R");
                try {
                    eventRecording = Boolean.parseBoolean(arg);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Illegal argument in option '-rec'");
                }

            }


        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse command line properties");
            help();
        } catch (IllegalArgumentException e2) {
            log.log(Level.SEVERE, e2.getMessage());
            help();
        }
    }

    private void help() {
        // This prints out some help
        HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("Main", options);
        System.exit(0);
    }

/////////////////////// GETTERS

    public boolean isGuiApplication() {
        return guiApplication;
    }

    public File getFile() {
        return file;
    }

    public boolean isOpenFile() {
        return openFile;
    }

    public boolean isRunFile() {
        return runFile;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public boolean isRunHttpServer() {
        return runHttpServer;
    }

    public boolean isRunSocketServer() {
        return runSocketServer;
    }

    public boolean isEventRecording() {
        return eventRecording;
    }
}