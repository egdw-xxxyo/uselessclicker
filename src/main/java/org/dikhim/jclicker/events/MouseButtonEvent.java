package org.dikhim.jclicker.events;

public class MouseButtonEvent {
	private int x;
	private int y;
	private long time;
	private String button;
	private String action;
	public MouseButtonEvent(String button, String action, int x, int y, long time) {
		super();
		this.x = x;
		this.y = y;
		this.button = button;
		this.setAction(action);
		this.time = time;
	}
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * @return the button
	 */
	public String getButton() {
		return button;
	}
	/**
	 * @param button
	 *            the button to set
	 */
	public void setButton(String button) {
		this.button = button;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

}
