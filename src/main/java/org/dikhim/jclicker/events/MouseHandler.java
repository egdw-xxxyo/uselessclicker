package org.dikhim.jclicker.events;

import java.util.HashSet;
import java.util.Set;

public class MouseHandler {
	private String name;
	private Set<String> buttons = new HashSet<>();
	private Runnable handler;
	public MouseHandler(String name, String buttons, Runnable handler) {
		this.name = name;
		this.handler = handler;
		String[] btns = buttons.split(" ");
		for(String b:btns) {
			if(!b.equals(""))this.buttons.add(b);
		}
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the buttons
	 */
	public Set<String> getButtons() {
		return buttons;
	}
	/**
	 * @param buttons the buttons to set
	 */
	public void setButtons(Set<String> buttons) {
		this.buttons = buttons;
	}
	/**
	 * @return the handler
	 */
	public Runnable getHandler() {
		return handler;
	}
	/**
	 * @param handler the handler to set
	 */
	public void setHandler(Runnable handler) {
		this.handler = handler;
	}
	public void fire(Set<String> pressedButtons) {
		if(buttons.equals(pressedButtons))handler.run();
		
	}
	
	
	

}
