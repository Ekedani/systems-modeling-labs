package core;

import java.util.ArrayList;
import java.util.Random;

public class Element {
    private String name;
    private double tCurr;
    private double tNext;
    private double delayMean;
    private double delayDev;
    private final Distribution distribution;
    private int quantity = 0;
    private int state = 0;
    private final ArrayList<NextElement> nextElements;
    private int id;

    private static int nextId = 0;

    public Element() {
        name = "element" + id;
        tNext = Double.MAX_VALUE;
        tCurr = tNext;
        delayMean = 1.0;
        distribution = Distribution.EXPONENTIAL;
        nextElements = new ArrayList<>();
        id = nextId;
        nextId++;
    }

    public double getDelay() {
        return switch (distribution) {
            case EXPONENTIAL -> FunRand.Exponential(delayMean);
            case UNIFORM -> FunRand.Uniform(delayMean, delayDev);
            case NORMAL -> FunRand.Normal(delayMean, delayDev);
            case ERLANG -> FunRand.Erlang(delayMean, delayDev);
            default -> delayMean;
        };
    }

    public void setDelayMean(double delayMean) {
        this.delayMean = delayMean;
    }

    public void setDelayDev(double delayDev) {
        this.delayDev = delayDev;
    }

    public int getQuantity() {
        return quantity;
    }

    public void changeQuantity(int delta) {
        this.quantity += delta;
    }

    public Element getNextElementByPriority() {
        NextElement nextElement = new NextElement(null);
        return nextElement.getElement();
    }

    public Element getNextElementByProbability() {
        var probability = new Random().nextDouble();
        NextElement nextElement = new NextElement(null);
        return nextElement.getElement();
    }

    public void inAct() {
    }

    public void outAct() {
        quantity++;
    }

    public double getTNext() {
        return tNext;
    }

    public void setTNext(double tNext) {
        this.tNext = tNext;
    }
}
