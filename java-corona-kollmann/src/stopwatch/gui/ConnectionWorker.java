package stopwatch.gui;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import javax.swing.SwingWorker;
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
        System.out.println("Do in Background" + Thread.currentThread().getId());
        Thread.sleep(1000);
        
        publish(1);
        
        Thread.sleep(1000);
        
        publish(2);
        
        Thread.sleep(1000);
        return "OK";
    }
}
