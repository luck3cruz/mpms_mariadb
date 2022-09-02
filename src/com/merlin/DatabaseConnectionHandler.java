/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Lucky
 */
public class DatabaseConnectionHandler {
    Config con = new Config();
    private final String driver = "jdbc:mariadb://" + this.con.getProp("IP") + ":" + this.con.getProp("port") + "/merlininventorydatabase";
    private final String f_user = this.con.getProp("username");
    private final String f_pass = this.con.getProp("password");
    
    
    public Connection createConnection() {
        Connection connect = null;
        try {
            connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);
        } catch (SQLException ex) {
            JOptionPane.showConfirmDialog(null, "Unable to create Database Connection.", "Database Communication Error", JOptionPane.ERROR_MESSAGE);
//            Logger.getLogger(DatabaseConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connect;
    }
    
    public ResultSet executeQuery( String query) {
        ResultSet rset = null;
        try {
            Connection connect = DriverManager.getConnection(this.driver, this.f_user, this.f_pass);;
            Statement state = connect.createStatement();
            rset = state.executeQuery(query);
            state.close();
            connect.close();
        } catch (SQLException ex) {
            JOptionPane.showConfirmDialog(null, "Unable to execute database query.", "Database Communication Error", JOptionPane.ERROR_MESSAGE);
//            Logger.getLogger(DatabaseConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rset;
    }
}
