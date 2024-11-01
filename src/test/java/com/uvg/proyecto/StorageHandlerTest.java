package com.uvg.proyecto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uvg.proyecto.Classes.Cita;
import com.uvg.proyecto.Classes.Clinica;
import com.uvg.proyecto.Classes.Doctor;
import com.uvg.proyecto.Classes.Paciente;
import com.uvg.proyecto.Classes.Prescription;
import com.uvg.proyecto.Data.StorageHandler;

public class StorageHandlerTest {
    private StorageHandler storageHandler;
    private Doctor testDoc;
    private Paciente testPaciente;
    @SuppressWarnings("unused")
    private Clinica testClinica;
    private Prescription testPrescription;


    @BeforeEach
    void setUp() {
        storageHandler = new StorageHandler();
        testDoc = new Doctor("dr me", "clinica1");
        testPaciente = new Paciente("patient me");
        testClinica = new Clinica("My speciality");
        testPrescription = new Prescription(testDoc.getId(), testPaciente.getId(), "prescription me");
    }

    @AfterEach
    void tearDown() {
        storageHandler.deleteDoctor(testDoc.getId());
        storageHandler.deletePatient(testPaciente.getId());
    }

    // Main Functionality

    @Test
    public void createPatient() {
        // Try to save it for the first time
        boolean isPacienteAdded = storageHandler.createPaciente(testPaciente);
        assertTrue(isPacienteAdded, "Paciente must be added to the storageHandler");
        // Try to save it again, this time it should not let you save
        boolean isDuplicatePacienteAdded = storageHandler.createPaciente(testPaciente);
        assertFalse(isDuplicatePacienteAdded, "Paciente must NOT be added to the storageHandler");
    }

    @Test
    public void deletePatient() {
        // create paciente
        storageHandler.createPaciente(testPaciente);
        // Try removing a paciente
        boolean isPacienteRemoved = storageHandler.deletePatient(testPaciente.getId());
        assertTrue(isPacienteRemoved, "Paciente must be removed i the storageHandler");
        storageHandler.getPacienteById(testPaciente.getId()); //TODO FIX
        // Try removing a non existing pacciente
        boolean isNonExistingPacienteRemoved = storageHandler.deletePatient(testPaciente.getId());
        assertFalse(isNonExistingPacienteRemoved, "Paciente that doesn't exists cannot be deleted");
    }

    @Test
    public void createDoctor() {
        storageHandler.deleteDoctor(testDoc.getId());

        boolean isDoctorCreated = storageHandler.createDoctor(testDoc);
        assertTrue(isDoctorCreated, "Doctor must be created");

        boolean isDuplicateDoctorCreated = storageHandler.createDoctor(testDoc);
        assertFalse(isDuplicateDoctorCreated, "Doctor must not be created");
    }

    @Test
    public void deleteDoctor() {
        storageHandler.createDoctor(testDoc);
        boolean isDoctorDeleted = storageHandler.deleteDoctor(testDoc.getId());
        assertTrue(isDoctorDeleted, "Doctor must be deleted");

        boolean isNonExistingDoctorDeleted = storageHandler.deleteDoctor(testDoc.getId());
        assertFalse(isNonExistingDoctorDeleted, "Doctor must not be deleted");
    }

    // Doctors

    @Test
    public void addPacienteToDoc() {
        // add Pacient should be able to add a patient to the doctor
        boolean isPacienteAdded = storageHandler.addPacienteToDoctor(testDoc, testPaciente);
        assertTrue(isPacienteAdded, "Paciente must be added to the storageHandler");

        // Check if paciente is on the doctor
        ArrayList<Paciente> pacientes = storageHandler.getDrPacientes(testDoc);
        assertTrue(
                pacientes
                        .stream()
                        .anyMatch(paciente -> paciente.getId() == testPaciente.getId()));

        // Try to add it again but should not work since the id is the same
        boolean isSamePatientAdded = storageHandler.addPacienteToDoctor(testDoc, testPaciente);
        assertFalse(isSamePatientAdded, "testPaciente must not be added to the storageHandler since it already exists");
    }

    @Test
    public void removePacienteFromDoc() {
        // add Pacient should be able to add a patient to the doctor
        storageHandler.addPacienteToDoctor(testDoc, testPaciente);

        boolean isPacienteRemoved = storageHandler.removePacienteFromDoc(testDoc, testPaciente);
        assertTrue(isPacienteRemoved, "Paciente must be removed from the doctor");

        // Check if paciente is on the doctor
        ArrayList<Paciente> pacientes = storageHandler.getDrPacientes(testDoc);
        assertFalse(
                pacientes
                        .stream()
                        .anyMatch(paciente -> paciente.getId() == testPaciente.getId()));

    }

