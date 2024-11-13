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
import com.uvg.proyecto.Adapters.LocalDateAdapter;
import com.uvg.proyecto.Classes.Cita;
import com.uvg.proyecto.Classes.Clinica;
import com.uvg.proyecto.Classes.Doctor;
import com.uvg.proyecto.Classes.Paciente;
import com.uvg.proyecto.Classes.Prescription;
import com.uvg.proyecto.Utils.IdGenerator;
import java.util.Random;

public class StorageHandler {
    
    /**
     * This is the path for the patient.json file.
     */
    private final String PATIENT_FILE = "src/main/java/com/uvg/proyecto/JSON/patient.json";
    /**
     * This is the path for the doctor.json file.
     */
    private final String DOCTOR_FILE = "src/main/java/com/uvg/proyecto/JSON/doctor.json";
    /**
     * This is the path for the clinica.json file.
     */
    private final String CLINICA_FILE = "src/main/java/com/uvg/proyecto/JSON/clinica.json";
    private Random random = new Random();
    private Gson gson;

    public StorageHandler() {
        GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd").setPrettyPrinting();
        gson = builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        // Check if files exist or create them
        this.checkFilesOrCreatesThem();
        this.initIds();
    }

    /**
     * Verifies if the json files for patients, doctors and clinics exist,
     * if not, creates them and save an empty list of each.
     */
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

