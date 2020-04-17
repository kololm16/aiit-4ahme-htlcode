/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stopwatch;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author olive
 */
public class Server {
    final List<ConnectionHandler> handlers = new List<>();
    ServerSocket serversocket = new ServerSocket();
    long timeOffset;
    long startMillis;
    
    public void start (int port) {
    timeOffset = 0;
    startMillis = System.currentTimeMillis();
    
    }
    
    public boolean isTimerRunning() {
        return false;
    }
    
    public long getTimerMillis() {
        return 0;
    }
    
    public static void main(String[] args) {
        
    }
    
    class ConnectionHandler implements Runnable {
        Socket socket;
        boolean master;

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
}
