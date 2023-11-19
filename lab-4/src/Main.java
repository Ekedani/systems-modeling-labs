import java.util.ArrayList;

public class Main {
    public static int N_MIN = 10;
    public static int N_STEP = 10;
    public static int N_MAX = 200;
    public static int EXPERIMENTS_COUNT = 10;
    public static int MODEL_TYPE = 1;

    public static void main(String[] args) {
        for (int N = N_MIN; N <= N_MAX; N += N_STEP) {
            var results = new ArrayList<Double>();
            for (int i = 0; i < EXPERIMENTS_COUNT; i++) {
                var model = getCurrentModel(N);

                var start = System.currentTimeMillis();
                model.simulate(10000);
                var end = System.currentTimeMillis();

                results.add((double) (end - start));
            }
            var averageRuntime = results.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            System.out.println(
                    "Model with N = " + N +
                            ", average runtime = " +
                            averageRuntime / 1000 + " s" +
                            " (" + averageRuntime + " ms)"
            );
        }
    }


    public static Model getCurrentModel(int N) {
        return switch (MODEL_TYPE) {
            case 1 -> generateFirstTaskModel(N);
            case 2 -> generateSecondTaskModel(N);
            default -> null;
        };
    }


    /**
     * Generates a Model representing a linear system workflow with Create and Process elements.
     *
     * @param n The number of Process elements to be generated and interconnected in the model.
     * @return A Model representing a linear system workflow with Create and Process elements.
     */
    public static Model generateFirstTaskModel(int n) {
        var elements = new ArrayList<Element>();
        var create = new Create("Create", 1.0);
        create.setDistribution(Distribution.EXPONENTIAL);
        create.setRouting(Routing.BY_PROBABILITY);
        elements.add(create);

        Element previousElement = create;
        for (int i = 0; i < n; i++) {
            var process = new Process("Process #" + i, 1.0, 1);
            process.setDistribution(Distribution.EXPONENTIAL);
            process.setRouting(Routing.BY_PROBABILITY);
            process.setMaxQueueSize(Integer.MAX_VALUE);
            elements.add(process);

            previousElement.addRoutes(new Route(process, 1.0));
            previousElement = process;
        }
        return new Model(elements.toArray(new Element[0]));
    }

    /**
     * Generates a model for a parallel system workflow with Create and multiple interconnected Process elements.
     *
     * @param n The number of Process elements to be generated and interconnected in parallel in the model.
     * @return A model for a parallel system workflow with Create and multiple interconnected Process elements.
     */
    public static Model generateSecondTaskModel(int n) {
        var elements = new ArrayList<Element>();
        var create = new Create("Create", 1.0);
        create.setDistribution(Distribution.EXPONENTIAL);
        create.setRouting(Routing.BY_PROBABILITY);
        elements.add(create);

        for (int i = 0; i < n; i++) {
            var process = new Process("Process #" + i, 1.0, 1);
            process.setDistribution(Distribution.EXPONENTIAL);
            process.setRouting(Routing.BY_PROBABILITY);
            process.setMaxQueueSize(Integer.MAX_VALUE);
            elements.add(process);
            create.addRoutes(new Route(process, 1.0));
        }
        return new Model(elements.toArray(new Element[0]));
    }
}