package org.dikhim.jclicker.actions.managers;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.Assert.*;

public class KeyEventsManagerTest {
    private static KeyEventsManager keyEventsManager;
    private static Logger LOGGER = LoggerFactory.getLogger(KeyEventsManagerTest.class);

    @BeforeClass
    public static void init() {
        keyEventsManager = KeyEventsManager.getInstance();
    }

    @Test
    public void checkCapsLock() {
        boolean isOn = keyEventsManager.isCapsLockLocked();
        LOGGER.debug("caps lock is on:{}", isOn);
        assertTrue(true);
    }

    @Test
    public void checkNumLock() {
        boolean isOn = keyEventsManager.isNumLockLocked();
        LOGGER.debug("num lock is on:{}", isOn);
        assertTrue(true);
    }

    @Test
    public void checkScrollLock() {
        boolean isOn = keyEventsManager.isScrollLockLocked();
        LOGGER.debug("scroll lock is on:{}", isOn);
        assertTrue(true);
    }


}