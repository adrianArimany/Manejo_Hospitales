package com.uvg.proyecto.Utils;

import com.uvg.proyecto.Data.UserTypes;

public class IdGenerator {
    public static int doctorId = 0;
    public static int clinicId = 0;
    public static int patientId = 0;
    public static int citaId = 0;
    public static int prescriptionId = 0;
    

    /**
     * Generates a new ID for the given UserType.
     * 
     * @param userTypes The UserType for which to generate an ID.
     * @return true if the ID was generated successfully, false otherwise.
     */
    public static int generateId(UserTypes userTypes) {
        switch (userTypes) {
            case Paciente:
                return patientId++;
            case Doctor:
                return doctorId++;
            case Clinica:
                return clinicId++;
            case Cita:
                return citaId++;
            case Prescription:
                return prescriptionId++;
            default:
                break;
        }
        return -1;
    }


    /**
     * Initializes the ID counters for patients, doctors, and clinics.
     *
     * @param patientId the starting ID for patients
     * @param doctorId the starting ID for doctors
     * @param clinicId the starting ID for clinics
     */
    public static void initIds(int patientId, int doctorId, int clinicId) {
        IdGenerator.patientId = patientId;
        IdGenerator.doctorId = doctorId;
        IdGenerator.clinicId = clinicId;
    }

    /**
     * Initializes the ID counters for patients, doctors, clinics, citas, and prescriptions.
     * 
     * @param patientId the starting ID for patients
     * @param doctorId the starting ID for doctors
     * @param clinicId the starting ID for clinics
     * @param citaId the starting ID for citas
     * @param prescriptionId the starting ID for prescriptions
     */
    public static void initIds(int patientId, int doctorId, int clinicId, int citaId, int prescriptionId) {
        IdGenerator.patientId = patientId;
        IdGenerator.doctorId = doctorId;
        IdGenerator.clinicId = clinicId;
        IdGenerator.citaId = citaId;
        IdGenerator.prescriptionId = prescriptionId;
    }
}
