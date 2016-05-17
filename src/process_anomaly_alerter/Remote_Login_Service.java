/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package process_anomaly_alerter;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author naval_
 */
public class Remote_Login_Service {

    String user = "";
    String password = "";
    String host = "";
    
    Remote_Login_Service() {
        int counter=0;
        Data_Store DS = new Data_Store();
        String[][] AllClients_Credentials = DS.Fetch_Client_Credentials();
        while(AllClients_Credentials[counter][0] != null){
            Thread thread = new Thread(new DoLogin_FetchData(AllClients_Credentials[counter++][0],AllClients_Credentials[counter++][1],AllClients_Credentials[counter++][2]));
            thread.start();
        }
    }
}


class DoLogin_FetchData implements Runnable{

    int port=22;
    String User_List_File="UserList.csv";
    
    @Override
    public void run() {
        System.out.println("running thread");
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    DoLogin_FetchData(String user,String password, String host){
        Log_Collector LC = new Log_Collector();
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection for host: "+host);

            // Avoid asking for key confirmation
    
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);

            session.connect();

            Channel channel = session.openChannel("shell");
            channel.connect();

            DataInputStream dataIn = new DataInputStream(channel.getInputStream());  
            DataOutputStream dataOut = new DataOutputStream(channel.getOutputStream());  

            // send ls command to the server  
            dataOut.writeBytes("ps -ef\r\n \n exit\r\n");  
            dataOut.flush();  

            // and print the response   
            String line = dataIn.readLine();
            String result = line + "\n";
        
            while ((line = dataIn.readLine()) != null) {
                result += line + "\n";
                System.out.println("Logger: "+line);
            }
        
            dataIn.close();  
            dataOut.close();  
            channel.disconnect();  
            session.disconnect();
            LC.Collect_Log(user,host, result);
        } catch(Exception e){System.err.print(e);
        }    
    }
    
}