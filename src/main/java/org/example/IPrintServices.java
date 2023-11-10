package org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.Dictionary;

public interface IPrintServices extends Remote {
    //Sign in Method
    public Dictionary<String,Integer> signIn(String userId, String password) throws RemoteException;
    public String echo(String input) throws RemoteException;
    // prints file filename on the specified printer
    public String print(String filename, String printer, Dictionary<String,Integer> userInfo) throws RemoteException;
    // lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>
    public String queue(String printer, Dictionary<String,Integer> userInfo) throws RemoteException;
    // moves job to the top of the queue
    public String topQueue(String printer, int job, Dictionary<String,Integer> userInfo) throws RemoteException;
    // starts the print server
    public String start( Dictionary<String,Integer> userInfo) throws RemoteException;
    // stop the print server
    public String stop(Dictionary<String,Integer> userInfo) throws RemoteException;
    // stops the print server, clears the print queue and starts the print server again
    public String restart(Dictionary<String,Integer> userInfo) throws RemoteException;
    // prints status of printer on the user's display
    public String status(String printer, Dictionary<String,Integer> userInfo) throws RemoteException;
    // prints the value of the parameter on the print server to the user's display
    public String readConfig(String parameter, Dictionary<String,Integer> userInfo) throws RemoteException;
    // sets the parameter on the print server to value
    public String setConfig(String parameter, String value, Dictionary<String,Integer> userInfo) throws RemoteException;
}
