package org.dikhim.jclicker.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FxmlSkin<C extends CustomControl> extends SkinBase<C> {
    private C control;
    private String fxml;

    public FxmlSkin(C control, String fxml) {
        super(control);
        this.control = control;
        this.fxml = fxml;
        this.load();
    }

    private void load() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        loader.setController(control);
        try {
            Region root = loader.load();
            control.setChild(root);
        } catch (IOException ex) {
            Logger.getLogger(FxmlSkin.class.getName()).log(Level.SEVERE, "Couldn't load fxml file:" + fxml, ex);
        }
    }

}