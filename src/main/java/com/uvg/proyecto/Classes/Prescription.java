package com.uvg.proyecto.Classes;


import com.uvg.proyecto.Data.UserTypes;
import com.uvg.proyecto.Utils.IdGenerator;

public class Prescription {
    public int id;
    public int doctor;
    public int paciente;
    public String medicines;
    public String pacienteName;
    public String doctorName;



    public Prescription(){
        this.id = IdGenerator.generateId(UserTypes.Prescription);
    }

    public Prescription(int doctor, int paciente) {
        this.doctor = doctor;
        this.paciente = paciente;
    }

    public Prescription(int id, int doctor, int paciente, String medicines) {
        this.id = id;
        this.doctor = doctor;
        this.paciente = paciente;
        this.medicines = medicines;
    }

    public Prescription(int doctor, int paciente, String medicines) {
        this.id = IdGenerator.generateId(UserTypes.Prescription);
        this.doctor = doctor;
        this.paciente = paciente;
        this.medicines = medicines;
    }
    
    
    public Prescription(int doctor, int paciente, String doctorName , String pacienteName , String medicines) {
        this.id = IdGenerator.generateId(UserTypes.Prescription);
        this.doctor = doctor;
        this.paciente = paciente;
        this.medicines = medicines;
    }
    

    /**
     * Returns the ID of this prescription.
     *
     * @return the ID of the prescription
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of this prescription.
     * 
     * @param id the ID of the prescription
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the ID of the doctor who prescribed the medicines associated with this prescription.
     * 
     * @return the ID of the doctor
     */
    public int getDoctor() {
        return doctor;
    }

    /**
     * Sets the ID of the doctor who prescribed the medicines associated with this prescription.
     * 
     * @param doctor the ID of the doctor
     */
    public void setDoctor(int doctor) {
        this.doctor = doctor;
    }

    /**
     * Returns the ID of the patient associated with this prescription.
     * 
     * @return the ID of the patient
     */
    public int getPaciente() {
        return paciente;
    }

    /**
     * Sets the ID of the patient associated with this prescription.
     * 
     * @param paciente the ID of the patient
     */
    public void setPaciente(int paciente) {
        this.paciente = paciente;
    }

    /**
     * Returns the medicines associated with this prescription.
     * 
     * @return a String representing the medicines
     */
    public String getMedicines() {
        return medicines;
    }

    /**
     * Sets the medicines associated with this prescription.
     * 
     * @param medicines a String representing the medicines
     */
    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    /**
     * Returns a string representation of the prescription.
     * 
     * The representation includes the prescription's ID, doctor, patient, and
     * medicines.
     * 
     * @return a string representation of the prescription
     */
    @Override
    public String toString() {
        return "Prescription [id=" + id + ", doctor=" + doctor + ", paciente=" + paciente + ", medicines=" + medicines
                + "]";
    }

    

}
