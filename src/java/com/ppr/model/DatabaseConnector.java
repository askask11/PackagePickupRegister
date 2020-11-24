/*
 * Author: jianqing
 * Date: Aug 3, 2020
 * Description: This document is created for
 */
package com.ppr.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author jianqing
 */
public class DatabaseConnector implements AutoCloseable
{
    
    
    private Connection dbConn;

    public DatabaseConnector()
    {
        this.dbConn = null;
    }
    
   
    public DatabaseConnector(String dbName, String host, String username, String password,boolean  ssl) throws SQLException, ClassNotFoundException
    {
        establishConnection(dbName, host, username, password, ssl);
    }
    
     ////////////////////////////////////////////////////
    //////////////////////////packages/////////////
    /////////////////////////////////////////
    /////////////////////////////////////////////
    
    public String getGuoguoByCode(String code) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT guoguo FROM packages WHERE code=?");
        ps.setString(1, code);
        ResultSet rs = ps.executeQuery();
        
        if(rs.next())
        {
            return rs.getString("guoguo");
        }else
        {
            return null;
        }
        
    }
    
    
    public int updatePackageLocaltimePhoneByCode(String time,String phone,String code) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE packages SET pickupTime=?, phone=? WHERE code=?");
        ps.setString(1, time);
        ps.setString(2, phone);
        ps.setString(3, code);
        return ps.executeUpdate();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    ////////////////////////////////////////////////////
    //////////////////////////CONSTRUCTIONAL MEHTODS/////////////
    /////////////////////////////////////////
    /////////////////////////////////////////////
    public void establishConnection(String dbName, String host, String dbUsername, String dbPassword, boolean useSSL) throws SQLException, ClassNotFoundException
    {
        //NO this.dbConn = dbConn;
        String connectionURL = "jdbc:mysql://" + host + "/" + dbName;
        this.dbConn = null;
        //Find the driver and make connection;

        Class.forName("com.mysql.cj.jdbc.Driver"); //URL for new version jdbc connector.
        Properties properties = new Properties(); //connection system property
        properties.setProperty("user", dbUsername);
        properties.setProperty("password", dbPassword);
        properties.setProperty("useSSL", Boolean.toString(useSSL));//set this true if domain suppotes SSL
        //"-u root -p mysql1 -useSSL false"
        this.dbConn = DriverManager.getConnection(connectionURL, properties);
    }
    public static DatabaseConnector getDefaultInstance() throws SQLException, ClassNotFoundException
    {
        return new DatabaseConnector("ppr", "localhost:3306", "Main", "DMXCjeU98Io1FP2h", false);
    }

    @Override
    public void close() throws Exception
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if(this.dbConn != null)
            this.dbConn.close();
    }
    
    
    public static void main(String[] args)
    {
        try(DatabaseConnector db = DatabaseConnector.getDefaultInstance())
        {
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
