import core.Create;
import core.Dispose;
import core.Model;
import core.Process;

public class Main {
    public static void main(String[] args) {
        System.out.println("lab-3");
    }

    public static void task1() {
        var create = new Create("create", 1.0);
        var process = new Process("process", 2.0, 2);
        var dispose = new Dispose("dispose");

        var model = new Model(create, dispose);
        model.simulate(100.0);
    }
}