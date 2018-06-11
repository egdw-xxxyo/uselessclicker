package org.dikhim.jclicker.actions.utils.decoders;

import org.dikhim.jclicker.actions.actions.Action;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class NaturalActionDecoderTest {

    private static String data ="MOUSE_MOVE_TO:853 450 DELAY:8 MOUSE_MOVE_TO:853 450 DELAY:0";
    @Test
    public void decode() {
        NaturalActionDecoder actionDecoder = new NaturalActionDecoder();
        List<Action> actions = actionDecoder.decode(data);
        assertNotEquals(actions.size(),0);
    }
}