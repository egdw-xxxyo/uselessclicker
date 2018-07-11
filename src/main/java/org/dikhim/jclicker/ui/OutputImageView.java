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
import org.dikhim.jclicker.WindowManager;
import org.dikhim.jclicker.jsengine.objects.CreateObject;
import org.dikhim.jclicker.jsengine.objects.JsCreateObject;
import org.dikhim.jclicker.jsengine.objects.generators.CreateObjectGenerator;
import org.dikhim.jclicker.util.ImageUtil;
import org.dikhim.jclicker.util.Out;
import org.dikhim.jclicker.util.ZipBase64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static org.dikhim.jclicker.util.ImageUtil.resizeImage;

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
    private BufferedImage transformedImage;
    private DoubleProperty scale = new SimpleDoubleProperty(1);

    private IntegerProperty top = new SimpleIntegerProperty(0);
    private IntegerProperty right = new SimpleIntegerProperty(0);
    private IntegerProperty bottom = new SimpleIntegerProperty(0);
    private IntegerProperty left = new SimpleIntegerProperty(0);

    private Consumer<String> onInsert;


    @FXML
    void insert(ActionEvent event) {
        if(originalImage == null) return;
        BufferedImage croppedImage = ImageUtil.crop(originalImage, top.get(), right.get(), bottom.get(), left.get());
        try {
            byte[] data = ImageUtil.getByteArray(croppedImage);
            String encodedData = ZipBase64.encode(data);
            CreateObjectGenerator createObjectGenerator = new CreateObjectGenerator(120);
            createObjectGenerator.image(encodedData);
            String resultString = createObjectGenerator.getGeneratedCode();

            onInsert.accept(resultString);
        } catch (IOException e) {
            Out.println("Cannot convert image to the string representation");
        }
    }

    @FXML
    void load(ActionEvent event) {
        String data = WindowManager.getInstance().showImageInputDialog();
        if (!data.isEmpty()) {
            BufferedImage tmp = originalImage;
            try {
                CreateObject createObject = new JsCreateObject();
                originalImage = createObject.image(data);
                if (originalImage == null) throw new IOException();
                reset();
                repaint();
            } catch (Exception e) {
                Out.println("Cannot load image from the string");
                originalImage = tmp;
            }
        }
    }

    @FXML
    void open(ActionEvent event) {
        File file = WindowManager.getInstance().openImageFile();
        BufferedImage tmp = originalImage;
        if (file != null) {
            try {
                originalImage = ImageIO.read(file);
                if (originalImage == null) throw new IOException();
                reset();
                repaint();
            } catch (Exception e) {
                Out.println("Cannot open image");
                originalImage = tmp;
            }
        }
    }


    @FXML
    void save(ActionEvent event) {
        if(originalImage == null) return;
        File file = WindowManager.getInstance().saveImageFileAs();
        if (file != null) {
            try {
                ImageIO.write(transformedImage, "PNG", file);
            } catch (IOException e) {
                Out.println("Cannot save image");
            }
        }
    }

    @FXML
    void reset(ActionEvent event) {
        if (originalImage == null) return;
        reset();
    }

    private void reset() {
        scale.set(1);
        top.set(0);
        right.set(0);
        bottom.set(0);
        left.set(0);
        repaint();
    }

    @FXML
    void topDownward(ActionEvent event) {
        if (originalImage == null) return;
        if (getCroppedImageHeight() > 1) top.set(top.get() + 1);
        repaint();
    }

    @FXML
    void topUpward(ActionEvent event) {
        if (originalImage == null) return;
        if (top.get() > 0) top.set(top.get() - 1);
        repaint();
    }

    @FXML
    void rightLeftward(ActionEvent event) {
        if (originalImage == null) return;
        if (getCroppedImageWidth() > 1) right.set(right.get() + 1);
        repaint();
    }

    @FXML
    void rightRightward(ActionEvent event) {
        if (originalImage == null) return;
        if (right.get() > 0) right.set(right.get() - 1);
        repaint();
    }

    @FXML
    void bottomDownward(ActionEvent event) {
        if (originalImage == null) return;
        if (bottom.get() > 0) bottom.set(bottom.get() - 1);
        repaint();
    }

    @FXML
    void bottomUpward(ActionEvent event) {
        if (getCroppedImageHeight() > 1) bottom.set(bottom.get() + 1);
        repaint();
    }

    @FXML
    void leftLeftward(ActionEvent event) {
        if (originalImage == null) return;
        if (left.get() > 0) left.set(left.get() - 1);
        repaint();
    }

    @FXML
    void leftRightward(ActionEvent event) {
        if (originalImage == null) return;
        if (getCroppedImageWidth() > 1) left.set(left.get() + 1);
        repaint();
    }

    @FXML
    void zoomIn(ActionEvent event) {
        if (originalImage == null) return;
        if (scale.get() < 32) scale.setValue(scale.get() * 2);
        repaint();
    }

    @FXML
    void zoomOut(ActionEvent event) {
        if (originalImage == null) return;
        if (scale.get() > 0.5) scale.setValue(scale.get() / 2);
        repaint();
    }

    private int getCroppedImageWidth() {
        return originalImage.getWidth() - right.get() - left.get();
    }

    private int getCroppedImageHeight() {
        return originalImage.getHeight() - top.get() - bottom.get();
    }

    private void repaint() {
        if (originalImage == null) return;
        new Thread(() -> {
            transformedImage = ImageUtil.crop(originalImage, top.get(), right.get(), bottom.get(), left.get());

            double scaleDiff;
            if (Math.max(transformedImage.getHeight(), transformedImage.getWidth()) * scale.get() > 2048) {
                int tempScale = 1;
                while (Math.max(transformedImage.getHeight(), transformedImage.getWidth()) * scale.get() < 2048) {
                    tempScale++;
                }
                transformedImage = resizeImage(transformedImage, tempScale);

                scaleDiff = scale.get() / tempScale;

            } else {
                transformedImage = resizeImage(transformedImage, scale.get());
                scaleDiff = 1;
            }


            Platform.runLater(() -> {
                image.setFitWidth(transformedImage.getWidth() * scaleDiff);
                image.setFitHeight(transformedImage.getHeight() * scaleDiff);
                image.setImage(SwingFXUtils.toFXImage(transformedImage, null));
            });
        }).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AnchorPane.setTopAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);
        AnchorPane.setBottomAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);
        image.setSmooth(false);
        originalImage = null;
        repaint();
    }

    ///
    public void setOnInsert(Consumer<String> onInsert) {
        this.onInsert = onInsert;
    }

    public void loadImage(BufferedImage image) {
        originalImage = image;
        reset();
        repaint();
    }
}
