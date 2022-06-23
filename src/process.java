import java.util.*;

//constructor
class process {
    int p, bT, aT, eT, pT = 0, cT;

    process(int process, int burstTime, int arrivalTime, int priority) {
        this.p = process;
        this.bT = burstTime;
        this.aT = arrivalTime;
        this.pT = priority;
    }

    void setCompletionTime(int cT) { this.cT = cT; }
    int getCompletionTime() { return this.cT; }
    int reduceBurstTime() { this.bT -= 1; return bT; }
    int addExecutionTime() { this.eT += 1; return eT; }
}

//Reference: https://www.geeksforgeeks.org/arrays-sort-in-java-with-examples/
/** sort by arrivalTime **/
class sortByArrival implements Comparator<process> {
    public int compare(process a, process b) {
        return a.aT - b.aT;
    }
}

/** sort by burstTime **/
class sortByBurst implements Comparator<process> {
    public int compare(process a, process b) {
        return a.bT - b.bT;
    }
}

/** sort by processID **/
class sortByProcessID implements Comparator<process> {
    public int compare(process a, process b) {
        return a.p - b.p;
    }
}

/** sort by priority **/
class sortByPriority implements Comparator<process> {
    public int compare(process a, process b) {
        return a.pT - b.pT;
    }
}

