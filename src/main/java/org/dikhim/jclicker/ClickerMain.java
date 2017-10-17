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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClickerMain extends Application {
	public static Script script;
	public static Stage stage;
	public static Robot robot;
	public static Actions actions;
	public static JSEngine jse;
	public static MainController controller;
	@Override
	public void start(Stage primaryStage) throws IOException {
		System.out.println("Start");
		Logger logger = Logger
				.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			System.exit(-1);
		}
		System.out.println("Logger off");
		stage = primaryStage;
		try {
			robot = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
			OutStream.print(e1.getMessage());
		}
		System.out.println("Robot created");
		jse = new JSEngine(robot);
		System.out.println("JS created");
		actions = new Actions();

		
		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/MainScene.fxml").openStream());

		System.out.println("FXML loded");
		controller = (MainController)fxmlLoader.getController();
		System.out.println("Controller get");
		primaryStage.setTitle("JClicker");
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/cursor.png")));
		primaryStage.setScene(new Scene(root, 600, 400));
		primaryStage.show();
		System.out.println("Show");

		MouseEventsManager mouseListener = MouseEventsManager.getInstance();
		GlobalScreen.addNativeMouseListener(mouseListener);
		GlobalScreen.addNativeMouseMotionListener(mouseListener);

		KeyEventsManager keyListener = KeyEventsManager.getInstance();
		GlobalScreen.addNativeKeyListener(keyListener);

		keyListener.addPressListener(
				new ShortcutEqualsHandler("stopScript", "CONTROL ALT S", () -> {
					jse.stop();
				}));

	}

	
	
	
	public static void runCode() {
		try {
			jse.start();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			OutStream.print(e.getMessage());
		}
	}
	public static void runCode(String code) {
		jse.putCode(code);
		try {
			jse.start();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			OutStream.print(e.getMessage());
		}
	}
	@Override
	public void stop() throws Exception {
		try {
			GlobalScreen.unregisterNativeHook();
			jse.stop();
		} catch (NativeHookException e) {
			e.printStackTrace();
			OutStream.print(e.getMessage());
		}
		super.stop();
	}
	
	public static void setScript(Script script) {
		ClickerMain.script= script;
		ClickerMain.updateTitle();
		
	}
	
	public static Script getScript() {
		return script;
	}
	
	public static void updateTitle() {
		File file = script.getScriptFile();
		if(file!=null) {
			
			stage.setTitle(script.getScriptFile().getName());
		}else {
			stage.setTitle("newFile.js");
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
