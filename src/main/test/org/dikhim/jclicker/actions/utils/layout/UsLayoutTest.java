package org.dikhim.jclicker.actions.utils.layout;

import org.junit.Test;

import static org.junit.Assert.*;

public class UsLayoutTest {

    @Test
    public void getKeyFor() {
        UsLayout usLayout = new UsLayout();
        assertEquals("A", usLayout.getKeyFor("a"));
        assertEquals("A", usLayout.getKeyFor("A"));

        assertEquals("1", usLayout.getKeyFor("1"));
        assertEquals("1", usLayout.getKeyFor("!"));

        assertEquals("=", usLayout.getKeyFor("="));
        assertEquals("=", usLayout.getKeyFor("+"));

        assertEquals("\\", usLayout.getKeyFor("|"));
        assertEquals("\\", usLayout.getKeyFor("|"));

        assertEquals(" ", usLayout.getKeyFor(" "));
        assertEquals(" ", usLayout.getKeyFor(" "));
    }

    @Test
    public void getIndexFor() {
        UsLayout usLayout = new UsLayout();
        assertEquals(0, usLayout.getIndexFor(usLayout.getKeyFor("a"),"a"));
        assertEquals(1, usLayout.getIndexFor(usLayout.getKeyFor("A"),"A"));

        assertEquals(0, usLayout.getIndexFor(usLayout.getKeyFor("1"),"1"));
        assertEquals(1, usLayout.getIndexFor(usLayout.getKeyFor("!"),"!"));
        
        assertEquals(0, usLayout.getIndexFor(usLayout.getKeyFor(" ")," "));
    }
}