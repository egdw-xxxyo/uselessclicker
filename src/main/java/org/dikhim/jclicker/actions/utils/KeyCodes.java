package org.dikhim.jclicker.actions.utils;


import java.util.ArrayList;
import java.util.List;

import static java.awt.event.KeyEvent.*;
import static org.jnativehook.keyboard.NativeKeyEvent.*;

/**
 * Created by dikobraz on 25.03.17.
 */


@SuppressWarnings({"SpellCheckingInspection", "unused"})
public class KeyCodes {
    private static List<KeyCode> codes;

    static {
        codes = new ArrayList<>();
        codes.add(new KeyCode("0", VC_0, VK_0, 0));
        codes.add(new KeyCode("1", VC_1, VK_1, 1));
        codes.add(new KeyCode("2", VC_2, VK_2, 2));
        codes.add(new KeyCode("3", VC_3, VK_3, 3));
        codes.add(new KeyCode("4", VC_4, VK_4, 4));
        codes.add(new KeyCode("5", VC_5, VK_5, 5));
        codes.add(new KeyCode("6", VC_6, VK_6, 6));
        codes.add(new KeyCode("7", VC_7, VK_7, 7));
        codes.add(new KeyCode("8", VC_8, VK_8, 8));
        codes.add(new KeyCode("9", VC_9, VK_9, 9));
        codes.add(new KeyCode("A", VC_A, VK_A, 10));
        codes.add(new KeyCode("B", VC_B, VK_B, 11));
        codes.add(new KeyCode("C", VC_C, VK_C, 12));
        codes.add(new KeyCode("D", VC_D, VK_D, 13));
        codes.add(new KeyCode("E", VC_E, VK_E, 14));
        codes.add(new KeyCode("F", VC_F, VK_F, 15));
        codes.add(new KeyCode("G", VC_G, VK_G, 16));
        codes.add(new KeyCode("H", VC_H, VK_H, 17));
        codes.add(new KeyCode("J", VC_J, VK_J, 18));
        codes.add(new KeyCode("I", VC_I, VK_I, 19));
        codes.add(new KeyCode("K", VC_K, VK_K, 20));
        codes.add(new KeyCode("L", VC_L, VK_L, 21));
        codes.add(new KeyCode("M", VC_M, VK_M, 22));
        codes.add(new KeyCode("N", VC_N, VK_N, 23));
        codes.add(new KeyCode("O", VC_O, VK_O, 24));
        codes.add(new KeyCode("P", VC_P, VK_P, 25));
        codes.add(new KeyCode("Q", VC_Q, VK_Q, 26));
        codes.add(new KeyCode("R", VC_R, VK_R, 27));
        codes.add(new KeyCode("S", VC_S, VK_S, 28));
        codes.add(new KeyCode("T", VC_T, VK_T, 29));
        codes.add(new KeyCode("U", VC_U, VK_U, 30));
        codes.add(new KeyCode("V", VC_V, VK_V, 31));
        codes.add(new KeyCode("W", VC_W, VK_W, 32));
        codes.add(new KeyCode("X", VC_X, VK_X, 33));
        codes.add(new KeyCode("Y", VC_Y, VK_Y, 34));
        codes.add(new KeyCode("Z", VC_Z, VK_Z, 35));

        codes.add(new KeyCode("ENTER", VC_ENTER, VK_ENTER, 36));
        codes.add(new KeyCode("BACKSPACE", VC_BACKSPACE, VK_BACK_SPACE, 37));
        codes.add(new KeyCode("TAB", VC_TAB, VK_TAB, 38));

        //return VK_CANCEL));
        //return VK_CLEAR));

        codes.add(new KeyCode("SHIFT", VC_SHIFT_L, VK_SHIFT, 39));
        codes.add(new KeyCode("SHIFT", VC_SHIFT_R, VK_SHIFT, 40));
        codes.add(new KeyCode("CONTROL", VC_CONTROL_L, VK_CONTROL, 41));
        codes.add(new KeyCode("CONTROL", VC_CONTROL_R, VK_CONTROL, 42));
        codes.add(new KeyCode("ALT", VC_ALT_L, VK_ALT, 43));
        codes.add(new KeyCode("ALT", VC_ALT_R, VK_ALT, 44));
        codes.add(new KeyCode("PAUSE", VC_PAUSE, VK_PAUSE, 45));
        codes.add(new KeyCode("CAPS_LOCK", VC_CAPS_LOCK, VK_CAPS_LOCK, 46));
        codes.add(new KeyCode("ESCAPE", VC_ESCAPE, VK_ESCAPE, 47));
        codes.add(new KeyCode("SPACE", VC_SPACE, VK_SPACE, 48));
        codes.add(new KeyCode("PAGE_UP", VC_PAGE_UP, VK_PAGE_UP, 49));
        codes.add(new KeyCode("PAGE_DOWN", VC_PAGE_DOWN, VK_PAGE_DOWN, 50));
        codes.add(new KeyCode("END", VC_END, VK_END, 51));
        codes.add(new KeyCode("HOME", VC_HOME, VK_HOME, 52));

        codes.add(new KeyCode("LEFT", VC_LEFT, VK_LEFT, 53));
        codes.add(new KeyCode("RIGHT", VC_RIGHT, VK_RIGHT, 54));
        codes.add(new KeyCode("UP", VC_UP, VK_UP, 55));
        codes.add(new KeyCode("DOWN", VC_DOWN, VK_DOWN, 56));
        codes.add(new KeyCode(",", VC_COMMA, VK_COMMA, 57));
        codes.add(new KeyCode("-", VC_MINUS, VK_MINUS, 58));
        codes.add(new KeyCode(".", VC_PERIOD, VK_PERIOD, 59));
        codes.add(new KeyCode("/", VC_SLASH, VK_SLASH, 60));

        codes.add(new KeyCode(";", VC_SEMICOLON, VK_SEMICOLON, 61));
        codes.add(new KeyCode("=", VC_EQUALS, VK_EQUALS, 62));

        codes.add(new KeyCode("[", VC_OPEN_BRACKET, VK_OPEN_BRACKET, 63));
        codes.add(new KeyCode("]", VC_CLOSE_BRACKET, VK_CLOSE_BRACKET, 64));
        codes.add(new KeyCode("\\", VC_BACK_SLASH, VK_BACK_SLASH, 65));

        // Numpad
        codes.add(new KeyCode("NUM_LOCK", VC_NUM_LOCK, VK_NUM_LOCK, 66));
        codes.add(new KeyCode("NUMPAD0", VC_KP_0, VK_NUMPAD0, 67));
        codes.add(new KeyCode("NUMPAD1", VC_KP_1, VK_NUMPAD1, 68));
        codes.add(new KeyCode("NUMPAD2", VC_KP_2, VK_NUMPAD2, 69));
        codes.add(new KeyCode("NUMPAD3", VC_KP_3, VK_NUMPAD3, 70));
        codes.add(new KeyCode("NUMPAD4", VC_KP_4, VK_NUMPAD4, 71));
        codes.add(new KeyCode("NUMPAD5", VC_KP_5, VK_NUMPAD5, 72));
        codes.add(new KeyCode("NUMPAD6", VC_KP_6, VK_NUMPAD6, 73));
        codes.add(new KeyCode("NUMPAD7", VC_KP_7, VK_NUMPAD7, 74));
        codes.add(new KeyCode("NUMPAD8", VC_KP_8, VK_NUMPAD8, 75));
        codes.add(new KeyCode("NUMPAD9", VC_KP_9, VK_NUMPAD9, 76));

        codes.add(new KeyCode("MULTIPLY", VC_KP_MULTIPLY, VK_MULTIPLY, 77));
        codes.add(new KeyCode("DIVIDE", VC_KP_DIVIDE, VK_DIVIDE, 78));

        codes.add(new KeyCode("F1", VC_F1, VK_F1, 79));
        codes.add(new KeyCode("F2", VC_F2, VK_F2, 80));
        codes.add(new KeyCode("F3", VC_F3, VK_F3, 81));
        codes.add(new KeyCode("F4", VC_F4, VK_F4, 82));
        codes.add(new KeyCode("F5", VC_F5, VK_F5, 83));
        codes.add(new KeyCode("F6", VC_F6, VK_F6, 84));
        codes.add(new KeyCode("F7", VC_F7, VK_F7, 85));
        codes.add(new KeyCode("F8", VC_F8, VK_F8, 86));
        codes.add(new KeyCode("F9", VC_F9, VK_F9, 87));
        codes.add(new KeyCode("F10", VC_F10, VK_F10, 88));
        codes.add(new KeyCode("F11", VC_F11, VK_F11, 89));
        codes.add(new KeyCode("F12", VC_F12, VK_F12, 90));
        codes.add(new KeyCode("F13", VC_F13, VK_F13, 91));
        codes.add(new KeyCode("F14", VC_F14, VK_F14, 92));
        codes.add(new KeyCode("F15", VC_F15, VK_F15, 93));
        codes.add(new KeyCode("F16", VC_F16, VK_F16, 94));
        codes.add(new KeyCode("F17", VC_F17, VK_F17, 95));
        codes.add(new KeyCode("F18", VC_F18, VK_F18, 96));
        codes.add(new KeyCode("F19", VC_F19, VK_F19, 97));
        codes.add(new KeyCode("F20", VC_F20, VK_F20, 98));
        codes.add(new KeyCode("F21", VC_F21, VK_F21, 99));
        codes.add(new KeyCode("F22", VC_F22, VK_F22, 100));
        codes.add(new KeyCode("F23", VC_F23, VK_F23, 101));
        codes.add(new KeyCode("F24", VC_F24, VK_F24, 102));


        codes.add(new KeyCode("PRINTSCREEN", VC_PRINTSCREEN, VK_PRINTSCREEN, 103));
        codes.add(new KeyCode("INSERT", VC_INSERT, VK_INSERT, 104));
        //return VK_HELP));
        //return VK_META));
        codes.add(new KeyCode("`", VC_BACKQUOTE, VK_BACK_QUOTE, 105));
        codes.add(new KeyCode("'", VC_QUOTE, VK_QUOTE, 106));
        codes.add(new KeyCode("DELETE", VC_DELETE, VK_DELETE, 107));
    }
    private static KeyCode getByName(String name){
        for(KeyCode kc:codes){
            if(kc.getName().equals(name))return kc;
        }
        return null;
    }
    private static KeyCode getByNativeCode(int code){
        for(KeyCode kc:codes){
            if(kc.getNativeCode() ==code)return kc;
        }
        return null;
    }
    private static KeyCode getByEventCode(int code){
        for(KeyCode kc:codes){
            if(kc.getEventCode() ==code)return kc;
        }
        return null;
    }

