# CPU Scheduling

It is the CPU scheduling algorithms which manages which process will use a given resource at a time. The focus of such algorithms is to maximize CPU resources usage and minimize waiting time for each process. In this machine problem, the different CPU algorithms that was simulated are `Shortest-Job- First`, `Priority Scheduling`, and `Round Robin`.

## Shortest-Job-First
Shortest-Job-First (SJF) is a scheduling algorithms that selects the waiting process with the shortest burst time to execute next. SJF CPU scheduling in preemptive mode allocates resources to processes based on the shortest remaining time to completion.

## Priority Scheduling
In Priority Scheduling, processes are executed according to their priority. The process with the highest priority is to be executed first. If the processes have the same priority, then they are sorted according to their Arrival Time.

## Round Robin
Round Robin is used to fairly schedule the processes. A fixed amount of time, quantum, is provided for each process to execute. Once a process is executed for the given time period, it is moved to the end of the queue, and other processes executes for the same time quantum.

### Reminder
`test.txt` contains the process informaiton that will be used for the CPU Scheduling.
