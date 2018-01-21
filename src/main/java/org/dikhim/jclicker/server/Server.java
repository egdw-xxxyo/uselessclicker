package org.dikhim.jclicker.server;

import javafx.collections.ObservableList;
import org.dikhim.jclicker.util.Cli;

import java.util.List;

public interface Server {

    String getAddress();

    ObservableList<Client> getConnectedClients();

    int getPort();

    String getStatus();

    boolean isActive();

    void restart();

    void setPort(int portNumber);

    void start();

    void stop();

}
