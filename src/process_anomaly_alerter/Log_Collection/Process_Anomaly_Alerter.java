/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package process_anomaly_alerter.Log_Collection;

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
        while(true){
            Remote_Login_Service rms = new Remote_Login_Service() ;
            Thread.sleep(iteration_period);
        }
    }
}