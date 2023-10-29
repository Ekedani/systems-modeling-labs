import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Create c = new Create(2.0);
        c.setName("CREATE");

        Process p = new Process(1.0);
        p.setName("PROCESS 1");
        p.setMaxqueue(5);
        c.setNextElement(p);

        c.setDistribution("exp");
        p.setDistribution("exp");

        System.out.println("id0 = " + c.getId() + " id1=" + p.getId());

        ArrayList<Element> list = new ArrayList<>();
        list.add(c);
        list.add(p);

        Model model = new Model(list);
        model.simulate(1000.0);
    }
}