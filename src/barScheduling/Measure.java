package barScheduling;

import java.io.IOException;
import java.io.Writer;
import java.io.FileWriter;   
import java.io.File;  // Import the File class

public class Measure {

    //used for response time, waiting time, turnaround time, throughput, 
	private long[] startTime;
	private long[] endTime;
    private long[] totalTime;
    private File dataRecord;
    private String metric;
    

    public Measure(int sched, String metric, int patronCount, int quantum){
        this(sched, metric, 0, patronCount, quantum);
    }

    public Measure(int sched, String metric, int experimentNumber, int patronCount, int quantum){
        this.metric = metric;
        startTime = new long[patronCount];
        endTime = new long[patronCount];
        totalTime = new long[patronCount];

        if ((Constants.findQ && metric.equals("turnaround time")) || !(Constants.findQ)){
            String f = "q"+quantum+"_"+metric+"_"+experimentNumber;
            switch(sched) {
                case 0:
                    f = "FCFS "+f;
                  break;
                case 1:
                    f = "SJF "+f;
                  break;
                case 2:
                    f = "RR "+f;
            }
    
            this.dataRecord = new File("data/"+f+".txt");           // make sure file exists
            try{
                FileWriter writer = new FileWriter(dataRecord); // Clear file and add title
                writer.write(f+"\n");
                writer.close();
            }
            catch(IOException e){ System.out.println("error occurred");}
        }

    }

    public void tick(int patronId){
        startTime[patronId] = System.currentTimeMillis();
    }

    public void tock(int patronId){ //end timing
		endTime[patronId] = System.currentTimeMillis();
        totalTime[patronId] +=  (endTime[patronId]-startTime[patronId]);
        //System.out.println(totalTime[patronId]);
	}

    private long record(long time, String description){
        if ((Constants.findQ && metric.equals("turnaround time")) || !(Constants.findQ)){
            try{
                FileWriter writer = new FileWriter(dataRecord, true);
                writer.write(description + Long.toString(time)+"\n");
                writer.close();
            }
            catch(IOException e){ System.out.println("error occurred at record time");}
            //System.out.println(time);
        }
        return time;
    }

    public long recordStart(){
        // This method is mostly for throughput and cpu utilization 
        return record(System.currentTimeMillis(), "start time: ");
    }

    public long recordEnd(){
        // This method is mostly for throughput and cpu utilization 
        return record(System.currentTimeMillis(), "End Time: ");
    }

    public void recordCPUU(){
        // used for cpu utilisation 
        try{
            FileWriter writer = new FileWriter(dataRecord, true);
            for (int i =0; i < startTime.length; i++){
                writer.write(totalTime[i]+"\n");
            }
            writer.close();
        }
        catch(IOException e){ System.out.println("error occurred at record time");}
    }

    public void recordDuration(){
        // used for waiting time, turnaround time, response time 
        if ((Constants.findQ && metric.equals("turnaround time")) || !(Constants.findQ)){
            try{
                FileWriter writer = new FileWriter(dataRecord, true);
                for (int i =0; i < totalTime.length; i++){
                    writer.write(totalTime[i]+"\n");
                }
                writer.close();
            }
            catch(IOException e){ System.out.println("error occurred at record time");}
        }
    }

    public void recordEndArray(){
        // used for throughput 
        if ((Constants.findQ && metric.equals("turnaround time")) || !(Constants.findQ)){
            try{
                FileWriter writer = new FileWriter(dataRecord, true);
                for (int i =0; i < endTime.length; i++){
                    writer.write(endTime[i]+"\n");
                }
                writer.close();
            }
            catch(IOException e){ System.out.println("error occurred at record time");}
        }
    }
    

    


}
