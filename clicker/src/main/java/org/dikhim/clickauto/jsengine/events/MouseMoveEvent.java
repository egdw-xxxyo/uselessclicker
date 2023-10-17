package org.dikhim.clickauto.jsengine.events;

public class MouseMoveEvent implements MouseEvent {
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


	@Override
	public EventType getType() {
		return EventType.MOUSE_MOVE;
	}

	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}

}
