/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package process_anomaly_alerter.Log_Collection;
import process_anomaly_alerter.Anomaly_Detection.Detection_Initiator;
import static java.lang.Thread.sleep;

/**
 *
 * @author naval_
 */
public class Process_Anomaly_Alerter {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        int iteration_period = 10000;  // in milliseconds
        Thread DI = new Thread(new Detection_Initiator());
        DI.start();        

        //Remote_Login_Service rms = new Remote_Login_Service(iteration_period) ;
    
    }
}