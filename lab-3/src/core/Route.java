package core;

import java.util.concurrent.Callable;

public class Route {
    private final Element element;
    private int priority = 0;
    private double probability = 0.0;
    private Callable<Boolean> block = null;

    public Route(Element element) {
        this.element = element;
    }

    public Route(Element element, double probability) {
        this.element = element;
        this.probability = probability;
    }

    public Route(Element element, double probability, int priority) {
        this.element = element;
        this.priority = priority;
        this.probability = probability;
    }

    public Route(Element element, double probability, int priority, Callable<Boolean> block) {
        this.element = element;
        this.probability = probability;
        this.priority = priority;
        this.block = block;
    }

    public boolean isBlocked() {
        if (block == null) {
            return false;
        }
        try {
            return block.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
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