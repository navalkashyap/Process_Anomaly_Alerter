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
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static process_anomaly_alerter.Anomaly_Detection.Process_Whitelist_Anomaly_Detector.anomaly;
/**
 *
 * @author naval_
 */
public class Process_Memory_Anomaly_Detector {
    
    Process_Memory_Anomaly_Detector(File newLogfile, String host, String emailList, boolean isTrainingProcess) {
        
        if(isTrainingProcess){
            TrainDataSet(newLogfile,host);
        } else
            DetectAnomaly(newLogfile,host,emailList);
    }
    
    static String computeRange(String input,String lowerrange,String higherrange){
        String range=null;
        float Range = Float.parseFloat(input);
        float lowerRange = Float.parseFloat(lowerrange);
        float higherRange = Float.parseFloat(higherrange);
        if(Range<lowerRange)
            lowerRange = Range;
        else if(Range>higherRange)
            higherRange = Range;
        range = ","+Float.toString(lowerRange)+","+Float.toString(higherRange);
        return range;
    }
    
    // Return True: when input is outside the range
    static Boolean compareRange(String input,String lowerrange,String higherrange){
        if(Float.parseFloat(input)<Float.parseFloat(lowerrange))
            return true;
        else if(Float.parseFloat(input)>Float.parseFloat(higherrange))
            return true;
        else
            return false;
    }
    
    // If return is False : Alert occured first time for that host and need to alert the Admin
    static Boolean AlertAlreadySent(String Process, String host, String alertFile,int alertLifeTime) throws FileNotFoundException, IOException{
        String line="",updateAlert="",updateLine="",Split=",",BSplit=";";
        Boolean sendAlert=false;
        Date date = new Date(); 
        File readAlertFile = new File(alertFile);
        if(!readAlertFile.exists())
            readAlertFile.createNewFile();
        Scanner read_Alert = new Scanner(readAlertFile);
        int newAlertAdded = -1;         //Increment this counter whenever line was
        while(read_Alert.hasNextLine()) {
            line=read_Alert.nextLine();
            if(line.contains(Process)){
                newAlertAdded++;
                updateAlert=updateAlert+line.split(BSplit)[0];
                for(int i = 1;i<line.split(BSplit).length;i++){
                    String temp = line.split(BSplit)[i];
                    if(temp.contains(host)){
                        if(date.getTime()-Long.parseLong(temp.split(Split)[1]) > alertLifeTime) {
                            updateLine=updateLine+";"+host+","+date.getTime();
                            sendAlert=true;
                        } else 
                            updateLine=updateLine+";"+temp;                        
                    } else {
                        updateLine=updateLine+";"+temp;
                    }
                }
                if(!updateLine.contains(host)){
                    updateLine=updateLine+";"+host+","+date.getTime();                          
                    sendAlert=true;
                }
                updateAlert+=updateLine+"\n";
            } else {
                updateAlert=updateAlert+line+"\n";
            }
        }       
        if(newAlertAdded==-1){    // this means this process never added before
            updateAlert=updateAlert+Process+";"+host+","+date.getTime()+"\n";
            sendAlert=true;
        }
        
        FileWriter writeAlertFile = new FileWriter(alertFile);
        writeAlertFile.write(updateAlert);
        writeAlertFile.flush();
        writeAlertFile.close();

        return sendAlert;
    }
    
    static void DetectAnomaly(File newLogfile, String host,String emailList){
        String trainedFile = host+"_trained.log";
        String SplitBy = ",";
        String emailSubject = "Medium Alert!! Some Process using unusal Memory in host ";
        StringBuilder newAnomaly = new StringBuilder();
        String line;
        try {
            RandomAccessFile trainedData = new RandomAccessFile(new File(trainedFile),"r");
            Scanner read_Data = new Scanner(newLogfile);
            long pointer=0;
            while(read_Data.hasNextLine()) {
                String readLine = read_Data.nextLine();
                String[] newData = readLine.split(SplitBy);
                while(true){
                    pointer = trainedData.getFilePointer();
                    if((line = trainedData.readLine()) != null){
                        String[] Data = line.split(SplitBy);
                        if(Data[0].equals(newData[11])){                                                            
                            if(compareRange(newData[4],Data[1],Data[2])) {            //Generate Alert
                                if(AlertAlreadySent(newData[11],host,"Alert_Memory_Anomaly.log",600000))
                                    newAnomaly.append("Process "+newData[11]+"\t"+newData[4]+"\n");
                            }      
                            break;
                        }
                    } else {
                        trainedData.seek(pointer);                       
                    }                    
                }
            }
            trainedData.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
	} catch (IOException e) {
            e.printStackTrace();
	}
        if(newAnomaly.length()!=0){
            new SendEmail(emailList, newAnomaly.toString(), emailSubject+host);
        }                    
    }
    
    
    static void TrainDataSet(File newLogfile,String host){
        String trainedFile = host+"_trained.log";
        String SplitBy = ",";
        StringBuilder newtrainedData = new StringBuilder();
        String line,range;
        try {
            RandomAccessFile trainedData = new RandomAccessFile(new File(trainedFile),"rw");
            Scanner read_Data = new Scanner(newLogfile);           
            long pointer=0;
            while(read_Data.hasNextLine()) {
                String readLine = read_Data.nextLine();
                String[] newData = readLine.split(SplitBy);
                while(true){
                    pointer = trainedData.getFilePointer();
                    if((line = trainedData.readLine()) != null){
                        String[] Data = line.split(SplitBy);
                        if(Data[0].equals(newData[11])){
                            range = computeRange(newData[4],Data[1],Data[2]);
                            newtrainedData.append(newData[11]+range+"\n");
                            break;
                        }
                    } else {
                        trainedData.seek(pointer);
                        newtrainedData.append(newData[11]+","+newData[4]+","+newData[4]+"\n");
                        break;
                    }                    
                }
            }
            trainedData.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
	} catch (IOException e) {
            e.printStackTrace();
	}
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(trainedFile), StandardCharsets.UTF_8));       
            writer.write(newtrainedData.toString());
        } catch (IOException ex) {
        } finally {
            try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
    }
}
