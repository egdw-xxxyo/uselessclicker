package org.dikhim.jclicker;

import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.prefs.Preferences;

import org.apache.commons.io.FileUtils;
import org.dikhim.jclicker.events.KeyEventsManager;
import org.dikhim.jclicker.events.MouseButtonEvent;
import org.dikhim.jclicker.events.MouseEventsManager;
import org.dikhim.jclicker.events.MouseHandler;
import org.dikhim.jclicker.events.MouseMoveEvent;
import org.dikhim.jclicker.events.ShortcutEqualsHandler;
import org.dikhim.jclicker.events.ShortcutIncludesHandler;
import org.dikhim.jclicker.model.Script;
import org.dikhim.jclicker.util.MouseMoveUtil;
import org.dikhim.jclicker.util.OutStream;
import org.dikhim.jclicker.util.SourcePropertyFile;

import com.sun.javafx.scene.KeyboardShortcutsHandler;

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
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class MainController {
	@FXML
	private void initialize() {
		// init textareas
		areaCode.textProperty().bindBidirectional(codeTextProperty);
		areaCodeSample.textProperty().bindBidirectional(codeSampleProperty);
		areaOut.textProperty().bindBidirectional(outTextProperty);

		areaCode.addEventFilter(KeyEvent.KEY_PRESSED,
				new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent event) {
						if (!enableCodeType)
							event.consume();
					}
				});
		areaCode.addEventFilter(KeyEvent.KEY_TYPED,
				new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent event) {
						if (!enableCodeType)
							event.consume();
					}
				});
		// init toggles
		initToggles();
		SourcePropertyFile pf = new SourcePropertyFile(new File(
				getClass().getResource("/strings/codesamples.js").getFile()));
		setCodeSamples(pf);
	}
	/**
	 * Sets code samples as user data to each element
	 * 
	 * @param prop
	 *            source property file
	 */
	public void setCodeSamples(SourcePropertyFile prop) {
		btnInsertKeyName.setUserData(prop.get("insertKeyName"));
		btnInsertKeyCode.setUserData(prop.get("insertKeyCode"));
		btnInsertKeyCodeWithDelay
				.setUserData(prop.get("insertKeyCodeWithDelay"));

		btnInsertMouseName.setUserData(prop.get("insertMouseName"));
		btnInsertMouseCode.setUserData(prop.get("insertMouseCode"));
		btnInsertMouseCodeWithDelay
				.setUserData(prop.get("insertMouseCodeWithDelay"));
		btnInsertMouseRelativeCode
				.setUserData(prop.get("insertMouseRelativeCode"));

		btnInsertMouseCodeClick.setUserData(prop.get("insertMouseCodeClick"));

		btnInsertAbsolutePath.setUserData(prop.get("insertAbsolutePath"));
		btnInsertRelativePath.setUserData(prop.get("insertRelativePath"));
		btnInsertAbsolutePathWithDelays
				.setUserData(prop.get("insertAbsolutePathWithDelays"));
		btnInsertRelativePathWithDelays
				.setUserData(prop.get("insertRelativePathWithDelays"));
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
	private TextArea areaCode;
	private StringProperty codeTextProperty = new SimpleStringProperty("");
	private boolean enableCodeType = true;

	@FXML
	private TextArea areaCodeSample;
	private StringProperty codeSampleProperty = new SimpleStringProperty("");

	@FXML
	private TextArea areaOut;
	private StringProperty outTextProperty = OutStream.getProperty();

	@FXML
	public void stopScript() {
		ClickerMain.stopScript();
	}
	@FXML
	public void runScript() {
		OutStream.clear();
		select(null);
		ClickerMain.runScript();
	}
	@FXML
	public void newFile() {
		stopScript();
		codeTextProperty.set("");
		Script script = new Script();
		ClickerMain.setScript(script);
		script.setScript(codeTextProperty);
		areaCode.setEditable(true);
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

			areaCode.textProperty().bindBidirectional(script.getScript());
			areaCode.setEditable(true);
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
				e.printStackTrace();
			}

		}
		ClickerMain.updateTitle();
		prefs.put("last-saved-folder",
				script.getScriptFile().getParentFile().getAbsolutePath());
	}

	//////
	// Keyboard
	@FXML
	private ToggleButton btnInsertKeyName;

	@FXML
	private ToggleButton btnInsertKeyCode;

	@FXML
	private ToggleButton btnInsertKeyCodeWithDelay;

	// Mouse buttons
	@FXML
	private ToggleButton btnInsertMouseName;

	@FXML
	private ToggleButton btnInsertMouseCode;

	@FXML
	private ToggleButton btnInsertMouseCodeWithDelay;

	@FXML
	private ToggleButton btnInsertMouseRelativeCode;
	// click
	@FXML
	private ToggleButton btnInsertMouseCodeClick;

	// Movement
	@FXML
	private ToggleButton btnInsertAbsolutePath;

	@FXML
	private ToggleButton btnInsertRelativePath;

	@FXML
	private ToggleButton btnInsertAbsolutePathWithDelays;

	@FXML
	private ToggleButton btnInsertRelativePathWithDelays;

	private List<ToggleButton> listOfToggles = new ArrayList<>();
	/**
	 * Adds all toggles to listOfToggles
	 */
	private void initToggles() {
		// Keyboard
		listOfToggles.add(btnInsertKeyName);
		listOfToggles.add(btnInsertKeyCode);
		listOfToggles.add(btnInsertKeyCodeWithDelay);

		// mouse
		listOfToggles.add(btnInsertMouseName);
		listOfToggles.add(btnInsertMouseCode);
		listOfToggles.add(btnInsertMouseCodeWithDelay);
		listOfToggles.add(btnInsertMouseRelativeCode);
		// click
		listOfToggles.add(btnInsertMouseCodeClick);
		// Movement
		listOfToggles.add(btnInsertAbsolutePath);
		listOfToggles.add(btnInsertRelativePath);
		listOfToggles.add(btnInsertAbsolutePathWithDelays);
		listOfToggles.add(btnInsertRelativePathWithDelays);

	}
	/**
	 * Deselect all toggle except the parameter
	 * 
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
	// Keyboard
	@FXML
	void insertKeyName(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		KeyEventsManager manager = KeyEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType = false;
			manager.addPressListener(new ShortcutIncludesHandler(
					"insert.keyboard.name", "", () -> {
						int caretPosition = areaCode.getCaretPosition();
						areaCode.insertText(caretPosition,
								manager.getLastPressed() + " ");

					}));
		} else {
			// if toggle has been deselected
			manager.removePressListenersByPrefix("insert.keyboard.name");
			enableCodeType = true;
		}
	}

	@FXML
	void insertKeyCode(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		KeyEventsManager manager = KeyEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType = false;
			manager.addPressListener(new ShortcutIncludesHandler(
					"insert.keyboard.code.press", "", () -> {
						int caretPosition = areaCode.getCaretPosition();
						areaCode.insertText(caretPosition, "key.press('"
								+ manager.getLastPressed() + "');\n");

					}));

			manager.addReleaseListener(new ShortcutIncludesHandler(
					"insert.keyboard.code.release", "", () -> {
						int caretPosition = areaCode.getCaretPosition();
						areaCode.insertText(caretPosition, "key.release('"
								+ manager.getLastReleased() + "');\n");

					}));
		} else {
			// if toggle has been deselected
			manager.removePressListenersByPrefix("insert.keyboard.code");
			manager.removeReleaseListenersByPrefix("insert.keyboard.code");
			enableCodeType = true;
		}
	}

	@FXML
	void insertKeyCodeWithDelay(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		KeyEventsManager manager = KeyEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType = false;
			manager.resetTimeLog();
			manager.addPressListener(new ShortcutIncludesHandler(
					"insert.keyboard.code.press", "", () -> {
						int caretPosition = areaCode.getCaretPosition();
						StringBuilder sb = new StringBuilder();
						int delay = manager.getLastDelay();
						if (delay != 0) {
							sb.append("system.sleep(").append(delay)
									.append(");\n");
						}
						sb.append("key.press('")
								.append(manager.getLastPressed())
								.append("');\n");
						areaCode.insertText(caretPosition, sb.toString());

					}));
			manager.addReleaseListener(new ShortcutIncludesHandler(
					"insert.keyboard.code.release", "", () -> {
						int caretPosition = areaCode.getCaretPosition();
						StringBuilder sb = new StringBuilder();
						int delay = manager.getLastDelay();
						if (delay != 0) {
							sb.append("system.sleep(").append(delay)
									.append(");\n");
						}
						sb.append("key.release('")
								.append(manager.getLastReleased())
								.append("');\n");
						areaCode.insertText(caretPosition, sb.toString());

					}));
		} else {
			// if toggle has been deselected
			manager.removePressListenersByPrefix("insert.keyboard.code");
			manager.removeReleaseListenersByPrefix("insert.keyboard.code");
			enableCodeType = true;
		}
	}

	// Mouse buttons
	@FXML
	void insertMouseCode(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType = false;
			manager.addPressListener(
					new MouseHandler("insert.mouse.press", "", () -> {
						if (!KeyEventsManager.getInstance()
								.isPressed("CONTROL"))
							return;
						int caretPosition = areaCode.getCaretPosition();
						areaCode.insertText(caretPosition,
								"mouse.pressAt('" + manager.getLastPressed()
										+ "'," + manager.getX() + ","
										+ manager.getY() + ");\n");

					}));
			manager.addReleaseListener(
					new MouseHandler("insert.mouse.release", "", () -> {
						if (!KeyEventsManager.getInstance()
								.isPressed("CONTROL"))
							return;
						int caretPosition = areaCode.getCaretPosition();
						areaCode.insertText(caretPosition,
								"mouse.releaseAt('" + manager.getLastReleased()
										+ "'," + manager.getX() + ","
										+ manager.getY() + ");\n");

					}));
		} else {
			// if toggle has been deselected
			manager.removePressListenersByPrefix("insert.mouse");
			manager.removeReleaseListenersByPrefix("insert.mouse");
			enableCodeType = true;
		}
	}

	@FXML
	void insertMouseCodeWithDelay(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType = false;
			manager.addPressListener(
					new MouseHandler("insert.mouse.press", "", () -> {
						if (!KeyEventsManager.getInstance()
								.isPressed("CONTROL"))
							return;
						int caretPosition = areaCode.getCaretPosition();
						StringBuilder sb = new StringBuilder();
						int delay = manager.getLastKeyDelay();
						if (delay != 0) {
							sb.append("system.sleep(").append(delay)
									.append(");\n");
						}
						sb.append("mouse.pressAt('")
								.append(manager.getLastPressed()).append("',")
								.append(manager.getX()).append(",")
								.append(manager.getY()).append(");\n");
						areaCode.insertText(caretPosition, sb.toString());

					}));
			manager.addReleaseListener(
					new MouseHandler("insert.mouse.release", "", () -> {
						if (!KeyEventsManager.getInstance()
								.isPressed("CONTROL"))
							return;
						int caretPosition = areaCode.getCaretPosition();
						StringBuilder sb = new StringBuilder();
						int delay = manager.getLastKeyDelay();
						if (delay != 0) {
							sb.append("system.sleep(").append(delay)
									.append(");\n");
						}
						sb.append("mouse.releaseAt('")
								.append(manager.getLastReleased()).append("',")
								.append(manager.getX()).append(",")
								.append(manager.getY()).append(");\n");
						areaCode.insertText(caretPosition, sb.toString());
					}));
		} else {
			// if toggle has been deselected
			manager.removePressListenersByPrefix("insert.mouse");
			manager.removeReleaseListenersByPrefix("insert.mouse");
			enableCodeType = true;
		}
	}

	@FXML
	void insertMouseName(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType = false;
			manager.addPressListener(
					new MouseHandler("insert.mouse.press", "", () -> {
						if (!KeyEventsManager.getInstance()
								.isPressed("CONTROL"))
							return;
						int caretPosition = areaCode.getCaretPosition();
						StringBuilder sb = new StringBuilder();
						sb.append(manager.getLastPressed()).append(" ");
						areaCode.insertText(caretPosition, sb.toString());
					}));
		} else {
			// if toggle has been deselected
			manager.removePressListenersByPrefix("insert.mouse");
			enableCodeType = true;
		}
	}

	MouseMoveEvent lastMoveEvent;
	@FXML
	void insertMouseRelativeCode(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType = false;
			// Start record by set the first movement point
			KeyEventsManager.getInstance().addPressListener(
					new ShortcutEqualsHandler("mouse.move.key.press", "CONTROL",
							() -> {
								lastMoveEvent = manager.getLastMoveEvent();
							}));

			// insert code on press button
			manager.addPressListener(
					new MouseHandler("insert.mouse.press", "", () -> {
						if (!KeyEventsManager.getInstance()
								.isPressed("CONTROL"))
							return;
						int caretPosition = areaCode.getCaretPosition();
						StringBuilder sb = new StringBuilder();

						MouseButtonEvent lastButtonEvent = manager
								.getLastButtonEvent();
						if (lastButtonEvent == null)
							return;
						int dx = (int) (lastButtonEvent.getX()
								- lastMoveEvent.getX());
						int dy = (int) (lastButtonEvent.getY()
								- lastMoveEvent.getY());
						lastMoveEvent = manager.getLastMoveEvent();
						sb.append("mouse.moveAndPress('")
								.append(lastButtonEvent.getButton())
								.append("',").append(dx).append(",").append(dy)
								.append(");\n");
						areaCode.insertText(caretPosition, sb.toString());

					}));
			// insert code on release button
			manager.addReleaseListener(
					new MouseHandler("insert.mouse.release", "", () -> {
						if (!KeyEventsManager.getInstance()
								.isPressed("CONTROL"))
							return;
						int caretPosition = areaCode.getCaretPosition();
						StringBuilder sb = new StringBuilder();
						MouseButtonEvent lastButtonEvent = manager
								.getLastButtonEvent();
						if (lastButtonEvent == null)
							return;
						int dx = (int) (lastButtonEvent.getX()
								- lastMoveEvent.getX());
						int dy = (int) (lastButtonEvent.getY()
								- lastMoveEvent.getY());
						lastMoveEvent = manager.getLastMoveEvent();
						sb.append("mouse.moveAndRelease('")
								.append(lastButtonEvent.getButton())
								.append("',").append(dx).append(",").append(dy)
								.append(");\n");
						areaCode.insertText(caretPosition, sb.toString());

					}));
			// Past code on release controll key
			KeyEventsManager.getInstance().addReleaseListener(
					new ShortcutEqualsHandler("mouse.move.key.press", "CONTROL",
							() -> {
								int caretPosition = areaCode.getCaretPosition();
								StringBuilder sb = new StringBuilder();
								MouseMoveEvent moveEvent = manager
										.getLastMoveEvent();
								if (moveEvent == null)
									return;
								int dx = (int) (moveEvent.getX()
										- lastMoveEvent.getX());
								int dy = (int) (moveEvent.getY()
										- lastMoveEvent.getY());

								sb.append("mouse.move('").append(dx).append(",")
										.append(dy).append(");\n");
								areaCode.insertText(caretPosition,
										sb.toString());
							}));
		} else {
			// if toggle has been deselected
			KeyEventsManager.getInstance()
					.removePressListenersByPrefix("mouse.move");
			KeyEventsManager.getInstance()
					.removeReleaseListenersByPrefix("mouse.move");
			manager.removePressListenersByPrefix("insert.mouse");
			manager.removeReleaseListenersByPrefix("insert.mouse");
			enableCodeType = true;
		}
	}
	// Clicks
	@FXML
	void insertMouseCodeClick(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType = false;

			// insert code on press button
			manager.addPressListener(
					new MouseHandler("insert.mouse.press", "", () -> {
						if (!KeyEventsManager.getInstance()
								.isPressed("CONTROL"))
							return;
						int caretPosition = areaCode.getCaretPosition();
						StringBuilder sb = new StringBuilder();

						MouseButtonEvent lastButtonEvent = manager
								.getLastButtonEvent();
						MouseButtonEvent preLastButtonEvent = manager
								.getPreLastButtonEvent();
						if (preLastButtonEvent == null)
							return;
						if (!lastButtonEvent.getAction()
								.equals(preLastButtonEvent.getAction())) {
							return;
						}
						sb.append("mouse.pressAt('")
								.append(preLastButtonEvent.getButton())
								.append("',").append(preLastButtonEvent.getX())
								.append(",").append(preLastButtonEvent.getY())
								.append(");\n");
						areaCode.insertText(caretPosition, sb.toString());

					}));
			// insert code on release button
			manager.addReleaseListener(
					new MouseHandler("insert.mouse.release", "", () -> {
						if (!KeyEventsManager.getInstance()
								.isPressed("CONTROL"))
							return;
						int caretPosition = areaCode.getCaretPosition();
						StringBuilder sb = new StringBuilder();

						MouseButtonEvent lastButtonEvent = manager
								.getLastButtonEvent();
						MouseButtonEvent preLastButtonEvent = manager
								.getPreLastButtonEvent();
						if (preLastButtonEvent == null)
							return;
						if (lastButtonEvent.getButton()
								.equals(preLastButtonEvent.getButton())) {
							if (lastButtonEvent.getX() == preLastButtonEvent
									.getX()
									&& lastButtonEvent
											.getY() == preLastButtonEvent
													.getY()) {
								sb.append("mouse.clickAt('")
										.append(lastButtonEvent.getButton())
										.append("',")
										.append(lastButtonEvent.getX())
										.append(",")
										.append(lastButtonEvent.getY())
										.append(");\n");
							} else {
								sb.append("mouse.pressAt('")
										.append(preLastButtonEvent.getButton())
										.append("',")
										.append(preLastButtonEvent.getX())
										.append(",")
										.append(preLastButtonEvent.getY())
										.append(");\n");
								sb.append("mouse.releaseAt('")
										.append(lastButtonEvent.getButton())
										.append("',")
										.append(lastButtonEvent.getX())
										.append(",")
										.append(lastButtonEvent.getY())
										.append(");\n");
							}

						} else {

							if (preLastButtonEvent.getAction()
									.equals("PRESS")) {
								sb.append("mouse.pressAt('")
										.append(preLastButtonEvent.getButton())
										.append("',")
										.append(preLastButtonEvent.getX())
										.append(",")
										.append(preLastButtonEvent.getY())
										.append(");\n");
							}
							sb.append("mouse.releaseAt('")
									.append(lastButtonEvent.getButton())
									.append("',").append(lastButtonEvent.getX())
									.append(",").append(lastButtonEvent.getY())
									.append(");\n");
						}
						areaCode.insertText(caretPosition, sb.toString());

					}));
		} else {
			// if toggle has been deselected
			manager.removePressListenersByPrefix("insert.mouse");
			manager.removeReleaseListenersByPrefix("insert.mouse");
			enableCodeType = true;
		}
	}

	// Movement
	private MouseMoveUtil movementPath;
	@FXML
	void insertAbsolutePath(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType = false;
			// on press key start record path
			KeyEventsManager.getInstance().addPressListener(
					new ShortcutEqualsHandler("mouse.move.key.press", "CONTROL",
							() -> {
								movementPath = new MouseMoveUtil();
							}));
			// on release key stop record, insert code and clear path;
			KeyEventsManager.getInstance().addReleaseListener(
					new ShortcutEqualsHandler("mouse.move.key.release",
							"CONTROL", () -> {
								int caretPosition = areaCode.getCaretPosition();
								areaCode.insertText(caretPosition, movementPath
										.getMoveCodeAbsolutePath(80));
								movementPath = null;
							}));

			manager.addMoveListener(new MouseHandler("mouse.move", "", () -> {
				if (movementPath != null) {
					movementPath.add(manager.getX(), manager.getY());
				}
			}));
		} else {
			// if toggle has been deselected
			KeyEventsManager.getInstance()
					.removePressListenersByPrefix("mouse.move");
			KeyEventsManager.getInstance()
					.removeReleaseListenersByPrefix("mouse.move");
			manager.removeMoveListenersByPrefix("mouse.move");
			enableCodeType = true;
		}
	}
	@FXML
	void insertRelativePath(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType = false;
			// on press key start record path
			KeyEventsManager.getInstance().addPressListener(
					new ShortcutEqualsHandler("mouse.move.key.press", "CONTROL",
							() -> {
								movementPath = new MouseMoveUtil();
							}));
			// on release key stop record, insert code and clear path;
			KeyEventsManager.getInstance().addReleaseListener(
					new ShortcutEqualsHandler("mouse.move.key.release",
							"CONTROL", () -> {
								int caretPosition = areaCode.getCaretPosition();
								areaCode.insertText(caretPosition, movementPath
										.getMoveCodeRelativePath(80));
								movementPath = null;
							}));

			manager.addMoveListener(new MouseHandler("mouse.move", "", () -> {
				if (movementPath != null) {
					movementPath.add(manager.getX() - manager.getXFromEnd(1),
							manager.getY() - manager.getYFromEnd(1));
				}
			}));
		} else {
			// if toggle has been deselected
			KeyEventsManager.getInstance()
					.removePressListenersByPrefix("mouse.move");
			KeyEventsManager.getInstance()
					.removeReleaseListenersByPrefix("mouse.move");
			manager.removeMoveListenersByPrefix("mouse.move");
			enableCodeType = true;
		}
	}

	@FXML
	void insertAbsolutePathWithDelays(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType = false;
			// on press key start record path
			KeyEventsManager.getInstance().addPressListener(
					new ShortcutEqualsHandler("mouse.move.key.press", "CONTROL",
							() -> {
								movementPath = new MouseMoveUtil();
								manager.resetMoveTimeLog();
							}));
			// on release key stop record, insert code and clear path;
			KeyEventsManager.getInstance().addReleaseListener(
					new ShortcutEqualsHandler("mouse.move.key.release",
							"CONTROL", () -> {
								int caretPosition = areaCode.getCaretPosition();
								areaCode.insertText(caretPosition, movementPath
										.getMoveCodeAbsolutePathWithDelays(80));
								movementPath = null;
							}));

			manager.addMoveListener(new MouseHandler("mouse.move", "", () -> {
				if (movementPath != null) {
					movementPath.add(manager.getX(), manager.getY(),
							manager.getLastMoveDelay());
				}
			}));
		} else {
			// if toggle has been deselected
			KeyEventsManager.getInstance()
					.removePressListenersByPrefix("mouse.move");
			KeyEventsManager.getInstance()
					.removeReleaseListenersByPrefix("mouse.move");
			manager.removeMoveListenersByPrefix("mouse.move");
			enableCodeType = true;
		}
	}

	@FXML
	void insertRelativePathWithDelays(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			select(toggle);
			enableCodeType = false;
			// on press key start record path
			KeyEventsManager.getInstance().addPressListener(
					new ShortcutEqualsHandler("mouse.move.key.press", "CONTROL",
							() -> {
								movementPath = new MouseMoveUtil();
								manager.resetMoveTimeLog();
							}));
			// on release key stop record, insert code and clear path;
			KeyEventsManager.getInstance().addReleaseListener(
					new ShortcutEqualsHandler("mouse.move.key.release",
							"CONTROL", () -> {
								int caretPosition = areaCode.getCaretPosition();
								areaCode.insertText(caretPosition, movementPath
										.getMoveCodeRelativePathWithDelays(80));
								movementPath = null;
							}));

			manager.addMoveListener(new MouseHandler("mouse.move", "", () -> {
				if (movementPath != null) {
					movementPath.add(manager.getX() - manager.getXFromEnd(1),
							manager.getY() - manager.getYFromEnd(1),
							manager.getLastMoveDelay());
				}
			}));
		} else {
			// if toggle has been deselected
			KeyEventsManager.getInstance()
					.removePressListenersByPrefix("mouse.move");
			KeyEventsManager.getInstance()
					.removeReleaseListenersByPrefix("mouse.move");
			manager.removeMoveListenersByPrefix("mouse.move");
			enableCodeType = true;
		}
	}
	
	//TODO
    @FXML
    void insertKeyType(ActionEvent event) {

    }

    //mouse basics
    @FXML
    void insertMousePress(ActionEvent event) {

    }
    
    @FXML
    void insertMouseRelease(ActionEvent event) {

    }
    
    @FXML
    void insertMouseClickAt(ActionEvent event) {

    }
    
    @FXML
    void insertMouseMoveAt(ActionEvent event) {

    }
    
    @FXML
    void insertMouseMove(ActionEvent event) {

    }
    
    
    // Show code samples
    @FXML
	public void showCodeSample(MouseEvent event) {
		ToggleButton btn = (ToggleButton) event.getSource();
		areaCodeSample.setText((String) btn.getUserData());
		areaCodeSample.setVisible(true);
	}
    @FXML
	public void hideCodeSample(MouseEvent event) {
		areaCodeSample.setVisible(false);
	}
	//
	//
	//
	//// templates
    @FXML
	public void insertTemplate(ActionEvent event) {
		Button b = (Button) event.getSource();
		System.out.println(b.getText());
	}
    @FXML
	public void showCodeTemplate(MouseEvent event) {
		Button btn = (Button) event.getSource();
		areaCodeSample.setText((String) btn.getUserData());
		areaCodeSample.setVisible(true);
	}
    @FXML
	public void hideCodeTemplate(MouseEvent event) {
		areaCodeSample.setVisible(false);
	}

}
