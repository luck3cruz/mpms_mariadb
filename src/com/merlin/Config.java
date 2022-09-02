package com.merlin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class Config {

    public static Properties prop = new Properties();

    public void saveProp(String title, String value) {
        try {
            prop.setProperty(title, value);
            if (System.getProperty("os.name").contains("Mac OS X")) {
                prop.store(new FileOutputStream(new File(getClass().getResource("/images/config.properties").toURI())), (String) null);
            } else if (System.getProperty("os.name").contains("Windows")) {
                prop.store(new FileOutputStream("C:\\MPIS\\config.properties"), (String)null);
            }
        } catch (IOException e) {
        } catch (URISyntaxException ex) {
//            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getProp(String title) {
        String value = "";
        try {
            
            if (System.getProperty("os.name").contains("Mac OS X")) {
                prop.load(new FileInputStream(new File(getClass().getResource("/images/config.properties").toURI())));
            } else if (System.getProperty("os.name").contains("Windows")) {
                prop.load(new FileInputStream("C:\\MPIS\\config.properties"));
            }
            value = prop.getProperty(title);
        } catch (IOException e) {
        } catch (URISyntaxException ex) {
//            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }
}
