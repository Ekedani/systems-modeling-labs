import core.Process;
import core.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("lab-3");
        bank();
    }

    public static void task1() {
        var create = new Create("Create", 1.0);
        var process = new Process("Process #1", 2.0, 4);
        var process2 = new Process("Process #2", 2.0, 4);
        var dispose = new Dispose("Dispose");

        create.addRoutes(
                new Route(process, 0.1, 2, () -> Math.random() > 0.01),
                new Route(process2, 0.45, 1),
                new Route(dispose, 0.45, 1)
        );

        process.addRoutes(
                new Route(dispose, 1.0)
        );

        var model = new Model(create, process, process2, dispose);
        model.simulate(1000);
    }

    public static void bank() {
        var create = new Create("Create", 0.5, 0.1);
        var cashierWindow1 = new Process("Cashier window #1", 1, 0.3, 1);
        var cashierWindow2 = new Process("Cashier window #2", 1, 0.3, 1);
        var dispose = new Dispose("Dispose");

        create.setDistribution(Distribution.EXPONENTIAL);
        cashierWindow1.setDistribution(Distribution.NORMAL);
        cashierWindow2.setDistribution(Distribution.NORMAL);

        cashierWindow1.setMaxQueueSize(3);
        cashierWindow2.setMaxQueueSize(3);

        cashierWindow1.initializeChannelsWithJobs(1);
        cashierWindow1.initializeQueueWithJobs(2);
        cashierWindow2.initializeChannelsWithJobs(1);
        cashierWindow2.initializeQueueWithJobs(2);

        create.addRoutes(
                new Route(cashierWindow1, 0.5, 1),
                new Route(cashierWindow2, 0.5, 0)
        );

        cashierWindow1.addRoutes(
                new Route(dispose, 1.0)
        );

        cashierWindow2.addRoutes(
                new Route(dispose, 1.0)
        );

        var model = new Model(create, cashierWindow1, cashierWindow2, dispose);
        model.simulate(1000);
    }
}