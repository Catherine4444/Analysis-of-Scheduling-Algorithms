# Analysis-of-Scheduling-Algorithms
This project investigates the difference in performance of The First Come First Serve (FCFS), Shortest Job First (SJF) and Round Robin (RR) scheduling algorithm by using the Barman simulation created by A/Prof Michelle Kuttel. 

The algorithms’ performance are compared according to their 
- CPU Utilization, Throughput,
- Turnaround Time,
- Response Time,
- Waiting Time,
- Starvation,
- Predictability, and
- Fairness.

Refer to the report.pdf for details on how the analysis was conducted, and the results. 

In src/barScheduling:
The instrumentation code can be found in Measure.java, Constants.java and searching Constants in the rest of the files

quantum.sh and algo.sh were written to automate the experimental process of finding an optimal quantum and assessing the algorithms, respectively.

graph.ipynb is where the graphs in the report were generated using graph_data/



