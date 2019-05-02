package org.dikhim.jclicker.jsengine.objects;

import org.dikhim.jclicker.actions.actions.*;
import org.dikhim.jclicker.actions.utils.ActionRunner;
import org.dikhim.jclicker.actions.utils.decoders.ActionDecoder;
import org.dikhim.jclicker.actions.utils.decoders.ActionDecoderFactory;
import org.dikhim.jclicker.jsengine.clickauto.objects.CombinedObject;
import org.dikhim.jclicker.jsengine.clickauto.objects.KeyboardObject;
import org.dikhim.jclicker.jsengine.clickauto.objects.MouseObject;
import org.dikhim.jclicker.jsengine.clickauto.objects.SystemObject;
import org.dikhim.jclicker.util.Out;

import java.util.List;

public class JsCombinedObject implements CombinedObject {
    private MouseObject mouseObject;
    private KeyboardObject keyboardObject;
    private SystemObject systemObject;

    public JsCombinedObject( MouseObject mouseObject, KeyboardObject keyboardObject, SystemObject systemObject) {
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
            Out.println(e.getMessage());
            return;
        }

        ActionRunner actionRunner = new ActionRunner(keyboardObject,mouseObject,systemObject);
        actionRunner.run(actions);
    }
}
