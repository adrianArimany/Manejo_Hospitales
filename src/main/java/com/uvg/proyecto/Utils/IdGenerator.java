package com.uvg.proyecto.Utils;


import java.util.PriorityQueue;

import com.uvg.proyecto.Data.UserTypes;

public class IdGenerator {
    public static int doctorId = 0;
    public static int clinicId = 0;
    public static int patientId = 0;
    public static int citaId = 0;
    public static int prescriptionId = 0;

    private static PriorityQueue<Integer> availablePatientIds = new PriorityQueue<>();
    private static PriorityQueue<Integer> availableDoctorIds = new PriorityQueue<>();
    private static PriorityQueue<Integer> availableClinicIds = new PriorityQueue<>();
    private static PriorityQueue<Integer> availableCitaIds = new PriorityQueue<>();
    private static PriorityQueue<Integer> availablePrescriptionIds = new PriorityQueue<>();

    /**
     * Generates a new ID for the given UserType.
     * 
     * @param userTypes The UserType for which to generate an ID.
     * @return true if the ID was generated successfully, false otherwise.
     */
    public static int generateId(UserTypes userTypes) {
        switch (userTypes) {
            case Paciente:
                // Check if there are any available IDs to reuse for patients
                if (!availablePatientIds.isEmpty()) {
                    return availablePatientIds.poll();
                }
                return patientId++;
            case Doctor:
                if (!availableDoctorIds.isEmpty()) {
                    return availableDoctorIds.poll();
                }
                return doctorId++;
            case Clinica:
                if (!availableClinicIds.isEmpty()) {
                    return availableClinicIds.poll();
                }
                return clinicId++;
            case Cita:
                if (!availableCitaIds.isEmpty()) {
                    return availableCitaIds.poll();
                }
                return citaId++;
            case Prescription:
                if (!availablePrescriptionIds.isEmpty()) {
                    return availablePrescriptionIds.poll();
                }
                return prescriptionId++;
            default:
                return -1;
        }
        //return -1;
    }

     // Methods to release deleted IDs back into the available pool for reuse
     public static void releasePatientId(int id) {
        availablePatientIds.offer(id);
    }

    public static void releaseDoctorId(int id) {
        availableDoctorIds.offer(id);
    }

    public static void releaseClinicId(int id) {
        availableClinicIds.offer(id);
    }

    public static void releaseCitaId(int id) {
        availableCitaIds.offer(id);
    }

    public static void releasePrescriptionId(int id) {
        availablePrescriptionIds.offer(id);
    }

    /**
     * Initializes the ID counters for patients, doctors, and clinics.
     *
     * @param patientId the starting ID for patients
     * @param doctorId the starting ID for doctors
     * @param clinicId the starting ID for clinics
     * 
     * ()!!
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
