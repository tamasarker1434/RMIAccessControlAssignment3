package org.example;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

import static java.rmi.Naming.lookup;

public class Client {
    static Scanner scannerObj = new Scanner(System.in);
    static IPrintServices iPrintServices;

    static {
        try {
            iPrintServices = (IPrintServices) lookup("rmi://localhost:5000/home");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException, SQLException {
        System.out.println("------" + iPrintServices.echo("Server is Connected------"));
        String choice = "";
        do {
            System.out.println("1. Login\n2. Delete User\n3. Signup User\n4. Combine Role\n5. Assign Role\n0. Exit");
            System.out.printf("Enter Choice :");
            choice = scannerObj.nextLine();
            switch (choice){
                case "1":
                    ShowLoginMenu();
                    break;
                case "2":
                    DeleteUser();
                    break;
                case "3":
                    SignupUser();
                    break;
                case "4":
                    System.out.println("1. Management\n2. Service Technician\n3. Power User\n4. Ordinary User");
                    System.out.printf("Enter 1st Role :");
                    int roleOne = Integer.parseInt(scannerObj.nextLine());
                    System.out.printf("Enter 2nd Role :");
                    int roleTwo= Integer.parseInt(scannerObj.nextLine());
                    System.out.printf("Enter Role Name :");
                    String roleName = scannerObj.nextLine();
                    boolean  result = iPrintServices.CombineRole(roleOne,roleTwo,roleName);
                    if (result)
                        System.out.println("Successfully Create Role");
                    else
                        System.out.println("Action Failed");
                    break;
                case "5":
                    System.out.println("1. Management\n2. Service Technician\n3. Power User\n4. Ordinary User\n5. New Role");
                    System.out.printf("Enter Role :");
                    int role= Integer.parseInt(scannerObj.nextLine());
                    System.out.printf("Enter Employee Name :");
                    String userName = scannerObj.nextLine();
                    int results = iPrintServices.AssignRoleGeorge(userName,role);
                    if (results == 1)
                        System.out.println("Successfully Assigned Role");
                    else
                        System.out.println("Action Failed");
                    break;
                case "0":
                    choice = "0";
                    break;
                default:
                    if (choice!="0")
                        System.out.println("Enter Valid Choice!!!");
                    break;
            }
        }while (choice!="0");
    }
    private static void SignupUser() throws RemoteException, SQLException{
        UserDetails userDetails = new UserDetails();
        System.out.printf("Enter Username = ");
        userDetails.userName = scannerObj.nextLine();
        System.out.printf("Enter password = ");
        userDetails.password = passwordConversion(scannerObj.nextLine());
        System.out.println("1. Management\n2. Service technician\n3. Power User\n4. Ordinary User");
        System.out.printf("Enter User Role = ");
        userDetails.userRoleId = Integer.parseInt(scannerObj.nextLine());
        boolean result = iPrintServices.singnUpUser(userDetails);
        if (result)
            System.out.println("Action Successful!!!");
        else
            System.out.println("Action Failed!!!");
    }
    private static void DeleteUser() throws RemoteException, SQLException{
        System.out.printf("Enter Username = ");
        String userId = scannerObj.nextLine();
        boolean result = iPrintServices.deleteUser(userId);
        if (result)
            System.out.println("Action Successful!!!");
        else
            System.out.println("Action Failed!!!");
    }
    private static void ShowLoginMenu() throws RemoteException, SQLException {
        String userId, password ;
        System.out.printf("Enter Username = ");
        userId = scannerObj.nextLine();
        System.out.printf("Enter Password = ");
        password = scannerObj.nextLine();
        password = passwordConversion(password);
        Dictionary<String,UserDetails> userInfo = iPrintServices.loginUser(userId, password);
        if (userInfo.size()>0) {
            ShowPrintingMenu(userInfo);
        }
        else {
            System.out.println("Entered Wrong Username or Password");
            ShowLoginMenu();
        }
    }
    private static String passwordConversion(String password){
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] sha256Pass = digest.digest(password.getBytes());
            for (byte b : sha256Pass) {
                hexString.append(String.format("%02x", b));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return hexString.toString();
    }
    private static void ShowPrintingMenu(Dictionary<String,UserDetails> userInfo) throws RemoteException {
        String choice= "",response, sessionId= userInfo.keys().nextElement();
        UserDetails userDetails =userInfo.get(sessionId);
        Dictionary<Integer, String> accessedFunctions =  userDetails.function;
        Dictionary<String, String> newAccessFunc = new Hashtable<>();
        int funcCount = accessedFunctions.size();
        do {
            int i =1;
            System.out.println("0. Exit");
            Enumeration enu = accessedFunctions.keys();
            while (enu.hasMoreElements()) {
                String menuId = enu.nextElement().toString();
                String menuItem= accessedFunctions.get(Integer.parseInt(menuId));
                System.out.println(i +". "+ menuItem);
                newAccessFunc.put(String.valueOf(i),menuId);
                i++;
            }
            System.out.printf("Enter Choice :");
            choice = scannerObj.nextLine();
            if (!choice.isEmpty() && Integer.parseInt(choice)<=funcCount && Integer.parseInt(choice)!= 0)
                choice = newAccessFunc.get(choice);
            else if (Integer.parseInt(choice)>funcCount)
                choice="";
            switch (choice){
                case "1":
                    System.out.println("Client sending request with SessionId= "+ sessionId);
                    response = iPrintServices.print("fileName","printerName",sessionId);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "2":
                    System.out.println("Client send request with SessionId= "+ sessionId);
                    response = iPrintServices.queue("printerName",sessionId);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "3":
                    System.out.println("Client send request with SessionId= "+ sessionId);
                    response = iPrintServices.topQueue("printerName",1,sessionId);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "4":
                    System.out.println("Client send request with SessionId= "+ sessionId);
                    response = iPrintServices.start(sessionId);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "5":
                    System.out.println("Client send request with SessionId= "+ sessionId);
                    response = iPrintServices.stop(sessionId);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "6":
                    System.out.println("Client send request with SessionId= "+ sessionId);
                    response = iPrintServices.restart(sessionId);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "7":
                    System.out.println("Client send request with SessionId= "+ sessionId);
                    response = iPrintServices.status("printer",sessionId);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "8":
                    System.out.println("Client send request with SessionId= "+ sessionId);
                    response = iPrintServices.readConfig("parameter",sessionId);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "9":
                    System.out.println("Client send request with SessionId= "+ sessionId);
                    response = iPrintServices.setConfig("parameter","value",sessionId);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "0":
                    choice = "0";
                    break;
                default:
                    if (choice!="0")
                        System.out.println("Enter Valid Choice!!!");
                    break;
            }

        } while (choice != "0");
    }
}
