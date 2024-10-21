package com.uvg.proyecto.Authenticator;

public class Authenticator {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";

    /**
     * Verifica la contrasena y usuario del administrador.
     * @param username del administrador
     * @param password del adminstrador
     * @return true = username y password are correct; false = username and password are inccorect.
     */
    public static boolean verifyCredentials(String username, String password) {
        return USERNAME.equals(username) && PASSWORD.equals(password);
    }
}