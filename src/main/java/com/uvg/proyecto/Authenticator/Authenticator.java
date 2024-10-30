package com.uvg.proyecto.Authenticator;

import com.uvg.proyecto.Data.PropertiesFile;

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