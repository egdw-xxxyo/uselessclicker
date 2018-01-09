package org.dikhim.jclicker.jsengine.objects.generators;

import org.dikhim.jclicker.jsengine.objects.MouseObject;

public class MouseObjectCodeGenerator extends SimpleCodeGenerator implements MouseObject {

    public MouseObjectCodeGenerator(int lineSize) {
        super("mouse", lineSize);
    }

    public void button(String button, String action) {
        begin().append("('")
                .append(button).append("','")
                .append(action).append("');\n");
    }

    public void buttonAt(String button, String action, int x, int y) {
        begin().append("('")
                .append(button).append("','")
                .append(action).append("',")
                .append(x).append(",")
                .append(y).append(");\n");
    }

    public void click(String button) {
        begin().append("('")
                .append(button).append("');\n");
    }

    public void clickAt(String button, int x, int y) {
        begin().append("('")
                .append(button).append("',")
                .append(x).append(",")
                .append(y).append(");\n");
    }

    public int getMinDelay() {
        begin().append("();\n");
        return 0;
    }

    public int getMoveDelay() {
        begin().append("();\n");
        return 0;
    }

    public int getMultipliedMoveDelay() {
        begin().append("();\n");
        return 0;
    }

    public int getMultipliedPressDelay() {
        begin().append("();\n");
        return 0;
    }

    public int getMultipliedReleaseDelay() {
        begin().append("();\n");
        return 0;
    }

    public int getMultipliedWheelDelay() {
        begin().append("();\n");
        return 0;
    }

    public float getMultiplier() {
        begin().append("();\n");
        return 0;
    }


    public int getPressDelay() {
        begin().append("();\n");
        return 0;
    }

    public int getReleaseDelay() {
        begin().append("();\n");
        return 0;
    }

    public float getSpeed() {
        begin().append("();\n");
        return 0;
    }

    public int getWheelDelay() {
        begin().append("();\n");
        return 0;
    }

    public int getX() {
        begin().append("();\n");
        return 0;
    }

    public int getY() {
        begin().append("();\n");
        return 0;
    }

    public void move(int dx, int dy) {
        begin().append("(")
                .append(dx).append(",")
                .append(dy).append(");\n");
    }

    public void moveAbsolute(String path) {
        begin().append("('")
                .append(path).append("');\n");
    }

    public void moveAbsolute_D(String path) {
        begin().append("('")
                .append(path).append("');\n");
    }

    public void moveAndButton(String button, String action, int dx, int dy) {
        begin().append("('")
                .append(button).append("','")
                .append(action).append("',")
                .append(dx).append(",")
                .append(dy).append(");\n");
    }

    public void moveAndClick(String button, int dx, int dy) {
        begin().append("('")
                .append(button).append("',")
                .append(dx).append(",")
                .append(dy).append(");\n");
    }

    public void moveAndPress(String button, int dx, int dy) {
        begin().append("('")
                .append(button).append("',")
                .append(dx).append(",")
                .append(dy).append(");\n");
    }

    public void moveAndRelease(String button, int dx, int dy) {
        begin().append("('")
                .append(button).append("',")
                .append(dx).append(",")
                .append(dy).append(");\n");
    }

    public void moveAndWheel(String direction, int amount, int dx, int dy) {
        begin().append("('")
                .append(direction).append("',")
                .append(amount).append(",")
                .append(dx).append(",")
                .append(dy).append(");\n");
    }

    public void moveRelative(String path) {
        begin().append("('")
                .append(path).append("');\n");
    }

    public void moveRelative_D(String path) {
        begin().append("('")
                .append(path).append("');\n");
    }

    public void moveTo(int x, int y) {
        begin().append("(")
                .append(x).append(",")
                .append(y).append(");\n");
    }

    public void press(String button) {
        begin().append("('")
                .append(button).append("');\n");
    }

    public void pressAt(String button, int x, int y) {
        begin().append("('")
                .append(button).append("',")
                .append(x).append(",")
                .append(y).append(");\n");
    }

    public void release(String button) {
        begin().append("('")
                .append(button).append("');\n");
    }

    public void releaseAt(String button, int x, int y) {
        begin().append("('")
                .append(button).append("',")
                .append(x).append(",")
                .append(y).append(");\n");
    }

    public void resetDelays() {
        begin().append("();\n");
    }

    public void resetMultiplier() {
        begin().append("();\n");
    }

    public void resetSpeed() {
        begin().append("();\n");
    }

    public void setDelays(int delay) {
        begin().append("(")
                .append(delay).append(");\n");
    }

    public void setMinDelay(int minDelay) {
        begin().append("(")
                .append(minDelay).append(");\n");
    }

    public void setMoveDelay(int moveDelay) {
        begin().append("(")
                .append(moveDelay).append(");\n");
    }

    public void setMultiplier(float multiplier) {
        begin().append("(")
                .append(multiplier).append(");\n");
    }

    public void setPressDelay(int pressDelay) {
        begin().append("(")
                .append(pressDelay).append(");\n");
    }

    public void setReleaseDelay(int releaseDelay) {
        begin().append("(")
                .append(releaseDelay).append(");\n");
    }

    public void setSpeed(float multiplier) {
        begin().append("(")
                .append(multiplier).append(");\n");
    }

    public void setWheelDelay(int wheelDelay) {
        begin().append("(")
                .append(wheelDelay).append(");\n");
    }

    public void setX(int x) {
        begin().append("(")
                .append(x).append(");\n");
    }

    public void setY(int y) {
        begin().append("(")
                .append(y).append(");\n");
    }

    public void wheel(String direction, int amount) {
        begin().append("('")
                .append(direction).append("',")
                .append(amount).append(");\n");
    }

    public void wheelAt(String direction, int amount, int x, int y) {
        begin().append("('")
                .append(direction).append("',")
                .append(amount).append(",")
                .append(x).append(",")
                .append(y).append(");\n");
    }


}
