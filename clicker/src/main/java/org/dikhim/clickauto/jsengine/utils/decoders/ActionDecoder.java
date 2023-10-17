package org.dikhim.clickauto.jsengine.utils.decoders;


import org.dikhim.clickauto.jsengine.actions.Action;

import java.util.List;

public interface ActionDecoder {
    List<Action> decode(String code);
}
