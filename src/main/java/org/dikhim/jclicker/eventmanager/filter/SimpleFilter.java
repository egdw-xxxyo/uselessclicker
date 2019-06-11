package org.dikhim.jclicker.eventmanager.filter;

public abstract class SimpleFilter implements Filter {
    private String id;

    public SimpleFilter(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return null;
    }
}
