package org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Dictionary;

public interface IPrintServices extends Remote {
    //Sign in Method
    public Dictionary<String,UserDetails> signIn(String userId, String password) throws RemoteException, SQLException;
    public String echo(String input) throws RemoteException;
    // prints file filename on the specified printer
    public String print(String filename, String printer, String sessionId) throws RemoteException;
    // lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>
    public String queue(String printer, String sessionId) throws RemoteException;
    // moves job to the top of the queue
    public String topQueue(String printer, int job,String sessionId) throws RemoteException;
    // starts the print server
    public String start( String sessionId) throws RemoteException;
    // stop the print server
    public String stop(String sessionId) throws RemoteException;
    // stops the print server, clears the print queue and starts the print server again
    public String restart(String sessionId) throws RemoteException;
    // prints status of printer on the user's display
    public String status(String printer, String sessionId) throws RemoteException;
    // prints the value of the parameter on the print server to the user's display
    public String readConfig(String parameter, String sessionId) throws RemoteException;
    // sets the parameter on the print server to value
    public String setConfig(String parameter, String value, String sessionId) throws RemoteException;
    //Delete user
    public boolean deleteUser(String userId) throws RemoteException;
    //Signup User
    public boolean singnUpUser(UserDetails userDetails) throws RemoteException;
}
