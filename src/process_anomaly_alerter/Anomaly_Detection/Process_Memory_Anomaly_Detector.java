/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package process_anomaly_alerter.Anomaly_Detection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
/**
 *
 * @author naval_
 */
public class Process_Memory_Anomaly_Detector {
    public static void main(String args[]){
        String host="192.168.188.133";
        String trainedFile = host+"_trained.log";
        String SplitBy = ",";
        String newtrainedData = "";
        File readData = new File(host+".log");
        File trainedData = new File(trainedFile);
        Path file = Paths.get(trainedFile);
        try {
        Scanner train_Data = new Scanner(trainedData);
        Scanner read_Data = new Scanner(readData);
        int counter=0;
            while(read_Data.hasNextLine()) {
		String readLine = read_Data.nextLine();
                String[] newData = readLine.split(SplitBy);
                while(true){
                    if(train_Data.hasNextLine()){
                        String[] Data = train_Data.nextLine().split(SplitBy);
                        if(Data[11].equals(newData[11])){
                            System.out.println("yeah");
                        }
                    } else {
                            System.out.println(counter++);
                        newtrainedData = newtrainedData+readLine+"\n";
                        break;
                    }
                    
                }
                
            }
        System.out.println(newtrainedData);
        System.out.println();
    Writer writer = null;
    try {
        writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(trainedFile), StandardCharsets.UTF_8));
        writer.write(newtrainedData);
    } catch (IOException ex) {
        
    } finally {
        try {writer.close();} catch (Exception ex) {/*ignore*/}
    }                       
        } catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} 
    }
}
