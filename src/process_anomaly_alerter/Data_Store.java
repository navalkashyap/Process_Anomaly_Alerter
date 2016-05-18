/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package process_anomaly_alerter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author naval_
 */
public class Data_Store {
    public void Data_Store(){
        
    }
    
    public int Fetch_list_of_Clients(){
        String csvFile = "UserList.csv";
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
        int counter=0;
        String[] Allusers = new String[100];
	try {
		br = new BufferedReader(new FileReader(csvFile));
		while ((line = br.readLine()) != null) {
			String[] Credentials = line.split(cvsSplitBy);
                        Allusers[counter++]=Credentials[0];
			System.out.println("Credentials [Name= " + Credentials[0] + "]");
		}
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}        
        return counter;
    }
    
    
    
    public String[][] Fetch_Client_Credentials() {
	String csvFile = "UserList.csv";
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
        int counter=0;
        String[][] Allusers = new String[100][3];
	try {
		br = new BufferedReader(new FileReader(csvFile));
		while ((line = br.readLine()) != null) {
			String[] Credentials = line.split(cvsSplitBy);
                        for(int i=0;i<3;i++)
                            Allusers[counter][i]=Credentials[i];
                        counter++;
                        System.out.println("Credentials [Name= " + Credentials[0] + " , Password= " + Credentials[1] + " Host= "+Credentials[2]+ "]");
		}
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
        return Allusers;
    }
    
    
   Boolean Store_Data(String Client_Name,String host, String Log_Data){
    Writer writer = null;
    try {
        writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(Client_Name+"_"+host+".log"), StandardCharsets.UTF_8));
        writer.write(Log_Data);
    } catch (IOException ex) {
        return false;
    } finally {
        try {writer.close();} catch (Exception ex) {/*ignore*/}
    }
    return true;
   }
}
