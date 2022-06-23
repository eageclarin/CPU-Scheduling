import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.min;

/** schedule processes **/
class roundRobin extends TimerTask {
    private final process process[]; static int isNotP;
    int time = 0; int currentBT, currentID, q, prevID = Integer.MAX_VALUE, counter=0;
    ArrayDeque<process> queue = new ArrayDeque<process>();

    roundRobin (process process[], int quantum, boolean isNotPreemptive) {
        this.process = process; q = quantum;
        if (isNotPreemptive == false) { isNotP = 0; } else { isNotP = 1; }
        currentBT = Integer.MAX_VALUE;
    }

    public void run() {
        //System.out.printf(java.time.LocalTime.now()+"\t");
        //start scheduling at first arrivalTime
        if (time >= process[0].aT) {
            completeTask(time);
        } else {
            try {
                //System.out.printf("No Process Yet\n");
                System.out.printf("|\t");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        time++;
    }

    private void completeTask(int time) {
        try {
            //only traverse through processes with burstTime > 0
            for (int i=0; i<process.length; i++) {
                if (process[i].aT == time && process[i].bT > 0) {
                    queue.add(process[i]); //add process to queue with aT at current time
                } 
            }
            
            currentID = queue.peekFirst().p - 1; //get index of first element
            if (counter == q) { //if counter reached time quantum
                queue.add(process[currentID]); queue.removeFirst(); //transfer from head to end of queue
                currentID = queue.peekFirst().p-1; //get index of first element
                counter = 0; //reset counter
            } counter++;

            //reduce burst time and add execution time of current process
            process[currentID].reduceBurstTime(); process[currentID].addExecutionTime();
            currentBT = process[currentID].bT;

            //check current burst time; if 0, reset its value for next process
            if (currentBT == 0) {
                queue.removeFirst();
                process[currentID].setCompletionTime(time+1);
                currentBT = Integer.MAX_VALUE;
                counter = 0;
            }

            //print gantt chart
            //isntead of: System.out.printf("Running Process P"+process[currentID].p+"...\n");
            if (currentID == prevID) { //if same process
                System.out.printf("\t");
            } else { //if process was changed
                System.out.printf("|   P"+process[currentID].p+"\t");
                prevID = currentID;
            }

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class rr {
    //startSchedule: start the timer, start scheduling processes
    public void startSchedule(process process[], int completionTime, int quantum, boolean isNotPreemptive) {
        //Reference: https://www.journaldev.com/1050/java-timer-timertask-example
        Timer timer = new Timer(true);
        timer.schedule(new roundRobin(process, quantum, isNotPreemptive), 0, 1000); //run task every 1 second

        try {
            Thread.sleep(completionTime*1000); //end timer after completion time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.cancel(); //all processes done
        System.out.printf("|"); 
    }

    //turnaroundTime & waitingTime: calculate turnaround and waiting time
    private int turnaroundTime(int cT, int aT) { return cT - aT; }
    private int waitingTime(int tAT, int bT) { return tAT - bT; }

    //print gantt chart borders
    private void printSeconds (int completionTime) {
        for (int i=0; i<=completionTime; i++) {
            System.out.printf(""+i+"\t");
        } System.out.println();
    }
    private void printTopBottom (int completionTime) {
        System.out.printf(" ");
        for (int i=0; i<completionTime; i++) {
            System.out.printf("--------");
        } System.out.println();
    }

    //schedule: main function
    public void schedule(ArrayList<Integer> p, ArrayList<Integer> burstTime, ArrayList<Integer> arrivalTime, int quantum) {
        process process[] = new process[p.size()]; int completionTime=0;
        
        //store to array
        for (int i=0; i<p.size() && i<10; i++) {
            int burst = burstTime.get(i);
            if (min(25, burst) == 0) { burst = ThreadLocalRandom.current().nextInt(25); }

            process[i] = new process(p.get(i), min(25, burst), min(25, arrivalTime.get(i)), 0);
            completionTime += burstTime.get(i);
        } completionTime += process[0].aT; //in case arrival time doesnt start at 0
        
        //print time in seconds; print top part of gantt chart
        System.out.println("GANTT CHART: (If needed, adjust window size and/or zoom out to get a straight gantt chart)");
        printSeconds(completionTime);
        printTopBottom(completionTime);

        //check if preemptive or not
        boolean isNotPreemptive = Arrays.stream( arrivalTime.toArray() ).allMatch( t -> t==arrivalTime.toArray()[0] ); //check if all elements match
        if (!isNotPreemptive) { //preemptive
            Arrays.sort(process, new sortByArrival());
            startSchedule(process, completionTime, min(10, quantum), false);
        } else { //nonpreemptive
            Arrays.sort(process, new sortByProcessID());
            startSchedule(process, completionTime, min(10, quantum), true);
        }

        System.out.println();
        printTopBottom(completionTime); //print bottom of gantt chart

        //print summary
        System.out.println("\nSUMMARY: Round Robin");
        System.out.printf("\tBurst Time\tArrival Time\tCompletion Time\tWaiting Time\tTurnaround Time\n");
        Arrays.sort(process, new sortByProcessID()); int sumTAT=0, sumWAT=0;
        for (int i=0; i<process.length; i++) {
            int cT = process[i].getCompletionTime();
            int tAT = turnaroundTime(cT, arrivalTime.get(i)); int wAT = waitingTime(tAT, burstTime.get(i));
            sumTAT += tAT; sumWAT += wAT;

            System.out.printf("P" +process[i].p+ "\t" +burstTime.get(i)+ "\t\t" +arrivalTime.get(i)+ "\t\t" +cT+ "\t\t" +wAT+ "\t\t" +tAT+ "\n");
        }
        float avgTAT = (float)sumTAT/process.length; float avgWAT = (float)sumWAT/process.length;
        System.out.printf("\nAverage Waiting Time: "+avgWAT+"s\nAverage Turnaround Time: "+avgTAT+"s\n");
    }
}
