package core;

public class Job {
    private final int id;
    private final double timeIn;
    private double timeOut;
    private double timeWait;
    private double timeWork;

    private static int nextId = 0;

    public Job(double timeIn) {
        this.timeIn = timeIn;
        this.id = nextId;
        nextId++;
    }

    public int getId() {
        return id;
    }

    public double getTimeIn() {
        return timeIn;
    }

    public double getTimeOut() {
        return timeOut;
    }

    public double getTimeWait() {
        return timeWait;
    }

    public double getTimeWork() {
        return timeWork;
    }

    public void setTimeOut(double timeOut) {
        this.timeOut = timeOut;
    }
}
