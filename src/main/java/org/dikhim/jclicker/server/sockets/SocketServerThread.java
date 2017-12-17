package org.dikhim.jclicker.server.sockets;

import org.dikhim.jclicker.jsengine.MouseObject;
import org.dikhim.jclicker.util.WebUtils;
import org.dikhim.jclicker.util.output.Out;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.List;

public class SocketServerThread extends Thread {


    private int port;

    private List<Thread> connectedClients = Collections.synchronizedList(new ArrayList<>());

    public SocketServerThread(int port) {
        super("SocketServerThread");
        this.port = port;
    }



    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)
        ) {
            Out.println("SocketServer started on "+ WebUtils.getLocalIpAddress()+":"+port);
            // set timeout for waiting client
            serverSocket.setSoTimeout(500);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();


                    Thread clientThread = new ClientSocketThread(clientSocket);
                    Out.println("Connected: "+clientThread.getName());
                    clientThread.start();
                    connectedClients.add(clientThread);
                } catch (SocketTimeoutException timeOutE) {

                    // Remove all interrupted client threads
                    List<Thread> interruptedClients = new ArrayList<>();
                    for (Thread thread : connectedClients) {
                        if (!thread.isAlive()) {
                            interruptedClients.add(thread);
                            Out.println("Disconnected: "+thread.getName());
                        }
                    }
                    connectedClients.removeAll(interruptedClients);

                    // if server is interrupted
                    if (isInterrupted()) {
                        for (Thread thread : connectedClients) {
                            thread.interrupt();
                        }
                        break;
                    }
                }
            }
            Out.println("Server stopped");
        } catch (IOException |IllegalArgumentException e) {
            Out.println(e.getMessage());
        }
    }


}
