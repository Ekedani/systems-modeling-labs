package core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class Process extends Element {
    protected final Deque<Job> queue = new ArrayDeque<>();
    protected final ArrayList<Channel> channels = new ArrayList<>();
    protected int failures = 0;
    protected int maxQueueSize = Integer.MAX_VALUE;
    protected double meanQueue = 0.0;
    protected double workTime = 0.0;
    protected double totalLeaveTime = 0.0;
    protected double previousLeaveTime = 0.0;

    public Process(String name, double delayMean, int channelsNum) {
        super(name, delayMean);
        for (int i = 0; i < channelsNum; i++) {
            channels.add(new Channel());
        }
    }

    public Process(String name, double delayMean, double delayDev, int channelsNum) {
        super(name, delayMean, delayDev);
        for (int i = 0; i < channelsNum; i++) {
            channels.add(new Channel());
        }
    }

    public void initializeChannelsWithJobs(int jobsNum) {
        jobsNum = Math.min(jobsNum, channels.size());
        for (int i = 0; i < jobsNum; i++) {
            channels.get(i).setCurrentJob(new Job(0.0));
            channels.get(i).setTNext(super.getTCurr() + super.getDelay());
        }
    }

    public void initializeQueueWithJobs(int jobsNum) {
        jobsNum = Math.min(jobsNum, maxQueueSize);
        for (int i = 0; i < jobsNum; i++) {
            queue.add(new Job(0.0));
        }
    }

    @Override
    public void inAct(Job job) {
        super.inAct(job);
        var freeChannel = getFreeChannel();
        if (freeChannel != null) {
            freeChannel.setCurrentJob(job);
            freeChannel.setTNext(super.getTCurr() + super.getDelay());
        } else {
            if (queue.size() < getMaxQueueSize()) {
                queue.add(job);
            } else {
                failures++;
            }
        }
    }

    @Override
    public void outAct() {
        processCurrentJobs();
        startNextJobs();
    }

    protected void processCurrentJobs() {
        var channelsWithMinTNext = getChannelsWithMinTNext();
        for (var channel : channelsWithMinTNext) {
            var job = channel.getCurrentJob();

            var nextRoute = getNextRoute();
            if (nextRoute.isBlocked()) {
                continue;
            }

            if (nextRoute.getElement() != null) {
                job.setTimeOut(super.getTCurr());
                nextRoute.getElement().inAct(job);
            }

            channel.setCurrentJob(null);
            channel.setTNext(Double.MAX_VALUE);
            changeQuantity(1);
            totalLeaveTime += super.getTCurr() - previousLeaveTime;
            previousLeaveTime = super.getTCurr();
        }
    }

    protected void startNextJobs() {
        var freeChannel = getFreeChannel();
        while (!queue.isEmpty() && freeChannel != null) {
            var job = queue.poll();
            freeChannel.setCurrentJob(job);
            freeChannel.setTNext(super.getTCurr() + super.getDelay());
            freeChannel = getFreeChannel();
        }
    }

    private ArrayList<Channel> getChannelsWithMinTNext() {
        var channelsWithMinTNext = new ArrayList<Channel>();
        var minTNext = Double.MAX_VALUE;
        for (var channel : channels) {
            if (channel.getTNext() < minTNext) {
                minTNext = channel.getTNext();
            }
        }
        for (var channel : channels) {
            if (channel.getTNext() == minTNext) {
                channelsWithMinTNext.add(channel);
            }
        }
        return channelsWithMinTNext;
    }

    protected Channel getFreeChannel() {
        for (var channel : channels) {
            if (channel.getState() == 0) {
                return channel;
            }
        }
        return null;
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    public void setMaxQueueSize(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }

    public int getFailures() {
        return failures;
    }

    public double getMeanQueue() {
        return meanQueue;
    }

    public double getWorkTime() {
        return workTime;
    }

    @Override
    public void doStatistics(double delta) {
        super.doStatistics(delta);
        meanQueue += queue.size() * delta;
        workTime += getState() * delta;
    }

    @Override
    public int getState() {
        int state = 0;
        for (Channel channel : channels) {
            state |= channel.getState();
        }
        return state;
    }

    @Override
    public double getTNext() {
        double tNext = Double.MAX_VALUE;
        for (Channel channel : channels) {
            if (channel.getTNext() < tNext) {
                tNext = channel.getTNext();
            }
        }
        return tNext;
    }

    @Override
    public void setTNext(double tNext) {
        double previousTNext = getTNext();
        for (Channel channel : channels) {
            if (channel.getTNext() == previousTNext) {
                channel.setTNext(tNext);
            }
        }
    }

    @Override
    public void printInfo() {
        System.out.println(getName() +
                " state = " + getState() +
                " quantity = " + getQuantity() +
                " tnext = " + getTNext() +
                " failures = " + failures +
                " queue size = " + queue.size()
        );
    }


    public int getQueueSize() {
        return queue.size();
    }

    public double getMeanLeaveInterval() {
        return totalLeaveTime / getQuantity();
    }

    public ArrayList<Job> getUnprocessedJobs() {
        var jobs = new ArrayList<Job>();
        for (var channel : channels) {
            if (channel.getCurrentJob() != null) {
                jobs.add(channel.getCurrentJob());
            }
        }
        if (!queue.isEmpty()) {
            jobs.addAll(queue);
        }
        for (var job : jobs) {
            job.setTimeOut(super.getTCurr());

        }
        return jobs;
    }

    protected static class Channel {
        private Job currentJob = null;
        private double tNext = Double.MAX_VALUE;

        public int getState() {
            return currentJob == null ? 0 : 1;
        }

        public Job getCurrentJob() {
            return currentJob;
        }

        public void setCurrentJob(Job currentJob) {
            this.currentJob = currentJob;
        }

        public double getTNext() {
            return tNext;
        }

        public void setTNext(double tnext) {
            this.tNext = tnext;
        }
    }

}
