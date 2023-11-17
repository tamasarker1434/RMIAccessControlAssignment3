package org.example;

import java.util.Dictionary;
import java.io.Serializable;
public class UserDetails implements Serializable{
    public int userId;
    public int userRoleId;
    public String userRole;
    public Dictionary<Integer,String> function;
    public int userStatus;
}
