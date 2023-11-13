import bank.BankModel;
import bank.SwitchingProcess;
import clinic.PatientCreate;
import clinic.RegistrationProcess;
import core.Process;
import core.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("lab-3");
        bank();
    }

    public static void bank() {
        var create = new Create("Create #1", 0.5, 0.1);
        var cashierWindow1 = new SwitchingProcess("Cashier window #1", 1, 0.3, 1, 2);
        var cashierWindow2 = new SwitchingProcess("Cashier window #2", 1, 0.3, 1, 2);
        var dispose = new Dispose("Dispose #1");

        cashierWindow1.initializeChannelsWithJobs(1);
        cashierWindow1.initializeQueueWithJobs(2);
        cashierWindow1.setNeighbors(cashierWindow2);
        cashierWindow2.initializeChannelsWithJobs(1);
        cashierWindow2.initializeQueueWithJobs(2);
        cashierWindow2.setNeighbors(cashierWindow1);

        create.setDistribution(Distribution.EXPONENTIAL);
        cashierWindow1.setDistribution(Distribution.EXPONENTIAL);
        cashierWindow1.setDelayMean(0.3);
        cashierWindow2.setDistribution(Distribution.EXPONENTIAL);
        cashierWindow2.setDelayMean(0.3);

        cashierWindow1.setMaxQueueSize(3);
        cashierWindow2.setMaxQueueSize(3);

        create.setRouting(Routing.BY_PRIORITY);
        create.addRoutes(
                new Route(cashierWindow1, 0.5, 1, () -> cashierWindow2.getQueueSize() < cashierWindow1.getQueueSize()),
                new Route(cashierWindow2, 0.5, 0)
        );

        cashierWindow1.addRoutes(
                new Route(dispose)
        );

        cashierWindow2.addRoutes(
                new Route(dispose)
        );

        var model = new BankModel(create, cashierWindow1, cashierWindow2, dispose);
        model.simulate(1000);
    }

    public static void clinic() {
        final int[] patientTypes = {1, 2, 3};
        final double[] patientFrequencies = {0.5, 0.1, 0.4};
        final double[] patientDelays = {15, 40, 30};

        var create = new PatientCreate("Patient Creator", 0.5);
        var registration = new RegistrationProcess("Registration", 1, 2);
        var wardsTransfer = new Process("Wards Transfer", 1, 2);
        var laboratoryTransfer = new Process("Laboratory Transfer", 1, 1000);
        var laboratoryRegistration = new Process("Laboratory Registration", 1, 1);
        var laboratoryAnalysis = new Process("Laboratory Analysis", 1, 2);
        var registrationTransfer = new Process("Registration Transfer", 1, 2);

        var wardsDispose = new Dispose("Dispose [Type 1 & 2]");
        var laboratoryDispose = new Dispose("Dispose [Type 3]");


        create.setPatientTypedFrequencies(patientTypes, patientFrequencies);
        registration.setPatientTypedDelays(patientTypes, patientDelays);
        registration.setPrioritizedPatientType(1);

        create.setDistribution(Distribution.EXPONENTIAL);
        registration.setDistribution(Distribution.EXPONENTIAL);
        wardsTransfer.setDistribution(Distribution.UNIFORM);
        laboratoryTransfer.setDistribution(Distribution.UNIFORM);
        laboratoryRegistration.setDistribution(Distribution.ERLANG);
        laboratoryAnalysis.setDistribution(Distribution.ERLANG);
        registrationTransfer.setDistribution(Distribution.UNIFORM);

        create.addRoutes(
                new Route(registration)
        );
        registration.addRoutes(
                new Route(wardsTransfer),
                new Route(laboratoryTransfer)
        );
        wardsTransfer.addRoutes(
                new Route(wardsDispose)
        );
        laboratoryTransfer.addRoutes(
                new Route(laboratoryRegistration)
        );
        laboratoryRegistration.addRoutes(
                new Route(laboratoryAnalysis)
        );
        laboratoryAnalysis.addRoutes(
                new Route(laboratoryDispose),
                new Route(registrationTransfer)
        );
        registrationTransfer.addRoutes(
                new Route(registration)
        );

    }
}