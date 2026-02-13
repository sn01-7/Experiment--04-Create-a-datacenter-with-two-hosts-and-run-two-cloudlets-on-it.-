# Experiment  Create-a-datacenter-with-two-hosts-and-run-two-cloudlets-on-it.
Create a datacenter with two hosts and run two cloudlets on it. The cloudlets run in VMswith Different MIPS requirements. The cloudlets will take different time to complete the execution Depending on the requested VM performance.
 
## Overview
This project demonstrates a CloudSim 3.0.3 simulation experiment where two cloudlets are executed on virtual machines with different MIPS values.

Since the virtual machines have different computational capacities, the cloudlets complete execution at different times depending on VM performance.

This experiment is commonly used in cloud computing laboratory exercises, academic coursework, and performance analysis studies.

---

## Aim
To create a datacenter with two hosts and execute two cloudlets on virtual machines having different MIPS requirements such that cloudlets take different execution times.

---

## Objectives
- To simulate a cloud datacenter using CloudSim
- To create multiple hosts within a datacenter
- To configure virtual machines with different MIPS values
- To execute cloudlets with identical workloads
- To analyze execution time differences

---

## Tools and Technologies

| Tool | Version |
|------|---------|
| Java JDK | 1.8 |
| CloudSim | 3.0.3 |
| IDE | IntelliJ IDEA / Eclipse |
| Operating System | Windows / Linux |

---

## System Configuration

### Datacenter Configuration

| Parameter | Value |
|------------|-------|
| Datacenters | 1 |
| Hosts | 2 |
| RAM | 4096 MB |
| Storage | 1,000,000 MB |
| Bandwidth | 10,000 Mbps |
| VM Scheduler | Time Shared |

---

### Virtual Machine Configuration

| Parameter | VM1 | VM2 |
|------------|------|------|
| MIPS | 500 | 2000 |
| RAM | 512 MB | 512 MB |
| Bandwidth | 1000 Mbps | 1000 Mbps |
| VMM | Xen | Xen |
| Scheduler | Time Shared | Time Shared |

---

### Cloudlet Configuration

| Parameter | Value |
|------------|--------|
| Cloudlets | 2 |
| Length | 40,000 MI |
| File Size | 300 MB |
| Output Size | 300 MB |
| Processing Elements | 1 |

---

## Program File

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

---

## Algorithm

1. Initialize the CloudSim library.
2. Create a datacenter with two hosts.
3. Configure host resources.
4. Create a datacenter broker.
5. Create two virtual machines with different MIPS values.
6. Submit the VM list to the broker.
7. Create two cloudlets with identical workloads.
8. Assign cloudlets to VMs.
9. Start the CloudSim simulation.
10. Collect execution results and stop the simulation.

---

## Execution Time Formula

Execution Time = Cloudlet Length / VM MIPS

---

## Sample Output


---

## Result

The experiment successfully demonstrates that cloudlets executed on higher MIPS virtual machines complete faster than those executed on lower MIPS VMs, even when the workload is identical.

---

## Conclusion

This simulation validates that CloudSim scheduling accurately reflects performance differences in heterogeneous cloud environments. Higher MIPS virtual machines reduce execution time for identical workloads.




