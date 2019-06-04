package org.dikhim.jclicker.jsengine.clickauto.generators;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

public abstract class SimpleOldCodeGenerator implements OldCodeGenerator {
    private String objectName;
    private int lineSize;
    private StringBuilder sb = new StringBuilder();

    private List<Method> methods = new ArrayList<>();

    private final int MIN_LINE_SIZE = 50;

    SimpleOldCodeGenerator(String objectName, int lineSize, Class c) {
        this.objectName = objectName;
        setLineSize(lineSize);
        methods.addAll(Arrays.asList(c.getDeclaredMethods()));
        for(Class<?> inf: c.getInterfaces()){
            methods.addAll(Arrays.asList(inf.getDeclaredMethods()));
        }
    }

    SimpleOldCodeGenerator(String objectName, Class c) {
        this.objectName = objectName;
        methods.addAll(Arrays.asList(c.getDeclaredMethods()));
        for(Class<?> inf: c.getInterfaces()){
            methods.addAll(Arrays.asList(inf.getDeclaredMethods()));
        }
        
        setLineSize(MIN_LINE_SIZE);
    }

    /**
     * Appends the specified string to character sequence.
     *
     * @param str a string.
     * @return a reference to this object
     */
    protected SimpleOldCodeGenerator append(String str) {
        sb.append("'").append(str).append("'");
        return this;
    }

    /**
     * Appends the string representation of the int argument to this sequence.
     *
     * @param i an int.
     * @return a reference to this object
     */
    protected SimpleOldCodeGenerator append(int i) {
        sb.append(i);
        return this;
    }

    /**
     * Appends the string representation of the float argument to this sequence.
     *
     * @param f a float.
     * @return a reference to this object
     */
    protected SimpleOldCodeGenerator append(float f) {
        sb.append(f);
        return this;
    }
    /**
     * Appends the string representation of the double argument to this sequence.
     *
     * @param f a double.
     * @return a reference to this object
     */
    protected SimpleOldCodeGenerator append(double f) {
        sb.append(f);
        return this;
    }

    @Override
    public int getLineSize() {
        return lineSize;
    }

    @Override
    public void setLineSize(int lineSize) {
        if (lineSize < MIN_LINE_SIZE) {
            this.lineSize = MIN_LINE_SIZE;
        } else {
            this.lineSize = lineSize;
        }
    }

    @Override
    public String getObjectName() {
        return objectName;
    }

    @Override
    public String getGeneratedCode() {
        return separateOnLines(sb, lineSize);
    }

    public void buildStringForCurrentMethod(Object... params) {
        String objectName = getObjectName();
        String methodName = getMethodName();

        sb = new StringBuilder();
        sb.append(objectName).append(".")
                .append(methodName).append("(");

        Method m = getMethodWithName(methodName);
        Type[] gpType;
        if (m != null) {
            gpType = m.getGenericParameterTypes();
        } else {
            return;
        }

        for (int i = 0; i < gpType.length; i++) {
            if (gpType[i].equals(String.class)) {
                append((String) params[i]);
            } else if (gpType[i].equals(int.class)) {
                append((int) params[i]);
            } else if (gpType[i].equals(float.class)) {
                append((float) params[i]);
            }else if (gpType[i].equals(double.class)) {
                append((double) params[i]);
            }
            if (i != gpType.length - 1) sb.append(",");
        }
        sb.append(");\n");
    }

    @Override
    public void invokeMethodWithDefaultParams(String methodName) {
        Method m = getMethodWithName(methodName);
        Type[] gpType;
        if (m != null) {
            gpType = m.getGenericParameterTypes();
        } else {
            return;
        }
        Object[] params = new Object[gpType.length];
        for (int i = 0; i < gpType.length; i++) {
            if (gpType[i].equals(String.class)) {
                params[i] = "";
            } else if (gpType[i].equals(int.class)) {
                params[i] = 0;
            } else if (gpType[i].equals(float.class)) {
                params[i] = 0;
            }
        }
        try {
            m.invoke(this, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getMethodNames() {
        Set<String> methodsNamesSet = new HashSet<>();
        for (Method m : methods) {
            if (Modifier.isPublic(m.getModifiers()))
                methodsNamesSet.add(m.getName());
        }
        List<String> methodNames = new ArrayList<>(methodsNamesSet);
        methodNames.sort(Comparator.naturalOrder());
        return methodNames;
    }


    private Method getMethodWithName(String methodName) {
        for (Method m : methods) {
            if (!m.getName().equals(methodName)) {
                continue;
            }
            return m;
        }
        return null;
    }

    public StringBuilder getSb() {
        return sb;
    }
}
