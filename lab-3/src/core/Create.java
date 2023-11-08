package core;

public class Create extends Element {
    public Create(String name, double delay) {
        super(name, delay);
        super.setTNext(0.0);
    }

    @Override
    public void outAct() {
        super.outAct();
        super.setTNext(super.getTCurr() + super.getDelay());
        var createdJob = new Job(super.getTCurr());
        super.getNextRoute().inAct(createdJob);
    }
}
