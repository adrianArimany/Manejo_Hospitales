package com.uvg.proyecto.Classes;

import java.util.List;

/**
 * The Doctor class represents a doctor with an ID, name, and clinic.
 * 
 */
public class Doctor {
    private String id;
    private String nombre;
    private String clinica;
    //private String office;

    /**
     * Constructs a new Doctor with the specified ID, name, and clinic.
     *
     * @param id      the ID of the doctor
     * @param nombre  the name of the doctor
     * @param clinica the clinic of the doctor
     */
    public Doctor(String id, String nombre, String clinica) {
        this.id = id;
        this.nombre = nombre;
        this.clinica = clinica;
    }

    /**
     * Returns the ID of the doctor.
     *
     * @return the ID of the doctor
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the ID of the doctor.
     *
     * @param id the new ID of the doctor
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the name of the doctor.
     *
     * @return the name of the doctor
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Sets the name of the doctor.
     *
     * @param nombre the new name of the doctor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Returns the clinic of the doctor.
     *
     * @return the clinic of the doctor
     */
    public String getClinica() {
        return this.clinica;
    }

    /**
     * Sets the clinic of the doctor.
     *
     * @param clinica the new clinic of the doctor
     */
    public void setClinica(String clinica) {
        this.clinica = clinica;
    }
    
    /**
     * AAZ
     * Busca el historial medico de un paciente 
     * @param paciente el paciente que se quiere buscar su historial medico
     * @return Un formato para de String que contiene la informacion medica del paciente.
     */
    public String verHistorialMedico(Paciente paciente) {
        List<String> historial = paciente.getHistorialMedico();
        StringBuilder stringBuilder = new StringBuilder(); //Modifica un String sin necesidad de cambiarlo en la memoria.
        stringBuilder.append("Historial médico de ").append(paciente.getNombre()).append(":\n");
        for (String registro : historial) {
            stringBuilder.append(registro).append("\n"); //el \n crea una nueva linea
        }
        return stringBuilder.toString(); //regresa el historial medico como un string. 
    }

    /**
     * Returns a string representation of the doctor.
     * The string representation consists of the ID, name, and clinic separated by commas.
     *
     * @return a string representation of the doctor
     */
    @Override
    public String toString() {
        return id + "," + nombre + "," + clinica;
    }
}