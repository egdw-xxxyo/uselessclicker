package org.dikhim.jclicker.jsengine.objects.generators;

import org.dikhim.jclicker.jsengine.objects.SystemObject;

public class SystemObjectCodeGenerator extends SimpleCodeGenerator implements SystemObject {

    public SystemObjectCodeGenerator(int lineSize) {
        super("system", lineSize);
    }

    public float getMultiplier() {
        begin().append("();\n");
        return 0;
    }

    public void print(String s) {
        begin().append("('")
                .append(s).append("');\n");
    }

    public void println(String s) {
        begin().append("('")
                .append(s).append("');\n");
    }

    public void registerShortcut(String shortcut, String function) {
        begin().append("('")
                .append(shortcut).append("','")
                .append(function).append("');\n");
    }

    public void resetMultiplier() {
        begin().append("();\n");
    }

    public void setMultiplier(float multiplier) {
        begin().append("(")
                .append(multiplier).append(");\n");
    }

    public void sleep(int ms) {
        begin().append("(")
                .append(ms).append(");\n");
    }
}
