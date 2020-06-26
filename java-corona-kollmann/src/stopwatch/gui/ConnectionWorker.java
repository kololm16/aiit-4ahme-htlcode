package stopwatch.gui;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;
import javax.swing.SwingWorker;
import stopwatch.server.Request;
import stopwatch.server.Response;

/**
 *
 * @author olive
 */
public class ConnectionWorker extends SwingWorker<String, Integer> {
    private Socket socket;
    
    
    
    public ConnectionWorker(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }
    
    @Override
    protected String doInBackground() throws Exception {
        return "OK";
    }
}
