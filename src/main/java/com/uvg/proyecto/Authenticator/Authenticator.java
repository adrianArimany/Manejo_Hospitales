package com.uvg.proyecto.Authenticator;

import com.uvg.proyecto.Data.PropertiesFile;
/**
 * This class is meant to create the method so that a user can log in to the admin menu only.
 * 
 * There is another class that will handle the authentication for each doctor seperately from this class.
 */

public class Authenticator {
    PropertiesFile propertiesFile = new PropertiesFile();

    /**
     * Verifica la contrasena y usuario del administrador.
     * @param username del administrador
     * @param password del adminstrador
     * @return true = username y password are correct; false = username and password are inccorect.
     */
    public boolean verifyCredentials(String username, String password) {
        return propertiesFile.getUsername().equals(username) && propertiesFile.getPassword().equals(password);
    }
}