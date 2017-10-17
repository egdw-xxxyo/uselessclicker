package org.dikhim.jclicker.util;

public class MouseMoveUtil {
	private StringBuilder builder;
	private final int SHIFT = 13350;
	public MouseMoveUtil() {
		this.builder = new StringBuilder();
	}
	public MouseMoveUtil(String path) {
		this.builder = new StringBuilder(path);
	}
	
	public void set(String path) {
		builder = new StringBuilder(path);
	}
	public void add(int x, int y) {
		builder.append((char) (x + SHIFT));
		builder.append((char) (y + SHIFT));
	}
	public void add(int delay, int x, int y) {
		builder.append((char) (delay + SHIFT));
		builder.append((char) (x + SHIFT));
		builder.append((char) (y + SHIFT));
	}

	public int getNext() {
		char c =  builder.charAt(0);
		int out = ((int)c)-SHIFT;
		builder.delete(0, 1);
		return out;
	}
	
	public boolean hasNext() {
		return builder.length()!=0;
	}
	public String getString() {
		return builder.toString();
	}
	public String getMoveCodeAbsolutePath(int lineSize) {
		StringBuilder sb = new StringBuilder("");
		sb.append("mouse.moveAbsolutePath('");
		appendTextWithLineSize(sb, lineSize);
		return sb.toString();
	}

	private void appendTextWithLineSize(StringBuilder sb, int lineSize) {
		char[] c = builder.toString().toCharArray();
		for (char ch : c) {
			if ((sb.length() + 2) % lineSize != 0) {
				sb.append(ch);
			} else {
				sb.append("'+\n");
				sb.append('\'');
				sb.append(ch);
			}
		}
		sb.append("');");
	}

	public String getMoveCodeAbsolutePathWithDelays(int lineSize) {
		StringBuilder sb = new StringBuilder("");
		sb.append("mouse.moveAbsolutePathWithDelays('");
		appendTextWithLineSize(sb, lineSize);
		sb.append("');");
		return sb.toString();
	}

	public String getMoveCodeRelativePath(int lineSize) {
		StringBuilder sb = new StringBuilder("");
		sb.append("mouse.moveRelativePath('");
		appendTextWithLineSize(sb, lineSize);
		sb.append("');");
		return sb.toString();
	}

	public String getMoveCodeRelativePathWithDelays(int lineSize) {
		StringBuilder sb = new StringBuilder("");
		sb.append("mouse.moveRelativePathWithDelays('");
		appendTextWithLineSize(sb, lineSize);
		sb.append("');");
		return sb.toString();
	}

	
}
