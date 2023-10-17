package org.dikhim.clickauto.util.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimplePrinter implements Printer {

    private List<Consumer<String>> printHandlers = new ArrayList<>();

    @Override
    public void print(String s) {
        printHandlers.forEach(h -> h.accept(s));
    }

    @Override
    public void println(String s) {
        printHandlers.forEach(h -> h.accept(s + "\n"));
    }

    @Override
    public void addHandler(Consumer<String> consumer) {
        printHandlers.add(consumer);
    }

    @Override
    public void removeHandlers() {
        printHandlers.clear();
    }

    @Override
    public void format(String string, Object... args) {
        printHandlers.forEach(h -> h.accept(String.format(string, args)));
    }
}
