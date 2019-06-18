package org.dikhim.jclicker.jfxcomponents;

import javafx.scene.control.Control;
import javafx.scene.layout.Region;

public class CustomControl extends Control {
    private Region region;
    protected CustomControl() {
        super();
    }

    public void setChild(Region region) {
        if(this.region != null) throw new IllegalArgumentException("Custom control can contain only one child node");
        this.region = region;
        this.region.prefWidthProperty().bind(widthProperty());
        this.region.prefHeightProperty().bind(heightProperty());
        getChildren().add(region);
    }
}
