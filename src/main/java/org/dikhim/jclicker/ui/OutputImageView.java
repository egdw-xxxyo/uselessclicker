package org.dikhim.jclicker.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.dikhim.jclicker.util.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OutputImageView extends AnchorPane implements Initializable {

    public OutputImageView() {
        load();
    }

    private void load() {
        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setClassLoader(getClass().getClassLoader());
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setLocation(getClass().getResource("/fxml/OutputImageView.fxml"));

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private ImageView image;

    @FXML
    private Button leftLeftward;

    @FXML
    private Button leftRightward;

    @FXML
    private Button topDownward;

    @FXML
    private Button topUpward;

    @FXML
    private Button rightLeftward;

    @FXML
    private Button rightRightward;

    @FXML
    private Button bottomDownward;

    @FXML
    private Button bottomUpward;

    @FXML
    private Button openBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button insertBtn;

    @FXML
    private Button loadBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private Button zommInBtn;

    @FXML
    private Button zoomOutBtn;

    @FXML
    private Label scaleLbl;

    
    private BufferedImage loadedImage;
    private BufferedImage visibleImage;
    private DoubleProperty scale = new SimpleDoubleProperty(1);

    @FXML
    void bottomDownward(ActionEvent event) {

    }

    @FXML
    void bottomUpward(ActionEvent event) {

    }

    @FXML
    void insert(ActionEvent event) {

    }

    @FXML
    void leftLeftward(ActionEvent event) {

    }

    @FXML
    void leftRightward(ActionEvent event) {

    }

    @FXML
    void load(ActionEvent event) {

    }

    @FXML
    void open(ActionEvent event) {

    }

    @FXML
    void reset(ActionEvent event) {

    }

    @FXML
    void rightLeftward(ActionEvent event) {

    }

    @FXML
    void rightRightward(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {

    }

    @FXML
    void topDownward(ActionEvent event) {

    }

    @FXML
    void topUpward(ActionEvent event) {

    }

    @FXML
    void zoomIn(ActionEvent event) {

    }

    @FXML
    void zoomOut(ActionEvent event) {

    }
    
    private void repaint() {
        
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadedImage = ImageIO.read(getClass().getResourceAsStream("/images/application.png"));
            visibleImage = ImageUtil.clone(loadedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
