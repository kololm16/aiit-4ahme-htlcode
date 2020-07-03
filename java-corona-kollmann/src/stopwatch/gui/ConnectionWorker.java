package stopwatch.gui;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import stopwatch.server.Request;
import stopwatch.server.Response;

/**
 *
 * @author olive
 */
public class ConnectionWorker extends SwingWorker<String, Response> {
    private boolean tryToStart;
    private boolean tryToStop;
    private boolean tryToClear;
    private boolean tryToEnd;
    
    private boolean cancel;
    private int sliderValue = 0;
    
    private Socket socket;
    
    public ConnectionWorker(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }

    public synchronized void setSliderValue(int sliderValue) {
        this.sliderValue = sliderValue;
    }

    public void setTryToStart(boolean tryToStart) {
        this.tryToStart = tryToStart;
    }

    public void setTryToStop(boolean tryToStop) {
        this.tryToStop = tryToStop;
    }

    public void setTryToClear(boolean tryToClear) {
        this.tryToClear = tryToClear;
    }

    public void setTryToEnd(boolean tryToEnd) {
        this.tryToEnd = tryToEnd;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    protected void process(List<Response> chunks) {
    }
    
    @Override
    protected String doInBackground() throws Exception {
        final Gson g = new Gson();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        final OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
        while(!cancel) {
            try {                
                final Request requ = new Request(true, tryToStart, tryToStop, tryToClear, tryToEnd);
                final String requString = g.toJson(requ);
                writer.write(requString);
                writer.flush();

                tryToStart = false;
                tryToStop = false;
                tryToClear = false;
                tryToEnd = false;

                final String respString = reader.readLine();
                final Response resp = g.fromJson(respString, Response.class);
                publish(resp);
                
                synchronized(this) {
                    int localSliderValue = sliderValue;
                    Thread.sleep(1000 - localSliderValue);
                }   
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return "finished";
    }
}
