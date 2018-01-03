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

	/**
	 * Adds one param. Use it wisely to manual build path
	 * @param param
	 */
	public void add(int param){
		builder.append((char) (param + SHIFT));
	}
	/**
	 * Adds to int params to path. Use it for add point
	 * @param x
	 * @param y
	 */
	public void add(int x, int y) {

		builder.append((char) (y + SHIFT));
	}

	/**
	 * Adds 3 params to path. Use it to add delay and point
	 * @param x
	 * @param y
	 * @param delay
	 */
	public void add(int x, int y, int delay) {
		builder.append((char) (x + SHIFT));
		builder.append((char) (y + SHIFT));
		builder.append((char) (delay + SHIFT));
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
		sb.append("mouse.moveAbsolute('");
		appendTextWithLineSize(sb, lineSize);
		return sb.toString();
	}

	private void appendTextWithLineSize(StringBuilder sb, int lineSize) {
		char[] c = builder.toString().toCharArray();
		for (char ch : c) {
			if ((sb.length() + 3) % lineSize != 0) {
				sb.append(ch);
			} else {
				sb.append("'+\n");
				sb.append('\'');
				sb.append(ch);
			}
		}
		sb.append("');\n");
	}

	public String getMoveCodeAbsolutePathWithDelays(int lineSize) {
		StringBuilder sb = new StringBuilder("");
		sb.append("mouse.moveAbsolute_D('");
		appendTextWithLineSize(sb, lineSize);
		return sb.toString();
	}

	public String getMoveCodeRelativePath(int lineSize) {
		StringBuilder sb = new StringBuilder("");
		sb.append("mouse.moveRelative('");
		appendTextWithLineSize(sb, lineSize);
		return sb.toString();
	}

	public String getMoveCodeRelativePathWithDelays(int lineSize) {
		StringBuilder sb = new StringBuilder("");
		sb.append("mouse.moveRelative_D('");
		appendTextWithLineSize(sb, lineSize);
		return sb.toString();
	}

	public boolean isEmpty(){
		return builder.length()==0;
	}

	public void clear(){
		builder = new StringBuilder("");
	}
	
}
