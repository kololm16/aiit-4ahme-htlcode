/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stopwatch;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author olive
 */
public class Server {

    private class Request {
        private boolean master;
        private boolean start;
        private boolean stop;
        private boolean clear;
        private boolean end;
        
        private boolean isMaster() {
            return master;
        }
        
        private boolean isStart() {
            return start;
        }
        
        private boolean isStop() {
            return stop;
        }
        
        private boolean isClear() {
            return clear;
        }
        
        private boolean isEnd() {
            return end;
        }

        public void setMaster(boolean master) {
            this.master = master;
        }

        public void setStart(boolean start) {
            this.start = start;
        }

        public void setStop(boolean stop) {
            this.stop = stop;
        }

        public void setClear(boolean clear) {
            this.clear = clear;
        }

        public void setEnd(boolean end) {
            this.end = end;
        }
    }
    
    private class ConnectionHandler implements Runnable {
        private final Socket socket;
        private boolean master;

        public ConnectionHandler(Socket socket) {
            this.socket = socket;
        }
        
        public boolean isClosed() {
            return socket.isClosed();
        }
        
        public boolean isMaster() {
            return master;
        }
        
        @Override
        public void run() { 
            try{
                while(true) {
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    final String line = reader.readLine(); // Zeichen werden in Line gespeichert.

                    final Gson gson = new Gson();
                    gson.toJson(line); // die einkommenden Zeilen werden in ein Objekt gespeichert
                    final Request r = gson.fromJson(line, Request.class); // neues Request Objekt, welches die Zeilen beinhaltet.

                    if(r.isMaster()) {
                        for(ConnectionHandler c: handlers) {
                            master = true;
                            if(c != this && c.isMaster() == true) {
                                master = false;
                                break;
                            }
                        }
                    }
                    if(master == true) {
                        if(r.isStart()) {
                            startMillis = System.currentTimeMillis();
                        }
                        if(r.isClear()) {
                            if(isTimerRunning()) {
                                startMillis = System.currentTimeMillis();
                            }
                            timeOffset = 0;
                        }
                        if(r.isStop()) {
                            timeOffset = getTimerMillis();
                            startMillis = 0;
                        }
                        if(r.isEnd()) {
                            //Server Applikation beenden
                            handlers.remove(this);
                        }
                    }
                    //Response
                    Response resp = new Response(master, 5, isTimerRunning(), getTimerMillis());
                    String respString = gson.toJson(resp);
                    OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
                }    
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private final ServerSocket serverSocket;
    private final List<ConnectionHandler> handlers = new ArrayList<>();
    private long timeOffset;
    private long startMillis;
    
    public void start (int port) throws IOException{
        serverSocket = new ServerSocket(port);
        timeOffset = 0;
        
        while(true) {
            final Socket clientSocket = serverSocket.accept();
            //TODO Überprüfen, welche Handler entfernt werden können
            if(handlers.size() < 3) {
                final ConnectionHandler handler = new ConnectionHandler(clientSocket);
                new Thread(handler).start(); // Hintergrundthread
                handlers.add(handler);
            } else {
                clientSocket.close();
            }
        } 
    }
    
    public boolean isTimerRunning() {
        return startMillis > 0;
    }
    
    public long getTimerMillis() {     
        if(startMillis > 0) {
            return System.currentTimeMillis() - startMillis + timeOffset;
        } else {
            return timeOffset;
        }
    }
    
    public static void main(String[] args) throws IOException {
        final Server server = new Server();
        server.start(8080);
    }
}
