package core;

import java.util.ArrayList;

public class Model {
    private final ArrayList<Element> elements;
    double tCurr;
    double tNext;
    int event;

    public Model(ArrayList<Element> elements) {
        this.elements = elements;
        tNext = 0.0;
        tCurr = tNext;
        event = 0;
    }

    public void simulate() {

    }

    public void printInfo() {
    }

    public void printResult() {
    }
}
