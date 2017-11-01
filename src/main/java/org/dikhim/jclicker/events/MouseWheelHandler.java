package org.dikhim.jclicker.events;

import java.util.function.Consumer;

public class MouseWheelHandler {
    private String name;
    private String direction;
    private Consumer<MouseWheelEvent> handler;

    public MouseWheelHandler(String name, String direction, Consumer<MouseWheelEvent> handler) {
        this.name = name;
        this.direction = direction;
        this.handler = handler;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Consumer<MouseWheelEvent> getHandler() {
        return handler;
    }

    public void setHandler(Consumer<MouseWheelEvent> handler) {
        this.handler = handler;
    }
    public void fire(MouseWheelEvent event){
        if(this.direction.isEmpty()) {
            handler.accept(event);
            return;
        }
        if(event.getDirection().equals(this.direction))
            handler.accept(event);
    }
}
