package org.dikhim.jclicker.jsengine.objects;

import java.util.*;

import org.dikhim.jclicker.actions.utils.KeyCodes;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.utils.typer.Typer;
import org.dikhim.jclicker.actions.utils.typer.TyperFactory;
import org.dikhim.jclicker.jsengine.robot.Robot;
import org.dikhim.jclicker.util.Out;

/**
 * Created by dikobraz on 31.03.17.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class JsKeyboardObject implements KeyboardObject {

    // Constants
    private final int PRESS_DELAY = 10;
    private final int RELEASE_DELAY = 10;
    private final float MULTIPLIER = 1f;
    private final int MIN_DELAY = 5;

    private int pressDelay = PRESS_DELAY;
    private int releaseDelay = RELEASE_DELAY;
    private float multiplier = MULTIPLIER;
    private int minDelay = MIN_DELAY;


    private Robot robot;
    private final Object monitor;

    public JsKeyboardObject(Robot robot) {
        this.robot = robot;
        this.monitor = robot.getMonitor();
    }

    @Override
    public int getMinDelay() {
        synchronized (monitor) {
            return minDelay;
        }
    }

    @Override
    public int getMultipliedPressDelay() {
        synchronized (monitor) {
            return checkDelay((int) (pressDelay * multiplier));
        }
    }

    @Override
    public int getMultipliedReleaseDelay() {
        synchronized (monitor) {
            return checkDelay((int) (releaseDelay * multiplier));
        }
    }

    @Override
    public float getMultiplier() {
        synchronized (monitor) {
            return multiplier;
        }
    }

    @Override
    public int getPressDelay() {
        synchronized (monitor) {
            return pressDelay;
        }
    }

    @Override
    public int getReleaseDelay() {
        synchronized (monitor) {
            return releaseDelay;
        }
    }

    @Override
    public float getSpeed() {
        synchronized (monitor) {
            return 1f / getMultiplier();
        }
    }

    @Override
    public boolean isPressed(String keys) {
        synchronized (monitor) {
            Set<String> keySet = new HashSet<>(Arrays.asList(keys.split(" ")));
            return KeyEventsManager.getInstance().isPressed(keySet);
        }
    }

    @Override
    public boolean isCapsLocked() {
        return KeyEventsManager.getInstance().isCapsLockLocked();
    }

    @Override
    public boolean isNumLocked() {
        return KeyEventsManager.getInstance().isNumLockLocked();
    }

    @Override
    public boolean isScrollLocked() {
        return KeyEventsManager.getInstance().isScrollLockLocked();
    }

    @Override
    public void perform(String keys, String action) {
        synchronized (monitor) {
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
                    Out.println(String.format("Undefined key actions '%s' in perform method", action));
            }
        }
    }

    @Override
    public void press(String keys) {
        synchronized (monitor) {
            Set<String> keySet = new LinkedHashSet<>(Arrays.asList(keys.split(" ")));
            for (String key : keySet) {
                int keyCode = KeyCodes.getEventCodeByName(key);
                if (keyCode != -1) {
                    robot.keyPress(keyCode);
                    robot.delay(getMultipliedPressDelay());
                } else {
                    Out.println(String.format("Undefined key '%s'in sequence '%s' in press method", key, keys));
                }
            }
        }
    }

    @Override
    public void release(String keys) {
        synchronized (monitor) {
            Set<String> keySet = new LinkedHashSet<>(Arrays.asList(keys.split(" ")));
            for (String key : keySet) {
                int keyCode = KeyCodes.getEventCodeByName(key);
                if (keyCode != -1) {
                    robot.keyRelease(keyCode);
                    robot.delay(getMultipliedReleaseDelay());
                } else {
                    Out.println(String.format("Undefined key '%s' in release method", key));
                }
            }
        }
    }

    @Override
    public void resetDelays() {
        synchronized (monitor) {
            this.pressDelay = PRESS_DELAY;
            this.releaseDelay = RELEASE_DELAY;
        }
    }

    @Override
    public void resetMultiplier() {
        synchronized (monitor) {
            this.multiplier = MULTIPLIER;
        }
    }

    @Override
    public void resetSpeed() {
        synchronized (monitor) {
            resetMultiplier();
        }
    }

    @Override
    public void setDelays(int delay) {
        synchronized (monitor) {
            setPressDelay(delay);
            setReleaseDelay(delay);
        }
    }

    @Override
    public void setMinDelay(int delay) {
        synchronized (monitor) {
            this.minDelay = delay;
        }
    }

    @Override
    public void setMultiplier(float multiplier) {
        synchronized (monitor) {
            if (multiplier < 0) {
                this.multiplier = 0;
            } else {
                this.multiplier = multiplier;
            }
        }
    }

    @Override
    public void setPressDelay(int pressDelay) {
        synchronized (monitor) {
            if (pressDelay < 0) {
                this.pressDelay = 0;
            } else {
                this.pressDelay = pressDelay;
            }
        }
    }

    @Override
    public void setReleaseDelay(int releaseDelay) {
        synchronized (monitor) {
            if (releaseDelay < 0) {
                this.releaseDelay = 0;
            } else {
                this.releaseDelay = releaseDelay;
            }
        }
    }

    @Override
    public void setSpeed(float multiplier) {
        synchronized (monitor) {
            setMultiplier(1f / multiplier);
        }
    }

    @Override
    public void type(String keys) {
        synchronized (monitor) {
            String[] keyList = keys.split(" ");
            for (String key : keyList) {
                int keyCode = KeyCodes.getEventCodeByName(key);
                if (keyCode != -1) {
                    robot.keyPress(keyCode);
                    robot.delay(getMultipliedPressDelay());
                    robot.keyRelease(keyCode);
                    robot.delay(getMultipliedReleaseDelay());
                } else {
                    Out.println(String.format("Undefined key '%s' in type method", key));
                }
            }
        }
    }

    @Override
    public void typeText(String layout, String text) {
        try {
            Typer typer = TyperFactory.createTyperForLayout(this, layout);
            typer.type(text);
        } catch (Exception e) {
            Out.println(e.getMessage());
        }
    }

    private int checkDelay(int delay) {
        if (delay < minDelay) return minDelay;

        return delay;
    }
}
