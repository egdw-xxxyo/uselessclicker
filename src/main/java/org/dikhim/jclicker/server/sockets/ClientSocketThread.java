package org.dikhim.jclicker.server.sockets;

import org.dikhim.jclicker.jsengine.MouseObject;
import org.dikhim.jclicker.util.WebUtils;
import org.dikhim.jclicker.util.output.Out;

import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ClientSocketThread extends Thread {
    private Socket socket = null;
    PrintWriter out;
    BufferedReader in;


    private int soTimeout = 500;

    private int threadTimeOut = 1800000; //30 minutes
    private MouseObject mouse;
    public ClientSocketThread(Socket socket) {
        super("Client "+socket.getRemoteSocketAddress().toString().substring(1));
        try {
            Robot robot = new Robot();
            mouse = new MouseObject(robot);
            this.socket = socket;
            this.socket.setSoTimeout(soTimeout);
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        mouse.setAllDelays(0);
    }

    public void run() {

        try {
            // create out/in streams
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            String inputLine;
            int inactiveTime = 0;
            while (true) {
                try {
                    if(isInterrupted())break;
                    inactiveTime = 0;
                    inputLine = in.readLine();
                    execute(inputLine);
                } catch (SocketTimeoutException e) {
                    inactiveTime += soTimeout;
                    if (inactiveTime > threadTimeOut) break;
                } catch (IllegalArgumentException e) {
                    Out.println(e.getMessage());
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // executes input line from client

    public void execute(String params) {
        try {
            // if null socket is closed
            if(params==null){
                interrupt();
                return;
            }
            // echo message "hi"
            if (params.equals("hi")){
                out.println("hi");
                return;
            }
            // execute params list
            executeParams(WebUtils.queryToMap(params));
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
                mouse.press(params.get("button"));
                break;
            case "/mouse/release":
                mouse.release(params.get("button"));
                break;
            case "/mouse/wheel":
                mouse.wheel(params.get("direction"),Integer.parseInt(params.get("amount")));
                break;
        }

    }



}