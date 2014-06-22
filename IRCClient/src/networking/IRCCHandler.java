package networking;

/*
 * To change this template, choose Tools | Templates
 * and open the template in tahe editor.
 */
import client.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class IRCCHandler implements Runnable {

    Thread thread;
    String server;
    int port;
    boolean isConnected = false;
    Socket socket;
    BufferedReader reader;
    BufferedWriter writer;
    private ArrayList<IRCSListener> iRCSListeners;

    public IRCCHandler(String server, int port) {
        this.server = server;
        this.port = port;
        iRCSListeners = new ArrayList<IRCSListener>();

        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            socket = new Socket(server, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            if (socket != null) {
                isConnected = true;
                notifyConnected();
            }
        } catch (Exception ex) {
            notifyError(ex);
            return;
        }


        String line = null;
        try {
            while (isConnected) {
                while ((line = reader.readLine()) != null) {
                    if (line.indexOf("ERROR") != -1) {
                        isConnected = false;
                        notifyDisconnected();
                        shutdown();
                        break;
                    }
                    notifyListeners(line);
                }
            }

        } catch (SocketException ex) {
            System.out.println("socket closed");
        } catch (IOException ex) {
            System.out.println("ioexception occured");
        } 
    }

    public void shutdown() {
        try {
            isConnected = false;
            if(socket != null)
                socket.close();
        } catch (IOException ex) {
            Logger.getLogger(IRCChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void write(String s) {
        write(s, true);
    }

    public void write(String s, boolean flush) {
        if (isConnected) {
            try {
                writer.write(s + "\r\n");
                if (flush) {
                    writer.flush();
                }
            } catch (IOException ex) {
                Logger.getLogger(IRCCHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getLine() {
        try {
            return reader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(IRCCHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    private void notifyConnected() {
        for (int i = 0; i < iRCSListeners.size(); i++) {
            ((IRCSListener) iRCSListeners.get(i)).onConnect();
        }
    }

    private void notifyDisconnected() {
        for (int i = 0; i < iRCSListeners.size(); i++) {
            ((IRCSListener) iRCSListeners.get(i)).onDisconnect();
        }
    }

    private void notifyListeners(String s) {
        for (int i = 0; i < iRCSListeners.size(); i++) {
            ((IRCSListener) iRCSListeners.get(i)).dataReceived(s);
        }
    }

    private void notifyError(Exception e) {
        for (int i = 0; i < iRCSListeners.size(); i++) {
            ((IRCSListener) iRCSListeners.get(i)).onError(e);
        }
    }
    public void addSocketListener(IRCSListener s) {
        iRCSListeners.add(s);
    }

    public void removeSocketListener(IRCSListener s) {
        iRCSListeners.remove(s);
    }
}