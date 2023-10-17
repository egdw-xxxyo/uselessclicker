package org.dikhim.clickauto.jsengine.objects;


import org.dikhim.clickauto.jsengine.actions.Action;
import org.dikhim.clickauto.jsengine.utils.ActionRunner;
import org.dikhim.clickauto.jsengine.utils.decoders.ActionDecoder;
import org.dikhim.clickauto.jsengine.utils.decoders.ActionDecoderFactory;
import org.dikhim.clickauto.util.logger.ClickAutoLog;

import java.util.List;

public class ScriptCombinedObject implements CombinedObject {
    protected MouseObject mouseObject;
    protected KeyboardObject keyboardObject;
    protected SystemObject systemObject;

    public ScriptCombinedObject(MouseObject mouseObject, KeyboardObject keyboardObject, SystemObject systemObject) {
        this.mouseObject = mouseObject;
        this.keyboardObject = keyboardObject;
        this.systemObject = systemObject;
    }

    @Override
    public void run(String encoding, String code) {
        ActionDecoder actionDecoder = ActionDecoderFactory.get(encoding);
        List<Action> actions;
        try {
            actions = actionDecoder.decode(code);
        } catch (IllegalArgumentException e) {
            ClickAutoLog.get().error(e.getMessage());
            return;
        }

        ActionRunner actionRunner = new ActionRunner(keyboardObject,mouseObject,systemObject);
        actionRunner.run(actions);
    }
}
