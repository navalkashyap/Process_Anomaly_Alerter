package process_anomaly_alerter.Anomaly_Detection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Sneha
 */
// This class demonstrates an anomaly detection tool that identifies a blacklisted process in the log file and throws an alert 
public class BlackListAnomaly {    

    String host = "";
    String blacklistedFile = "blacklist.txt";
    String emailSubject="High alert! Blacklisted process detected in host ";
    String emailList = "";
    public BlackListAnomaly(File newLogfile, String host, String emailList, boolean isTrainingProcess) throws IOException {
        this.host=host;
        this.emailList=emailList;
        readFromFile(newLogfile);
        
    }
    
    //This method is used to read and split the log files to fetch the processName column
    public  void readFromFile(File newLogfile) throws FileNotFoundException, IOException
    {        
        StringBuilder newAnomaly = new StringBuilder();
        Scanner SC = new Scanner(newLogfile);
        while(SC.hasNextLine()) {
            String[] splited = SC.nextLine().split(",");
            if(checkIfBlackListedProcess(splited[11]))
                newAnomaly.append(splited[11]);
        }
        SC.close();
        if(newAnomaly.length()!=0){
            new SendEmail(emailList,newAnomaly.toString(), emailSubject+host);
        }
    }
    // This method is used to check if a blacklist process is present in the log file
    public  Boolean checkIfBlackListedProcess(String processName) throws FileNotFoundException, IOException
    {
        //Reading from blacklist.txt to an ArrayList
        BufferedReader in = new BufferedReader(new FileReader(blacklistedFile));
        String str;
        ArrayList<String> list = new ArrayList<String>();
        while((str = in.readLine()) != null){
            list.add(str);
        }
        String[] stringArr = list.toArray(new String[0]);
        // Compare if processName matches any entries in the blacklist including substring matches
        // Loop through everything in arraylist and see check for each individual entry if there is full/ substring match in processname
        for (int i = 0; i<list.size(); i++)
        {
            if(processName.contains(list.get(i)))
            {
                return true;
              //System.out.println("Alert!!! " + "Blacklist process "+ processName +" detected!!");
            }
        }
        return false;
    }
}
