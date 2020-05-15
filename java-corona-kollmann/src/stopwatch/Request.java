package stopwatch;

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
        return master;
    }

    public boolean isStart() {
        return start;
    }

    public boolean isStop() {
        return stop;
    }

    public boolean isClear() {
        return clear;
    }

    public boolean isEnd() {
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
