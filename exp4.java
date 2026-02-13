import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.*;

import java.util.*;

public class DifferentMipsExample {

    public static void main(String[] args) {

        try {
            // Initialize CloudSim
            CloudSim.init(1, Calendar.getInstance(), false);

            // Create Datacenter
            Datacenter datacenter = createDatacenter("Datacenter_0");

            // Create Broker
            DatacenterBroker broker = new DatacenterBroker("Broker");
            int brokerId = broker.getId();

            // Create VMs with DIFFERENT MIPS
            List<Vm> vmList = new ArrayList<>();

            Vm vm1 = new Vm(0, brokerId, 500, 1, 512, 1000, 10000,
                    "Xen", new CloudletSchedulerTimeShared());

            Vm vm2 = new Vm(1, brokerId, 2000, 1, 512, 1000, 10000,
                    "Xen", new CloudletSchedulerTimeShared());

            vmList.add(vm1);
            vmList.add(vm2);
            broker.submitVmList(vmList);

            // Create Cloudlets (same workload)
            List<Cloudlet> cloudletList = new ArrayList<>();

            UtilizationModel utilization = new UtilizationModelFull();

            Cloudlet cloudlet1 = new Cloudlet(0, 40000, 1,
                    300, 300, utilization, utilization, utilization);

            Cloudlet cloudlet2 = new Cloudlet(1, 40000, 1,
                    300, 300, utilization, utilization, utilization);

            cloudlet1.setUserId(brokerId);
            cloudlet2.setUserId(brokerId);

            cloudlet1.setVmId(0);
            cloudlet2.setVmId(1);

            cloudletList.add(cloudlet1);
            cloudletList.add(cloudlet2);

            broker.submitCloudletList(cloudletList);

            // Run Simulation
            CloudSim.startSimulation();

            List<Cloudlet> resultList = broker.getCloudletReceivedList();

            CloudSim.stopSimulation();

            printResults(resultList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DATACENTER =================
    private static Datacenter createDatacenter(String name) throws Exception {

        List<Host> hostList = new ArrayList<>();

        // Create TWO hosts
        for (int i = 0; i < 2; i++) {

            List<Pe> peList = new ArrayList<>();
            peList.add(new Pe(0, new PeProvisionerSimple(3000)));

            Host host = new Host(
                    i,
                    new RamProvisionerSimple(4096),
                    new BwProvisionerSimple(10000),
                    1000000,
                    peList,
                    new VmSchedulerTimeShared(peList)
            );

            hostList.add(host);
        }

        DatacenterCharacteristics characteristics =
                new DatacenterCharacteristics(
                        "x86", "Linux", "Xen",
                        hostList, 10.0, 3.0,
                        0.05, 0.1, 0.1);

        return new Datacenter(
                name,
                characteristics,
                new VmAllocationPolicySimple(hostList),
                new LinkedList<>(),
                0);
    }

    // ================= PRINT RESULTS =================
    private static void printResults(List<Cloudlet> list) {

        System.out.println("\n========== OUTPUT ==========");
        System.out.println("Cloudlet | VM | Status | ExecTime | Start | Finish");

        for (Cloudlet cl : list) {
            System.out.printf("%5d\t%3d\t%s\t%.2f\t%.2f\t%.2f\n",
                    cl.getCloudletId(),
                    cl.getVmId(),
                    cl.getStatus() == Cloudlet.SUCCESS ? "SUCCESS" : "FAILED",
                    cl.getActualCPUTime(),
                    cl.getExecStartTime(),
                    cl.getFinishTime());
        }
    }
}
