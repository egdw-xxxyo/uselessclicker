package org.dikhim.jclicker.jsengine.objects.generators;

import org.dikhim.jclicker.jsengine.objects.ClipboardObject;

public class ClipboardObjectCodeGenerator extends SimpleCodeGenerator implements ClipboardObject {
    public ClipboardObjectCodeGenerator(String objectName, int lineSize) {
        super(objectName, lineSize);
    }

    public ClipboardObjectCodeGenerator(int lineSize) {
        super("clipboard", lineSize);
    }

    public String get() {
        begin().append("();\n");
        return null;
    }

    public void set(String str) {
        begin().append("('")
                .append(str).append("');\n");
    }
}
