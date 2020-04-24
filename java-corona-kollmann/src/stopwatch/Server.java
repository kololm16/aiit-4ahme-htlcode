/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stopwatch;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author olive
 */
public class Server {
    private class ConnectionHandler implements Runnable {
        private Socket socket;
        private boolean master;

        public ConnectionHandler(Socket socket) {
            this.socket = socket;
        }
        
        public boolean isClosed() {
            return true;
        }
        
        public boolean isMaster() {
            return false;
        }
        
        public void run() { 
        }
    }
    
    private ServerSocket serverSocket;
    private final List<ConnectionHandler> handlers;
    private long timeOffset;
    private long startMillis;

    public Server(List<ConnectionHandler> handlers) {
        this.handlers = handlers;
    }
    
    public void start (int port) throws IOException{
        serverSocket = new ServerSocket(port);
        timeOffset = 0;
        while (true) {
            Socket clientSocket = serverSocket.accept();
            ConnectionHandler h = new ConnectionHandler(clientSocket);
            handlers.add(h);
        }
       
    }
    
    public boolean isTimerRunning() {
        return startMillis > 0;
    }
    
    public long getTimerMillis() {     
        return timeOffset;
    }
    
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(0);
    }
}
