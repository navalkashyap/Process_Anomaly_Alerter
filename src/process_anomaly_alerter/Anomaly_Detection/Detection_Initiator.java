package process_anomaly_alerter.Anomaly_Detection;
import process_anomaly_alerter.Log_Collection.Data_Store;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Detection_Initiator extends TimerTask {

	@Override
	public void run() {
		 Data_Store DS = new Data_Store();
		 String[][] Allusers= DS.Fetch_Client_Credentials(); 
		 for(int i=0; Allusers[i][0] != null; i++)
		 {
			String fileName =  Allusers[i][2] +".log"; 
			//PLEASE NOTE: THIS IS NOT THE FINAL!! Parameters are to be added here properly after editing is done. 
			new Process_Whitelist_Anomaly_Detector(new File(fileName), Allusers[i][2],true);
			new Process_Memory_Anomaly_Detector(new File(fileName), Allusers[i][2],true ); 			
                     /*try {
                         new BlackListAnomaly(new File(fileName), Allusers[i][2],true);
                     } catch (IOException ex) {
                         Logger.getLogger(Detection_Initiator.class.getName()).log(Level.SEVERE, null, ex);
                     }
                             */
                 }
	} 
	public void setTimer(){ 
		Timer timer = new Timer();
		//delay in ms,time in ms between successive task operations 
		timer.schedule(new Detection_Initiator(), 0, 10000);
	}
	
}
