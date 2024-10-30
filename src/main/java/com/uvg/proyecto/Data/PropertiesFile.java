package com.uvg.proyecto.Data;
import java.util.Properties;
import java.io.*;

public class PropertiesFile {
    private static final String PROPERTIES_PATH = "src/main/java/com/uvg/proyecto/Properties/hospitalData.properties";
    private Properties properties;

    public PropertiesFile() {
        properties = new Properties();
        loadProperties();
    }
    public void loadProperties() {
        try (InputStream input = new FileInputStream(PROPERTIES_PATH)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveProperties() {
        try (OutputStream output = new FileOutputStream(PROPERTIES_PATH)) {
            properties.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String changeHospitalName(String newName) {
        properties.setProperty("hospital.name", newName);
        saveProperties();
        return "Hospital name updated to: " + newName;
    }

    public String getHospitalName() {
        return properties.getProperty("hospital.name", "Default Hospital Name");
    }

    public String getUsername() {
        return properties.getProperty("hospital.username", "admin");
    }

    public String getPassword() {
        return properties.getProperty("hospital.password", "Password");
    }
    public String changeUsername(String newUsername) {
        properties.setProperty("hospital.username", newUsername);
        saveProperties();
        return "Username updated to: " + newUsername;
    }

    public String changePassword(String newPassword) {
        properties.setProperty("hospital.password", newPassword);
        saveProperties();
        return "Password updated successfully.";
    }
}
