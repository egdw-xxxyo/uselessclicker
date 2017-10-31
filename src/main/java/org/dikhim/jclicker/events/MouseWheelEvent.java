package org.dikhim.jclicker.events;

public class MouseWheelEvent {
	private int x;
	private int y;
	private String direction;
	private int amount;
	private long time;
	
	public MouseWheelEvent(String direction, int amount,long time,int x, int y) {
		this.direction = direction;
		this.amount = amount;
		this.setTime(time);
		this.x = x;
		this.y = y;
	}
	
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}

}
