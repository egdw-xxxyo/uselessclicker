package org.dikhim.jclicker.util;


public class MouseMoveUtil {
	private StringBuilder builder = new StringBuilder("");
	
	
	public void add(int x,int y) {
		builder.append((char)(x+500));
		builder.append((char)(y+500));
	}
	public void add(int delay,int x,int y) {
		builder.append((char)(delay+500));
		builder.append((char)(x+500));
		builder.append((char)(y+500));
	}
	
	public String getMoveCodeAbsolutePath(int lineSize) {
		StringBuilder sb= new StringBuilder("");
		sb.append("mouse.moveAbsolutePath('");
		appendTextWithLineSize(sb, lineSize);
		return sb.toString();
	}
	private void appendTextWithLineSize(StringBuilder sb, int lineSize) {
		char[] c  = builder.toString().toCharArray();
		for(char ch:c) {
			if((sb.length()+2)%lineSize!=0) {
				sb.append(ch);
			}else {
				sb.append("'+\n");
				sb.append('\'');
				sb.append(ch);
			}
		}
		sb.append("');");
	}
	public String getMoveCodeAbsolutePathWithDelays(int lineSize) {
		StringBuilder sb= new StringBuilder("");
		sb.append("mouse.moveAbsolutePathWithDelays('");
		appendTextWithLineSize(sb, lineSize);
		sb.append("');");
		return sb.toString();
	}
	public String getMoveCodeRelativePath(int lineSize) {
		StringBuilder sb= new StringBuilder("");
		sb.append("mouse.moveRelativePath('");
		appendTextWithLineSize(sb, lineSize);
		sb.append("');");
		return sb.toString();
	}
	public String getMoveCodeRelativePathWithDelays(int lineSize) {
		StringBuilder sb= new StringBuilder("");
		sb.append("mouse.moveRelativePathWithDelays('");
		appendTextWithLineSize(sb, lineSize);
		sb.append("');");
		return sb.toString();
	}
	
	
}
