package server;

import interfaces.Result;
import java.io.Serializable;

public class ResultImpl implements Result, Serializable {
    private Object output;
    private double time;

    public ResultImpl(Object o, double t) {
        output = o;
        time = t;
    }

    public Object output() {
        return output;
    }

    public double scoreTime() {
        return time;
    }
}
