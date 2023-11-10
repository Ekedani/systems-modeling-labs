import core.Process;
import core.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("lab-3");
        bank();
    }

    public static void bank() {
        var create = new Create("Create #1", 0.5, 0.1);
        var cashierWindow1 = new Process("Cashier window #1", 1, 0.3, 1);
        var cashierWindow2 = new Process("Cashier window #2", 1, 0.3, 1);
        var dispose = new Dispose("Dispose #1");

        cashierWindow1.initializeChannelsWithJobs(1);
        cashierWindow2.initializeChannelsWithJobs(1);
        cashierWindow1.initializeQueueWithJobs(2);
        cashierWindow2.initializeQueueWithJobs(2);

        create.setDistribution(Distribution.EXPONENTIAL);
        cashierWindow1.setDistribution(Distribution.EXPONENTIAL);
        cashierWindow2.setDistribution(Distribution.EXPONENTIAL);

        cashierWindow1.setMaxQueueSize(3);
        cashierWindow2.setMaxQueueSize(3);

        create.setRouting(Routing.BY_PROBABILITY);
        create.addRoutes(
                new Route(cashierWindow1, 0.5, 0),
                new Route(cashierWindow2, 0.5, 0)
        );

        cashierWindow1.addRoutes(
                new Route(dispose)
        );

        cashierWindow2.addRoutes(
                new Route(dispose)
        );

        var model = new Model(create, cashierWindow1, cashierWindow2, dispose);
        model.simulate(1000);
    }
}