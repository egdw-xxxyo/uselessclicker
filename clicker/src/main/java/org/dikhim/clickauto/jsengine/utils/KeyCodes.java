package org.dikhim.clickauto.jsengine.utils;


import java.util.ArrayList;
import java.util.List;

import static java.awt.event.KeyEvent.*;

/**
 * Created by dikobraz on 25.03.17.
 */


@SuppressWarnings({"SpellCheckingInspection", "unused"})
public class KeyCodes {
    private static List<KeyCode> codes;

    static {
        codes = new ArrayList<>();
        codes.add(new KeyCode("0", VK_0, 0));
        codes.add(new KeyCode("1", VK_1, 1));
        codes.add(new KeyCode("2", VK_2, 2));
        codes.add(new KeyCode("3", VK_3, 3));
        codes.add(new KeyCode("4", VK_4, 4));
        codes.add(new KeyCode("5", VK_5, 5));
        codes.add(new KeyCode("6", VK_6, 6));
        codes.add(new KeyCode("7", VK_7, 7));
        codes.add(new KeyCode("8", VK_8, 8));
        codes.add(new KeyCode("9", VK_9, 9));
        codes.add(new KeyCode("A", VK_A, 10));
        codes.add(new KeyCode("B", VK_B, 11));
        codes.add(new KeyCode("C", VK_C, 12));
        codes.add(new KeyCode("D", VK_D, 13));
        codes.add(new KeyCode("E", VK_E, 14));
        codes.add(new KeyCode("F", VK_F, 15));
        codes.add(new KeyCode("G", VK_G, 16));
        codes.add(new KeyCode("H", VK_H, 17));
        codes.add(new KeyCode("J", VK_J, 18));
        codes.add(new KeyCode("I", VK_I, 19));
        codes.add(new KeyCode("K", VK_K, 20));
        codes.add(new KeyCode("L", VK_L, 21));
        codes.add(new KeyCode("M", VK_M, 22));
        codes.add(new KeyCode("N", VK_N, 23));
        codes.add(new KeyCode("O", VK_O, 24));
        codes.add(new KeyCode("P", VK_P, 25));
        codes.add(new KeyCode("Q", VK_Q, 26));
        codes.add(new KeyCode("R", VK_R, 27));
        codes.add(new KeyCode("S", VK_S, 28));
        codes.add(new KeyCode("T", VK_T, 29));
        codes.add(new KeyCode("U", VK_U, 30));
        codes.add(new KeyCode("V", VK_V, 31));
        codes.add(new KeyCode("W", VK_W, 32));
        codes.add(new KeyCode("X", VK_X, 33));
        codes.add(new KeyCode("Y", VK_Y, 34));
        codes.add(new KeyCode("Z", VK_Z, 35));

        codes.add(new KeyCode("ENTER", VK_ENTER, 36));
        codes.add(new KeyCode("BACKSPACE", VK_BACK_SPACE, 37));
        codes.add(new KeyCode("TAB", VK_TAB, 38));

        //return VK_CANCEL));
        //return VK_CLEAR));

        codes.add(new KeyCode("SHIFT", VK_SHIFT, 39));
        codes.add(new KeyCode("SHIFT", VK_SHIFT, 40));
        codes.add(new KeyCode("CONTROL", VK_CONTROL, 41));
        codes.add(new KeyCode("CONTROL", VK_CONTROL, 42));
        codes.add(new KeyCode("ALT", VK_ALT, 43));
        codes.add(new KeyCode("ALT", VK_ALT, 44));
        codes.add(new KeyCode("PAUSE", VK_PAUSE, 45));
        codes.add(new KeyCode("CAPS_LOCK", VK_CAPS_LOCK, 46));
        codes.add(new KeyCode("ESCAPE", VK_ESCAPE, 47));
        codes.add(new KeyCode("SPACE", VK_SPACE, 48));
        codes.add(new KeyCode("PAGE_UP", VK_PAGE_UP, 49));
        codes.add(new KeyCode("PAGE_DOWN", VK_PAGE_DOWN, 50));
        codes.add(new KeyCode("END", VK_END, 51));
        codes.add(new KeyCode("HOME", VK_HOME, 52));

        codes.add(new KeyCode("LEFT", VK_LEFT, 53));
        codes.add(new KeyCode("RIGHT", VK_RIGHT, 54));
        codes.add(new KeyCode("UP", VK_UP, 55));
        codes.add(new KeyCode("DOWN", VK_DOWN, 56));
        codes.add(new KeyCode(",", VK_COMMA, 57));
        codes.add(new KeyCode("-", VK_MINUS, 58));
        codes.add(new KeyCode(".", VK_PERIOD, 59));
        codes.add(new KeyCode("/", VK_SLASH, 60));

        codes.add(new KeyCode(";", VK_SEMICOLON, 61));
        codes.add(new KeyCode("=", VK_EQUALS, 62));

        codes.add(new KeyCode("[", VK_OPEN_BRACKET, 63));
        codes.add(new KeyCode("]", VK_CLOSE_BRACKET, 64));
        codes.add(new KeyCode("\\", VK_BACK_SLASH, 65));

        // Numpad
        codes.add(new KeyCode("NUM_LOCK", VK_NUM_LOCK, 66));
        codes.add(new KeyCode("NUMPAD0", VK_NUMPAD0, 67));
        codes.add(new KeyCode("NUMPAD1", VK_NUMPAD1, 68));
        codes.add(new KeyCode("NUMPAD2", VK_NUMPAD2, 69));
        codes.add(new KeyCode("NUMPAD3", VK_NUMPAD3, 70));
        codes.add(new KeyCode("NUMPAD4", VK_NUMPAD4, 71));
        codes.add(new KeyCode("NUMPAD5", VK_NUMPAD5, 72));
        codes.add(new KeyCode("NUMPAD6", VK_NUMPAD6, 73));
        codes.add(new KeyCode("NUMPAD7", VK_NUMPAD7, 74));
        codes.add(new KeyCode("NUMPAD8", VK_NUMPAD8, 75));
        codes.add(new KeyCode("NUMPAD9", VK_NUMPAD9, 76));

        codes.add(new KeyCode("MULTIPLY", VK_MULTIPLY, 77));
        codes.add(new KeyCode("DIVIDE", VK_DIVIDE, 78));

        codes.add(new KeyCode("F1", VK_F1, 79));
        codes.add(new KeyCode("F2", VK_F2, 80));
        codes.add(new KeyCode("F3", VK_F3, 81));
        codes.add(new KeyCode("F4", VK_F4, 82));
        codes.add(new KeyCode("F5", VK_F5, 83));
        codes.add(new KeyCode("F6", VK_F6, 84));
        codes.add(new KeyCode("F7", VK_F7, 85));
        codes.add(new KeyCode("F8", VK_F8, 86));
        codes.add(new KeyCode("F9", VK_F9, 87));
        codes.add(new KeyCode("F10", VK_F10, 88));
        codes.add(new KeyCode("F11", VK_F11, 89));
        codes.add(new KeyCode("F12", VK_F12, 90));
        codes.add(new KeyCode("F13", VK_F13, 91));
        codes.add(new KeyCode("F14", VK_F14, 92));
        codes.add(new KeyCode("F15", VK_F15, 93));
        codes.add(new KeyCode("F16", VK_F16, 94));
        codes.add(new KeyCode("F17", VK_F17, 95));
        codes.add(new KeyCode("F18", VK_F18, 96));
        codes.add(new KeyCode("F19", VK_F19, 97));
        codes.add(new KeyCode("F20", VK_F20, 98));
        codes.add(new KeyCode("F21", VK_F21, 99));
        codes.add(new KeyCode("F22", VK_F22, 100));
        codes.add(new KeyCode("F23", VK_F23, 101));
        codes.add(new KeyCode("F24", VK_F24, 102));


        codes.add(new KeyCode("PRINTSCREEN", VK_PRINTSCREEN, 103));
        codes.add(new KeyCode("INSERT", VK_INSERT, 104));
        //return VK_HELP));
        //return VK_META));
        codes.add(new KeyCode("`", VK_BACK_QUOTE, 105));
        codes.add(new KeyCode("'", VK_QUOTE, 106));
        codes.add(new KeyCode("DELETE", VK_DELETE, 107));
        codes.add(new KeyCode("ALT_GR", VK_ALT, 108));


    }
    private static KeyCode getByName(String name){
        for(KeyCode kc:codes){
            if(kc.getName().equals(name))return kc;
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

    public static String getNameByEventCode(int code){
        KeyCode k = getByEventCode(code);
        if(k==null)return "";
    	return k.getName();
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

