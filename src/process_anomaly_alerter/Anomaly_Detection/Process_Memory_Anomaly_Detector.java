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
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author naval_
 */
public class Process_Memory_Anomaly_Detector {
    public static void main(String args[]){
        while(true){
            TrainDataSet("192.168.188.133");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Process_Memory_Anomaly_Detector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
    
    static void TrainDataSet(String host){
        String trainedFile = host+"_trained.log";
        String SplitBy = ",";
        String newtrainedData = "";
        File readData = new File(host+".log");
        String line,range;
        Path file = Paths.get(trainedFile);
        try {
            RandomAccessFile trainedData = new RandomAccessFile(new File(trainedFile),"rw");
            Scanner read_Data = new Scanner(readData);           
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
                            newtrainedData = newtrainedData+readLine+range+"\n";
                            break;
                        }
                    } else {
                        trainedData.seek(pointer);
                        newtrainedData = newtrainedData+readLine+","+newData[4]+","+newData[4]+"\n";
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
            writer.write(newtrainedData);
        } catch (IOException ex) {
        } finally {
            try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
    }
}
