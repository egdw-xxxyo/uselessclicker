package org.dikhim.jclicker.jsengine.clickauto.generators;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SimpleCodeGenerator implements CodeGenerator {
    private static final int MIN_LINE_SIZE = 50;

    private int lineSize;
    private String objectName;
    List<Method> methods;

    public SimpleCodeGenerator(String objectName, Class c, int lineSize) {
        this.objectName = objectName;
        this.lineSize = lineSize;
        methods = new ArrayList<>(Arrays.asList(c.getDeclaredMethods()));
        for (Class<?> inf : c.getInterfaces()) {
            methods.addAll(Arrays.asList(inf.getDeclaredMethods()));
        }
    }

    public SimpleCodeGenerator(String objectName, Class c) {
        this(objectName, c, MIN_LINE_SIZE);
    }

    @Override
    public String forMethod(String methodName, Object... params) {
        Optional<Method> method = methods.stream().filter(method1 -> method1.getName().equals(methodName)).findFirst();
        if (!method.isPresent())
            throw new IllegalArgumentException("Given class doesn't contain the specified method");

        if (method.get().getParameterCount() != params.length)
            throw new IllegalArgumentException("Given wrong number of parameters");

        StringBuilder sb = new StringBuilder();

        sb.append(objectName);
        sb.append(".");
        sb.append(methodName);
        sb.append("(");

        for (int i = 0; i < params.length; i++) {
            append(sb, params[i]);
            if (i != params.length - 1) {
                sb.append(",");
            }
        }
        sb.append(");\n");

        return separateOnLines(sb, lineSize);
    }

    private void append(StringBuilder sb, Object param) {
        if (param instanceof Integer) {
            append(sb, (int) (param));
        } else if (param instanceof Float) {
            append(sb, (float) (param));
        } else if (param instanceof Double) {
            append(sb, (double) (param));
        } else if (param instanceof Long) {
            append(sb, (long) (param));
        } else if (param instanceof String) {
            append(sb, (String) (param));
        } else if (param instanceof Boolean) {
            append(sb, (boolean) (param));
        }
    }

    /**
     * Appends the specified string to character sequence.
     *
     * @param str a string.
     */
    protected void append(StringBuilder sb, String str) {
        sb.append("'").append(str).append("'");
    }

    /**
     * Appends the string representation of the int argument to this sequence.
     *
     * @param i an int.
     */
    protected void append(StringBuilder sb, int i) {
        sb.append(i);
    }

    /**
     * Appends the string representation of the float argument to this sequence.
     *
     * @param f a float.
     */
    protected void append(StringBuilder sb, float f) {
        sb.append(f);
    }

    /**
     * Appends the string representation of the double argument to this sequence.
     *
     * @param f a double.
     */
    protected void append(StringBuilder sb, double f) {
        sb.append(f);
    }

    /**
     * Appends the string representation of the boolean argument to this sequence.
     *
     * @param f a boolean.
     */
    protected void append(StringBuilder sb, boolean f) {
        sb.append(f);
    }

    /**
     * Appends the string representation of the long argument to this sequence.
     *
     * @param f a boolean.
     */
    protected void append(StringBuilder sb, long f) {
        sb.append(f);
    }

    @Override
    public int getLineSize() {
        return lineSize;
    }

    @Override
    public List<String> getMethodNames() {
        return methods.stream().map(Method::getName).collect(Collectors.toList());
    }
}
