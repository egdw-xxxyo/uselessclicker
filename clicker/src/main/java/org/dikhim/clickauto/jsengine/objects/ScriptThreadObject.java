package org.dikhim.clickauto.jsengine.objects;

public class ScriptThreadObject implements ThreadObject {
    @Override
    public boolean interrupted() {
        return Thread.interrupted();    
    }
}
