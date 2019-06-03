package org.dikhim.jclicker.server.http;

import org.dikhim.jclicker.jsengine.clickauto.objects.ComputerObject;

public class HttpClient {
    private int uid;
    private ComputerObject computerObject;

    public HttpClient(int uid, ComputerObject computerObject) {
        this.uid = uid;
        this.computerObject = computerObject;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public ComputerObject getComputerObject() {
        return computerObject;
    }

    public void setComputerObject(ComputerObject computerObject) {
        this.computerObject = computerObject;
    }
}
