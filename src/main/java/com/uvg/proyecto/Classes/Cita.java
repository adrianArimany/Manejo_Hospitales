package com.uvg.proyecto.Classes;

import com.uvg.proyecto.Data.UserTypes;
import com.uvg.proyecto.Utils.IdGenerator;

public class Cita {
    public int id;
    public int doctor;
    public String doctorName;
    public int paciente;
    public String pacienteName;
    public String enfermedades;
    public String date;
    public int clinica; //perhaps no need for this.
    public String clinicaSpeciality;


    public Cita(int doctor, int paciente, String date, String enfermedades) {
        this.id = IdGenerator.generateId(UserTypes.Cita);
        this.doctor = doctor;
        this.paciente = paciente;
        this.date = date;
        this.enfermedades = enfermedades;
    }

    public Cita(int doctor, int paciente, String doctorName, String pacienteName , String date, String enfermedades) {
        this.id = IdGenerator.generateId(UserTypes.Cita);
        this.doctor = doctor;
        this.paciente = paciente;
        this.pacienteName = pacienteName;
        this.doctorName = doctorName;
        this.date = date;
        this.enfermedades = enfermedades;
    }


    /**
     * In case if in the future you wish to add the clinic as well.
     * @param doctor
     * @param paciente
     * @param doctorName
     * @param pacienteName
     * @param clinicaSpeciality
     * @param date
     * @param enfermedades
     */
    public Cita(int doctor, int paciente, String doctorName, String pacienteName , String clinicaSpeciality , String date, String enfermedades) {
        this.id = IdGenerator.generateId(UserTypes.Cita);
        this.doctor = doctor;
        this.paciente = paciente;
        this.pacienteName = pacienteName;
        this.doctorName = doctorName;
        this.date = date;
        this.enfermedades = enfermedades;
        this.clinicaSpeciality = clinicaSpeciality;
    }


    public Cita(int doctor, int paciente) {
        this.id = IdGenerator.generateId(UserTypes.Cita);
        this.doctor = doctor;
        this.paciente = paciente;
    }

    @Override
    public String toString() {
        return "Cita [Token: " + this.id + 
        ".\n doctor Name: " + this.doctorName + " (ID: " + this.doctor + ")" + 
        ".\n patient Name: " + this.pacienteName + " (ID: " + this.paciente + ")" + 
        ".\n Appointment date: " + this.date + 
        ".\n Symptoms: " + this.enfermedades + " ]";
        

    }

    /**
     * Gets the id of the appointment.
     * @return the id of the appointment
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the ID of the appointment.
     * 
     * @param id the new ID for the appointment
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the ID of the doctor associated with this appointment.
     * 
     * @return the ID of the doctor
     */
    public int getDoctor() {
        return doctor;
    }

    /**
     * Sets the ID of the doctor associated with this appointment.
     * 
     * @param doctor the new ID for the doctor
     */
    public void setDoctor(int doctor) {
        this.doctor = doctor;
    }

    /**
    * Returns the name of the doctor associated with this appointment.
    * 
    * @return the name of the doctor
    */
    public String getDoctorName() {
        return doctorName;
    }

    /**
     * Sets the name of the doctor associated with this appointment.
     * 
     * @param doctorName the new name for the doctor
     */
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    /**
     * Returns the ID of the patient associated with this appointment.
     * 
     * @return the ID of the patient
     */
    public int getPaciente() {
        return paciente;
    }

    public void setPaciente(int paciente) {
        this.paciente = paciente;
    }

    /**
     * Returns the name of the patient associated with this appointment.
     * 
     * @return the name of the patient
     */
    public String getPacienteName() {
        return pacienteName;
    }

    /**
     * Sets the name of the patient associated with this appointment.
     * 
     * @param pacienteName the new name for the patient
     */
    public void setPacienteName(String pacienteName) {
        this.pacienteName = pacienteName;
    }


    /**
     * Returns the speciality of the clinic associated with this appointment.
     * 
     * @return the speciality of the clinic
     */
    public String getClinicaSpeciality() {
        return clinicaSpeciality;
    }

    /**
     * Sets the speciality of the clinic associated with this appointment.
     * 
     * @param clinicaSpeciality the new speciality of the clinic
     */
    public void setClinicaSpeciality(String clinicaSpeciality) {
        this.clinicaSpeciality = clinicaSpeciality;
    }

/**
 * Returns the date of the appointment.
 * 
 * @return the date of the appointment
 */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of the appointment.
     * 
     * @param date the new date of the appointment
     */
    public void setDate(String date) {
        this.date = date;
    }
    /**
     * Returns the diseases associated with this appointment.
     * 
     * @return the diseases associated with the appointment
     */
    public String getEnfermedades() {
        return enfermedades;
    }
    /**
     * Sets the diseases associated with this appointment.
     * 
     * @param enfermedades the new diseases associated with the appointment
     */
    public void setEnfermedades(String enfermedades) {
        this.enfermedades = enfermedades;
    }


}
