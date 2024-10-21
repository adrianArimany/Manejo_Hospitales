package com.uvg.proyecto.Classes;

/**
 * Representa una clínica médica.
 * Esta clase implementa Serializable para permitir la serialización de objetos Clinica.
 */
public class Clinica { 
    private String nombre;
    private String direccion;
    private String id;
    

    /**
     * Constructor para crear una nueva instancia de Clinica.
     *
     * @param nombre El nombre de la clínica.
     * @param direccion La dirección de la clínica.
     */
    public Clinica(String id, String nombre, String direccion) { 
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    /**
     * Obtiene el id de la clínica.
     *
     * @return the id of the clinic
     */
    public String getId() {
        return this.id;
    }

    /**
     * Stablished the id of the clinic.
     * @param id new id for the clinic
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la clínica.
     *
     * @return El nombre de la clínica.
     */
    public String getNombre() {  
        return this.nombre;
    }

    /**
     * Establece el nombre de la clínica.
     *
     * @param nombre El nuevo nombre de la clínica.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la dirección de la clínica.
     *
     * @return La dirección de la clínica.
     */
    public String getDireccion() {
        return this.direccion;
    }

    /**
     * Establece la dirección de la clínica.
     *
     * @param direccion La nueva dirección de la clínica.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    
    /**
     * Devuelve una representación en cadena de la clínica.
     *
     * @return Una cadena que contiene el nombre y la dirección de la clínica, separados por una coma.
     */
    @Override
    public String toString() {
        return nombre + "," + direccion;
    }
}
