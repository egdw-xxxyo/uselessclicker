package org.dikhim.clickauto.jsengine.objects;

import org.dikhim.clickauto.jsengine.robot.Robot;

public class AnimatedMouse {
    private final ScriptMouseObject mouse;
    private final Robot robot;
    private AnimationMethod method = new LinearMethod();

    public AnimatedMouse(ScriptMouseObject mouseObject, Robot robot) {
        this.mouse = mouseObject;
        this.robot = robot;
    }

    public void move(int dx, int dy, int delay) {
        synchronized (robot) {
            long startTime = System.currentTimeMillis();
            double animationTime = mouse.getMultiplier() * delay;
            int currentDx = 0;
            int currentDy = 0;

            double currentTime = System.currentTimeMillis();
            animationTime += currentTime;
            while (currentTime + mouse.getMultipliedMoveDelay() * 2 < animationTime) {
                double position = method.transform((delay - (animationTime - currentTime)) / delay);


                int diffDx = (int) (position * dx - currentDx);
                int diffDy = (int) (position * dy - currentDy);
                mouse.move(diffDx, diffDy);
                currentDx += diffDx;
                currentDy += diffDy;
                currentTime = System.currentTimeMillis();
            }
            mouse.move(dx - currentDx, dy - currentDy);
            mouse.delay((int) (delay - (System.currentTimeMillis() - startTime)));
        }
    }


    public void moveTo(int x, int y, int delay) {
        synchronized (robot) {
            long startTime = System.currentTimeMillis();
            double animationTime = mouse.getMultiplier() * delay;
            int initX = mouse.getX();
            int initY = mouse.getY();
            int dx = x - initX;
            int dy = y - initY;

            double currentTime = System.currentTimeMillis();
            animationTime += currentTime;
            while (currentTime + mouse.getMultipliedMoveDelay() * 2 < animationTime) {
                double position = method.transform((delay - (animationTime - currentTime)) / delay);
                mouse.moveTo((int) (initX + dx * position), (int) (initY + dy * position));
                currentTime = System.currentTimeMillis();
            }
            mouse.moveTo(x, y);
            mouse.delay((int) (delay - (System.currentTimeMillis() - startTime)));
        }
    }

    public void pressLeftAt(int x, int y, int delay) {
        synchronized (robot) {
            moveTo(x, y, delay);
            mouse.pressLeft();
        }
    }

    public void pressRightAt(int x, int y, int delay) {
        synchronized (robot) {
            moveTo(x, y, delay);
            mouse.pressRight();
        }
    }

    public void pressMiddleAt(int x, int y, int delay) {
        synchronized (robot) {
            moveTo(x, y, delay);
            mouse.pressMiddle();
        }
    }

    public void releaseLeftAt(int x, int y, int delay) {
        synchronized (robot) {
            moveTo(x, y, delay);
            mouse.releaseLeft();
        }
    }

    public void releaseRightAt(int x, int y, int delay) {
        synchronized (robot) {
            moveTo(x, y, delay);
            mouse.releaseRight();
        }
    }

    public void releaseMiddleAt(int x, int y, int delay) {
        synchronized (robot) {
            moveTo(x, y, delay);
            mouse.releaseMiddle();
        }
    }

    public void clickLeftAt(int x, int y, int delay) {
        synchronized (robot) {
            moveTo(x, y, delay);
            mouse.clickLeft();
        }
    }

    public void clickRightAt(int x, int y, int delay) {
        synchronized (robot) {
            moveTo(x, y, delay);
            mouse.clickRight();
        }
    }

    public void clickMiddleAt(int x, int y, int delay) {
        synchronized (robot) {
            moveTo(x, y, delay);
            mouse.clickMiddle();
        }
    }

    public void wheelUpAt(int x, int y, int amount, int delay) {
        synchronized (robot) {
            moveTo(x, y, delay);
            mouse.wheelUp(amount);
        }
    }

    public void wheelDownAt(int x, int y, int amount, int delay) {
        synchronized (robot) {
            moveTo(x, y, delay);
            mouse.wheelDown(amount);
        }
    }


    public void moveAndPressLeft(int dx, int dy, int delay) {
        synchronized (robot) {
            move(dx, dy, delay);
            mouse.pressLeft();
        }
    }

    public void moveAndPressRight(int dx, int dy, int delay) {
        synchronized (robot) {
            move(dx, dy, delay);
            mouse.pressRight();
        }
    }

    public void moveAndPressMiddle(int dx, int dy, int delay) {
        synchronized (robot) {
            move(dx, dy, delay);
            mouse.pressMiddle();
        }
    }

    public void moveAndReleaseLeft(int dx, int dy, int delay) {
        synchronized (robot) {
            move(dx, dy, delay);
            mouse.releaseLeft();
        }
    }

    public void moveAndReleaseRight(int dx, int dy, int delay) {
        synchronized (robot) {
            move(dx, dy, delay);
            mouse.releaseRight();
        }
    }

    public void moveAndReleaseMiddle(int dx, int dy, int delay) {
        synchronized (robot) {
            move(dx, dy, delay);
            mouse.releaseMiddle();
        }
    }

    public void moveAndClickLeft(int dx, int dy, int delay) {
        synchronized (robot) {
            move(dx, dy, delay);
            mouse.clickLeft();
        }
    }

    public void moveAndClickRight(int dx, int dy, int delay) {
        synchronized (robot) {
            move(dx, dy, delay);
            mouse.clickRight();
        }
    }

    public void moveAndClickMiddle(int dx, int dy, int delay) {
        synchronized (robot) {
            move(dx, dy, delay);
            mouse.clickMiddle();
        }
    }

    public void moveAndWheelUp(int dx, int dy, int amount, int delay) {
        synchronized (robot) {
            move(dx, dy, delay);
            mouse.wheelUp(amount);
        }
    }

    public void moveAndWheelDown(int dx, int dy, int amount, int delay) {
        synchronized (robot) {
            move(dx, dy, delay);
            mouse.wheelDown(amount);
        }
    }

    public void setLinearMethod() {
        synchronized (robot) {
            method = new LinearMethod();
        }
    }

    public void setExponentialMethod() {
        synchronized (robot) {
            method = new ExponentialMethod();
        }
    }

    public void setAcceleratingMethod() {
        synchronized (robot) {
            method = new AcceleratingMethod();
        }
    }

    public void setDeceleratingMethod() {
        synchronized (robot) {
            method = new DeceleratingMethod();
        }
    }
}
