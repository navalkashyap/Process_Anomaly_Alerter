package process_anomaly_alerter.Anomaly_Detection;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Properties;
import java.io.FileWriter;
import java.io.IOException;

// This class implies a Log Anomaly Detector tool that detects anomalies based on logs
public class Process_Whitelist_Anomaly_Detector {

	static ArrayList<String> anomalies = new ArrayList<String>();
	static StringBuilder anomaly = new StringBuilder();
	static HashSet<String> oldLogHashSet = new HashSet<String>();
        static String trainingFile="Whitelist_Trained_Logs.log";
	// This Constructor will be called by the Detection Initiator to execute the tool
	// This tool will look at the new logs and find if there are new anomaly processes
	// If yes, it will alert the Administrator
	// newLogFile: new log Fie object for fetching the new logs
	// IPAddress: ip address of the client
	// isTrainingProcess: It lets us know if the initiator has initiated a training process or not
	public Process_Whitelist_Anomaly_Detector(File newLogfile, String IPAddress, boolean isTrainingProcess) {
		File oldLogfile = new File(trainingFile);
		SearchLogFiles(oldLogfile);
		boolean isAnomalyFound = compareLogData(newLogfile);
		if(isAnomalyFound && !isTrainingProcess) {
			SendEmail sendEmailObject = new SendEmail(anomaly, IPAddress);
		}
	}
	
	// This method is used to scan the old log file and storing that in a hash set
	// oldLogFile: the old log File Object used to fetch old logs
	public static void SearchLogFiles(File oldLogfile){	
		Scanner scanOldLogFile;
		try {
			scanOldLogFile = new Scanner(oldLogfile);
			while(scanOldLogFile.hasNextLine()) {
				String lastlogpart = FetchLastLogPart(scanOldLogFile.nextLine());
				oldLogHashSet.add(lastlogpart);
				}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	// This method is used Split the log data to fetch the last column of the data(the process name/command running)
	// logdata: logdata that needs to splitted
	public static String FetchLastLogPart(String logdata){
		String[] logparts = logdata.split(",");
		String lastLogPart = logparts[logparts.length - 1];
		return lastLogPart;
	}

	// This method compares the Old log file data with the new log file data and creates a String of anomalies if found
	// newLogFile : new log Fie object for fetching the new logs
	public static boolean compareLogData(File newLogfile){
		Scanner scanNewLogFile;
		boolean FLAG = false;
		try {
			scanNewLogFile = new Scanner(newLogfile);
			FileWriter writerOut = openOldLogFileToWrite();
			while(scanNewLogFile.hasNextLine()) {
				String logdata = FetchLastLogPart(scanNewLogFile.nextLine());
				if(!oldLogHashSet.contains(logdata)) {
						anomalies.add(logdata);
						writeNewLogToOldLogs(writerOut, logdata);
					}
			}
			writerOut.close();
			for(String a : anomalies){
				anomaly.append(a);
				anomaly.append("\n");
				FLAG = true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FLAG;

	}

	// This method writes new logs to old log file 
	// writerOut : FileWriter Object used to write
	// logdata : logdata to be written
	public static void writeNewLogToOldLogs(FileWriter writerOut, String logdata){
		try {
			writerOut.write(logdata + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// This method opens the Old Log file for appending the new logs 
	// returns FileWriter Object to write with
	public static FileWriter openOldLogFileToWrite(){
		FileWriter fw = null;
		try {
	   		fw = new FileWriter(trainingFile, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return fw;
	}

}
