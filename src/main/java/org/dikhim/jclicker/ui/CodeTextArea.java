package org.dikhim.jclicker.ui;

import com.sun.javafx.scene.control.skin.TextAreaSkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.util.Arrays;

public class CodeTextArea extends TextArea {
    private int tabSize = 4;

    private BooleanProperty active = new SimpleBooleanProperty(true);

    public CodeTextArea() {
        AnchorPane.setTopAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);
        AnchorPane.setBottomAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);
        addEventFilter(KeyEvent.KEY_PRESSED,
                e -> {
                    if (!isActive()) {
                        e.consume();
                    } else {
                        switch (e.getCode()) {
                            case TAB:
                                insertTab();
                                e.consume();
                                break;
                            case ENTER:
                                insertEnter();
                                e.consume();
                                break;
                        }
                    }

                });
        addEventFilter(KeyEvent.KEY_TYPED,
                e -> {
                    if (!isActive()) {
                        e.consume();
                    } else {
                        switch (e.getCode()) {
                            case TAB:
                                insertTab();
                                e.consume();
                                break;
                            case ENTER:
                                insertEnter();
                                e.consume();
                                break;
                        }
                    }

                });
        TextAreaSkin customContextSkin = new TextAreaSkin(this) {
            @Override
            public void populateContextMenu(ContextMenu contextMenu) {
                super.populateContextMenu(contextMenu);

                MenuItem insert = new MenuItem("Insert");
                insert.setOnAction(event -> {
                    Clipboard clipboard = Clipboard.getSystemClipboard();
                    String s = clipboard.getString();
                    if (s == null) return;
                    CodeTextArea.this.insertTextIntoCaretPosition(s);
                });

                contextMenu.getItems().add(0, new SeparatorMenuItem());
                contextMenu.getItems().add(0, insert);
            }
        };
        this.setSkin(customContextSkin);

    }

    
    private void insertTab() {

        IndexRange selection = this.getSelection();
        if (selection.getLength() > 0) {
            // compute index of first line
            char[] charArray = this.getText().toCharArray();
            int caret = selection.getStart();
            int firstLineSelectionIndex = 0;
            for (int i = caret - 1; i >= 0; i--) {
                if (charArray[i] == '\n') {
                    firstLineSelectionIndex = i + 1;
                    break;
                }
            }

            // insert spaces after each line separator
            String selectedText = this.getSelectedText();
            selectedText = selectedText.replaceAll("\n", "\n"+getTab());
            this.replaceSelection(selectedText);
            
            // insert spaces in first line
            this.insertText(firstLineSelectionIndex,getTab());
        }else {
            this.insertText(this.getCaretPosition(), getTab());
        }        
    }

    private void insertEnter() {
        int spacesCount = getSpacesForPreviousLine();
        insertNewLineWithSpaces(spacesCount);
    }

    private int getSpacesForPreviousLine() {
        char[] charArray = this.getText().toCharArray();
        int caret = this.getCaretPosition();
        int lineBeginning = 0;
        for (int i = caret - 1; i >= 0; i--) {
            if (charArray[i] == '\n') {
                lineBeginning = i + 1;
                break;
            }
        }
        int spacesCount = 0;
        for (int i = lineBeginning; i < caret; i++) {
            if (charArray[i] == ' ') {
                spacesCount++;
            } else {
                break;
            }
        }
        return spacesCount;
    }

    private void insertNewLineWithSpaces(int spaceCount) {
        if (spaceCount > 0) {
            char[] c = new char[spaceCount];
            Arrays.fill(c, ' ');
            String s = "\n" + String.valueOf(c);
            insertIntoCaretPosition(s);
        } else if (spaceCount == 0) {
            insertIntoCaretPosition("\n");
        }
    }

    public void insertIntoCaretPosition(String text) {
        this.insertText(this.getCaretPosition(), text);
    }

    public void insertTextIntoCaretPosition(String text) {
        if (!text.contains("\n")) {
            insertIntoCaretPosition(text);
            return;
        }
        String[] lines = text.split("\n");
        int spaces = getSpacesForPreviousLine();
        for (String line : lines) {
            insertIntoCaretPosition(line);
            insertNewLineWithSpaces(spaces);
        }
    }

    private String getTab() {
        char[] c = new char[tabSize];
        Arrays.fill(c, ' ');
        return String.valueOf(c);
    }
    
    public int getTabSize() {
        return tabSize;
    }

    public void setTabSize(int tabSize) {
        this.tabSize = tabSize;
    }

    public boolean isActive() {
        return active.get();
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public void addChangeListener(Runnable runnable) {
        textProperty().addListener((observable, oldValue, newValue) -> runnable.run());
    }
}
