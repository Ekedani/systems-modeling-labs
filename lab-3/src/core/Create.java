package core;

public class Create extends Element {
    public Create(String name, double delay) {
        super(name, delay);
        super.setTNext(0.0);
    }

    public Create(String name, double delay, double initialTNext) {
        super(name, delay);
        super.setTNext(initialTNext);
    }

    @Override
    public void outAct() {
        super.outAct();
        super.setTNext(super.getTCurr() + super.getDelay());
        var createdJob = new Job(super.getTCurr());
        super.getNextRoute().getElement().inAct(createdJob);
    }
}
