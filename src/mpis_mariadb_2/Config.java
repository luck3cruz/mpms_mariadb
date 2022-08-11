package mpis_mariadb_2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    public static Properties prop = new Properties();

    public void saveProp(String title, String value) {
        try {
            prop.setProperty(title, value);
            prop.store(new FileOutputStream("C:\\MPIS\\config.properties"), (String) null);
        } catch (IOException e) {
        }
    }

    public String getProp(String title) {
        String value = "";
        try {
            prop.load(new FileInputStream("C:\\MPIS\\config.properties"));
            value = prop.getProperty(title);
        } catch (IOException e) {
        }
        return value;
    }
}
