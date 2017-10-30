package org.dikhim.jclicker.events;

public class MouseWheelEvent {
	public MouseWheelEvent(String direction, int amount) {
		super();
		this.direction = direction;
		this.amount = amount;
	}
	private String direction;
	private int amount;
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

}
