package process_anomaly_alerter.Anomaly_Detection;
import process_anomaly_alerter.Log_Collection.Data_Store;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Detection_Initiator extends TimerTask {
    Boolean isTrainingProcess = false;
    int iteration_period = 10000;  // in milliseconds
    
    public Detection_Initiator(int iteration_period){
        this.iteration_period=iteration_period;
    }
    
	@Override
	public void run() {
            Data_Store DS = new Data_Store();
            String[] hostList= DS.Fetch_list_of_Clients(); 
            for(int i=0; hostList[i]!=null; i++) {
                String fileName =  hostList[i].split(";")[0] +".log";
                String hostName = hostList[i].split(";")[0];
                String adminList = hostList[i].split(";")[1];
                //PLEASE NOTE: THIS IS NOT THE FINAL!! Parameters are to be added here properly after editing is done. 
                new Process_Whitelist_Anomaly_Detector(new File(fileName), hostName, adminList,isTrainingProcess);
                new Process_Memory_Anomaly_Detector(new File(fileName), hostName, adminList,isTrainingProcess ); 			
                try {
                    if(!isTrainingProcess)
                        new BlackListAnomaly(new File(fileName), hostName, adminList,true);
                } catch (IOException ex) {
                    Logger.getLogger(Detection_Initiator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        
	public void setTimer(){ 
		Timer timer = new Timer();
		//delay in ms,time in ms between successive task operations 
		timer.schedule(new Detection_Initiator(iteration_period), 0, 10000);
	}
	
}
