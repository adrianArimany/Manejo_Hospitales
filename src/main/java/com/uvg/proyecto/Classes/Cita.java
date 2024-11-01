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
        return "Cita [Token:" + this.id + ", doctor Name: " + this.doctorName + "(ID: " + this.doctor + ")" + ", patient Name: " + this.pacienteName + "(ID: " + this.paciente + ")" + ".\n Appointment date: " + this.date + ", symptoms= " + this.enfermedades + "]";
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoctor() {
        return doctor;
    }

    public void setDoctor(int doctor) {
        this.doctor = doctor;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getPaciente() {
        return paciente;
    }

    public void setPaciente(int paciente) {
        this.paciente = paciente;
    }

    public String getPacienteName() {
        return pacienteName;
    }

    public void setPacienteName(String pacienteName) {
        this.pacienteName = pacienteName;
    }


    public String getClinicaSpeciality() {
        return clinicaSpeciality;
    }

    public void setClinicaSpeciality(String clinicaSpeciality) {
        this.clinicaSpeciality = clinicaSpeciality;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getEnfermedades() {
        return enfermedades;
    }
    public void setEnfermedades(String enfermedades) {
        this.enfermedades = enfermedades;
    }


}
