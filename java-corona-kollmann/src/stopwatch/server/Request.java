package stopwatch.server;

/**
 *
 * @author olive
 */
public class Request {
    
    public Boolean master;
    public Boolean start;
    public Boolean stop;
    public Boolean clear;
    public Boolean end;

    public boolean isMaster() {
        return master != null && master;
    }

    public boolean isStart() {
        return start != null && start;
    }

    public boolean isStop() {
        return stop != null && stop;
    }

    public boolean isClear() {
        return clear != null && clear;
    }

    public boolean isEnd() {
        return end != null && end;
    }

    @Override
    public String toString() {
        return "Request{" + "master=" + master + ", start=" + start + ", stop=" + stop + ", clear=" + clear + ", end=" + end + '}';
    }
}
