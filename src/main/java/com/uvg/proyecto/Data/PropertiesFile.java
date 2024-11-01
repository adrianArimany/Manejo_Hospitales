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
    /**
     * Reads the properties from a file.
     * 
     * @throws IOException if file is not found or can't be read.
     */
    public void loadProperties() {
        try (InputStream input = new FileInputStream(PROPERTIES_PATH)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Writes the properties to the file.
     * 
     * @throws IOException if file is not found or can't be written.
     */
    public void saveProperties() {
        try (OutputStream output = new FileOutputStream(PROPERTIES_PATH)) {
            properties.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Updates the name of the hospital.
     * 
     * @param newName the new name to give to the hospital
     * @return a confirmation message
     */
    public String changeHospitalName(String newName) {
        properties.setProperty("hospital.name", newName);
        saveProperties();
        return "Hospital name updated to: " + newName;
    }

    /**
     * Retrieves the name of the hospital from the properties file.
     * 
     * @return the name of the hospital, or "Default Hospital Name" if the property is not set
     */
    public String getHospitalName() {
        return properties.getProperty("hospital.name", "Default Hospital Name");
    }

    /**
     * Retrieves the username of the administrator from the properties file.
     * 
     * @return the username of the administrator, or "admin" if the property is not set
     */
    public String getUsername() {
        return properties.getProperty("hospital.username", "admin");
    }

    /**
     * Retrieves the password of the administrator from the properties file.
     * 
     * @return the password of the administrator, or "Password" if the property is not set
     */
    public String getPassword() {
        return properties.getProperty("hospital.password", "Password");
    }
    /**
     * Updates the username of the administrator.
     * 
     * @param newUsername the new username of the administrator
     * @return a confirmation message
     */
    public String changeUsername(String newUsername) {
        properties.setProperty("hospital.username", newUsername);
        saveProperties();
        return "Username updated to: " + newUsername;
    }

    /**
     * Updates the password of the administrator.
     * 
     * @param newPassword the new password of the administrator
     * @return a confirmation message
     */
    public String changePassword(String newPassword) {
        properties.setProperty("hospital.password", newPassword);
        saveProperties();
        return "Password updated successfully.";
    }
}
