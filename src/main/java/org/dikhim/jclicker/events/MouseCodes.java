package org.dikhim.jclicker.events;

import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;


import static org.jnativehook.mouse.NativeMouseEvent.*;
public class MouseCodes {
	private static List<MouseCode> codes;

    static {
    	codes = new ArrayList<>();
    	codes.add(new MouseCode("LEFT", BUTTON1, InputEvent.BUTTON1_MASK));    
    	codes.add(new MouseCode("MIDDLE", BUTTON2, InputEvent.BUTTON2_MASK));
    	codes.add(new MouseCode("RIGHT", BUTTON3, InputEvent.BUTTON3_MASK));
    }
    
    
    
    private static MouseCode getByName(String name){
        for(MouseCode kc:codes){
            if(kc.name.equals(name))return kc;
        }
        return null;
    }
    private static MouseCode getByNativeCode(int code){
        for(MouseCode kc:codes){
            if(kc.nativeCode==code)return kc;
        }
        return null;
    }
    private static MouseCode getByEventCode(int code){
        for(MouseCode kc:codes){
            if(kc.eventCode==code)return kc;
        }
        return null;
    }
    
    public static String getNameByNativeCode(int code){
        return getByNativeCode(code).name;
    }
    public static String getNameByEventCode(int code){
        return getByEventCode(code).name;
    }
    public static int getNativeCodeByName(String name){
        return getByName(name).nativeCode;
    }
    public static int getNativeCodeByEventCode(int code){
        return getByEventCode(code).nativeCode;
    }
    public static int getEventCodeByNativeCode(int code){
        return getByNativeCode(code).eventCode;
    }
    public static int getEventCodeByName(String name){
        return getByName(name).eventCode;
    }
    
    private static class MouseCode {
        private String name;
        private int nativeCode;
        private int eventCode;

        public MouseCode(String name, int nativeCode, int eventCode) {
            this.name = name;
            this.nativeCode = nativeCode;
            this.eventCode = eventCode;
        }
    }
}
