import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Create create = new Create(2.0);
        create.setName("CREATE");

        Process process = new Process(0.5);
        process.setName("PROCESS");
        process.setMaxqueue(5);
        create.setNextElement(process);

        create.setDistribution("exp");
        process.setDistribution("exp");

        ArrayList<Element> elements = new ArrayList<>(
                Arrays.asList(create, process)
        );

        Model model = new Model(elements);
        model.simulate(1000.0);
    }
}