package org.dikhim.jclicker.jsengine.objects;

import org.dikhim.jclicker.jsengine.robot.Robot;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

public class JsClipboardObject implements ClipboardObject{
    private final Object monitor;
    
    public JsClipboardObject(Robot robot) {
        this.monitor = robot.getMonitor();
    }

    /**
     * Get the String residing on the clipboard.
     *
     * @return any text found on the Clipboard; if none found, return an
     * empty String.
     */
    public String get() {
        synchronized (monitor) {
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
    public void set(String str){
        synchronized (monitor) {
            StringSelection stringSelection = new StringSelection(str);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }
}
