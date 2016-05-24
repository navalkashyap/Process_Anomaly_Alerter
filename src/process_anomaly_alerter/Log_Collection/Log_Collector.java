/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package process_anomaly_alerter.Log_Collection;

import java.util.logging.Logger;

/**
 *
 * @author naval_
 * This component take care of functionality of log collection and passing it into various 
 * Data store
 */
public class Log_Collector {

    public Boolean Collect_Log(String user,String host, String Log_Data) {

        Data_Store DS = new Data_Store();
        if(!DS.Store_Data(user,host,Log_Data)){
            Logger.getLogger("I/O issues, couldn't save the logs for user_host :"+user+"_"+host);
            return false;
        }
        return true;
    }
}