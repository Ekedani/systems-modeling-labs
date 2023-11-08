package core;

import java.util.ArrayList;

public class Dispose extends Element {
    ArrayList<Job> processedJobs = new ArrayList<>();

    public Dispose(String name) {
        super(name);
    }

    @Override
    public void inAct(Job job) {
        super.inAct(job);
        processedJobs.add(job);
    }
}
