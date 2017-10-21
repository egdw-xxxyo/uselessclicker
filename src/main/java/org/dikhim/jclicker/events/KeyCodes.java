package org.dikhim.jclicker.events;


import java.util.ArrayList;
import java.util.List;

import static java.awt.event.KeyEvent.*;
import static org.jnativehook.keyboard.NativeKeyEvent.*;

/**
 * Created by dikobraz on 25.03.17.
 */


public class KeyCodes {
    private static List<KeyCode> codes;

    static {
        codes = new ArrayList<>();
        codes.add(new KeyCode("1", VC_1, VK_1));
        codes.add(new KeyCode("2", VC_2, VK_2));
        codes.add(new KeyCode("3", VC_3, VK_3));
        codes.add(new KeyCode("4", VC_4, VK_4));
        codes.add(new KeyCode("5", VC_5, VK_5));
        codes.add(new KeyCode("6", VC_6, VK_6));
        codes.add(new KeyCode("7", VC_7, VK_7));
        codes.add(new KeyCode("8", VC_8, VK_8));
        codes.add(new KeyCode("9", VC_9, VK_9));
        codes.add(new KeyCode("0", VC_0, VK_0));
        codes.add(new KeyCode("A", VC_A, VK_A));
        codes.add(new KeyCode("B", VC_B, VK_B));
        codes.add(new KeyCode("C", VC_C, VK_C));
        codes.add(new KeyCode("D", VC_D, VK_D));
        codes.add(new KeyCode("E", VC_E, VK_E));
        codes.add(new KeyCode("F", VC_F, VK_F));
        codes.add(new KeyCode("G", VC_G, VK_G));
        codes.add(new KeyCode("H", VC_H, VK_H));
        codes.add(new KeyCode("J", VC_J, VK_J));
        codes.add(new KeyCode("I", VC_I, VK_I));
        codes.add(new KeyCode("K", VC_K, VK_K));
        codes.add(new KeyCode("L", VC_L, VK_L));
        codes.add(new KeyCode("M", VC_M, VK_M));
        codes.add(new KeyCode("N", VC_N, VK_N));
        codes.add(new KeyCode("O", VC_O, VK_O));
        codes.add(new KeyCode("P", VC_P, VK_P));
        codes.add(new KeyCode("Q", VC_Q, VK_Q));
        codes.add(new KeyCode("R", VC_R, VK_R));
        codes.add(new KeyCode("S", VC_S, VK_S));
        codes.add(new KeyCode("T", VC_T, VK_T));
        codes.add(new KeyCode("U", VC_U, VK_U));
        codes.add(new KeyCode("V", VC_V, VK_V));
        codes.add(new KeyCode("W", VC_W, VK_W));
        codes.add(new KeyCode("X", VC_X, VK_X));
        codes.add(new KeyCode("Y", VC_Y, VK_Y));
        codes.add(new KeyCode("Z", VC_Z, VK_Z));

        codes.add(new KeyCode("ENTER", VC_ENTER, VK_ENTER));
        codes.add(new KeyCode("BACKSPACE", VC_BACKSPACE, VK_BACK_SPACE));
        codes.add(new KeyCode("TAB", VC_TAB, VK_TAB));

        //return VK_CANCEL));
        //return VK_CLEAR));

        codes.add(new KeyCode("SHIFT", VC_SHIFT_L, VK_SHIFT));
        codes.add(new KeyCode("SHIFT", VC_SHIFT_R, VK_SHIFT));
        codes.add(new KeyCode("CONTROL", VC_CONTROL_L, VK_CONTROL));
        codes.add(new KeyCode("CONTROL", VC_CONTROL_R, VK_CONTROL));
        codes.add(new KeyCode("ALT", VC_ALT_L, VK_ALT));
        codes.add(new KeyCode("ALT", VC_ALT_R, VK_ALT));
        codes.add(new KeyCode("PAUSE", VC_PAUSE, VK_PAUSE));
        codes.add(new KeyCode("CAPS_LOCK", VC_CAPS_LOCK, VK_CAPS_LOCK));
        codes.add(new KeyCode("ESCAPE", VC_ESCAPE, VK_ESCAPE));
        codes.add(new KeyCode("SPACE", VC_SPACE, VK_SPACE));
        codes.add(new KeyCode("PAGE_UP", VC_PAGE_UP, VK_PAGE_UP));
        codes.add(new KeyCode("PAGE_DOWN", VC_PAGE_DOWN, VK_PAGE_DOWN));
        codes.add(new KeyCode("END", VC_END, VK_END));
        codes.add(new KeyCode("HOME", VC_HOME, VK_HOME));

        codes.add(new KeyCode("LEFT", VC_LEFT, VK_LEFT));
        codes.add(new KeyCode("RIGHT", VC_RIGHT, VK_RIGHT));
        codes.add(new KeyCode("UP", VC_UP, VK_UP));
        codes.add(new KeyCode("DOWN", VC_DOWN, VK_DOWN));
        codes.add(new KeyCode(",", VC_COMMA, VK_COMMA));
        codes.add(new KeyCode("-", VC_MINUS, VK_MINUS));
        codes.add(new KeyCode(".", VC_PERIOD, VK_PERIOD));
        codes.add(new KeyCode("/", VC_SLASH, VK_SLASH));

        codes.add(new KeyCode(";", VC_SEMICOLON, VK_SEMICOLON));
        codes.add(new KeyCode("=", VC_EQUALS, VK_EQUALS));

        codes.add(new KeyCode("[", VC_OPEN_BRACKET, VK_OPEN_BRACKET));
        codes.add(new KeyCode("]", VC_CLOSE_BRACKET, VK_CLOSE_BRACKET));
        codes.add(new KeyCode("\\", VC_BACK_SLASH, VK_BACK_SLASH));
        /**
         * Numpad
         */
        codes.add(new KeyCode("NUM_LOCK", VC_NUM_LOCK, VK_NUM_LOCK));
        codes.add(new KeyCode("NUMPAD0", VC_KP_0, VK_NUMPAD0));
        codes.add(new KeyCode("NUMPAD1", VC_KP_1, VK_NUMPAD1));
        codes.add(new KeyCode("NUMPAD2", VC_KP_2, VK_NUMPAD2));
        codes.add(new KeyCode("NUMPAD3", VC_KP_3, VK_NUMPAD3));
        codes.add(new KeyCode("NUMPAD4", VC_KP_4, VK_NUMPAD4));
        codes.add(new KeyCode("NUMPAD5", VC_KP_5, VK_NUMPAD5));
        codes.add(new KeyCode("NUMPAD6", VC_KP_6, VK_NUMPAD6));
        codes.add(new KeyCode("NUMPAD7", VC_KP_7, VK_NUMPAD7));
        codes.add(new KeyCode("NUMPAD8", VC_KP_8, VK_NUMPAD8));
        codes.add(new KeyCode("NUMPAD9", VC_KP_9, VK_NUMPAD9));

        codes.add(new KeyCode("MULTIPLY", VC_KP_MULTIPLY, VK_MULTIPLY));
        codes.add(new KeyCode("DIVIDE", VC_KP_DIVIDE, VK_DIVIDE));

        codes.add(new KeyCode("F1", VC_F1, VK_F1));
        codes.add(new KeyCode("F2", VC_F2, VK_F2));
        codes.add(new KeyCode("F3", VC_F3, VK_F3));
        codes.add(new KeyCode("F4", VC_F4, VK_F4));
        codes.add(new KeyCode("F5", VC_F5, VK_F5));
        codes.add(new KeyCode("F6", VC_F6, VK_F6));
        codes.add(new KeyCode("F7", VC_F7, VK_F7));
        codes.add(new KeyCode("F8", VC_F8, VK_F8));
        codes.add(new KeyCode("F9", VC_F9, VK_F9));
        codes.add(new KeyCode("F10", VC_F10, VK_F10));
        codes.add(new KeyCode("F11", VC_F11, VK_F11));
        codes.add(new KeyCode("F12", VC_F12, VK_F12));
        codes.add(new KeyCode("F13", VC_F13, VK_F13));
        codes.add(new KeyCode("F14", VC_F14, VK_F14));
        codes.add(new KeyCode("F15", VC_F15, VK_F15));
        codes.add(new KeyCode("F16", VC_F16, VK_F16));
        codes.add(new KeyCode("F17", VC_F17, VK_F17));
        codes.add(new KeyCode("F18", VC_F18, VK_F18));
        codes.add(new KeyCode("F19", VC_F19, VK_F19));
        codes.add(new KeyCode("F20", VC_F20, VK_F20));
        codes.add(new KeyCode("F21", VC_F21, VK_F21));
        codes.add(new KeyCode("F22", VC_F22, VK_F22));
        codes.add(new KeyCode("F23", VC_F23, VK_F23));
        codes.add(new KeyCode("F24", VC_F24, VK_F24));


        codes.add(new KeyCode("PRINTSCREEN", VC_PRINTSCREEN, VK_PRINTSCREEN));
        codes.add(new KeyCode("INSERT", VC_INSERT, VK_INSERT));
        //return VK_HELP));
        //return VK_META));
        codes.add(new KeyCode("`", VC_BACKQUOTE, VK_BACK_QUOTE));
        codes.add(new KeyCode("'", VC_QUOTE, VK_QUOTE));
        codes.add(new KeyCode("DELETE", VC_DELETE, VK_DELETE));
    }
    private static KeyCode getByName(String name){
        for(KeyCode kc:codes){
            if(kc.name.equals(name))return kc;
        }
        return null;
    }
    private static KeyCode getByNativeCode(int code){
        for(KeyCode kc:codes){
            if(kc.nativeCode==code)return kc;
        }
        return null;
    }
    private static KeyCode getByEventCode(int code){
        for(KeyCode kc:codes){
            if(kc.eventCode==code)return kc;
        }
        return null;
    }

    public static String getNameByNativeCode(int code){
    	KeyCode k = getByNativeCode(code);
    	if(k==null)return "";
    	return k.name;
    }
    public static String getNameByEventCode(int code){
        KeyCode k = getByEventCode(code);
        if(k==null)return "";
    	return k.name;
    }
    public static int getNativeCodeByName(String name){
        KeyCode k = getByName(name);
        if(k==null)return -1;
        return k.nativeCode;
    }
    public static int getNativeCodeByEventCode(int code){
        KeyCode k = getByEventCode(code);
        if(k==null)return -1;
        return k.nativeCode;
    }
    public static int getEventCodeByNativeCode(int code){
        KeyCode k = getByNativeCode(code);
        if(k==null)return -1;
        return k.eventCode;
    }
    public static int getEventCodeByName(String name){
        KeyCode k = getByName(name);
        if(k==null)return -1;
        return k.eventCode;
    }

    public static String[] getKeyNames(String shortcutString){
        return shortcutString.split("-");
    }
    private static class KeyCode {
        private String name;
        private int nativeCode;
        private int eventCode;

        public KeyCode(String name, int nativeCode, int eventCode) {
            this.name = name;
            this.nativeCode = nativeCode;
            this.eventCode = eventCode;
        }
    }
}

