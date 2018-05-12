package org.dikhim.jclicker.server;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import org.dikhim.jclicker.server.socket.Client;

public interface Server {

    String getAddress();

    int getPort();

    String getStatus();

    boolean isActive();

    void restart();

    void setPort(int portNumber);

    void start();

    void stop();

    ObservableList<Client> getConnectedClientsProperty();

    IntegerProperty getPortProperty();
    
    StringProperty getStatusProperty();

    StringProperty getAddressProperty();

}
