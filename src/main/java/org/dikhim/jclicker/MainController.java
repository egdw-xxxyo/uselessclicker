package org.dikhim.jclicker;

import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.prefs.Preferences;

import org.apache.commons.io.FileUtils;
import org.dikhim.jclicker.events.KeyEventsManager;
import org.dikhim.jclicker.events.MouseEventsManager;
import org.dikhim.jclicker.events.MouseHandler;
import org.dikhim.jclicker.events.ShortcutIncludesHandler;
import org.dikhim.jclicker.model.Script;
import org.dikhim.jclicker.util.OutStream;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

public class MainController {
	@FXML
	private void initialize() {

		codeTextArea.textProperty().bindBidirectional(codeTextProperty);
		outTxt.textProperty().bindBidirectional(outTextProperty);

		listOfToggles.add(btnInsertKeyName);
		listOfToggles.add(btnInsertKeyCode);
		listOfToggles.add(btnInsertKeyCodeWithDelay);
		listOfToggles.add(btnInsertMouseName);
		listOfToggles.add(btnInsertMouseCode);
		listOfToggles.add(btnInsertMouseCodeWithDelay);
		
		codeTextArea.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(!enableCodeType) event.consume();
			}
		});
		codeTextArea.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(!enableCodeType) event.consume();
			}
		});
	}
	
	@FXML
	private Button btnRunScript;
	@FXML
	private Button btnStopScript;
	@FXML
	private Button btnNewFile;
	@FXML
	private Button btnOpenFile;
	@FXML
	private Button btnSaveFile;
	
	
	
	@FXML
	private TextArea codeTextArea;
	private StringProperty codeTextProperty = new SimpleStringProperty("");
	private boolean enableCodeType= true;

	@FXML
	private TextArea outTxt;
	private StringProperty outTextProperty = OutStream.getProperty();

	

	@FXML
	public void stopScript() {
		ClickerMain.actions.startRecording();
	}
	@FXML
	public void runScript() {
		ClickerMain.runCode(codeTextArea.getText());
	}
	@FXML
	public void newFile() {
		stopScript();
		codeTextProperty.set("");
		Script script = new Script();
		ClickerMain.setScript(script);
		script.setScript(codeTextProperty);
		codeTextArea.setEditable(true);
	}

	@FXML
	public void openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All types", "*.*"),
				new FileChooser.ExtensionFilter("JavaScript", "*.js"));

		Preferences prefs = Preferences.userNodeForPackage(ClickerMain.class);
		String pathFolder = prefs.get("last-opened-folder", null);
		if (pathFolder != null)
			fileChooser.setInitialDirectory(new File(pathFolder));

		File file = fileChooser.showOpenDialog(ClickerMain.stage);
		if (file != null) {
			Script script = new Script(file);
			ClickerMain.setScript(script);
			prefs.put("last-opened-folder",
					file.getParentFile().getAbsolutePath());

			codeTextArea.textProperty().bindBidirectional(script.getScript());
			codeTextArea.setEditable(true);
		}

	}

	@FXML
	public void saveFile() {
		Preferences prefs = Preferences.userNodeForPackage(ClickerMain.class);

		Script script = ClickerMain.getScript();
		if (script == null)
			return;

		if (script.getScriptFile() == null) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save file");
			fileChooser.setInitialFileName("newFile.js");

			String pathFolder = prefs.get("last-saved-folder", null);
			if (pathFolder != null)
				fileChooser.setInitialDirectory(new File(pathFolder));

			File file = fileChooser.showSaveDialog(ClickerMain.stage);

			if (file != null) {
				try {
					FileUtils.writeStringToFile(file, script.getScript().get(),
							Charset.defaultCharset());
					script.setScriptFile(file);
					prefs.put("last-saved-folder", script.getScriptFile()
							.getParentFile().getAbsolutePath());

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} else {
			try {
				FileUtils.writeStringToFile(script.getScriptFile(),
						script.getScript().get(), Charset.defaultCharset());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ClickerMain.updateTitle();

	}
	/**
	 * Save script in new file
	 */
	@FXML
	public void saveFileAs() {
		System.out.println("Save as");
		Preferences prefs = Preferences.userNodeForPackage(ClickerMain.class);

		Script script = ClickerMain.getScript();
		if (script == null)
			return;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save file");
		fileChooser.setInitialFileName("newFile.js");

		String pathFolder = prefs.get("last-saved-folder", null);
		if (pathFolder != null)
			fileChooser.setInitialDirectory(new File(pathFolder));

		File file = fileChooser.showSaveDialog(ClickerMain.stage);

		if (file != null) {
			script.setScriptFile(file);
			try {
				FileUtils.writeStringToFile(script.getScriptFile(),
						script.getScript().get(), Charset.defaultCharset());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		ClickerMain.updateTitle();
		prefs.put("last-saved-folder",
				script.getScriptFile().getParentFile().getAbsolutePath());
	}



	//////
	@FXML
	private ToggleButton btnInsertKeyName;

	@FXML
	private ToggleButton btnInsertKeyCode;

	@FXML
	private ToggleButton btnInsertKeyCodeWithDelay;

	@FXML
	private ToggleButton btnInsertMouseName;

	@FXML
	private ToggleButton btnInsertMouseCode;

	@FXML
	private ToggleButton btnInsertMouseCodeWithDelay;

	private List<ToggleButton> listOfToggles = new ArrayList<>();
	/**
	 * Deselect all toggle except the parameter
	 * @param selectedToggle
	 */
	private void select(ToggleButton selectedToggle) {
		for (ToggleButton t : listOfToggles) {
			if (t.isSelected()) {
				if (t != selectedToggle)
					t.fire();
			}
		}
	}
	@FXML
	void insertKeyName(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		KeyEventsManager manager = KeyEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType=false;
			manager.addPressListener(new ShortcutIncludesHandler(
					"insert.keyboard.name", "", () -> {
						int caretPosition = codeTextArea.getCaretPosition();
						codeTextArea.insertText(caretPosition,
								manager.getLastPressed() + " ");

					}));
		} else {
			// if toggle has been deselected
			manager.removePressListenersByPrefix("insert.keyboard.name");
			enableCodeType=true;
		}
	}

	@FXML
	void insertKeyCode(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		KeyEventsManager manager = KeyEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType=false;
			manager.addPressListener(new ShortcutIncludesHandler(
					"insert.keyboard.code.press", "", () -> {
						int caretPosition = codeTextArea.getCaretPosition();
						codeTextArea.insertText(caretPosition, "key.press('"
								+ manager.getLastPressed() + "');\n");

					}));

			manager.addReleaseListener(new ShortcutIncludesHandler(
					"insert.keyboard.code.release", "", () -> {
						int caretPosition = codeTextArea.getCaretPosition();
						codeTextArea.insertText(caretPosition, "key.release('"
								+ manager.getLastReleased() + "');\n");

					}));
		} else {
			// if toggle has been deselected
			manager.removePressListenersByPrefix("insert.keyboard.code");
			manager.removeReleaseListenersByPrefix("insert.keyboard.code");
			enableCodeType=true;
		}
	}

	@FXML
	void insertKeyCodeWithDelay(ActionEvent event) {
		OutStream.println("This button doesn't work yet");
		ToggleButton toggle = (ToggleButton) event.getSource();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType=false;
		} else {
			// if toggle has been deselected
			enableCodeType=true;
		}
	}

	@FXML
	void insertMouseCode(ActionEvent event) {
		OutStream.println("This button doesn't work yet");
		ToggleButton toggle = (ToggleButton) event.getSource();

		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType=false;
			manager.addPressListener(new MouseHandler("insert.mouse.press", "", ()-> {
				if (!KeyEventsManager.getInstance().isPressed("CONTROL"))
					return;
				int caretPosition = codeTextArea.getCaretPosition();
				codeTextArea.insertText(caretPosition, 
						"mouse.pressAt('"+
						manager.getLastPressed() + "',"+
						manager.getX() + "," +
						manager.getY() + ");\n");

			}));
			manager.addReleaseListener(new MouseHandler("insert.mouse.release", "", ()-> {
				if (!KeyEventsManager.getInstance().isPressed("CONTROL"))
					return;
				int caretPosition = codeTextArea.getCaretPosition();
				codeTextArea.insertText(caretPosition, 
						"mouse.releaseAt('"+
						manager.getLastReleased() + "',"+
						manager.getX() + "," +
						manager.getY() + ");\n");

			}));
		} else {
			// if toggle has been deselected
			manager.removePressListenersByPrefix("insert.mouse");
			manager.removeReleaseListenersByPrefix("insert.mouse");
			enableCodeType=true;
		}
	}

	@FXML
	void insertMouseCodeWithDelay(ActionEvent event) {
		OutStream.println("This button doesn't work yet");
		ToggleButton toggle = (ToggleButton) event.getSource();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType=false;
		} else {
			// if toggle has been deselected
			enableCodeType=true;
		}
	}

	@FXML
	void insertMouseName(ActionEvent event) {
		OutStream.println("This button doesn't work yet");
		ToggleButton toggle = (ToggleButton) event.getSource();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType=false;
		} else {
			// if toggle has been deselected
			enableCodeType=true;
		}
	}
	
}
