package org.dikhim.jclicker.jsengine.objects.generators;

public class SimpleCodeGenerator implements CodeGenerator {
    private String objectName = "mouse";
    private int lineSize = 40;
    private StringBuilder sb;

    public SimpleCodeGenerator(String objectName, int lineSize) {
        this.objectName = objectName;
        this.lineSize = lineSize;
    }


    /**
     * Puts object's and method's names.
     *
     * @return a reference to this object.
     */
    protected SimpleCodeGenerator begin() {
        sb = new StringBuilder();
        append(getObjectName()).append(".")
                .append(getMethodName());
        return this;
    }

    /**
     * Appends the specified string to character sequence.
     *
     * @param str a string.
     * @return a reference to this object
     */
    protected SimpleCodeGenerator append(String str) {
        sb.append(str);
        return this;
    }

    /**
     * Appends the string representation of the int argument to this sequence.
     *
     * @param i an int.
     * @return a reference to this object
     */
    protected SimpleCodeGenerator append(int i) {
        sb.append(i);
        return this;
    }

    /**
     * Appends the string representation of the float argument to this sequence.
     *
     * @param f a float.
     * @return a reference to this object
     */
    protected SimpleCodeGenerator append(float f) {
        sb.append(f);
        return this;
    }

    public int getLineSize() {
        return lineSize;
    }

    public void setLineSize(int lineSize) {
        this.lineSize = lineSize;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getGeneratedCode() {
        return sb.toString();
    }
}
