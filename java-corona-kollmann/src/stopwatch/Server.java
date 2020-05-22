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
        timeOffset = 0;
        startMillis = 0;

        while (true) {
            final Socket clientSocket = serversocket.accept();
            for (ConnectionHandler h : handlers) {
                if (h.isClosed()) {
                    handlers.remove(h);
                }
            }
            if (handlers.size() < 3) { //maximal 3 Verbindungen erlaubt
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
        if (startMillis == 0) {
            return timeOffset;
        } else {
            return System.currentTimeMillis() - startMillis + timeOffset;
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

            int count = 0;
            while (true) {
                try {
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    final OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
                    final String line = reader.readLine(); // Zeichen werden in Line gespeichert.
                    
                    if (line == null) {
                        socket.close();
                        return;
                    }
                    
                    count++;
                    
                    final Gson gson = new Gson();
                    gson.toJson(line); // die einkommenden Zeilen werden in ein Objekt gespeichert
                    System.out.println(line);
                    final Request r = gson.fromJson(line, Request.class); // neues Request Objekt, welches die Zeilen beinhaltet.
                    System.out.println(r);
                    
                    if (r.isMaster()) {
                        master = true;
                        for (ConnectionHandler c : handlers) {
                            if (c != this && c.isMaster() == true) {
                                master = false;
                                //response senden
                                break;
                            }
                        }
                    }

                    if (master) {
                        if (r.isStart()) {
                            startMillis = System.currentTimeMillis();
                        }
                        if (r.isClear()) {
                            if (isTimerRunning()) {
                                startMillis = System.currentTimeMillis();
                            }
                            timeOffset = 0;
                        }
                        if (r.isStop()) {
                            timeOffset = getTimerMillis();
                            startMillis = 0;
                        }
                        if (r.isEnd()) {
                            serversocket.close();
                            socket.close();
                            handlers.remove(this);
                            return;
                        }
                    }
                    //Response
                    final Response resp = new Response(master, count, isTimerRunning(), getTimerMillis());
                    System.out.println(resp);
                    final String respString = gson.toJson(resp);
                    System.out.println(respString);
                    writer.write(respString);
                    writer.flush();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
