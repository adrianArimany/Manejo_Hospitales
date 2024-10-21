package com.uvg.proyecto.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.uvg.proyecto.Classes.Clinica;
import com.uvg.proyecto.Classes.Doctor;
import com.uvg.proyecto.Classes.Paciente;

public class DataHandler {
/**
 * 
 * TODO:
 * 
 * Eliminate the System.out.println() in this class, there are like 10 of them.
 * 
 * 
 */



    private static final String DELIMITER = ",";
    private static final String CLINICA_FILE = "clinicas.csv";
    private static final String DOCTOR_FILE = "doctores.csv";
    private static final String PACIENTE_FILE = "pacientes.csv";

    /**
     * Generic method to read from file
     * @param fileName eitehr clinicas.csv, doctores.csv, or paciente.csv
     * @return
     */ 
    private List<String[]> readFromFile(String fileName) {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                records.add(parts);
            }
        } catch (IOException e) {
        }
        return records;
    }

    // Generic method to write to file
    private void writeToFile(String fileName, String data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lee y retorna una lista de clínicas desde el archivo CSV.
     *
     * @return Lista de objetos Clinica leída desde el archivo.
     */
    public List<Clinica> readClinicas() {
        List<Clinica> clinicas = new ArrayList<>();
        for (String[] record : readFromFile(CLINICA_FILE)) {
            if (record.length == 3) {
                clinicas.add(new Clinica(record[0], record[1], record[2]));
            }
        }
        return clinicas;
    }
    /**
     * Escribe una nueva clínica al archivo CSV.
     *
     * @param clinica Objeto Clinica a escribir en el archivo.
     */
    public void writeClinica(Clinica clinica) {
        if (!idExists(CLINICA_FILE, clinica.getId())) {
            writeToFile(CLINICA_FILE, clinica.toString());
        } else {
            System.out.println("ID de clínica ya existe.");
        }
    }

    /**
    * Lee y retorna una lista de pacientes desde el archivo CSV.
    *
    * @return Lista de objetos Paciente leída desde el archivo.
    */
    public List<Paciente> readPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        for (String[] record : readFromFile(PACIENTE_FILE)) {
            if (record.length == 7) {
                Paciente paciente = new Paciente(record[0], record[1], record[2], record[3]);
                paciente.agregarHistorialMedico(record[4]);
                paciente.agregarEnfermedad(record[5]);
                paciente.agregarCitaMedica(record[6]);
                pacientes.add(paciente);
            }
        }
        return pacientes;
    }
    /**
     * Escribe un nuevo paciente al archivo CSV.
     *
     * @param paciente Objeto Paciente a escribir en el archivo.
     */
    public Boolean writePaciente(Paciente paciente) { //make sure this method work as a Boolean and remember the comments in writeDoctor.
        if (!idExists(PACIENTE_FILE, paciente.getId())) { 
            writeToFile(PACIENTE_FILE, paciente.toString()); 
            return true;
        } else {
            return false;
        }
    }
    /**
     * Quitar y editar pacientes al archivo CSV.
     *
     * The method is a boolean, because if there is an error, this way in Main it can alert the user.
     * 
     * @param pacientes Objeto paciente a escribir en el archivo.
     */
    public Boolean writePacientes(List<Paciente> pacientes) { //What is the difference between this method and writePacientes. 
        try (PrintWriter writer = new PrintWriter(new FileWriter(PACIENTE_FILE))) {
            for (Paciente paciente : pacientes) {
                // Convert List<String> to a single string by joining with a delimiter, e.g., "|"
                String historial = String.join("|", paciente.getHistorialMedico());
                String enfermedades = String.join("|", paciente.getEnfermedades());
                String citasMedicas = String.join("|", paciente.getCitasMedicas());
    
                // Now use String.join to create the CSV entry
                String pacienteData = String.join(",", 
                    paciente.getId(), 
                    paciente.getNombre(), 
                    paciente.getClinica(), 
                    historial, 
                    enfermedades, 
                    citasMedicas
                );
                writer.println(pacienteData); 
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    


    /**
     * Lee y retorna una lista de doctores desde el archivo CSV.
     *
     * @return Lista de objetos Doctor leída desde el archivo.
     */
    public List<Doctor> readDoctores() {
        List<Doctor> doctores = new ArrayList<>();
        for (String[] record : readFromFile(DOCTOR_FILE)) {
            if (record.length == 3) {
                doctores.add(new Doctor(record[0], record[1], record[2]));
            }
        }
        return doctores;
    }
    /**
     * Escribe un nuevo doctor al archivo CSV.
     *
     * @param doctor Objeto Doctor a escribir en el archivo.
     */
    public Boolean writeDoctor(Doctor doctor) {
        if (!idExists(DOCTOR_FILE, doctor.getId())) { //Why is there a getId() in this?
            writeToFile(DOCTOR_FILE, doctor.toString()); // make sure that the toString is doing what is supposed to be doing.
            return true;
        } else {
            return false; //make sure that these booleans work
        }
    }


    /**
     * Escribe un nuevo doctor al archivo CSV.
     *
     * The method is a boolean, because if there is an error, this way in Main it can alert the user.
     * 
     * @param doctor Objeto Doctor a escribir en el archivo.
     */
    public Boolean writeDoctores(List<Doctor> doctores) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DOCTOR_FILE))) {
            for (Doctor doctor : doctores) {
                writer.println(doctor.toString());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Verifica si un ID ya existe en el archivo CSV especificado.
     * 
     * @param fileName El nombre del archivo a verificar.
     * @param id       El ID que se busca en el archivo.
     * @return true si el ID ya existe, false si no.
     */
    public boolean idExists(String fileName, String id) {
        List<String[]> records = readFromFile(fileName);
        for (String[] record : records) {
            if (record[0].equals(id)) {
                return true;
            }
        }
        return false;
    }  

     /**
     * Retrieves a Doctor object by ID from the CSV file.
     *
     * @param id The ID of the doctor to retrieve.
     * @return The Doctor object if found, null otherwise.
     */
    public Doctor getDoctorById(String id) {
        for (String[] record : readFromFile(DOCTOR_FILE)) {
            if (record.length == 3 && record[0].equals(id)) {
                return new Doctor(record[0], record[1], record[2]);
            }
        }
        return null;
    }

    /**
     * Retrieves a Paciente object by ID from the CSV file.
     *
     * @param id The ID of the patient to retrieve.
     * @return The Paciente object if found, null otherwise.
     */
    public Paciente getPacienteById(String id) {
        for (String[] record : readFromFile(PACIENTE_FILE)) {
            if (record.length == 4 && record[0].equals(id)) {
                return new Paciente(record[0], record[1], record[2], record[3]);
            }
        }
        return null;
    }


}
