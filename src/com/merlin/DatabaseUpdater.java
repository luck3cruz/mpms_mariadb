/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlin;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class aims to create a textfile that will store all INSERT and UPDATE STATEMENTS made from all MPI's branches' Databases that will later on be executed to the 
 * Main branch's database to facilitate a sort-of centralized database for all branches.
 * @author Lucky
 */
public class DatabaseUpdater {
    
    public void createNewFile(String filename) throws IOException {
        File f1 = new File(filename.concat(".txt"));
        f1.createNewFile();
    }
//    
//    public boolean createNewTextfile(String filename) {
//        return
//    }
//    
    public void writeToFile(String data, String filename) {
        FileWriter output = null;
        try {
            output = new FileWriter(filename);
            output.write("data");
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(DatabaseUpdater.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(DatabaseUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void readFile (String filename) {
        FileReader input = null;
        char[] data = new char[100];
        try {
            input = new FileReader(filename);
            System.out.println(data);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatabaseUpdater.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(DatabaseUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void editFile( String data, String filename) {
        try {
            FileWriter output = new FileWriter(filename, true);
            output.write(data);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseUpdater.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void deleteFile(String filename) {
        File f1 = new File(filename);
        if (f1.delete()) {
            System.out.println("file deleted successfully");
        } else {
            System.out.println("file delete error");
        }
    }
}