    private static KeyCode getByUselessCode(int code){
        for(KeyCode kc:codes){
            if(kc.getUselessCode() ==code)return kc;
        }
        return null;
    }

    public static String getNameByNativeCode(int code){
    	KeyCode k = getByNativeCode(code);
    	if(k==null)return "";
    	return k.getName();
    }
    public static String getNameByEventCode(int code){
        KeyCode k = getByEventCode(code);
        if(k==null)return "";
    	return k.getName();
    }
    public static int getNativeCodeByName(String name){
        KeyCode k = getByName(name);
        if(k==null)return -1;
        return k.getNativeCode();
    }
    public static int getNativeCodeByEventCode(int code){
        KeyCode k = getByEventCode(code);
        if(k==null)return -1;
        return k.getNativeCode();
    }
    public static int getEventCodeByNativeCode(int code){
        KeyCode k = getByNativeCode(code);
        if(k==null)return -1;
        return k.getEventCode();
    }
    public static int getEventCodeByName(String name){
        KeyCode k = getByName(name);
        if(k==null)return -1;
        return k.getEventCode();
    }

    public static int getUslessCodeByName(String name) {
        KeyCode k = getByName(name);
        if(k==null)return -1;
        return k.getUselessCode();
    }

    public static int getEventCodeByUselessCode(int code){
        KeyCode k = getByUselessCode(code);
        if(k==null)return -1;
        return k.getEventCode();
    }

    public static String getNameByUselessCode(int code){
        KeyCode k = getByUselessCode(code);
        if(k==null)return "";
        return k.getName();
    }

    public static List<KeyCode> getCodes() {
        return codes;
    }
}

