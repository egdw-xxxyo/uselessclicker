package org.dikhim.jclicker.ui;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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


    private BufferedImage originalImage;
    private DoubleProperty scale = new SimpleDoubleProperty(1);

    private IntegerProperty top = new SimpleIntegerProperty(0);
    private IntegerProperty right = new SimpleIntegerProperty(0);
    private IntegerProperty bottom = new SimpleIntegerProperty(0);
    private IntegerProperty left = new SimpleIntegerProperty(0);

    @FXML
    void insert(ActionEvent event) {

    }

    @FXML
    void load(ActionEvent event) {

    }

    @FXML
    void open(ActionEvent event) {

    }


    @FXML
    void save(ActionEvent event) {

    }

    @FXML
    void reset(ActionEvent event) {
        scale.set(1);
        top.set(0);
        right.set(0);
        bottom.set(0);
        left.set(0);
    }

    @FXML
    void topDownward(ActionEvent event) {
        if (getCropedImageHeight() > 1) top.set(top.get() + 1);
    }

    @FXML
    void topUpward(ActionEvent event) {
        if (top.get() > 0) top.set(top.get() - 1);
    }

    @FXML
    void rightLeftward(ActionEvent event) {
        if (getCropedImageWidth() > 1) right.set(right.get() + 1);
    }

    @FXML
    void rightRightward(ActionEvent event) {
        if (right.get() > 0) right.set(right.get() - 1);
    }

    @FXML
    void bottomDownward(ActionEvent event) {
        if (bottom.get() > 0) bottom.set(bottom.get() - 1);

    }

    @FXML
    void bottomUpward(ActionEvent event) {
        if (getCropedImageHeight() > 1) bottom.set(bottom.get() + 1);
    }

    @FXML
    void leftLeftward(ActionEvent event) {
        if (left.get() > 0) left.set(left.get() - 1);        
    }

    @FXML
    void leftRightward(ActionEvent event) {
        if (getCropedImageWidth() > 1) left.set(left.get() + 1);
    }

    @FXML
    void zoomIn(ActionEvent event) {
        if (scale.get() < 16) scale.setValue(scale.get() * 2);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        if (scale.get() > 0.25) scale.setValue(scale.get() / 2);

    }

    private int getCropedImageWidth() {
        return originalImage.getWidth() - right.get() - left.get();
    }

    private int getCropedImageHeight() {
        return originalImage.getHeight() - top.get() - bottom.get();
    }

    private void repaint() {
        new Thread(() -> {
            System.out.println(String.format("scale:%s crop:%s %s %s %s", scale.get(), top.get(), right.get(), bottom.get(), left.get()));
            BufferedImage transformedImage = ImageUtil.crop(originalImage, top.get(), right.get(), bottom.get(), left.get());
            final BufferedImage resultImage = ImageUtil.resizeImage(transformedImage, scale.get());
            Platform.runLater(()->{
                image.setFitWidth(resultImage.getWidth());
                image.setFitHeight(resultImage.getHeight());
                image.setImage(SwingFXUtils.toFXImage(resultImage, null));
                System.gc();
            });
        }).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            originalImage = ImageIO.read(getClass().getResourceAsStream("/images/application.png"));
            scale.addListener((observable, oldValue, newValue) -> repaint());
            top.addListener((observable, oldValue, newValue) -> repaint());
            right.addListener((observable, oldValue, newValue) -> repaint());
            bottom.addListener((observable, oldValue, newValue) -> repaint());
            left.addListener((observable, oldValue, newValue) -> repaint());
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
