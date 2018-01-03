package org.dikhim.jclicker.server;

public interface Server {

    String getAddress();

    int getPort();

    void setPort(int portNumber);

    String getStatus();

    boolean isActive();

    void start();

    void restart();

    void stop();

}
