package processanomaly;

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
public class BlacklistAnomaly 
{    
    public static void main(String[] args) throws IOException 
    {
        readFromFile();     
    }
    //This method is used to read and split the log files to fetch the processName column
    public static void readFromFile() throws FileNotFoundException, IOException
    {        
        BufferedReader br = new BufferedReader(new FileReader("/Users/Sneha/log2.txt"));
        String line;
        while((line = br.readLine()) != null)
        {
            String[] splited = line.split("\\t");
            
            checkIfBlackListedProcess(splited[3]);
        }
        br.close();
    }
    // This method is used to check if a blacklist process is present in the log file
    public static void checkIfBlackListedProcess(String processName) throws FileNotFoundException, IOException
    {
        //Reading from blacklist.txt to an ArrayList
        BufferedReader in = new BufferedReader(new FileReader("/Users/Sneha/Documents/blacklist.txt"));
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
