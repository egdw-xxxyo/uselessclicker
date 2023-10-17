package org.dikhim.clickauto.jsengine.objects;

import org.dikhim.clickauto.jsengine.robot.Robot;
import org.dikhim.clickauto.jsengine.utils.KeyCodes;
import org.dikhim.clickauto.jsengine.utils.typer.Typer;
import org.dikhim.clickauto.jsengine.utils.typer.Typers;
import org.dikhim.clickauto.util.MathUtil;
import org.dikhim.clickauto.util.logger.ClickAutoLog;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class ScriptKeyboardObject implements KeyboardObject {
    protected final int PRESS_DELAY = 10;
    protected final int RELEASE_DELAY = 10;
    protected final double MULTIPLIER = 1f;
    protected final int MIN_DELAY = 5;

    protected final Robot robot;
    
    protected volatile int pressDelay = PRESS_DELAY;
    protected volatile int releaseDelay = RELEASE_DELAY;
    protected volatile double multiplier = MULTIPLIER;
    protected volatile int minDelay = MIN_DELAY;

    public ScriptKeyboardObject(Robot robot) {
        this.robot = robot;
    }

    @Override
    public int getMinDelay() {
        synchronized (robot) {
            return minDelay;
        }
    }

    @Override
    public int getMultipliedPressDelay() {
        synchronized (robot) {
            return checkDelay((int) (pressDelay * multiplier));
        }
    }

    @Override
    public int getMultipliedReleaseDelay() {
        synchronized (robot) {
            return checkDelay((int) (releaseDelay * multiplier));
        }
    }

    @Override
    public double getMultiplier() {
        synchronized (robot) {
            return multiplier;
        }
    }

    @Override
    public int getPressDelay() {
        synchronized (robot) {
            return pressDelay;
        }
    }

    @Override
    public int getReleaseDelay() {
        synchronized (robot) {
            return releaseDelay;
        }
    }

    @Override
    public double getSpeed() {
        synchronized (robot) {
            if (multiplier == 0) return 999999999;
            return MathUtil.roundTo1(1.0 / multiplier);
        }
    }


    @Override
    public void perform(String keys, String action) {
        synchronized (robot) {
            switch (action) {
                case "PRESS":
                    press(keys);
                    break;
                case "RELEASE":
                    release(keys);
                    break;
                case "TYPE":
                    type(keys);
                default:
                    ClickAutoLog.get().error("Undefined key actions '%s' in perform method\n", action);
            }
        }
    }

    @Override
    public void press(String keys) {
        synchronized (robot) {
            Set<String> keySet = new LinkedHashSet<>(Arrays.asList(keys.split(" ")));
            for (String key : keySet) {
                int keyCode = KeyCodes.getEventCodeByName(key);
                if (keyCode != -1) {
                    robot.keyPress(keyCode);
                    delay(getMultipliedPressDelay());
                } else {
                    ClickAutoLog.get().error("Undefined key '%s'in sequence '%s' in press method\n", key, keys);
                }
            }
        }
    }

    @Override
    public void release(String keys) {
        synchronized (robot) {
            Set<String> keySet = new LinkedHashSet<>(Arrays.asList(keys.split(" ")));
            for (String key : keySet) {
                int keyCode = KeyCodes.getEventCodeByName(key);
                if (keyCode != -1) {
                    robot.keyRelease(keyCode);
                    delay(getMultipliedReleaseDelay());
                } else {
                    ClickAutoLog.get().error("Undefined key '%s' in release method\n", key);
                }
            }
        }
    }

    @Override
    public void resetDelays() {
        synchronized (robot) {
            this.pressDelay = PRESS_DELAY;
            this.releaseDelay = RELEASE_DELAY;
        }
    }

    @Override
    public void resetMultiplier() {
        synchronized (robot) {
            this.multiplier = MULTIPLIER;
        }
    }

    @Override
    public void resetSpeed() {
        synchronized (robot) {
            resetMultiplier();
        }
    }

    @Override
    public void setDelays(int delay) {
        synchronized (robot) {
            setPressDelay(delay);
            setReleaseDelay(delay);
        }
    }

    @Override
    public void setMinDelay(int delay) {
        synchronized (robot) {
            this.minDelay = delay;
        }
    }

    @Override
    public void setMultiplier(double multiplier) {
        synchronized (robot) {
            if (multiplier < 0) {
                this.multiplier = 0;
            } else {
                this.multiplier = multiplier;
            }
        }
    }

    @Override
    public void setPressDelay(int pressDelay) {
        synchronized (robot) {
            if (pressDelay < 0) {
                this.pressDelay = 0;
            } else {
                this.pressDelay = pressDelay;
            }
        }
    }

    @Override
    public void setReleaseDelay(int releaseDelay) {
        synchronized (robot) {
            if (releaseDelay < 0) {
                this.releaseDelay = 0;
            } else {
                this.releaseDelay = releaseDelay;
            }
        }
    }

    @Override
    public void setSpeed(double speed) {
        synchronized (robot) {
            if (speed < 0.1) {
                speed = 0.1;
            }
            speed = MathUtil.roundTo1(speed);
            setMultiplier(1f / speed);
        }
    }

    @Override
    public void type(String keys) {
        synchronized (robot) {
            String[] keyList = keys.split(" ");
            for (String key : keyList) {
                int keyCode = KeyCodes.getEventCodeByName(key);
                if (keyCode != -1) {
                    robot.keyPress(keyCode);
                    delay(getMultipliedPressDelay());
                    robot.keyRelease(keyCode);
                    delay(getMultipliedReleaseDelay());
                } else {
                    ClickAutoLog.get().error("Undefined key '%s' in type method\n", key);
                }
            }
        }
    }

    @Override
    public void typeText(String layout, String text) {
        try {
            Typer typer = Typers.create(this, layout);
            typer.type(text);
        } catch (Exception e) {
            ClickAutoLog.get().error(e.getMessage()+"\n");
        }
    }

    protected int checkDelay(int delay) {
        if (delay < minDelay) return minDelay;

        return delay;
    }

    protected void delay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
