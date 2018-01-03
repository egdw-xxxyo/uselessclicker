package org.dikhim.jclicker.server.sockets;

import org.dikhim.jclicker.util.WebUtils;
import org.dikhim.jclicker.util.output.Out;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.List;

public class SocketServerThread extends Thread {
    private int port;

    private List<ClientSocketThread> connectedClients = Collections.synchronizedList(new ArrayList<>());

    public SocketServerThread(int port) {
        super("SocketServerThread");
        this.port = port;
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)
        ) {
            Out.println("SocketServer started on " + WebUtils.getLocalIpAddress() + ":" + port);
            // set timeout for waiting client
            serverSocket.setSoTimeout(500);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();


                    ClientSocketThread clientThread = new ClientSocketThread(clientSocket);
                    Out.println("Connected: " + clientThread.getName());
                    clientThread.start();
                    connectedClients.add(clientThread);
                } catch (SocketTimeoutException timeOutE) {

                    // Remove all disconnected clients threads
                    removeDisconnectedClients();

                    // if server is interrupted
                    if (isInterrupted()) {
                        interruptClientsThreads();
                        break;
                    }
                }
            }
            Out.println("Server stopped");
        } catch (IOException | IllegalArgumentException e) {
            Out.println(e.getMessage());
        }
    }

    public List<String> getClientsInfo() {
        removeDisconnectedClients();
        List<String> clients = new ArrayList<>();
        for (Thread t : connectedClients) {
            clients.add(t.getName());
        }
        return clients;
    }

    private synchronized void interruptClientsThreads() {
        for (Thread thread : connectedClients) {
            thread.interrupt();
        }
    }

    private synchronized void removeDisconnectedClients() {
        List<Thread> disconnectedClients = new ArrayList<>();
        for (Thread thread : connectedClients) {
            if (!thread.isAlive() || thread.isInterrupted()) {
                disconnectedClients.add(thread);
                Out.println("Disconnected: " + thread.getName());
            }
        }
        connectedClients.removeAll(disconnectedClients);
    }

}
