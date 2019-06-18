package org.dikhim.jclicker.server.socket;

import org.dikhim.clickauto.jsengine.robot.Robot;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.jsengine.clickauto.objects.MouseObject;
import org.dikhim.jclicker.jsengine.clickauto.objects.UselessMouseObject;
import org.dikhim.jclicker.util.WebUtils;
import org.dikhim.jclicker.util.Out;

import java.net.*;
import java.io.*;
import java.util.Map;

public class ClientSocketThread extends Thread {
    private SocketServerThread socketServerThread;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;


    private int soTimeout = 500;

    private int threadTimeOut = 60000; //1 minute
    private MouseObject mouse;

    ClientSocketThread(Socket socket, SocketServerThread socketServerThread) {
        super("Client " + socket.getRemoteSocketAddress().toString().substring(1));
        this.socketServerThread = socketServerThread;
        Robot robot = Dependency.getRobot();
        mouse = new UselessMouseObject(robot);
        this.socket = socket;
        try {
            this.socket.setSoTimeout(soTimeout);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        mouse.setDelays(0);
    }

    public void run() {
        socketServerThread.addClient(this);

        try (PrintWriter out =
                     new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in =
                     new BufferedReader(new InputStreamReader(socket.getInputStream()))) {


            // create out/in streams
            this.out = out;
            this.in = in;

            String inputLine;
            int inactiveTime = 0;
            while (true) {
                try {
                    if (isInterrupted()) break;
                    inputLine = in.readLine();
                    inactiveTime = 0;
                    execute(inputLine);
                } catch (SocketTimeoutException e) {
                    inactiveTime += soTimeout;
                    if (inactiveTime > threadTimeOut) break;
                } catch (IllegalArgumentException e) {
                    Out.println(e.getMessage());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    // executes input line from client

    private void execute(String params) {
        try {
            if (params == null || params.equals("bye")) {
                // if null socket is closed
                interrupt();
            } else if (params.equals("hi")) {
                // echo message "hi"
                out.println("hi");
            } else {
                // execute params list
                executeParams(WebUtils.queryToMap(params));
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new IllegalArgumentException("incorrect parameter line \"" + params + "\" > " + e.getMessage());
        }
    }

    private void executeParams(Map<String, String> params) throws NullPointerException, IllegalArgumentException {
        String path = params.get("path");
        if (path == null) throw new IllegalArgumentException("undefined path param");
        switch (path) {
            case "/mouse/move":
                mouse.move(Integer.parseInt(params.get("dx")), Integer.parseInt(params.get("dy")));
                break;
            case "/mouse/press":
                if ("LEFT".equals(params.get("button").toUpperCase())) {
                    mouse.pressLeft();
                } else if ("RIGHT".equals(params.get("button").toUpperCase())) {
                    mouse.pressRight();
                }else if ("MIDDLE".equals(params.get("button").toUpperCase())) {
                    mouse.pressMiddle();
                }
                break;
            case "/mouse/release":
                if ("LEFT".equals(params.get("button").toUpperCase())) {
                    mouse.releaseLeft();
                } else if ("RIGHT".equals(params.get("button").toUpperCase())) {
                    mouse.releaseRight();
                }else if ("MIDDLE".equals(params.get("button").toUpperCase())) {
                    mouse.releaseMiddle();
                }
                break;
            case "/mouse/wheel":
                try {
                    int amount = Integer.parseInt(params.get("amount"));
                    if ("UP".equals(params.get("direction").toUpperCase())) {
                        mouse.wheelUp(amount);
                    } else if ("DOWN".equals(params.get("button").toUpperCase())) {
                        mouse.wheelDown(amount);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socketServerThread.removeClient(this);
        }
    }


}