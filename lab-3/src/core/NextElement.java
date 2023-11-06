package core;

import java.util.concurrent.Callable;

public class NextElement {
    private Element element;
    private int priority = 0;
    private double probability = 0.0;
    private Callable<Boolean> block = null;

    public NextElement(Element element) {
        this.element = element;
    }

    public NextElement(Element element, int priority) {
        this.element = element;
        this.priority = priority;
    }

    public NextElement(Element element, double probability) {
        this.element = element;
        this.probability = probability;
    }

    public void setBlock(Callable<Boolean> block) {
        this.block = block;
    }

    public boolean isBlocked() {
        if (block == null) {
            return false;
        }
        try {
            return block.call();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Element getElement() {
        return element;
    }

    public int getPriority() {
        return priority;
    }

    public double getProbability() {
        return probability;
    }
}