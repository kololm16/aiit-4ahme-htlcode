package stopwatch;

/**
 *
 * @author olive
 */
public class Response {
    public Boolean master;
    public int count;
    public Boolean running;
    public Long time;

    public Response(Boolean master, int count, Boolean running, Long time) {
        this.master = master;
        this.count = count;
        this.running = running;
        this.time = time;
    }
    
    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public long getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    } 

    @Override
    public String toString() {
        return "Response{" + "master=" + master + ", count=" + count + ", running=" + running + ", time=" + time + '}';
    }
}
