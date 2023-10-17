package org.dikhim.clickauto.util.logger;

import java.util.function.Consumer;

public class ClickAutoLog {
    private static ClickAutoLog log = new ClickAutoLog();

    public static ClickAutoLog get() {
        return log;
    }

    private Printer infoPrinter;
    private Printer errorPrinter;
    private Printer outPrinter;

    synchronized public void info(String string) {
        if (infoPrinter != null) infoPrinter.print(string);
    }

    synchronized public void info(String string, Object... args) {
        if (infoPrinter != null) infoPrinter.format(string, args);
    }

    synchronized public void error(String string) {
        if (errorPrinter != null) errorPrinter.print(string);
    }

    synchronized public void error(String string, Object... args) {
        if (errorPrinter != null) errorPrinter.format(string, args);
    }

    synchronized public void out(String string) {
        if (outPrinter != null) outPrinter.print(string);
    }

    synchronized public void out(String string, Object... args) {
        if (outPrinter != null) outPrinter.format(string, args);
    }

    synchronized public void addInfoHandler(Consumer<String> consumer) {
        if (infoPrinter == null) infoPrinter = new SimplePrinter();
        infoPrinter.addHandler(consumer);
    }

    synchronized public void addErrorHandler(Consumer<String> consumer) {
        if (errorPrinter == null) errorPrinter = new SimplePrinter();
        errorPrinter.addHandler(consumer);
    }

    synchronized public void addOutHandler(Consumer<String> consumer) {
        if (outPrinter == null) outPrinter = new SimplePrinter();
        outPrinter.addHandler(consumer);
    }

}
