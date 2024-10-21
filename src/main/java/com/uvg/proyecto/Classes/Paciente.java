package com.uvg.proyecto.Classes;

import java.io.Serializable;

/**
 * Esta clase maneja existe solamente para manejar a los pacientes del hospotal.
 * 
 * Todo: 
 * 
 * 1. Necesita mas attributos, como historial medico, enfemedades, cita medica, etc.
 * 
 * 
 */
import java.util.ArrayList;
import java.util.List;


public class Paciente implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String nombre;
    private String doctor;
    private String clinica;
    private List<String> historialMedico;
    private List<String> enfermedades;
    private List<String> citasMedicas;

    /**
     * Constructor de la clase Paciente.
     * Inicializa un nuevo objeto Paciente con la información proporcionada.
     *
     * @param id     El identificador único del paciente
     * @param nombre El nombre del paciente
     * @param doctor El nombre del doctor asignado al paciente
     * @param clinica El nombre de la clínica donde se atiende al paciente
     */
    public Paciente(String id, String nombre, String doctor, String clinica) {
        this.id = id;
        this.nombre = nombre;
        this.doctor = doctor;
        this.clinica = clinica;
        this.historialMedico = new ArrayList<>();
        this.enfermedades = new ArrayList<>();
        this.citasMedicas = new ArrayList<>();
        
    }

    /**
     * Obtiene el ID del paciente.
     *
     * @return El ID del paciente.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Establece el ID del paciente.
     *
     * @param id El nuevo ID a asignar al paciente.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del paciente.
     *
     * @return El nombre del paciente.
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Establece el nombre del paciente.
     *
     * @param nombre El nuevo nombre a asignar al paciente.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el nombre del doctor asignado al paciente.
     *
     * @return El nombre del doctor.
     */
    public String getDoctor() {
        return this.doctor;
    }

    /**
    *Establece el nombre del doctor 
    *
    * @param El nombre del doctor. 
    */
    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    /**
    *Obtiene el nombre de la clinica en el que el paciente es atendido.
    *
    *@return El nombre de la clinica.
    */
    public String getClinica() {
        return this.clinica;
    }

    /**
    *Establece el nombre de la clinica.
    *
    *@param El nombre de la clinica.
    */
    public void setClinica(String clinica) {
        this.clinica = clinica;
    }

    public List<String> getHistorialMedico() {
        return historialMedico;
    }

    public void agregarHistorialMedico(String historial) {
        this.historialMedico.add(historial);
    }

    public List<String> getEnfermedades() {
        return enfermedades;
    }

        public void agregarEnfermedad(String enfermedad) {
        this.enfermedades.add(enfermedad);
    }

    public List<String> getCitasMedicas() {
        return citasMedicas;
    }

    public void agregarCitaMedica(String cita) {
        this.citasMedicas.add(cita);
    }



    /**
    * Meodo para sobreescribir lo deseado.
    *
    *@return Los 4 datos, id, nombre, doctor y clinica. 
    */
    @Override
    public String toString() {
        return id + "," + nombre + "," + doctor + "," + clinica;
    }
}