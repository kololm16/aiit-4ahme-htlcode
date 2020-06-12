package stopwatch.server;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author oliver
 */
public class Server {

    private ServerSocket serversocket;
    private final List<ConnectionHandler> handlers = new ArrayList<>();
    private long timeOffset;
    private long startMillis;
    
    public Server() {}

    public void start(int port) throws IOException {
        serversocket = new ServerSocket(port);
        System.out.println("Server auf Port " + port + "gestartet!");
        timeOffset = 0;
        startMillis = 0;
        
        while (true) {
            Socket clientSocket = serversocket.accept();
            synchronized (handlers) {
                for (int i = 0; i < handlers.size(); i++) {
                    ConnectionHandler h = handlers.get(i);
                    if (h.isClosed()) {
                        handlers.remove(i--);
                    }
                }
                if (handlers.size() < 3) { //maximal 3 Verbindungen erlaubt
                    ConnectionHandler handler = new ConnectionHandler(clientSocket);
                    handlers.add(handler);
                    new Thread(handler).start(); // Hintergrundthread
                } else {
                    System.out.println("Connection refused (" + clientSocket.toString() + ")");
                    clientSocket.close();
                }
            }
        }
    }

    public boolean isTimerRunning() {
        synchronized (handlers) {
            return startMillis > 0;
        } 
    }

    public long getTimerMillis() {
        synchronized (handlers) {
            if (startMillis <= 0) {
                return timeOffset;
            } else {
                return System.currentTimeMillis() - startMillis + timeOffset;
            }
        }      
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(8080);
    }

    private class ConnectionHandler implements Runnable {

        private final Socket socket;
        private boolean master;

        public ConnectionHandler(Socket socket) {
            if(socket == null) {
                throw new NullPointerException();
            }
            this.socket = socket;
        }

        public boolean isClosed() {
            return socket == null || socket.isClosed();
        }

        public boolean isMaster() {
            return master;
        }

        @Override
        public void run() {
            int count = 0;
            try {
                System.out.println("ServerHandler Thread gestartet, Socket: " + socket);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                Gson gson = new Gson();
                
                while(true) {
                    final String line = reader.readLine(); // Zeichen werden in Line gespeichert.
                    if (line == null) {
                        socket.close();
                        return;
                    }
                    count++;

                    gson.toJson(line); // die einkommenden Zeilen werden in ein Objekt gespeichert
                    final Request requ = gson.fromJson(line, Request.class); // neues Request Objekt, welches die Zeilen beinhaltet.
                    //System.out.println("request: " + r.toString());

                    if (requ.master != null) {
                        if(requ.master) {
                            ConnectionHandler currentMaster = null;
                            synchronized (handlers) {
                                for (ConnectionHandler h : handlers) {
                                    if(h.isMaster() && !h.isClosed()) {
                                        currentMaster = h;
                                        break;
                                    }
                                }
                                if (currentMaster == null) {
                                    master = true;
                                }
                            }
                        } else {
                            master = false;
                        }
                    }
                    
                    boolean end = false;
                    synchronized (handlers) {
                        if (master) {
                            if (requ.start != null && requ.start) {
                                if(startMillis <= 0) {
                                    startMillis = System.currentTimeMillis();
                                }                            
                            }
                            if (requ.stop != null && requ.stop && startMillis > 0) {
                                timeOffset += System.currentTimeMillis() - startMillis;
                                startMillis = 0;
                            }
                            if (requ.clear != null && requ.clear) {
                                timeOffset = 0;
                                if (startMillis > 0) {
                                    startMillis = System.currentTimeMillis();
                                }
                            }
                            if (requ.end != null && requ.end) {
                                end = true;
                            }
                        }
                    }
                    
                    final Response resp = new Response();
                    resp.count = count;
                    resp.master = master;
                    resp.running = isTimerRunning();
                    resp.time = getTimerMillis();
                    final String respString = gson.toJson(resp);
                    //System.out.println(respString);
                    writer.write(respString + "\n");
                    writer.flush();
                    if (end) {
                        socket.close();
                        return;
                    }
                }
            } catch (Exception ex) {
                new Exception("SocketHandler Thread fails...", ex).printStackTrace(System.err);
            } 
        }
    }
}