    /**
     * Saves the given list of Pacientes to the json file.
     * 
     * @param pacientes The list of Pacientes to save.
     * @return true if the save is successful, false otherwise.
     */
    public boolean savePacientes(ArrayList<Paciente> pacientes) {
        try (Writer writer = new FileWriter(PATIENT_FILE)) {
            gson.toJson(pacientes, writer);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Saves the given list of Doctors to the json file.
     * 
     * @param doctors The list of Doctors to save.
     * @return true if the save is successful, false otherwise.
     */
    public boolean saveDoctors(ArrayList<Doctor> doctors) {
        try (Writer writer = new FileWriter(DOCTOR_FILE)) {
            gson.toJson(doctors, writer);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Saves the given list of Clinicas to the json file.
     * 
     * @param clinicas The list of Clinicas to save.
     * @return true if the save is successful, false otherwise.
     */
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

    /**
     * Removes a Paciente with the given ID from the storage if it exists.
     * 
     * This method first retrieves all existing Paciente records and checks if a
     * Paciente with the given ID exists. If the Paciente exists, it removes the
     * Paciente from the list, releases its ID for future use, and saves the
     * updated list to the file.
     * 
     * @param id The ID of the Paciente to be removed.
     * @return true if the Paciente was successfully removed and saved, false if
     *         a Paciente with the given ID does not exist or if saving failed.
     */
    public boolean deletePatient(int id) {
        // Get all pacientes
        ArrayList<Paciente> pacientes = this.getAllPatients();

        // Remove paciente with the specified id
        boolean removed = pacientes.removeIf(paciente -> paciente.getId() == id);

        // If a paciente was removed, release the ID and save changes to file
        if (removed) {
            IdGenerator.releasePatientId(id); // Reuse the ID for future patients
            return this.savePacientes(pacientes);
        }

        return false; // Return false if no paciente was removed
    }

    /**
     * Gets the list of Paciente objects associated with a Doctor.
     * 
     * Given a Doctor object, it returns the list of Paciente objects
     * associated with that doctor by their IDs.
     * 
     * @param doc the Doctor object
     * @return the list of Paciente objects associated with the doctor
     */
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
     * @param doc      The Doctor to add the Paciente to.
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


    /**
     * Adds a new medical record to the patient.
     * 
     * This method first checks if the patient exists and if the medical record is not null.
     * If the patient does not have a medical record, it creates a new list and adds the
     * record to it. Then it updates the patient's record.
     * 
     * @param pacienteId the ID of the patient to add the medical record to.
     * @param historialMedico the medical record to add.
     * @return true if the medical record was successfully added to the patient and the
     *         record was updated, false if the patient does not exist or if the medical
     *         record is null.
     */
    public boolean createHistorialMedico(int pacienteId, String historialMedico) {
        // Find the paciente to update
        Paciente paciente = this.getPacienteById(pacienteId);

        if (paciente == null)
            return false;

        // Check that historialMedico is not null
        if (paciente.getHistorialMedico() == null) {
            paciente.setHistorialMedico(new ArrayList<>());
        }

        paciente.agregarHistorialMedico(historialMedico);
        return this.updatePatient(paciente);
    }
    
    /**
     * Allows a doctor to prescribe a medicine to a patient.
     * 
     * Given a Prescription object, this method will find the corresponding patient and doctor in the database and associate the prescription with both.
     * 
     * @param prescription
     *            the Prescription object to be associated with the doctor and patient.
     * 
     * @return true if the prescription was successfully associated with the doctor and patient and both were updated, false if the doctor or patient do not exist.
     */
    public boolean drPrescribeMedicineToPatient(Prescription prescription) {
        // get pacciente from presciption
        ArrayList<Paciente> pacientes = this.getAllPatients();
        // get doctor from presciprtion
        ArrayList<Doctor> doctores = this.getAllDoctors();
        
        Paciente paciente = pacientes.stream().filter(p -> p.getId() == prescription.getPaciente()).findFirst().orElse(null);
        if (paciente == null) return false;
        paciente.addPrescription(prescription);
        Doctor doc = doctores.stream().filter(d -> d.getId() == prescription.getDoctor()).findFirst().orElse(null);
        if (doc == null) return false;
        doc.addPrescription(prescription);

        boolean isDoctorUpdated = this.updateDoctor(doc);
        boolean isPacienteUpdated = this.updatePatient(paciente);

        return isDoctorUpdated && isPacienteUpdated;
    }

    /**
     * Retrieves the list of appointments (citas) associated with the given doctor.
     *
     * @param testDoc the Doctor object for which to retrieve the list of appointments.
     * @return an ArrayList of Cita objects representing the doctor's appointments.
     */
    public ArrayList<Cita> drViewCitas(Doctor testDoc) {
        return testDoc.getCitas();
    }

    /**
     * Reads the list of patients from the JSON file.
     * 
     * @return an ArrayList of Paciente objects representing all the patients in the database.
     */
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

    /**
     * Reads the list of doctors from the JSON file.
     * 
     * @return an ArrayList of Doctor objects representing all the doctors in the database.
     */
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

    /**
     * Reads the list of clinics from the JSON file.
     * 
     * @return an ArrayList of Clinica objects representing all the clinics in the database.
     */
    private ArrayList<Clinica> getAllClinics() {
        File file = new File(CLINICA_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Clinica[] clinicas = gson.fromJson(reader, Clinica[].class);
            if (clinicas == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(List.of(clinicas));
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Checks if a paciente exists in the given list of pacientes.
     * 
     * @param pacientes the list of pacientes to search in.
     * @param id        the id of the paciente to search for.
     * @return true if the paciente exists, false otherwise.
     */
    private boolean pacienteExists(ArrayList<Paciente> pacientes, int id) {
        return pacientes.stream()
                .filter(patient -> patient.getId() == id)
                .findFirst()
                .isPresent();
    }

    /**
     * Removes a patient from the doctor's list of patients and updates the doctor
     * and patient records in the database.
     *
     * @param doc      the Doctor object from which the patient is to be removed.
     * @param paciente the Paciente object to be removed from the doctor's list.
     * @return true if the patient is successfully removed and the records are updated,
     *         false otherwise.
     */
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

    /**
     * Adds a new Doctor to the storage if it does not already exist.
     * 
     * This method first retrieves all existing Doctor records and checks if a
     * Doctor with the given ID already exists. If the Doctor does not exist,
     * it adds the new Doctor to the list and saves the updated list to the file.
     * 
     * @param doc The Doctor object to be added.
     * @return true if the Doctor was successfully added and saved, false if
     *         a Doctor with the same ID already exists or if saving failed.
     */
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

    /**
     * Deletes a Doctor from the storage if it exists.
     * 
     * This method first retrieves all existing Doctor records and checks if a
     * Doctor with the given ID exists. If the Doctor exists, it removes the
     * Doctor from the list, releases its ID for future use, and saves the
     * updated list to the file.
     * 
     * @param id The ID of the Doctor to be removed.
     * @return true if the Doctor was successfully removed and saved, false if
     *         a Doctor with the given ID does not exist or if saving failed.
     */
    public boolean deleteDoctor(int id) {
        // Get all doctors
        ArrayList<Doctor> doctors = this.getAllDoctors();

        // Remove doctor with same id
        boolean removed = doctors.removeIf(doc -> doc.getId() == id);

        // If a doctor was removed, release the ID and save changes to file
        if (removed) {
            IdGenerator.releaseDoctorId(id); // Reuse the ID for future doctors
            return this.saveDoctors(doctors);
        }

        return false; // Return false if no doctor was removed
    }

    /**
     * Updates a Doctor in the storage.
     * 
     * This method first retrieves all existing Doctor records, removes the
     * Doctor with the same ID as the given Doctor, and adds the given Doctor
     * to the list. Then it saves the updated list to the file.
     * 
     * @param doc The Doctor object to be updated.
     * @return true if the Doctor was successfully updated and saved, false if
     *         saving failed.
     */
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

    /**
     * Updates a Paciente in the storage.
     * 
     * This method first retrieves all existing Paciente records, removes the
     * Paciente with the same ID as the given Paciente, and adds the given 
     * Paciente to the list. Then it saves the updated list to the file.
     * 
     * @param paciente The Paciente object to be updated.
     * @return true if the Paciente was successfully updated and saved, false if
     *         saving failed.
     */
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


    
/**
 * Adds an appointment (cita) to both the Doctor's and Paciente's list of appointments.
 * 
 * This method creates a new Cita object based on the provided parameters and adds it to
 * the Doctor's and Paciente's records. It updates both records and associates the Paciente
 * with the Doctor if not already associated.
 * 
 * @param doc The Doctor who will have the appointment added to their schedule.
 * @param paciente The Paciente who will have the appointment added to their schedule.
 * @param clinicaSpeciality The specialty of the clinic where the appointment is scheduled.
 * @param date The date of the appointment.
 * @param enfermedades The medical conditions related to the appointment.
 * @return true if the appointment was successfully added to both the Doctor's and Paciente's records.
 */
    public boolean drAddCita(Doctor doc, Paciente paciente,String clinicaSpeciality, String date, String enfermedades) {
        //Creates new cita based on the parameters.
        Cita cita = new Cita(doc.getId(), paciente.getId(), doc.getNombre() , paciente.getNombre(), clinicaSpeciality,  date, enfermedades);
        
        // add cita to doctor
        doc.addCita(cita);
        this.updateDoctor(doc);
        
        // add cita to pac
        paciente.addCita(cita);
        this.updatePatient(paciente);

        // add paciente to doctor
        this.addPacienteToDoctor(doc, paciente);

        return true;
    }

    
    /**
     * Finds a random doctor from the given clinic speciality.
     * 
     * @param clinicaSpeciality The speciality of the clinic to select a doctor from.
     * @return A random doctor from the given clinic speciality.
     * @throws IllegalArgumentException if no doctors are available in the selected clinic.
     */
    public Doctor docAddedtoCita(String clinicaSpeciality) {
        // Add all the doctors.
        ArrayList<Doctor> doctors = this.getAllDoctors();
        // Filter the doctors in a clinic, ensuring getClinica() is not null
        List<Doctor> doctorsInClinic = doctors.stream()
            .filter(doc -> doc.getClinica() != null && doc.getClinica().equals(clinicaSpeciality))
            .toList();
        // Check if there are doctors available in the clinic
        if (doctorsInClinic.isEmpty()) {
            throw new IllegalArgumentException("No doctors available in the selected clinic.");
        }
        // Generate a random index for the list of doctors in the clinic
        int randomIndex = random.nextInt(doctorsInClinic.size());  
        Doctor randomDoc = doctorsInClinic.get(randomIndex);
        // Return the selected doctor
        return randomDoc;
    }
    /**
     * Retrieves a Paciente object by ID.
     *
     * @param id The ID of the patient to retrieve.
     * @return The Paciente object if found, null otherwise.
     */
    public Paciente getPacienteById(int id) {
        ArrayList<Paciente> pacientes = this.getAllPatients();
        return pacientes.stream().filter(paciente -> paciente.getId() == id).findFirst().orElse(null);
    }

    /**
     * Retrieves a Doctor object by ID.
     *
     * @param id The ID of the doctor to retrieve.
     * @return The Doctor object if found, null otherwise.
     */
    public Doctor getDoctorById(int id) {
        ArrayList<Doctor> doctors = this.getAllDoctors();
        return doctors.stream().filter(doctor -> doctor.getId() == id).findFirst().orElse(null);
    }

    /**
     * Retrieves the list of prescriptions associated with a patient.
     *
     * @param pacienteId the ID of the patient to retrieve the prescriptions for
     * @return the list of prescriptions associated with the patient
     */
    public ArrayList<Prescription> getPrescriptionsFromPatient(int pacienteId) {
        Paciente paciente = this.getPacienteById(pacienteId);
        return paciente.getPrescriptions();
    }

    /**
     * Retrieves the list of appointments (citas) associated with a patient.
     *
     * @param pacienteId The ID of the patient to retrieve the appointments for.
     * @return The list of appointments associated with the patient.
     */
    public List<Cita> getPacienteCitas(int pacienteId) {
        Paciente paciente = this.getPacienteById(pacienteId);
        List<Cita> citas = paciente.getCitas();
        return citas;
    }

    /**
     * Initializes the ID counters for patients, doctors, and clinics.
     * 
     * This method should be called when the storage handler is first initialized.
     * It scans the existing data and finds the highest existing IDs for patients,
     * doctors, and clinics. It then initializes the {@link IdGenerator} class
     * with the highest existing IDs + 1.
     * 
     * @return true if the ID counters were successfully initialized, false otherwise.
     */
    public boolean initIds() {
        ArrayList<Paciente> pacientes = this.getAllPatients();
        ArrayList<Doctor> doctors = this.getAllDoctors();
        ArrayList<Clinica> clinicas = this.getAllClinics();

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

        int highestClinicasId = 0;
        for (Clinica clinica : clinicas) {
            if (clinica.getId() > highestClinicasId) {
                highestClinicasId = clinica.getId();
            }
        }
        // Initialize the IdGenerator with the highest existing IDs + 1
        IdGenerator.initIds(highestPacienteId + 1, highestDoctorId + 1, highestClinicasId + 1);
        return true;
    }

    /**
     * Returns a list of all patients in the storage.
     * 
     * @return a list of Paciente objects representing all patients in the storage.
     */
    public ArrayList<Paciente> showAllPacientes() {
        return this.getAllPatients();
    }

    /**
     * Returns a list of all doctors in the storage.
     * 
     * @return an ArrayList of Doctor objects representing all doctors in the storage.
     */
    public ArrayList<Doctor> showAllDoctors() {
        return this.getAllDoctors();
    }

    /**
     * Adds a doctor to a clinic. If the doctor is already in another clinic,
     * it will be removed from that clinic before being added to the new one.
     * 
     * @param testDocId The ID of the doctor to add to the clinic.
     * @param clinicaId The ID of the clinic to add the doctor to.
     * @return true if the doctor was successfully added to the clinic, false otherwise.
     */
    public boolean addClinicToDoctor(int testDocId, int clinicaId) {
        // read all doctors
        ArrayList<Doctor> doctores = this.getAllDoctors();
        // read all clinics
        ArrayList<Clinica> clinicas = this.getAllClinics();
        // find doctor with matching id
        Doctor doctorFound = doctores.stream().filter(doctor -> doctor.getId() == testDocId).findFirst().orElse(null);
        if (doctorFound == null) return false;
        // find clinic with matching id 
        Clinica clinicaFound = clinicas.stream().filter(clinica -> clinica.getId() == clinicaId).findFirst().orElse(null); 
        if (clinicaFound == null) return false;

        // Check if doctor already in clinic
        if (doctorFound.getClinica() == clinicaFound.getEspecialidad()) return false;

        // If doctor in another clinic, remove it from that clinic
        if (doctorFound.getClinica() != null) {
            // get all clinics
            // clinica.getEspecialidad
            Clinica oldClinic = clinicas.stream().filter(clinica -> clinica.getEspecialidad().equalsIgnoreCase(doctorFound.getClinica())).findFirst().orElse(null);
            // doctorFound.getClinica()
            if (oldClinic != null) {
                ArrayList<Integer> newidsWithoutDoctor = new ArrayList<>(oldClinic.getDoctorId().stream().filter(doctorIDold -> doctorIDold != doctorFound.getId()).toList());
                oldClinic.setArrayDoctorId(newidsWithoutDoctor);

            }
        }

        // doctor add clinic
        doctorFound.setClinica(clinicaFound.getEspecialidad());
        // clinic add doctor
        clinicaFound.setDoctorId(doctorFound.getId());

        // Save to memory the updated values
        boolean isDoctorSaved = this.saveDoctors(doctores);
        boolean isClinicaSaved = this.saveClinica(clinicas);

        return (isClinicaSaved && isDoctorSaved);
    }

/**
 * Retrieves a list of Doctors associated with a specific clinic.
 *
 * This method takes a clinic ID, finds the corresponding Clinica object,
 * and returns a list of Doctor objects whose IDs are associated with that
 * clinic. If the clinic ID is not found, it returns an empty list.
 *
 * @param clinicaId The ID of the clinic.
 * @return An ArrayList of Doctor objects associated with the specified clinic,
 *         or an empty list if the clinic is not found.
 */
    public ArrayList<Doctor> getAllDoctorsFromClinic(int clinicaId) {
        // read all clinics
        ArrayList<Clinica> clinicas = this.getAllClinics();
        //find id from clinic
        Clinica clinicaFound = clinicas.stream().filter(clinica -> clinica.getId()==clinicaId).findFirst().orElse(null);
        if (clinicaFound == null) return new ArrayList<>();
        //return all doctors in that clinic

        // mapper doctorsId --> Doctors!
        // get all doctors
        ArrayList<Doctor> allDoctors = this.getAllDoctors();
        // keep doctors if their ID is in the clinicaFound doctorsID
        ArrayList<Integer> doctorsId = clinicaFound.getDoctorId();
        
        List<Doctor> doctorsFoundInClinca = allDoctors.stream().filter(doctor -> doctorsId.contains(doctor.getId())).toList();
        
        return new ArrayList<>(doctorsFoundInClinca);
    }

    /**
     * Adds a new Clinica to the storage.
     * 
     * @param clinica The Clinica object to add.
     * @return true if the Clinica was successfully added, false otherwise.
     */
    public boolean createNewClinic(Clinica clinica) {
        // Read all clinics from file
        ArrayList<Clinica> clinicas = this.getAllClinics();

        // Add new clinic to clinics
        clinicas.add(clinica);

        return this.saveClinica(clinicas);
    }

    /**
     * Elimina una clinica de la lista de clinicas.
     * 
     * @param idClinica El id de la clinica a eliminar.
     * @return true si se elimino correctamente la clinica, false de lo contrario.
     */
    public boolean eliminarClinica(int idClinica){
        ArrayList<Clinica> clinicas = this.getAllClinics();
        clinicas.removeIf(clinica -> clinica.getId() == idClinica);
        return this.saveClinica(clinicas);
    }

    /**
     * Retrieves a list of all Clinicas stored in the system.
     * 
     * @return An ArrayList of Clinica objects representing all clinics.
     */
    public ArrayList<Clinica> getAllClinicas() {
        ArrayList<Clinica> allClinicas = this.getAllClinics();
        return allClinicas;
    } 
    /**
     * Retrieves a list of all Doctor objects available for a user.
     * 
     * @return An ArrayList of Doctor objects representing all doctors.
     */
    public ArrayList<Doctor> getAllDoctorForUser() {
        ArrayList<Doctor> allDoctors = this.getAllDoctors();
        return allDoctors;
    } 
    
    /**
     * Retrieves a list of all Paciente objects available for a user.
     *
     * @return An ArrayList of Paciente objects representing all patients.
     */
    public ArrayList<Paciente> getAllPacientesForUser() {
        ArrayList<Paciente> allPacientes = this.getAllPatients();
        return allPacientes;
    }
}
