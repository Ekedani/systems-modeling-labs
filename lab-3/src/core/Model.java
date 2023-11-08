package core;

import java.util.ArrayList;
import java.util.Arrays;

public class Model {
    private final ArrayList<Element> elements;
    double tCurr;
    double tNext;
    int event;

    public Model(Element... elements) {
        this.elements = new ArrayList<>(Arrays.asList(elements));
        tNext = 0.0;
        tCurr = tNext;
        event = 0;
    }

    public void simulate(double time) {
        while (tCurr < time) {
            tNext = Double.MAX_VALUE;
            for (var element : elements) {
                if (element.getTNext() < tNext) {
                    tNext = element.getTNext();
                    event = element.getId();
                }
            }
            System.out.println("\nEvent in " + elements.get(event).getName() + ", tNext = " + tNext);
            var delta = tNext - tCurr;
            for (Element element : elements) {
                element.doStatistics(delta);
            }
            tCurr = tNext;
            for (var element : elements) {
                element.setTCurr(tCurr);
            }
            elements.get(event).outAct();
            // TODO: maybe should be reversed
            for (var element : elements) {
                if (element.getTNext() == tCurr) {
                    element.outAct();
                }
            }
            printInfo();
        }
        printResult();
    }

    public void printInfo() {
        for (var element : elements) {
            element.printInfo();
        }
    }

    public void printResult() {
        System.out.println("\n-------------RESULTS-------------");
        for (var element : elements) {
            element.printResult();
        }
    }


}
