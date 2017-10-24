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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class MainController {
	private MouseMoveUtil movementPath;
	private MouseMoveEvent lastMoveEvent;

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
		SourcePropertyFile propertyFile = new SourcePropertyFile(new File(
				getClass().getResource("/strings/codesamples.js").getFile()));
		setCodeSamples(propertyFile);
		initTemplateButtons(propertyFile);

		Script script = ClickerMain.getScript();
		codeTextProperty.set("");
		script.setScriptProperty(codeTextProperty);

		setScriptStatus(Status.SUSPENDED);
		setToggleStatus(null);
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
		// mouse basics
		btnInsertMouseName.setUserData(prop.get("insertMouseName"));

		// mouse code
		btnInsertMouseCode.setUserData(prop.get("insertMouseCode"));
		btnInsertMouseCodeWithDelay
				.setUserData(prop.get("insertMouseCodeWithDelay"));
		btnInsertMouseRelativeCode
				.setUserData(prop.get("insertMouseRelativeCode"));

		btnInsertMouseCodeClick.setUserData(prop.get("insertMouseCodeClick"));

		// mouse movement
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
		setScriptStatus(Status.SUSPENDED);
	}
	@FXML
	public void runScript() {
		OutStream.clear();
		select(null);
		setScriptStatus(Status.RUNNING);
		ClickerMain.runScript();
	}
	@FXML
	public void newFile() {
		stopScript();
		ClickerMain.newScript();
		updateScriptStatus();
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

			areaCode.textProperty()
					.bindBidirectional(script.getStringProperty());
		}
		updateScriptStatus();
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
					FileUtils.writeStringToFile(file,
							script.getStringProperty().get(),
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
						script.getStringProperty().get(),
						Charset.defaultCharset());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ClickerMain.updateTitle();
		updateScriptStatus();
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
						script.getStringProperty().get(),
						Charset.defaultCharset());
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

	// Mouse basics
	@FXML
	private ToggleButton btnInsertMouseName;

	@FXML
	private ToggleButton btnInsertMouseClick;

	@FXML
	private ToggleButton btnInsertMouseClickAt;

	@FXML
	private ToggleButton btnInsertMousePress;

	@FXML
	private ToggleButton btnInsertMousePressAt;

	@FXML
	private ToggleButton btnInsertMouseRelease;

	@FXML
	private ToggleButton btnInsertMouseReleaseAt;

	@FXML
	private ToggleButton btnInsertMouseMove;

	@FXML
	private ToggleButton btnInsertMouseMoveAt;

	// mouse code
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

		// mouse basics
		listOfToggles.add(btnInsertMouseName);
		listOfToggles.add(btnInsertMousePress);
		listOfToggles.add(btnInsertMousePressAt);
		listOfToggles.add(btnInsertMouseRelease);
		listOfToggles.add(btnInsertMouseReleaseAt);
		listOfToggles.add(btnInsertMouseClick);
		listOfToggles.add(btnInsertMouseClickAt);
		listOfToggles.add(btnInsertMouseMove);
		listOfToggles.add(btnInsertMouseMoveAt);

		// mouse press/release
		listOfToggles.add(btnInsertMouseCode);
		listOfToggles.add(btnInsertMouseCodeWithDelay);
		listOfToggles.add(btnInsertMouseRelativeCode);
		// mouse click
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
		setToggleStatus(selectedToggle);
	}
	// Keyboard
	@FXML
	void insertKeyName(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		select(toggle);
		KeyEventsManager manager = KeyEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
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
		select(toggle);
		KeyEventsManager manager = KeyEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
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
		select(toggle);
		KeyEventsManager manager = KeyEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
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
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
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
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
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
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
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

	@FXML
	void insertMouseRelativeCode(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
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
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
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

	@FXML
	void insertAbsolutePath(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
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
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
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
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
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
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
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

	// mouse basics
	@FXML
	void insertMousePress(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			enableCodeType = false;

			manager.addPressListener(new MouseHandler("mouse.press", "", () -> {
				if (!KeyEventsManager.getInstance().isPressed("CONTROL"))
					return;
				int caretPosition = areaCode.getCaretPosition();
				StringBuilder sb = new StringBuilder();
				sb.append("mouse.press('")
						.append(manager.getLastButtonEvent().getButton())
						.append("');\n");
				areaCode.insertText(caretPosition, sb.toString());
			}));
		} else {
			// if toggle has been deselected
			manager.removePressListenersByPrefix("mouse.press");
			enableCodeType = true;
		}
	}

	@FXML
	void insertMousePressAt(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			enableCodeType = false;

			manager.addPressListener(new MouseHandler("mouse.press", "", () -> {
				if (!KeyEventsManager.getInstance().isPressed("CONTROL"))
					return;
				int caretPosition = areaCode.getCaretPosition();
				MouseButtonEvent e = manager.getLastButtonEvent();
				StringBuilder sb = new StringBuilder();
				sb.append("mouse.pressAt('").append(e.getButton()).append("',")
						.append(e.getX()).append(",").append(e.getY())
						.append(");\n");
				areaCode.insertText(caretPosition, sb.toString());
			}));
		} else {
			// if toggle has been deselected
			manager.removePressListenersByPrefix("mouse.press");
			enableCodeType = true;
		}
	}

	@FXML
	void insertMouseRelease(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			enableCodeType = false;

			manager.addReleaseListener(
					new MouseHandler("mouse.release", "", () -> {
						if (!KeyEventsManager.getInstance()
								.isPressed("CONTROL"))
							return;
						int caretPosition = areaCode.getCaretPosition();
						StringBuilder sb = new StringBuilder();
						sb.append("mouse.release('").append(
								manager.getLastButtonEvent().getButton())
								.append("');\n");
						areaCode.insertText(caretPosition, sb.toString());
					}));
		} else {
			// if toggle has been deselected
			manager.removeReleaseListenersByPrefix("mouse.release");
			enableCodeType = true;
		}
	}

	@FXML
	void insertMouseReleaseAt(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			enableCodeType = false;

			manager.addReleaseListener(
					new MouseHandler("mouse.release", "", () -> {
						if (!KeyEventsManager.getInstance()
								.isPressed("CONTROL"))
							return;
						int caretPosition = areaCode.getCaretPosition();
						MouseButtonEvent e = manager.getLastButtonEvent();
						StringBuilder sb = new StringBuilder();
						sb.append("mouse.releaseAt('").append(e.getButton())
								.append("',").append(e.getX()).append(",")
								.append(e.getY()).append(");\n");
						areaCode.insertText(caretPosition, sb.toString());
					}));
		} else {
			// if toggle has been deselected
			manager.removeReleaseListenersByPrefix("mouse.release");
			enableCodeType = true;
		}
	}

	@FXML
	void insertMouseClick(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			enableCodeType = false;

			manager.addReleaseListener(
					new MouseHandler("mouse.click", "", () -> {
						if (!KeyEventsManager.getInstance()
								.isPressed("CONTROL"))
							return;
						MouseButtonEvent lastEvent = manager
								.getLastButtonEvent();
						MouseButtonEvent preLastEvent = manager
								.getPreLastButtonEvent();
						if (preLastEvent == null)
							return;
						if ((lastEvent.getX() != preLastEvent.getX())
								|| (lastEvent.getY() != preLastEvent.getY())) {
							return;
						}
						int caretPosition = areaCode.getCaretPosition();
						StringBuilder sb = new StringBuilder();
						sb.append("mouse.click('").append(lastEvent.getButton())
								.append("');\n");
						areaCode.insertText(caretPosition, sb.toString());
					}));
		} else {
			// if toggle has been deselected
			manager.removeReleaseListenersByPrefix("mouse.click");
			enableCodeType = true;
		}
	}
	@FXML
	void insertMouseClickAt(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			enableCodeType = false;

			manager.addReleaseListener(
					new MouseHandler("mouse.click", "", () -> {
						if (!KeyEventsManager.getInstance()
								.isPressed("CONTROL"))
							return;
						MouseButtonEvent lastEvent = manager
								.getLastButtonEvent();
						MouseButtonEvent preLastEvent = manager
								.getPreLastButtonEvent();
						if (preLastEvent == null)
							return;
						if ((lastEvent.getX() != preLastEvent.getX())
								|| (lastEvent.getY() != preLastEvent.getY())) {
							return;
						}
						int caretPosition = areaCode.getCaretPosition();
						StringBuilder sb = new StringBuilder();
						sb.append("mouse.clickAt('")
								.append(lastEvent.getButton()).append("',")
								.append(lastEvent.getX()).append(",")
								.append(lastEvent.getY()).append(");\n");
						areaCode.insertText(caretPosition, sb.toString());
					}));
		} else {
			// if toggle has been deselected
			manager.removeReleaseListenersByPrefix("mouse.click");
			enableCodeType = true;
		}
	}

	@FXML
	void insertMouseMoveAt(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			enableCodeType = false;
			manager.addPressListener(new MouseHandler("mouse.press", "", () -> {
				if (!KeyEventsManager.getInstance().isPressed("CONTROL"))
					return;
				int caretPosition = areaCode.getCaretPosition();
				MouseButtonEvent e = manager.getLastButtonEvent();
				StringBuilder sb = new StringBuilder();
				sb.append("mouse.moveAt(").append(e.getX()).append(",")
						.append(e.getY()).append(");\n");
				areaCode.insertText(caretPosition, sb.toString());
			}));

		} else {
			// if toggle has been deselected
			manager.removePressListenersByPrefix("mouse.press");
			enableCodeType = true;
		}
	}

	@FXML
	void insertMouseMove(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		select(toggle);
		MouseEventsManager manager = MouseEventsManager.getInstance();
		if (toggle.isSelected()) {
			// if toggle has been seleted
			enableCodeType = false;
			KeyEventsManager.getInstance().addPressListener(
					new ShortcutEqualsHandler("key.press", "CONTROL", () -> {
						lastMoveEvent = manager.getLastMoveEvent();
					}));
			KeyEventsManager.getInstance().addReleaseListener(
					new ShortcutEqualsHandler("key.release", "CONTROL", () -> {
						int caretPosition = areaCode.getCaretPosition();
						StringBuilder sb = new StringBuilder();
						int dx = manager.getLastMoveEvent().getX()
								- lastMoveEvent.getX();
						int dy = manager.getLastMoveEvent().getY()
								- lastMoveEvent.getY();
						sb.append("mouse.move(").append(dx).append(",")
								.append(dy).append(");\n");
						areaCode.insertText(caretPosition, sb.toString());
					}));

		} else {
			// if toggle has been deselected
			KeyEventsManager.getInstance()
					.removePressListenersByPrefix("key.press");
			KeyEventsManager.getInstance()
					.removeReleaseListenersByPrefix("key.release");
			enableCodeType = true;
		}
	}

	/**
	 * Shows hint area with tex from userData in button
	 * 
	 * @param event
	 */
	@FXML
	public void showCodeSample(MouseEvent event) {
		ToggleButton btn = (ToggleButton) event.getSource();
		areaCodeSample.setText((String) btn.getUserData());
		areaCodeSample.setVisible(true);
	}
	/**
	 * Hides hint area
	 * 
	 * @param event
	 */
	@FXML
	public void hideCodeSample(MouseEvent event) {
		areaCodeSample.setVisible(false);
	}

	//// template buttons

	@FXML
	private Button btnTemplateMouseClick;

	@FXML
	private Button btnTemplateMouseClickAt;

	@FXML
	private Button btnTemplateMouseGetMoveDelay;

	@FXML
	private Button btnTemplateMouseGetPressDelay;

	@FXML
	private Button btnTemplateMouseGetReleaseDelay;

	@FXML
	private Button btnTemplateMouseGetX;

	@FXML
	private Button btnTemplateMouseGetY;

	@FXML
	private Button btnTemplateMouseMove;

	@FXML
	private Button btnTemplateMouseMoveAbsolute;

	@FXML
	private Button btnTemplateMouseMoveAbsolute_D;

	@FXML
	private Button btnTemplateMouseMoveAndClick;

	@FXML
	private Button btnTemplateMouseMoveAndPress;

	@FXML
	private Button btnTemplateMouseMoveAndRelease;

	@FXML
	private Button btnTemplateMouseMoveRelative;

	@FXML
	private Button btnTemplateMouseMoveRelative_D;

	@FXML
	private Button btnTemplateMouseMoveTo;

	@FXML
	private Button btnTemplateMousePress;

	@FXML
	private Button btnTemplateMousePressAt;

	@FXML
	private Button btnTemplateMouseRelease;

	@FXML
	private Button btnTemplateMouseReleaseAt;

	@FXML
	private Button btnTemplateMouseSetMoveDelay;

	@FXML
	private Button btnTemplateMouseSetPressDelay;

	@FXML
	private Button btnTemplateMouseSetReleaseDelay;

	@FXML
	private Button btnTemplateMouseSetX;

	@FXML
	private Button btnTemplateMouseSetY;

	////////////// KEYBOARD
	@FXML
	private Button btnTemplateKeyGetPressDelay;

	@FXML
	private Button btnTemplateKeyGetReleaseDelay;

	@FXML
	private Button btnTemplateKeyIsPressed;

	@FXML
	private Button btnTemplateKeyPress;

	@FXML
	private Button btnTemplateKeyRelease;

	@FXML
	private Button btnTemplateKeyType;

	@FXML
	private Button btnTemplateKeySetPressDelay;

	@FXML
	private Button btnTemplateKeySetReleaseDelay;
	// System
	@FXML
	private Button btnTemplateSystemSleep;

	@FXML
	private Button btnTemplateSystemPrint;

	@FXML
	private Button btnTemplateSystemPrintln;

	@FXML
	private Button btnTemplateSystemRegisterShortcut;
	/**
	 * Initizlizes template buttons
	 * 
	 * @param prop
	 */
	private void initTemplateButtons(SourcePropertyFile prop) {
		Button b;
		b = btnTemplateMouseClick;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseClickAt;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseGetMoveDelay;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseGetPressDelay;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseGetReleaseDelay;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});

		b = btnTemplateMouseGetX;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseGetY;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseMove;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseMoveAbsolute;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseMoveAbsolute_D;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseMoveAndClick;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseMoveAndPress;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseMoveAndRelease;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseMoveRelative;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseMoveRelative_D;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseMoveTo;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMousePress;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMousePressAt;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseRelease;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseReleaseAt;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseSetMoveDelay;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseSetPressDelay;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseSetReleaseDelay;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseSetX;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseSetY;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseGetPressDelay;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateMouseGetReleaseDelay;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		// Keyboard
		b = btnTemplateKeyGetPressDelay;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateKeyGetReleaseDelay;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateKeyIsPressed;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateKeyPress;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateKeyRelease;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateKeyType;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateKeySetPressDelay;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateKeySetReleaseDelay;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		// System
		b = btnTemplateSystemSleep;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateSystemPrint;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateSystemPrintln;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});
		b = btnTemplateSystemRegisterShortcut;
		b.setUserData(new String[]{prop.get(b.getId()),
				prop.get(b.getId() + "Code")});

	}
	/**
	 * inserts template from userData in button
	 * 
	 * @param event
	 */
	@FXML
	public void insertTemplate(ActionEvent event) {
		Button btn = (Button) event.getSource();
		String[] data = (String[]) btn.getUserData();
		if (data == null)
			return;
		int caretPosition = areaCode.getCaretPosition();
		areaCode.insertText(caretPosition, data[1]);

	}
	/**
	 * Shows area with hints and sample code from userData in button
	 * 
	 * @param event
	 */
	@FXML
	public void showCodeTemplate(MouseEvent event) {
		Button btn = (Button) event.getSource();
		String[] data = (String[]) btn.getUserData();
		if (data == null)
			return;
		areaCodeSample.setText(data[0]);
		areaCodeSample.setVisible(true);
	}
	/**
	 * Hides hint area
	 * 
	 * @param event
	 */
	@FXML
	public void hideCodeTemplate(MouseEvent event) {
		areaCodeSample.setVisible(false);
	}

	// Status control

	private enum Status {
		RUNNING, SUSPENDED
	}
	@FXML
	private ToggleButton btnScriptStatus;

	@FXML
	private ToggleButton btnTogglesStatus;

	private void setScriptStatus(Status status) {
		btnScriptStatus.setUserData(status);
		if (status == Status.RUNNING) {
			btnScriptStatus.setSelected(true);
			btnScriptStatus
					.setText("Running:   " + ClickerMain.getScript().getName());
		} else if (status == Status.SUSPENDED) {
			btnScriptStatus.setSelected(false);
			btnScriptStatus
					.setText("Suspended: " + ClickerMain.getScript().getName());
		}
	}

	private void updateScriptStatus() {
		if (btnScriptStatus.isSelected()) {
			btnScriptStatus
					.setText("Running:   " + ClickerMain.getScript().getName());
		} else {
			btnScriptStatus
					.setText("Suspended: " + ClickerMain.getScript().getName());
		}
	}
	private void setToggleStatus(ToggleButton toggle) {
		if (toggle == null) {
			btnTogglesStatus.setText("Never used");
			btnTogglesStatus.setUserData(null);
			return;
		}
		btnTogglesStatus.setSelected(toggle.isSelected());
		String title = "";
		if (toggle.isSelected()) {
			title += "Active:    ";
		} else {
			title += "Last used: ";
		}
		title += getTemplateButtonPath(toggle);
		btnTogglesStatus.setText(title);
		btnTogglesStatus.setUserData(toggle);
	}

	private String getTemplateButtonPath(Object button) {
		String out = "";
		Node n = (Node) button;
		if (button instanceof Button) {
			out = ((Button) button).getText();

		} else if (button instanceof ToggleButton) {
			out = ((ToggleButton) button).getText();
		}

		do {
			if (n instanceof TitledPane) {
				out = ((TitledPane) n).getText() + "> " + out;
			}
			n = n.getParent();
		} while ((!(n instanceof AnchorPane)) && (n != null));
		return out;
	}
	@FXML
	private void onBtnStatusScript(ActionEvent event) {
		ToggleButton button = (ToggleButton) event.getSource();
		if (button.isSelected()) {
			runScript();
		} else {
			stopScript();
		}
	}
	@FXML
	private void onBtnStatusToggles(ActionEvent event) {
		ToggleButton toggle = (ToggleButton) event.getSource();
		if (toggle.getUserData() == null) {
			toggle.setSelected(false);
			return;
		}
		if (toggle.getUserData() instanceof ToggleButton) {
			((ToggleButton) toggle.getUserData()).fire();
		}
	}

}
