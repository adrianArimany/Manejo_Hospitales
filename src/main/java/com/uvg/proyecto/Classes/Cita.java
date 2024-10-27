package com.uvg.proyecto.Classes;

import java.time.LocalDate;

import com.uvg.proyecto.Data.UserTypes;
import com.uvg.proyecto.Utils.IdGenerator;

public class Cita {
    public int id;
    public int doctor;
    public int paciente;
    public LocalDate date;

    public Cita(int doctor, int paciente, LocalDate date) {
        this.id = IdGenerator.generateId(UserTypes.Cita);
        this.doctor = doctor;
        this.paciente = paciente;
        this.date = date;
    }

    public Cita(int doctor, int paciente) {
        this.id = IdGenerator.generateId(UserTypes.Cita);

        this.doctor = doctor;
        this.paciente = paciente;
    }

    @Override
    public String toString() {
        return "Cita [id=" + id + ", doctor=" + doctor + ", paciente=" + paciente + ", date=" + date + "]";
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}