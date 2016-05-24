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

    Process_Memory_Anomaly_Detector(File newLogfile, String host, boolean isTrainingProcess) {
        if(isTrainingProcess){
            DetectAnomaly(newLogfile,host);
        } else
            TrainDataSet(newLogfile,host);
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
    
    static Boolean compareRange(String input,String lowerrange,String higherrange){
        if(Float.parseFloat(input)<Float.parseFloat(lowerrange))
            return true;
        else if(Float.parseFloat(input)>Float.parseFloat(higherrange))
            return true;
        else
            return false;
    }
    
    static void DetectAnomaly(File newLogfile, String host){
        String trainedFile = host+"_trained.log";
        String SplitBy = ",",SplitAlert="----";
        String alertFile = "Alert_"+host+".log";
        StringBuilder newAnomaly = new StringBuilder();
        StringBuilder alertData = new StringBuilder();
        //File readData = new File(host+".log");
        File readAlertFile = new File(alertFile);
        String line,processName;
        Date date = new Date(); 
        try {
            RandomAccessFile trainedData = new RandomAccessFile(new File(trainedFile),"r");
            Scanner read_Data = new Scanner(newLogfile);
            //Scanner read_Data = new Scanner(readData);
            Scanner read_Alert = new Scanner(readAlertFile);
            long pointer=0;
            while(read_Data.hasNextLine()) {
                String readLine = read_Data.nextLine();
                String[] newData = readLine.split(SplitBy);
                while(true){
                    pointer = trainedData.getFilePointer();
                    if((line = trainedData.readLine()) != null){
                        String[] Data = line.split(SplitBy);
                        if(Data[11].equals(newData[11])){
                            if(compareRange(newData[4],Data[12],Data[13])) {            //Generate Alert
                                String Alert = "Process "+newData[11]+"\t"+newData[4]+SplitAlert+date.getTime()+"\n";
                                if(!read_Alert.hasNextLine()){
                                    alertData.append(Alert);
                                    newAnomaly.append(Alert);
                                    System.out.println("i1"+line);
                                        
                                } else {
                                    while(read_Alert.hasNextLine()){
                                        line = read_Alert.nextLine();
                                        System.out.println("i am here! "+line);
                                            
                                        if((line.contains(newData[11]) && (date.getTime()-Long.parseLong(line.split(SplitAlert)[1]) > 30000))) {
                                            System.out.println("i am here! "+newData[11]);
                                            newAnomaly.append(Alert);
                                            alertData.append(Alert);
                                        }
                                    }
                                }
                                
                            }      
                            break;
                        }
                    } else {
                        trainedData.seek(pointer);
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
                    new FileOutputStream(alertFile), StandardCharsets.UTF_8));       
            writer.write(alertData.toString());
        } catch (IOException ex) {
        } finally {
            try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
        if(newAnomaly.length()!=0){
        //System.out.println("I am here \n"+newAnomaly.toString()+".......");
            //SendEmail sendEmailObject = new SendEmail(newAnomaly, host);
        }                    
    }
    
    
    static void TrainDataSet(File newLogfile,String host){
        String trainedFile = host+"_trained.log";
        String SplitBy = ",";
        StringBuilder newtrainedData = new StringBuilder();;
        //File readData = new File(host+".log");
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
                        if(Data[11].equals(newData[11])){
                            range = computeRange(newData[4],Data[12],Data[13]);
                            newtrainedData.append(readLine+range+"\n");
                            break;
                        }
                    } else {
                        trainedData.seek(pointer);
                        newtrainedData.append(readLine+","+newData[4]+","+newData[4]+"\n");
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
