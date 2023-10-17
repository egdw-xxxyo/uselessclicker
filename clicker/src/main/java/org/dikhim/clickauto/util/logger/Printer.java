package org.dikhim.clickauto.util.logger;

import java.util.function.Consumer;

public interface Printer {
    void print(String s);

    void println(String s);

    void addHandler(Consumer<String> consumer);

    void removeHandlers();

    void format(String string, Object... args);
}
