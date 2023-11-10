package org.example;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.util.Dictionary;
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

    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        System.out.println("------" + iPrintServices.echo("Server is Connected------"));
        ShowLoginMenu();
    }
    private static void ShowLoginMenu() throws RemoteException {
        String userId, password ;
        System.out.printf("Enter Username = ");
        userId = scannerObj.nextLine();
        System.out.printf("Enter Password = ");
        password = scannerObj.nextLine();
        password = passwordConversion(password);
        Dictionary<String,Integer> userInfo = iPrintServices.signIn(userId, password);
        if (!userInfo.isEmpty()) {
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
    private static void ShowPrintingMenu(Dictionary<String,Integer> userInfo) throws RemoteException {
        String choice= "",response, sessionId= userInfo.keys().nextElement();
        do {
            System.out.printf("%n1.Print%n2.Queue%n3.Top Queue%n4.Start%n5.Stop%n6.Restart%n7.Status%n8.Read Config%n9.Set Config%n0.Logout%n");
            System.out.printf("Enter Choice :");
            choice = scannerObj.nextLine();
            switch (choice){
                case "1":
                    System.out.println("Client sending request with SessionId= "+ sessionId);
                    response = iPrintServices.print("fileName","printerName",userInfo);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "2":
                    System.out.println("Client send request with SessionId= "+ sessionId);
                    response = iPrintServices.queue("printerName",userInfo);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "3":
                    System.out.println("Client send request with SessionId= "+ sessionId);
                    response = iPrintServices.topQueue("printerName",1,userInfo);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "4":
                    System.out.println("Client send request with SessionId= "+ sessionId);
                    response = iPrintServices.start(userInfo);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "5":
                    System.out.println("Client send request with SessionId= "+ sessionId);
                    response = iPrintServices.stop(userInfo);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "6":
                    System.out.println("Client send request with SessionId= "+ sessionId);
                    response = iPrintServices.restart(userInfo);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "7":
                    System.out.println("Client send request with SessionId= "+ sessionId);
                    response = iPrintServices.status("printer",userInfo);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "8":
                    System.out.println("Client send request with SessionId= "+ sessionId);
                    response = iPrintServices.readConfig("parameter",userInfo);
                    if(response == null) {
                        choice = "0";
                        System.out.println("Unauthorised User!!!");
                    }
                    else
                        System.out.println(response);
                    break;
                case "9":
                    System.out.println("Client send request with SessionId= "+ sessionId);
                    response = iPrintServices.setConfig("parameter","value",userInfo);
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
