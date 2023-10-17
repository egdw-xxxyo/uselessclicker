package org.dikhim.clickauto.jsengine.objects;

import org.dikhim.clickauto.jsengine.robot.Robot;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

public class ScriptClipboardObject implements ClipboardObject{
    protected final Robot robot;
    
    public ScriptClipboardObject(Robot robot) {
        this.robot = robot;
    }

    /**
     * Get the String residing on the clipboard.
     *
     * @return any text found on the Clipboard; if none found, return an
     * empty String.
     */
    @Override
    public String get() {
        synchronized (robot) {
            String result = "";
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            //odd: the Object param of getContents is not currently used
            Transferable contents = clipboard.getContents(null);
            boolean hasTransferableText =
                    (contents != null) &&
                            contents.isDataFlavorSupported(DataFlavor.stringFlavor)
                    ;
            if (hasTransferableText) {
                try {
                    result = (String)contents.getTransferData(DataFlavor.stringFlavor);
                }
                catch (UnsupportedFlavorException | IOException ex){
                    System.out.println(ex);
                    ex.printStackTrace();
                }
            }
            return result;
        }
    }

    /**
     * Place a String on the clipboard.
     */
    @Override
    public void set(String str){
        synchronized (robot) {
            StringSelection stringSelection = new StringSelection(str);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }
}
