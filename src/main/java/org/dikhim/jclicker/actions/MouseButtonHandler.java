package org.dikhim.jclicker.actions;

import org.dikhim.jclicker.actions.events.MouseButtonEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class MouseButtonHandler {
    private String name;
    private String action;
    private Set<String> buttons = new HashSet<>();
    private Consumer<MouseButtonEvent> handler;

    public MouseButtonHandler(String name, String buttons, String action, Consumer<MouseButtonEvent> handler) {
        this.name = name;
        this.action = action;
        this.handler = handler;
        String[] buttonsArray = buttons.split(" ");
        for (String b : buttonsArray) {
            if (!b.equals("")) this.buttons.add(b);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getButtons() {
        return buttons;
    }

    public void setButtons(Set<String> buttons) {
        this.buttons = buttons;
    }

    public Consumer<MouseButtonEvent> getHandler() {
        return handler;
    }

    public void setHandler(Consumer<MouseButtonEvent> handler) {
        this.handler = handler;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void fire(MouseButtonEvent event) {
        // if no actions or actions are equals then continue
        if (!action.isEmpty() && !action.equals(event.getAction())) return;

        if (buttons.isEmpty()) {
            handler.accept(event);
            return;
        }

        if (buttons.equals(event.getPressed()))
            handler.accept(event);
    }
}
