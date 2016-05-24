package process_anomaly_alerter.Anomaly_Detection;

import java.io.BufferedReader;
import java.io.File;
import static java.io.FileDescriptor.in;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Collections.list;
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

    
    public BlackListAnomaly(File newLogfile, String IPAddress, boolean isTrainingProcess) throws IOException {
        readFromFile(newLogfile);
    }
    
    //This method is used to read and split the log files to fetch the processName column
    public static void readFromFile(File newLogfile) throws FileNotFoundException, IOException
    {        
        Scanner SC = new Scanner(newLogfile);
        String line;
        while((line = SC.nextLine()) != null)
        {
            String[] splited = line.split(",");
            
            checkIfBlackListedProcess(splited[11]);
        }
        SC.close();
    }
    // This method is used to check if a blacklist process is present in the log file
    public static void checkIfBlackListedProcess(String processName) throws FileNotFoundException, IOException
    {
        //Reading from blacklist.txt to an ArrayList
        BufferedReader in = new BufferedReader(new FileReader("blacklist.txt"));
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
              System.out.println("Alert!!! " + "Blacklist process "+ processName +" detected!!");
          }
        }
    }
}
