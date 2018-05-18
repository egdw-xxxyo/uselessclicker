package org.dikhim.jclicker.util;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Out {
    private static List<Consumer<String>> printMethods = new ArrayList<>();
    private static List<Runnable> clearMethods = new ArrayList<>();

    synchronized public static void addPrintMethod(Consumer<String> printMethod) {
        printMethods.add(printMethod);
    }

    synchronized public static void print(String text) {
        Platform.runLater(
                () -> printMethods.forEach(
                        (method) -> method.accept(text)));

    }

    synchronized public static void println(String text) {
        Platform.runLater(
                () -> printMethods.forEach(
                        (method) -> method.accept(text + "\n")));
    }

    synchronized public static void addClearMethod(Runnable clear) {
        clearMethods.add(clear);
    }

    synchronized public static void clear() {
        Platform.runLater(
                () -> clearMethods.forEach(
                        Runnable::run));
    }
}
