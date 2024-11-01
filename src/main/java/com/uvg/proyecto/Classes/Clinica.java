package com.uvg.proyecto.Classes;

import java.util.ArrayList;

import com.uvg.proyecto.Data.UserTypes;
import com.uvg.proyecto.Utils.IdGenerator;

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
 * Esta clase implementa Serializable para permitir la serialización de objetos
 * Clinica.
 */
public class Clinica {
    private String especialidad;
    private int id;
    private ArrayList<Integer> doctorId;

    public Clinica(String especialidad, ArrayList<Integer> doctorId) {
        this.id = IdGenerator.generateId(UserTypes.Clinica);
        this.especialidad = especialidad;
        this.doctorId = doctorId;
    }

    /**
     * Constructor para crear una nueva instancia de Clinica.
     *
     * @param especialidad    El especialidad de la clínica.
     * @param direccion La dirección de la clínica.
     */
    public Clinica(String especialidad) {
        this.id = IdGenerator.generateId(UserTypes.Clinica);
        this.especialidad = especialidad;
        this.doctorId = new ArrayList<Integer>(); 
    }

    /**
     * Obtiene el id de la clínica.
     *
     * @return the id of the clinic
     */
    public int getId() {
        return this.id;
    }

    /**
     * Stablished the id of the clinic.
     * 
     * @param id new id for the clinic
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el especialidad de la clínica.
     *
     * @return El especialidad de la clínica.
     */
    public String getEspecialidad() {
        return this.especialidad;
    }

    /**
     * Establece el especialidad de la clínica.
     *
     * @param especialidad El nuevo especialidad de la clínica.
     */
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * Obtiene una lista de IDs de doctores asociados con la clínica.
     *
     * @return Una lista de IDs de doctores.
     */
    public ArrayList<Integer> getDoctorId() {
        return doctorId;
    }

    /**
     * Adds a doctor ID to the list of IDs of doctors associated with the clinic.
     * 
     * @param doctorId The ID of the doctor to add.
     */
    public void setDoctorId(int doctorId) {
        this.doctorId.add(doctorId);
    }

    /**
     * Establece la lista de IDs de doctores asociados con la clínica.
     * 
     * @param doctorId La lista de IDs de doctores.
     */
    public void setArrayDoctorId(ArrayList<Integer> doctorId) {
        this.doctorId = doctorId;
    }


    /**
     * Devuelve una representaci n en cadena de la cl nica.
     * 
     * @return una cadena que describe la cl nica.
     */
    @Override
    public String toString() {
        return "Clinica [especialidad=" + especialidad + ", id=" + id + ", doctorId=" + doctorId + "]";
    }
}
