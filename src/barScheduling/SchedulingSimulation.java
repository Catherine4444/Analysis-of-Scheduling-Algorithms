//M. M. Kuttel 2025 mkuttel@gmail.com

package barScheduling;
// the main class, starts all threads and the simulation


import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;


public class SchedulingSimulation {
	static int noPatrons=10; //number of customers - default value if not provided on command line
	static int sched=0; //default scheduling algorithm, 0= FCFS, 1=SJF, 2=RR
	static int q=10000, s=0;
	static long seed=0;
	static CountDownLatch startSignal;	
	static Patron[] patrons; // array for customer threads
	static Barman Sarah;

	public static void main(String[] args) throws InterruptedException, IOException {

		//deal with command line arguments if provided
		if (args.length>=1) noPatrons=Integer.parseInt(args[0]);  //total people to enter room
		if (args.length>=2) sched=Integer.parseInt(args[1]); 	// alg to use
		if (args.length>=3) s=Integer.parseInt(args[2]);  //context switch 
		if(args.length>=4) q=Integer.parseInt(args[3]);  // time slice for RR
		if(args.length>=5) seed=Integer.parseInt(args[4]); // random number seed- set to compare apples with apples	

		if(args.length>=6) Constants.experimentNumber =Integer.parseInt(args[5]); // [for naming datafiles]
		if(args.length>=7)	Constants.findQ = (Integer.parseInt(args[6]) == 1) ? true : false; 	// if true, only measure turnaround time. 
																			// Useful for determining time quantum
		System.out.println(Constants.findQ);
		if (Constants.experimentNumber>0){
			Constants.turnaroundTime = new Measure(sched, "turnaround time", Constants.experimentNumber, noPatrons,q);
			if (!Constants.findQ){
				Constants.waitingTime = new Measure(sched, "waiting time", Constants.experimentNumber, noPatrons,q);
				Constants.responseTime = new Measure(sched,"response time", Constants.experimentNumber, noPatrons, q);
				Constants.cpuUtilization = new Measure(sched, "cpu utilization", Constants.experimentNumber, noPatrons, q);
				Constants.throughput = new Measure(sched,"throughput", Constants.experimentNumber, noPatrons, q);
			}
		}
		else{
			Constants.turnaroundTime = new Measure(sched, "turnaround time", noPatrons, q);
			if (!Constants.findQ){
				Constants.waitingTime = new Measure(sched, "waiting time", noPatrons, q);
				Constants.responseTime = new Measure(sched,"response time", noPatrons, q);
				Constants.cpuUtilization = new Measure(sched, "cpu utilization", noPatrons, q);
				Constants.throughput = new Measure(sched,"throughput", noPatrons, q);
			}
		}

		startSignal= new CountDownLatch(noPatrons+2);//Barman and patrons and main method must be ready
		
		//create barman
        Sarah= new Barman(startSignal,sched,q,s); 
     	Sarah.start();
  
	    //create all the patrons, who all need access to Barman
		patrons = new Patron[noPatrons];
		for (int i=0;i<noPatrons;i++) {
			patrons[i] = new Patron(i,startSignal,Sarah,seed);
			patrons[i].start();
		}
		
		if (seed>0) DrinkOrder.random = new Random(seed);// for consistent Patron behaviour

		
		System.out.println("------Sarah the Barman Scheduling Simulation------");
		System.out.println("-------------- with "+ Integer.toString(noPatrons) + " patrons---------------");
		switch(sched) {
		  case 0:
			  System.out.println("-------------- and FCSF scheduling ---------------");
		    break;
		  case 1:
			  System.out.println("-------------- and SJF scheduling ---------------");
		    break;
		  case 2:
			  System.out.println("-------------- and RR scheduling with q="+q+"-------------");
		}
		
			
      	startSignal.countDown(); //main method ready
      	
      	//wait till all patrons done, otherwise race condition on the file closing!
      	for (int i=0;i<noPatrons;i++)  patrons[i].join();

    	System.out.println("------Waiting for Barman------");
    	Sarah.interrupt();   //tell Barman to close up
    	Sarah.join(); //wait till she has
      	System.out.println("------Bar closed------");
		System.out.println("------Writing data to file------");
		
		Constants.turnaroundTime.recordDuration();
		if (!Constants.findQ){
			Constants.throughput.recordEndArray();
			Constants.cpuUtilization.recordCPUU();
			Constants.waitingTime.recordDuration();
			Constants.responseTime.recordDuration();
		}
		
 	}
}
