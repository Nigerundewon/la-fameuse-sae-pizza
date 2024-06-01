package dao;

import java.util.Properties;
import java.io.*;
import java.sql.*;  
public class DB {

    private static final DB INSTANCE = new DB();
    private static Connection CONNECTION;
    private static String url;
    private static String nom;
    private static String mdp;

    private DB(){
        try {
            Properties p = new Properties();
            p.load(new FileInputStream("/home/infoetu/lony.fauchoit.etu/tomcat/webapps/pizzeria/WEB-INF/src/dao/config.prop"));
            Class.forName(p.getProperty("driver"));
            url = p.getProperty("url");
            nom = p.getProperty("login");
            mdp = p.getProperty("password");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createConnection() {
        try {
            DB.CONNECTION = DriverManager.getConnection(url,nom,mdp);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection() {
        return CONNECTION;
    }

    public static void close() {
        try {
            CONNECTION.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void open() {
        try {
            createConnection();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}