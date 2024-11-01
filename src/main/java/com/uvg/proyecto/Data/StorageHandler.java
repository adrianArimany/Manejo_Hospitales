package com.uvg.proyecto.Data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uvg.proyecto.Classes.Cita;
import com.uvg.proyecto.Classes.Clinica;
import com.uvg.proyecto.Classes.Doctor;
import com.uvg.proyecto.Classes.Paciente;
import com.uvg.proyecto.Classes.Prescription;
import com.uvg.proyecto.Utils.IdGenerator;
import java.util.Random;

public class StorageHandler {
    private final String PATIENT_FILE = "src/main/java/com/uvg/proyecto/JSON/patient.json";
    private final String DOCTOR_FILE = "src/main/java/com/uvg/proyecto/JSON/doctor.json";
    private final String CLINICA_FILE = "src/main/java/com/uvg/proyecto/JSON/clinica.json";

    private Gson gson;

    public StorageHandler() {
        GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd").setPrettyPrinting();
        gson = builder.create();
        // Check if files exist or create them
        this.checkFilesOrCreatesThem();
        this.initIds();
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
            IdGenerator.releasePatientId(id); // Reuse the ID for future patients
            return this.savePacientes(pacientes);
        }

        return false; // Return false if no paciente was removed
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

    /**@Todo finish it. 
     * This is suppose to edit the historial medico in case the patient already has one.
     * @param pacienteId
     * @param historialMedico
     * @return
     */
    public boolean editHistorialMedico(int pacienteId, String historialMedico) {
        // //finds the paciente to update
        // Paciente paciente = this.getPacienteById(pacienteId);
        
        // if (paciente == null || paciente.getHistorialMedico().isEmpty())
        //     return false;
        
            


        return false;
    }
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
            IdGenerator.releaseDoctorId(id); // Reuse the ID for future doctors
            return this.saveDoctors(doctors);
        }

        return false; // Return false if no doctor was removed
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


    /**
     * 
     * @Todo Esto pero los parametros son id
     */
    public boolean drAddCita(Doctor doc, Paciente paciente,String clinicaSpeciality, String date, String enfermedades) {
        
        
        Cita cita = new Cita(doc.getId(), paciente.getId(), doc.getNombre() , paciente.getNombre(), clinicaSpeciality,  date, enfermedades);
        doc.addCita(cita);
        this.updateDoctor(doc);
        paciente.addCita(cita);
        this.updatePatient(paciente);

        return true;
    }

    /**
     * This method adds a doc to the appointment that the patient enters into the code.
     * @return
     */
    public Doctor docAddedtoCita(String clinicaSpeciality) {
        // Add all the doctors.
        ArrayList<Doctor> doctors = this.getAllDoctors();
        // Filter the doctors in a clinic, ensuring getClinica() is not null
        List<Doctor> doctorsInClinic = doctors.stream()
            .filter(doc -> doc.getClinica() != null && doc.getClinica().equals(clinicaSpeciality))
            .toList();
        System.out.println("Total doctors found: " + doctors.size());
        System.out.println("Doctors in clinic matching " + clinicaSpeciality + ": " + doctorsInClinic.size());
        // Check if there are doctors available in the clinic
        if (doctorsInClinic.isEmpty()) {
            throw new IllegalArgumentException("No doctors available in the selected clinic.");
        }
        
        
        // Generate a random index for the list of doctors in the clinic
        Random random = new Random();
        //int randomIndex = ThreadLocalRandom.current().nextInt(0, doctorsInClinic.size());
        int randomIndex = random.nextInt(doctorsInClinic.size());
        System.out.println("Random index selected: " + randomIndex);
        Doctor randomDoc = doctorsInClinic.get(randomIndex);
        System.out.println("Doctor selected: " + randomDoc.getNombre()); // Assuming getName() exists
    
        // Return the selected doctor
        return randomDoc;
    }
    

    /**
     * This method takes a cita and then trasnfers that cita to a doctor
     * @param cita
     * @return
     */
    public boolean addCitaToClinic(Cita cita) {
        //Add all the clincis

        ArrayList<Clinica> clinicas = this.getAllClinicas();
        // get the clinic with the same id as the cita
        Clinica clinica = clinicas.stream().filter(c -> c.getId() == cita.getId()).findFirst().orElse(null); //this is returning a null???
        //perhaps is creating a null because cita doesn't have defined id???
        if (clinica == null) {
            return false;
        }
        // filter the doctors in a clinic
        List<Doctor> doctorsInClinic = clinica.getDoctorId().stream().map(id -> this.getDoctorById(id)).filter(doc -> doc.getClinica() == clinica.getEspecialidad()).toList();
        

        //Do a random number from 1 to the size of the list of doctors in the clinic
        int randomIndex = ThreadLocalRandom.current().nextInt(0, doctorsInClinic.size());
        Doctor randomDoc = doctorsInClinic.get(randomIndex);

        // add the cita to the doctor
        randomDoc.addCita(cita);
        this.updateDoctor(randomDoc);

        // return doc
        return true;
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

    public ArrayList<Paciente> showAllPacientes() {
        return this.getAllPatients();
    }

    public ArrayList<Doctor> showAllDoctors() {
        return this.getAllDoctors();
    }

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

    public boolean createNewClinic(Clinica clinica) {
        // Read all clinics from file
        ArrayList<Clinica> clinicas = this.getAllClinics();

        // Add new clinic to clinics
        clinicas.add(clinica);

        return this.saveClinica(clinicas);
    }

    public boolean eliminarClinica(int idClinica){
        ArrayList<Clinica> clinicas = this.getAllClinics();
        clinicas.removeIf(clinica -> clinica.getId() == idClinica);
        return this.saveClinica(clinicas);
    }

    public ArrayList<Clinica> getAllClinicas() {
        return this.getAllClinics();
    } 
    public ArrayList<Doctor> getAllDoctorForUser() {
        return this.getAllDoctors();
    }  
}
