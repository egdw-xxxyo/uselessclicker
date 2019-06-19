package org.dikhim.jclicker.actions.utils.decoders;

import org.dikhim.jclicker.actions.actions.Action;

import java.util.List;


public interface ActionDecoder {
    List<Action> decode(String code);
}