    @Test
    public void drViewAllPatients() {
        storageHandler.createDoctor(testDoc);
        storageHandler.createPaciente(testPaciente);

        storageHandler.addPacienteToDoctor(testDoc, testPaciente);
        // getDrPatients should return all the patients from that doctor
        ArrayList<Paciente> pacientes = storageHandler.getDrPacientes(testDoc);
        assertNotNull(pacientes, "pacientes must not be null");

        assertEquals(pacientes.size(), 1);
        assertEquals(pacientes.get(0).getId(), testPaciente.getId());
    }

    @Test
    public void drAddCita() {
        boolean isCitaAdded = storageHandler.drAddCita(testDoc, testPaciente,"testClinic" ,"2021-06-01", "Dolor de cabeza");
        assertTrue(isCitaAdded, "Cita must be added to the doctor");

        
    }

    @Test
    public void drViewAppointments() {
        storageHandler.createDoctor(testDoc);
        storageHandler.createPaciente(testPaciente);

        storageHandler.addPacienteToDoctor(testDoc, testPaciente);
        storageHandler.drAddCita(testDoc, testPaciente, "" ,"2021-06-01", "dolor the cabeza");

        ArrayList<Cita> citas = storageHandler.drViewCitas(testDoc);
        assertNotNull(citas, "citas must not be null");

        assertEquals(citas.size(), 1);
        assertEquals(citas.get(0).getPaciente(), testPaciente.getId());
    }

    @Test
    public void prescribeMedicineToPatient() {
        boolean resultPrescription = storageHandler.drPrescribeMedicineToPatient(
                testPrescription);
        assertTrue(resultPrescription,
                "Storage handler should prescribe the medicine  to the patient");
    }

    // Paciente

    @Test
    public void getPrescriptionsFromPatient() {
        storageHandler.drPrescribeMedicineToPatient(testPrescription);
        ArrayList<Prescription> prescriptions = storageHandler.getPrescriptionsFromPatient(testPaciente.getId());
        assertNotNull(prescriptions, "prescriptions must not be null");

        assertEquals(prescriptions.size(), 1);
        assertEquals(prescriptions.get(0).getDoctor(), testDoc.getId());
    }

    @Test
    public void getPacienteCitas() {
        storageHandler.addPacienteToDoctor(testDoc, testPaciente);
        storageHandler.drAddCita(testDoc, testPaciente,"testClinic" ,"2021-06-01", "Dolor de cabeza y mocos");

        List<Cita> citas = storageHandler.getPacienteCitas(testPaciente.getId());
        assertNotNull(citas, "citas must not be null");

        assertEquals(citas.size(), 1);
        assertEquals(citas.get(0).getPaciente(), testPaciente.getId());
    }

  
    // Clinicas
    @Test
    public void createClinic() {
        @SuppressWarnings("unused")
        ArrayList<Integer> doctorId = new ArrayList<>();
        Clinica newClinic = new Clinica("prueva 1");
        
        boolean isClinicCreated = storageHandler.createNewClinic(newClinic);
        assertTrue(isClinicCreated, "Clinic must be created");
    }

    @Test
    public void addClinicToDoctor() {
        // Create doctor to document
        storageHandler.createDoctor(testDoc);

        // Crea la clinica y la agrega al doctor testDoc
        Clinica newClinic = new Clinica("testClinic");
        storageHandler.createNewClinic(newClinic);

        boolean isClinicAdded = storageHandler.addClinicToDoctor(testDoc.getId(), newClinic.getId());
        assertTrue(isClinicAdded, "Clinic must be added to the doctor");

        // Check si la clinica esta en el doctor
        ArrayList<Doctor> doctors = storageHandler.getAllDoctorsFromClinic(newClinic.getId()); 
        
        assertTrue(doctors.stream().anyMatch(doctor -> doctor.getId() == testDoc.getId()),
                "Doctor must be added to the clinic");
        assertTrue(doctors.size() == 1);
    }
    @Test
    public void addHistorialMedicoToPatint() {
        //Create historial medico to document (create function in storageHandler)
        //storageHandler.createHistorialMedico(paciente, );
        //Create 
        
    }
    
    //Citas:


    @Test
    public void addCitaPaciente() {

    }

}
