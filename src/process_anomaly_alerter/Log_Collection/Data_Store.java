package process_anomaly_alerter.Log_Collection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 *
 * @author naval_
 * This method helps to fetch credentials of the clients 
 * which are available to be connected to the server via the remote login service.
 */
public class Data_Store {


    public String[][] Fetch_Client_Credentials() {
	String csvFile = "UserList.csv";
        File csv = new File(csvFile);
	String line = "";
	String cvsSplitBy = ",";
        int counter=0;
        String[][] Allusers = new String[100][3];
        try {
                Scanner scan_csv = new Scanner(csv);
		while (scan_csv.hasNextLine()) {
			String[] Credentials = scan_csv.nextLine().split(cvsSplitBy);
                        for(int i=0;i<3;i++)
                            Allusers[counter][i]=Credentials[i];
                        counter++;
		}
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} 
        return Allusers;
    }
    
    
   Boolean Store_Data(String Client_Name,String host, String Log_Data){
    Writer writer = null;
    try {
        writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(host+".log"), StandardCharsets.UTF_8));
        // For appending the log file instead of overwriting
        // new FileOutputStream(Client_Name+"_"+host+".log",true), StandardCharsets.UTF_8));
        writer.write(Log_Data);
    } catch (IOException ex) {
        return false;
    } finally {
        try {writer.close();} catch (Exception ex) {/*ignore*/}
    }
    return true;
   }
}
