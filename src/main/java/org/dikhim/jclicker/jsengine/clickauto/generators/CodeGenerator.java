package org.dikhim.jclicker.jsengine.clickauto.generators;

import java.util.List;

public interface CodeGenerator {
    int getLineSize();

    String forMethod(String methodMane, Object... params);

    default String separateOnLines(StringBuilder inputStringBuilder, int lineSize) {
        if (inputStringBuilder.length() <= lineSize) return inputStringBuilder.toString();

        char[] c = inputStringBuilder.toString().toCharArray();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < c.length - 4; i++) {
            if ((sb.length() + 3) % lineSize != 0) {
                sb.append(c[i]);
            } else {
                sb.append("'+\n");
                sb.append('\'');
                sb.append(c[i]);
            }
        }
        sb.append("');\n");
        return sb.toString();
    }

    List<String> getMethodNames();
}
