package org.dikhim.clickauto.jsengine.utils.typer;

public class GbLayout extends UsLayout{
    {
        setLayout("gb");
        setDescription("English(UK)");
        put("2", "2", "\"");
        put("3", "3", "£");
        put("`", "`", "¬");
        put("\\", "#", "~");
        put("'", "'", "@");
        put("SPACE", " ", " ");
    }
}
