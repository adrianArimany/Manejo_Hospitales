package com.uvg.proyecto.Classes;

import java.util.ArrayList;
import java.util.List;

import com.uvg.proyecto.Data.UserTypes;
import com.uvg.proyecto.Utils.IdGenerator;

/**
 * The Doctor class represents a doctor with an ID, name, and clinic.
 * 
 */
public class Doctor {
    private int id;
    private String nombre;
    private String clinica;
    private ArrayList<Integer> pacientesId;
    private ArrayList<Cita> citas;
    private ArrayList<Prescription> prescriptions;

    /**
     * Constructs a new Doctor with the specified ID, name, and clinic.
     *
     * @param id      the ID of the doctor
     * @param nombre  the name of the doctor
     * @param clinica the clinic of the doctor
     */
    public Doctor(int id, String nombre, String clinica) {
        this.id = id;
        this.nombre = nombre;
        this.clinica = clinica;
        this.pacientesId = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
    }

    public Doctor(String nombre, String clinica) {
        this.id = IdGenerator.generateId(UserTypes.Doctor);
        this.nombre = nombre;
        this.clinica = clinica;
        this.pacientesId = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
    }

    public Doctor(String nombre) {
        this.id = IdGenerator.generateId(UserTypes.Doctor);
        this.nombre = nombre;
        this.clinica = null;
        this.pacientesId = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
    }

    public Doctor(int id, String nombre, String clinica, ArrayList<Integer> pacientesId, ArrayList<Cita> citas) {
        this.id = id;
        this.nombre = nombre;
        this.clinica = clinica;
        this.pacientesId = pacientesId;
        this.citas = citas;
        this.prescriptions = new ArrayList<>();
    }

    public void setPacientesId(ArrayList<Integer> pacientesId) {
        this.pacientesId = pacientesId;
    }

    public ArrayList<Cita> getCitas() {
        return citas;
    }

    public Cita getCitaById(int id) {
        for (Cita cita : citas) {
            if (cita.getId() == id) {
                return cita;
            }
        }
        return null;
    }

    public void setCitas(ArrayList<Cita> citas) {
        this.citas = citas;
    }

    /**
     * Returns the ID of the doctor.
     *
     * @return the ID of the doctor
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the ID of the doctor.
     *
     * @param id the new ID of the doctor
     */
    public void setId(int id) {
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
     * 
     * Busca el historial medico de un paciente
     * 
     * @param paciente el paciente que se quiere buscar su historial medico
     * @return Un formato para de String que contiene la informacion medica del
     *         paciente.
     */
    public String verHistorialMedico(Paciente paciente) {
        List<String> historial = paciente.getHistorialMedico();
        StringBuilder stringBuilder = new StringBuilder(); // Modifica un String sin necesidad de cambiarlo en la
                                                           // memoria.
        stringBuilder.append("Historial médico de ").append(paciente.getNombre()).append(":\n");
        for (String registro : historial) {
            stringBuilder.append(registro).append("\n"); // el \n crea una nueva linea
        }
        return stringBuilder.toString(); // regresa el historial medico como un string.
    }

    @Override
    public String toString() {
        return "Doctor [id=" + id + ", nombre=" + nombre + ", clinica=" + clinica + "]";
    }

    public ArrayList<Integer> getPacientesId() {
        return pacientesId;
    }

    public void setPacientes(ArrayList<Integer> pacientes) {
        this.pacientesId = pacientes;
    }

    public void addPacienteToDoc(int pacienteId) {
        this.pacientesId.add(pacienteId);
    }

    public void removePacienteFromDoc(int id) {
        this.pacientesId.removeIf(pacienteId -> pacienteId == id);
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
}