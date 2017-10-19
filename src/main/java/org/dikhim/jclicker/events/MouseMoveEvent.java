package org.dikhim.jclicker.events;

public class MouseMoveEvent {
	private int x;
	private int y;
	private long time;
	public MouseMoveEvent(int x, int y, long time) {
		super();
		this.x = x;
		this.y = y;
		this.time = time;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}

}
