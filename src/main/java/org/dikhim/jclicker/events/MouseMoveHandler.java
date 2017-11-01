package org.dikhim.jclicker.events;

import java.util.function.Consumer;

public class MouseMoveHandler {
    String name;
    Consumer<MouseMoveEvent> handler;

    public MouseMoveHandler(String name, Consumer<MouseMoveEvent> handler) {
        this.name = name;
        this.handler = handler;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Consumer<MouseMoveEvent> getHandler() {
        return handler;
    }

    public void setHandler(Consumer<MouseMoveEvent> handler) {
        this.handler = handler;
    }

    public void fire(MouseMoveEvent event){
        handler.accept(event);
    }
}
