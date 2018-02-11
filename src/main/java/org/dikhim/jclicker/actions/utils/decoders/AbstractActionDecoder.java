package org.dikhim.jclicker.actions.utils.decoders;

import org.dikhim.jclicker.actions.actions.*;
import org.dikhim.jclicker.actions.utils.KeyCodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.dikhim.jclicker.actions.utils.encoders.UnicodeActionEncoder.*;

public abstract class AbstractActionDecoder {

    private List<Action> decode(String code) {
        List<Action> actions = new ArrayList<>();

        char[] codeArray = code.toCharArray();
        int i = 0;
        while (i < codeArray.length) {
            char c = codeArray[i];
            ActionType actionType = getActionCodes().getKey(Character.toString(c));

            

        }
        return actions;
    }

    protected abstract Action decodeAction(ActionType actionType, char[] params);
}
