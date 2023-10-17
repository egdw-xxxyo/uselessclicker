package org.dikhim.clickauto.jsengine.utils;

import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;


public class MouseCodes {
    private static List<MouseCode> codes;

    static {
        codes = new ArrayList<>();
        codes.add(new MouseCode("LEFT", InputEvent.getMaskForButton(1)));
        codes.add(new MouseCode("MIDDLE", InputEvent.getMaskForButton(2)));
        codes.add(new MouseCode("RIGHT", InputEvent.getMaskForButton(3)));
    }

    private static MouseCode getByName(String name) {
        for (MouseCode kc : codes) {
            if (kc.name.equals(name)) return kc;
        }
        return null;
    }


    private static MouseCode getByEventCode(int code) {
        for (MouseCode kc : codes) {
            if (kc.eventCode == code) return kc;
        }
        return null;
    }

    public static String getNameByEventCode(int code) {
        MouseCode mc = getByEventCode(code);
        if(mc ==null)return "";
        return mc.getName();
    }

    public static int getEventCodeByName(String name) {
        MouseCode mc = getByName(name);
        if(mc==null)return -1;
        return mc.getEventCode();
    }

    private static class MouseCode {
        private String name;
        private int eventCode;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getEventCode() {
            return eventCode;
        }

        public void setEventCode(int eventCode) {
            this.eventCode = eventCode;
        }

        public MouseCode(String name, int eventCode) {
            this.name = name;
            this.eventCode = eventCode;
        }
    }
}
