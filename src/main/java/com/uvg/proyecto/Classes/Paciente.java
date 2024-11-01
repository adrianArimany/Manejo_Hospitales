package com.uvg.proyecto.Classes;


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

import com.uvg.proyecto.Data.UserTypes;
import com.uvg.proyecto.Utils.IdGenerator;

public class Paciente {
    private int id;
    private String nombre;
    private String doctor;
    private String clinica;
    private List<String> historialMedico;
    private List<String> enfermedades;
    private List<Integer> doctoresId;
    private List<Cita> citas;
    private ArrayList<Prescription> prescriptions;

    /**
     * Constructor de la clase Paciente.
     * Inicializa un nuevo objeto Paciente con la información proporcionada.
     *
     * @param id      El identificador único del paciente
     * @param nombre  El nombre del paciente
     * @param doctor  El nombre del doctor asignado al paciente
     * @param clinica El nombre de la clínica donde se atiende al paciente
     */
    public Paciente(String nombre, String doctor, String clinica) {
        this.id = IdGenerator.generateId(UserTypes.Paciente);
        this.nombre = nombre;
        this.doctor = doctor;
        this.clinica = clinica;
        this.historialMedico = new ArrayList<>();
        this.enfermedades = new ArrayList<>();
        this.doctoresId = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.prescriptions = new ArrayList<>();

    }

    public Paciente(String nombre, String doctor, String clinica, List<String> historialMedico,
            List<String> enfermedades, List<Integer> doctoresId, List<Cita> citas) {
        this.id = IdGenerator.generateId(UserTypes.Paciente);
        this.nombre = nombre;
        this.doctor = doctor;
        this.clinica = clinica;
        this.historialMedico = new ArrayList<>();
        this.enfermedades = new ArrayList<>();
        this.doctoresId = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
    }

    public Paciente(String nombre) {
        this.id = IdGenerator.generateId(UserTypes.Paciente);

        this.nombre = nombre;
        this.doctoresId = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        this.historialMedico = new ArrayList<>();
    }
    // public Paciente(String nombre, String historialMedico) {
    //     this.id = IdGenerator.generateId(UserTypes.Paciente);
    //     this.nombre = nombre;
    //     this.historialMedico = new ArrayList<>();
    // }
    /**
     * Obtiene el ID del paciente.
     *
     * @return El ID del paciente.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Establece el ID del paciente.
     *
     * @param id El nuevo ID a asignar al paciente.
     */
    public void setId(int id) {
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
     * Establece el nombre del doctor
     *
     * @param El nombre del doctor.
     */
    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    /**
     * Obtiene el nombre de la clinica en el que el paciente es atendido.
     *
     * @return El nombre de la clinica.
     */
    public String getClinica() {
        return this.clinica;
    }

    /**
     * Establece el nombre de la clinica.
     *
     * @param El nombre de la clinica.
     */
    public void setClinica(String clinica) {
        this.clinica = clinica;
    }

    public List<String> getHistorialMedico() {
        return historialMedico;
    }
    public void  setHistorialMedico(List<String>  historialMedico) {
        this.historialMedico = historialMedico;
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

    public void addDocToPaciente(int doc) {
        this.doctoresId.add(doc);
    }

    public void removeDocFromPaciente(int id) {
        this.doctoresId.removeIf(docId -> docId == id);
    }

    public void addCita(Cita cita) {
        this.citas.add(cita);
    }

    public ArrayList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(ArrayList<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public void addPrescription(Prescription prescription) {
        this.prescriptions.add(prescription);
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public List<Cita> getCitas() {
        return citas;
    }
    

}