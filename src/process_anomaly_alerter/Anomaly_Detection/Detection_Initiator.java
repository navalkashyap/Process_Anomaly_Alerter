package process_anomaly_alerter.Anomaly_Detection;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import process_anomaly_alerter.Log_Collection.Data_Store;

public class Detection_Initiator extends TimerTask {

	@Override
	public void run() {
		 Data_Store DS = new Data_Store();
		 String[][] Allusers= DS.Fetch_Client_Credentials(); 
		 for(int i=0; i<Allusers.length; i++)
		 {
			String fileName =  Allusers[i][2] +".log"; 
			
			//PLEASE NOTE: THIS IS NOT THE FINAL!! Parameters are to be added here properly after editing is done. 
			new Process_Whitelist_Anomaly_Detector(new File(fileName), Allusers[i][1],true);
			new Process_Memory_Anomaly_Detector(new File(fileName), Allusers[i][1],true ); 
			
		 }
		
	} 
	public void setTimer(){ 
		Timer timer = new Timer();
		//delay in ms,time in ms between successive task operations 
		timer.schedule(new Detection_Initiator(), 0, 10000);
	}
	
}
