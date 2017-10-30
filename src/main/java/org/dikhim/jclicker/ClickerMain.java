package org.dikhim.jclicker;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.ScriptException;

import org.dikhim.jclicker.actions.Actions;
import org.dikhim.jclicker.events.KeyEventsManager;
import org.dikhim.jclicker.events.MouseEventsManager;
import org.dikhim.jclicker.events.ShortcutEqualsHandler;
import org.dikhim.jclicker.jsengine.JSEngine;
import org.dikhim.jclicker.model.Script;
import org.dikhim.jclicker.util.OutStream;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClickerMain extends Application {
	public static Script script;
	public static Stage stage;
	public static Robot robot;
	public static JSEngine jse;
	public static MainController controller;
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		System.out.println("Start");
		Logger logger = Logger
				.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			System.exit(-1);
		}
		
		try {
			robot = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Program start failure");
			alert.setHeaderText("Can not create 'Robot' object ");
			alert.setContentText(
					"It can occurs if you have no permission for it\n" + "message:\n"+e1.getMessage());
			alert.showAndWait();
			stop();
		}
		jse = new JSEngine(robot);
		newScript();
		
		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader
				.load(getClass().getResource("/MainScene.fxml").openStream());

		System.out.println("FXML loded");
		controller = (MainController) fxmlLoader.getController();
		System.out.println("Controller loaded");
		primaryStage.getIcons().add(new Image(
				getClass().getResourceAsStream("/images/cursor.png")));
		primaryStage.setScene(new Scene(root, 800, 600));
		primaryStage.show();
		System.out.println("Show");

		MouseEventsManager mouseListener = MouseEventsManager.getInstance();
		GlobalScreen.addNativeMouseListener(mouseListener);
		GlobalScreen.addNativeMouseMotionListener(mouseListener);
		GlobalScreen.addNativeMouseWheelListener(mouseListener);
		KeyEventsManager keyListener = KeyEventsManager.getInstance();
		GlobalScreen.addNativeKeyListener(keyListener);

		keyListener.addPressListener(
				new ShortcutEqualsHandler("stopScript", "CONTROL ALT S", () -> {
					jse.stop();
				}));

	}

	public static void runScript() {
		if (script == null)
			return;
		jse.putCode(script.getStringProperty().get());
		try {
			jse.start();
		} catch (ScriptException e) {
			e.printStackTrace();
			OutStream.print(e.getMessage());
		}
	}
	public static void stopScript() {
		jse.stop();
	}
	@Override
	public void stop() throws Exception {
		try {
			GlobalScreen.unregisterNativeHook();
			jse.stop();
		} catch (NativeHookException e) {
			e.printStackTrace();
		}
		super.stop();
	}

	public static void setScript(Script script) {
		StringProperty prop =  ClickerMain.script.getStringProperty();
		prop.set(script.getStringProperty().get());
		script.setScriptProperty(prop);
		ClickerMain.script = script;
		ClickerMain.updateTitle();
	}

	public static Script getScript() {
		return script;
	}
	public static void newScript() {
		StringProperty prop;
		if(script!=null) {
			prop =  script.getStringProperty();
			prop.set("");
			script = new Script();
			script.setScriptProperty(prop);
		}else {
			script = new Script();
			
		}
		ClickerMain.updateTitle();
	}

	public static void updateTitle() {
		stage.setTitle(script.getName());
	}

	public static void main(String[] args) {
		launch(args);
	}

}
