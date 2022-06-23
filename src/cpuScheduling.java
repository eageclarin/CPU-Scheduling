import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class cpuScheduling {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        ArrayList<Integer> process = new ArrayList<Integer>();
        ArrayList<Integer> burstTime = new ArrayList<Integer>();
        ArrayList<Integer> arrivalTime = new ArrayList<Integer>();
        ArrayList<Integer> priority = new ArrayList<Integer>();
        
        System.out.print("\033[H\033[2J");
        System.out.printf("Provide file name containing process information: ");
        String fileName = input.next();
        File file = new File(fileName);
        System.out.println("Checking...");

        if (file.exists()) { //if file exists
            Scanner scanFile = new Scanner(new File(fileName));

            if (file.length() == 0) { //if file is empty
                System.out.println("File is empty.");
                input.close(); return;
            } else { //read file
                try {
                    scanFile.nextLine();
                    while (scanFile.hasNext()) {
                        process.add(Character.getNumericValue(scanFile.next().charAt(1)));
                        burstTime.add(scanFile.nextInt());
                        arrivalTime.add(scanFile.nextInt());
                        priority.add(scanFile.nextInt());
                    }
                } catch (InputMismatchException e) {
                    System.out.print("Some process information is missing.");
                    input.close(); return;
                }
                
            }
        } else { //file doesnt exist
            System.out.println("\nNo such file in directory.\n");
            input.close(); return;
        }

        TimeUnit.SECONDS.sleep(1);
        System.out.print("\033[H\033[2J");

        loop: while (true) {
            System.out.println("Choose an Algorithm:\nA. Shortest-Job First\nB. Priority Scheduling\nC. Round Robin\n\n");
            String algo = input.next().toUpperCase(); //automatically convert to uppercase
            if (algo.length() > 1) { algo = String.valueOf(algo.charAt(0)); } //in case user includes the .

            switch(algo) {
                case "A": //shortest-job first
                    System.out.println("You've chosen... Shortest-Job First"); 
                    System.out.println("Please wait..."); TimeUnit.SECONDS.sleep(1);
                    System.out.print("\033[H\033[2J");

                    System.out.println("**** SHORTEST-JOB FIRST ****");
                    sjf cpuSJF = new sjf();
                    cpuSJF.schedule(process, burstTime, arrivalTime);
                    break loop;
                case "B": //priority scheduling
                    System.out.println("You've chosen... Priority Scheduling"); 
                    System.out.println("Please wait..."); TimeUnit.SECONDS.sleep(1);
                    System.out.print("\033[H\033[2J");

                    System.out.println("**** PRIORITY SCHEDULING ****");
                    ps cpuPS = new ps();
                    cpuPS.schedule(process, burstTime, arrivalTime, priority);
                    break loop;
                case "C": //round robin
                    System.out.println("You've chosen... Round Robin"); 
                    System.out.println("Please wait..."); TimeUnit.SECONDS.sleep(1);
                    System.out.println("Provide time quantum in seconds: ");
                    try {
                        int quantum = input.nextInt();
                        System.out.print("\033[H\033[2J");
                        System.out.println("**** ROUND ROBIN ****");
                        rr cpuRR = new rr();
                        cpuRR.schedule(process, burstTime, arrivalTime, Math.min(10, quantum));
                    } catch(InputMismatchException e) {
                        System.err.println("Wrong input! Input again:");
                        input.nextLine();
                    }
                    
                    break loop;
                default:
                    System.out.print("\033[H\033[2J");
                    System.out.println("Invalid chocie. Choose again.");
            }
        }

        input.close();
    }
}
