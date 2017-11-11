package org.dikhim.jclicker.util.output;

public class SystemOutput implements CustomOutput{
    @Override
    public void print(String text) {
        System.out.print(text);
    }

    @Override
    public void println(String text) {
        System.out.println(text);
    }
}
