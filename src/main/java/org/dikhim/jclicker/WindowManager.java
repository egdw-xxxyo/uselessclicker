package org.dikhim.jclicker;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.dikhim.jclicker.controllers.MainController;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

@SuppressWarnings("AccessStaticViaInstance")
public class WindowManager {
    private static WindowManager windowManager;
    private Preferences preferences = Preferences.userRoot().node(getClass().getName());
    
    private ResourceBundle resourceBundle;
    private Map<String, Stage> stageMap = new HashMap<>();
    private Map<String, Scene> sceneMap = new HashMap<>();
    private Map<String, Object> paramMap = new HashMap<>();
    private Locale locale;

    public static void initialization(Map<String, Object> paramMap, Locale locale) {
        if (windowManager == null) {
            windowManager = new WindowManager(paramMap, locale);
        }
    }

    public static WindowManager getInstance() {
        return windowManager;
    }


    private WindowManager(Map<String, Object> paramMap, Locale locale) {
        this.paramMap = paramMap;
        this.locale = locale;
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        resourceBundle = ResourceBundle.getBundle("i18n/WindowNames",locale);

        sceneMap.put("about", loadAboutScene(locale));
        sceneMap.put("settings", loadConfigScene(locale));
        sceneMap.put("help", loadHelpScene(locale));
        sceneMap.put("main", loadMainScene(locale));
        sceneMap.put("server", loadServerScene(locale));


        Stage stage = new Stage();
        stage.setScene(sceneMap.get("about"));
        stage.setTitle(resourceBundle.getString("about"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/info.png")));
        stageMap.put("about", stage);

        stage = new Stage();
        stage.setScene(sceneMap.get("settings"));
        stage.setTitle(resourceBundle.getString("settings"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/config.png")));
        stageMap.put("settings", stage);

        stage = new Stage();
        stage.setScene(sceneMap.get("help"));
        stage.setTitle(resourceBundle.getString("help"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/info.png")));
        stageMap.put("help", stage);

        stage = new Stage();
        stage.setScene(sceneMap.get("main"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/cursor.png")));
        stageMap.put("main", stage);

        stage = new Stage();
        stage.setScene(sceneMap.get("server"));
        stage.setTitle(resourceBundle.getString("server"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/server.png")));
        stageMap.put("server", stage);
    }

    public void showStage(String stageName) {
        Stage stage = stageMap.get(stageName);
        if (stage != null) {
            stage.show();
        }
    }


    private Scene loadMainScene(Locale locale) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main/MainScene.fxml"));
        loader.setResources(ResourceBundle.getBundle("i18n/MainScene", locale));
        Parent root = loader.load();

        MainController controller = loader.getController();
        return new Scene(root);
    }

    private Scene loadHelpScene(Locale locale) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main/HelpScene.fxml"));
        loader.setResources(ResourceBundle.getBundle("i18n/HelpScene", locale));

        Parent root = loader.load();
        return new Scene(root);
    }

    private Scene loadAboutScene(Locale locale) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main/AboutScene.fxml"));
        loader.setResources(ResourceBundle.getBundle("i18n/AboutScene", locale));
        Parent root = loader.load();
        return new Scene(root);
    }

    private Scene loadConfigScene(Locale locale) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/config/ConfigScene.fxml"));
        loader.setResources(ResourceBundle.getBundle("i18n/SettingsScene", locale));
        Parent root = loader.load();
        return new Scene(root);
    }

    private Scene loadServerScene(Locale locale) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/server/ServerScene.fxml"));
        loader.setResources(ResourceBundle.getBundle("i18n/ServerScene", locale));
        Parent root = loader.load();
        return new Scene(root);
    }

    public Stage getStage(String stageName) {
        return stageMap.get(stageName);
    }
    
     
    public File openFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resourceBundle.getString("open"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(resourceBundle.getString("allTypes"), "*.*"),
                new FileChooser.ExtensionFilter("*.js", "*.js"));

        String pathFolder = preferences.get("last-opened-folder", "");
        if (!pathFolder.isEmpty()) {
            fileChooser.setInitialDirectory(new File(pathFolder));
        }
        File file = fileChooser.showOpenDialog(getStage("main"));
        if (file != null) {
            preferences.put("last-opened-folder", file.getParentFile().getAbsolutePath());
        }
        return file;
    }
    public File saveFileAs(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resourceBundle.getString("saveAs"));
        fileChooser.setInitialFileName("newFile.js");

        String pathFolder = preferences.get("last-saved-folder", "");
        if (!pathFolder.isEmpty())
            fileChooser.setInitialDirectory(new File(pathFolder));

        File file = fileChooser.showSaveDialog(getStage("main"));
        if (file != null) {
            preferences.put("last-saved-folder", file.getParentFile().getAbsolutePath());
        }
        return file;
    }
    
    
}
