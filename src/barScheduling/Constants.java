package barScheduling;


public class Constants{
    static boolean findQ = false;
    static int experimentNumber;
	// Measures without specifying the run number 
	static Measure turnaroundTime;
	static Measure waitingTime;
	static Measure responseTime;
	static Measure cpuUtilization;
	static Measure throughput;
    

    public static void setFindQToTrue(){
        findQ = true;
    }
}
