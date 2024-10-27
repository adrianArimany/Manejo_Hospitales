package com.uvg.proyecto.Classes;

/**
 * Considerations:
 * 
 * The id of the clinic can be set as the dirrection of the clinic, since all the clinics are in the same region
 * then it would be easier to have the id the set as the number of the clinic.
 * 
 * You also need to create a enum for the varios different specielities this hospital manages.
 * 
 *  
 */ 


/**
 * Representa una clínica médica.
 * Esta clase implementa Serializable para permitir la serialización de objetos Clinica.
 */
public class Clinica { 
    private String nombre;
    private String id;
    

    /**
     * Constructor para crear una nueva instancia de Clinica.
     *
     * @param nombre El nombre de la clínica.
     * @param direccion La dirección de la clínica.
     */
    public Clinica(String id, String nombre) { 
        this.id = id;
        this.nombre = nombre;
        
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
     * Devuelve una representación en cadena de la clínica.
     *
     * @return Una cadena que contiene el nombre y la dirección de la clínica, separados por una coma.
     */
    @Override
    public String toString() {
        return nombre + "," + id;
    }
}
