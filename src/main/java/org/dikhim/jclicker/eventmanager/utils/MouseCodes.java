package org.dikhim.jclicker.eventmanager.utils;

import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

import static org.jnativehook.mouse.NativeMouseEvent.*;

public class MouseCodes {
    private static List<MouseCode> codes;

    static {
        codes = new ArrayList<>();
        codes.add(new MouseCode("LEFT", BUTTON1, InputEvent.getMaskForButton(1)));
        codes.add(new MouseCode("MIDDLE", BUTTON2, InputEvent.getMaskForButton(2)));
        codes.add(new MouseCode("RIGHT", BUTTON3, InputEvent.getMaskForButton(3)));
    }


    private static MouseCode getByName(String name) {
        for (MouseCode kc : codes) {
            if (kc.name.equals(name)) return kc;
        }
        return null;
    }

    private static MouseCode getByNativeCode(int code) {
        for (MouseCode kc : codes) {
            if (kc.nativeCode == code) return kc;
        }
        return null;
    }

    private static MouseCode getByEventCode(int code) {
        for (MouseCode kc : codes) {
            if (kc.eventCode == code) return kc;
        }
        return null;
    }

    public static String getNameByNativeCode(int code) {
        MouseCode mc = getByNativeCode(code);
        if (mc == null) return "";
        return mc.getName();
    }

    public static String getNameByEventCode(int code) {
        MouseCode mc = getByEventCode(code);
        if(mc ==null)return "";
        return mc.getName();
    }

    public static int getNativeCodeByName(String name) {
        MouseCode mc =  getByName(name);
        if(mc==null)return -1;
        return mc.getNativeCode();
    }

    public static int getNativeCodeByEventCode(int code) {
        MouseCode mc = getByEventCode(code);
        if(mc==null)return -1;
        return mc.getNativeCode();
    }

    public static int getEventCodeByNativeCode(int code) {
        MouseCode mc = getByNativeCode(code);
        if(mc==null)return -1;
        return mc.getNativeCode();
    }

    public static int getEventCodeByName(String name) {
        MouseCode mc = getByName(name);
        if(mc==null)return -1;
        return mc.getEventCode();
    }

    private static class MouseCode {
        private String name;
        private int nativeCode;
        private int eventCode;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNativeCode() {
            return nativeCode;
        }

        public void setNativeCode(int nativeCode) {
            this.nativeCode = nativeCode;
        }

        public int getEventCode() {
            return eventCode;
        }

        public void setEventCode(int eventCode) {
            this.eventCode = eventCode;
        }

        public MouseCode(String name, int nativeCode, int eventCode) {
            this.name = name;
            this.nativeCode = nativeCode;
            this.eventCode = eventCode;
        }
    }
}
