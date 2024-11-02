package com.uvg.proyecto.Classes;


import com.uvg.proyecto.Data.UserTypes;
import com.uvg.proyecto.Utils.IdGenerator;
import java.time.LocalDate; //currently throwing a error with a gson depedency. So can't use it unless I fix that.
import java.time.format.DateTimeFormatter;

public class Prescription {
    public int id;
    public int doctor;
    public int paciente;
    public String medicines;
    public String pacienteName;
    public String doctorName;
    public LocalDate date;



    public Prescription(){
        this.id = IdGenerator.generateId(UserTypes.Prescription);
        this.date = LocalDate.now();
    }

    public Prescription(int doctor, int paciente) {
        this.doctor = doctor;
        this.paciente = paciente;
        this.date = LocalDate.now();
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
        this.date = LocalDate.now();
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
     * Returns the name of the doctor associated with this prescription.
     * 
     * @return the name of the doctor
     */
    public String getDoctorName() {
        return doctorName;
    }
    /**
     * Sets the name of the doctor associated with this prescription.
     * 
     * @param doctorName the name of the doctor
     */
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    
    /**
     * Returns the name of the patient associated with this prescription.
     * 
     * @return the name of the patient
     */
    public String getPacienteName( ) {
        return pacienteName;
    }

    /**
     * Sets the name of the patient associated with this prescription.
     * 
     * @param pacienteName the name of the patient
     */
    public void setPacienteName(String pacienteName) {
        this.pacienteName = pacienteName;
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
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return "Prescription: [Token: " + this.id + 
           ".\n Name of Doctor: " + this.doctorName + " (ID: " + this.doctor + " )" + 
           ".\n Name of Patient: " + this.pacienteName + " (ID: " + this.paciente + " )" + 
           ".\n Medicines: " + this.medicines + 
           ".\n Date: " + this.date.format(formatter) + " ]";
    }


    

}
