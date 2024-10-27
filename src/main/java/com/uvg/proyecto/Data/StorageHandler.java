package com.uvg.proyecto.Data;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uvg.proyecto.Classes.Cita;
import com.uvg.proyecto.Classes.Clinica;
import com.uvg.proyecto.Classes.Doctor;
import com.uvg.proyecto.Classes.Paciente;
import com.uvg.proyecto.Classes.Prescription;
import com.uvg.proyecto.Utils.IdGenerator;

public class StorageHandler {
    private final String PATIENT_FILE = "src/main/java/com/uvg/proyecto/JSON/patient.json";
    private final String DOCTOR_FILE = "src/main/java/com/uvg/proyecto/JSON/doctor.json";
    private final String CLINICA_FILE = "src/main/java/com/uvg/proyecto/JSON/clinica.json";

    private Gson gson;

    public StorageHandler() {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        gson = builder.create();
        // Check if files exist or create them
        this.checkFilesOrCreatesThem();
    }

    private void checkFilesOrCreatesThem() {
        try {
            File patientFile = new File(PATIENT_FILE);
            File doctorFile = new File(DOCTOR_FILE);
            File clinicaFile = new File(CLINICA_FILE);

            if (!patientFile.exists()) {
                patientFile.createNewFile();
                this.savePacientes(new ArrayList<>());
            }

            if (!doctorFile.exists()) {
                doctorFile.createNewFile();
                this.saveDoctors(new ArrayList<>());
            }

            if (!clinicaFile.exists()) {
                clinicaFile.createNewFile();
                this.saveClinica(new ArrayList<>());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean savePacientes(ArrayList<Paciente> pacientes) {
        try (Writer writer = new FileWriter(PATIENT_FILE)) {
            gson.toJson(pacientes, writer);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public boolean saveDoctors(ArrayList<Doctor> doctors) {
        try (Writer writer = new FileWriter(DOCTOR_FILE)) {
            gson.toJson(doctors, writer);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public boolean saveClinica(ArrayList<Clinica> clinicas) {
        try (Writer writer = new FileWriter(CLINICA_FILE)) {
            gson.toJson(clinicas, writer);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

/**
 * Adds a new Paciente to the storage if it does not already exist.
 * 
 * This method first retrieves all existing Paciente records and checks if a 
 * Paciente with the given ID already exists. If the Paciente does not exist,
 * it adds the new Paciente to the list and saves the updated list to the file.
 * 
 * @param paciente The Paciente object to be added.
 * @return true if the Paciente was successfully added and saved, false if 
 *         a Paciente with the same ID already exists or if saving failed.
 */
    public boolean createPaciente(Paciente paciente) {
        // Get all pacientes
        ArrayList<Paciente> pacientes = this.getAllPatients();

        // Check if paciente already exists
        if (this.pacienteExists(pacientes, paciente.getId()))
            return false;

        // Add new paciente to pacientes
        pacientes.add(paciente);

        // Save it to the file
        if (this.savePacientes(pacientes))
            return true;

        return false;
    }

    public boolean deletePatient(int id) {
        // Get all pacientes
        ArrayList<Paciente> pacientes = this.getAllPatients();

        // Remove paciente with the specified id
        boolean removed = pacientes.removeIf(paciente -> paciente.getId() == id);

        // If a paciente was removed, release the ID and save changes to file
        if (removed) {
            IdGenerator.releasePatientId(id);  // Reuse the ID for future patients
            return this.savePacientes(pacientes);
        }

        return false;  // Return false if no paciente was removed
    }


    public ArrayList<Paciente> getDrPacientes(Doctor doc) {
        ArrayList<Integer> pacientesIds = doc.getPacientesId();

        // Get patients from their ids
        ArrayList<Paciente> pacientes = this.getAllPatients();

        ArrayList<Paciente> pacientesDelDoc = new ArrayList<>();
        for (Paciente paciente : pacientes) {
            if (pacientesIds.contains(paciente.getId())) {
                pacientesDelDoc.add(paciente);
            }
        }

        return pacientesDelDoc;
    }

    /**
     * Adds a Paciente to a Doctor's list of Pacientes.
     * 
     * This method first checks if the Paciente already exists under the Doctor.
     * If the Paciente does not exist, it adds the Paciente to the Doctor's list
     * and updates the Doctor's record. Then it adds the Doctor to the Paciente's
     * list of Doctors and updates the Paciente's record.
     * 
     * @param doc The Doctor to add the Paciente to.
     * @param paciente The Paciente to add to the Doctor.
     * @return true if the Paciente was successfully added to the Doctor and both
     *         records were updated, false if the Paciente already exists under the
     *         Doctor or if saving failed.
     */
    public boolean addPacienteToDoctor(Doctor doc, Paciente paciente) {
        // Check if paciente already exists under the doctor
        ArrayList<Paciente> pacientesDelDoc = this.getDrPacientes(doc);

        if (this.pacienteExists(pacientesDelDoc, paciente.getId())) {
            // Patient Exists already so let's not add it again
            return false;
        }

        doc.addPacienteToDoc(paciente.getId());
        boolean isDoctorUpdated = this.updateDoctor(doc);

        paciente.addDocToPaciente(doc.getId());
        boolean isPacienteUpdated = this.updatePatient(paciente);

        if (!isDoctorUpdated || !isPacienteUpdated) {
            return false;
        }

        return true;
    }

    public boolean drPrescribeMedicineToPatient(Doctor doc, Paciente paciente, Prescription prescription) {
        paciente.addPrescription(prescription);
        doc.addPrescription(prescription);

        boolean isDoctorUpdated = this.updateDoctor(doc);
        boolean isPacienteUpdated = this.updatePatient(paciente);

        return isDoctorUpdated && isPacienteUpdated;
    }

    public ArrayList<Cita> drViewCitas(Doctor testDoc) {
        return testDoc.getCitas();
    }

    private ArrayList<Paciente> getAllPatients() {
        File file = new File(PATIENT_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Paciente[] pacientes = gson.fromJson(reader, Paciente[].class);
            if (pacientes == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(List.of(pacientes));
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private ArrayList<Doctor> getAllDoctors() {
        File file = new File(DOCTOR_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Doctor[] doctors = gson.fromJson(reader, Doctor[].class);
            if (doctors == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(List.of(doctors));
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private boolean pacienteExists(ArrayList<Paciente> pacientes, int id) {
        return pacientes.stream()
                .filter(patient -> patient.getId() == id)
                .findFirst()
                .isPresent();
    }

    public boolean removePacienteFromDoc(Doctor doc, Paciente paciente) {
        // Check if paciente already exists under the doctor
        ArrayList<Paciente> pacientesDelDoc = this.getDrPacientes(doc);

        if (!this.pacienteExists(pacientesDelDoc, paciente.getId())) {
            // Patient Exists already so let's not add it again
            return false;
        }

        doc.removePacienteFromDoc(paciente.getId());
        boolean isDoctorUpdated = this.updateDoctor(doc);

        paciente.removeDocFromPaciente(doc.getId());
        boolean isPacienteUpdated = this.updatePatient(paciente);

        if (!isDoctorUpdated || !isPacienteUpdated) {
            return false;
        }

        return true;
    }

    public boolean createDoctor(Doctor doc) {
        // Get all doctors
        ArrayList<Doctor> doctors = this.getAllDoctors();

        // Check if doctor exists
        if (doctors
                .stream()
                .filter(doctor -> doctor.getId() == doc.getId())
                .findFirst()
                .isPresent()) {
            // Doctor already exists
            return false;
        }
        // Add new doctor to doctors
        doctors.add(doc);

        // Save it to the file
        if (this.saveDoctors(doctors))
            return true;

        return false;
    }


    public boolean deleteDoctor(int id) {
        // Get all doctors
        ArrayList<Doctor> doctors = this.getAllDoctors();

        // Remove doctor with same id
        boolean removed = doctors.removeIf(doc -> doc.getId() == id);

       // If a doctor was removed, release the ID and save changes to file
        if (removed) {
            IdGenerator.releaseDoctorId(id);  // Reuse the ID for future doctors
            return this.saveDoctors(doctors);
        }

        return false;  // Return false if no doctor was removed
    }

    private boolean updateDoctor(Doctor doc) {
        // Get all doctors
        ArrayList<Doctor> doctors = this.getAllDoctors();

        // Remove doctor with same id
        doctors.removeIf(doctor -> doctor.getId() == doc.getId());

        doctors.add(doc);

        // try to save to file
        if (this.saveDoctors(doctors))
            return true;

        return false;
    }

    private boolean updatePatient(Paciente paciente) {
        // Get all pacientes
        ArrayList<Paciente> pacientes = this.getAllPatients();

        // Remove paciente with same id
        pacientes.removeIf(pac -> pac.getId() == paciente.getId());

        pacientes.add(paciente);

        // try to save to file
        if (this.savePacientes(pacientes))
            return true;

        return false;
    }

    public boolean drAddCita(Doctor doc, Paciente paciente, String date) {
        // String date into localdate
        LocalDate localDate = LocalDate.parse(date);
        Cita cita = new Cita(doc.getId(), paciente.getId(), localDate);
        doc.addCita(cita);
        this.updateDoctor(doc);
        paciente.addCita(cita);
        this.updatePatient(paciente);

        return true;
    }

    /**
     * Retrieves a Paciente object by ID.
     *
     * @param id The ID of the patient to retrieve.
     * @return The Paciente object if found, null otherwise.
     */
    public Paciente getPacienteById(int id){
        ArrayList<Paciente> pacientes = this.getAllPatients();
        return pacientes.stream().filter(paciente -> paciente.getId() == id).findFirst().orElse(null);
    }
/**
 * Retrieves a Doctor object by ID.
 *
 * @param id The ID of the doctor to retrieve.
 * @return The Doctor object if found, null otherwise.
 */
    public Doctor getDoctorById(int id){
        ArrayList<Doctor> doctors = this.getAllDoctors();
        return doctors.stream().filter(doctor -> doctor.getId() == id).findFirst().orElse(null);
    }

    public ArrayList<Prescription> getPrescriptionsFromPatient(int pacienteId) {
        Paciente paciente = this.getPacienteById(pacienteId);
        return paciente.getPrescriptions();
    }

    public List<Cita> getPacienteCitas(int pacienteId) {
        Paciente paciente = this.getPacienteById(pacienteId);
        List<Cita> citas = paciente.getCitas();
        return citas;
    }

    public boolean initIds() {
    ArrayList<Paciente> pacientes = this.getAllPatients();
    ArrayList<Doctor> doctors = this.getAllDoctors();
    
    int highestPacienteId = 0;
    for (Paciente paciente : pacientes) {
        if (paciente.getId() > highestPacienteId) {
            highestPacienteId = paciente.getId();
        }
    }

    int highestDoctorId = 0;
    for (Doctor doctor : doctors) {
        if (doctor.getId() > highestDoctorId) {
            highestDoctorId = doctor.getId();
        }
    }

    // Initialize the IdGenerator with the highest existing IDs + 1
    IdGenerator.initIds(highestPacienteId + 1, highestDoctorId + 1, 0);
    return true;
}


    public ArrayList<Paciente> showAllPacientes() {
        return this.getAllPatients();
    }
    
    public ArrayList<Doctor> showAllDoctors() {
        return this.getAllDoctors();
    }

	public boolean addClinicToDoctor(Doctor testDoc, Clinica clinica) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'addClinicToDoctor'");
	}

    public ArrayList<Clinica> getClinicasFromDoctor(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getClinicasFromDoctor'");
    }
}
