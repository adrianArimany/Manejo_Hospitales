package com.uvg.proyecto.Classes;

import java.util.HashMap;

import com.uvg.proyecto.Data.UserTypes;
import com.uvg.proyecto.Utils.IdGenerator;

public class Prescription {
    public int id;
    public int doctor;
    public int paciente;
    public String medicines;

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


    public int getId() {
        return id;
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

    public int getPaciente() {
        return paciente;
    }

    public void setPaciente(int paciente) {
        this.paciente = paciente;
    }

    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    @Override
    public String toString() {
        return "Prescription [id=" + id + ", doctor=" + doctor + ", paciente=" + paciente + ", medicines=" + medicines
                + "]";
    }

    

}
