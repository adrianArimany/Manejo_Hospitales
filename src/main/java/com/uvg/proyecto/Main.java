package com.uvg.proyecto;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger; 

import com.uvg.proyecto.Authenticator.Authenticator;
import com.uvg.proyecto.Classes.Cita;
import com.uvg.proyecto.Classes.Clinica;
import com.uvg.proyecto.Classes.Doctor;
import com.uvg.proyecto.Classes.Paciente;
import com.uvg.proyecto.Classes.Prescription;
import com.uvg.proyecto.Data.PropertiesFile;
import com.uvg.proyecto.Data.StorageHandler;


/**
 * Main Class that runs the program. It provides a menu for the user to interact.
 * It uses the StorageHandler class to read and write data from and to the file.
 * It also uses the PropertiesFile class to read and write data from the properties file.
 * 
 * @version 1.0
 * -------------------------------------
 * @Todo
 * Currently no tasks for this version.
 */

public class Main {
    /**
     * Enum UserType is used to represent different user types in the program.
     * It contains three possible values: Paciente, Doctor, and Admin.
     * 
     */
    public enum UserType {
        Paciente,
        Doctor,
        Admin,
    }

    /**
     * The loginPac variable is a Paciente object that is used to store the currently logged-in Paciente.
     * exitSystem is a boolean variable that is used to determine if the program should exit.
     * loginDoc is a Doctor object that is used to store the currently logged-in Doctor.
     * 
     * clinicToShow is an ArrayList of Clinica objects that are used to store the clinics that are displayed in the menu.
     * doctoresToShow is an ArrayList of Doctor objects that are used to store the doctors that are displayed in the menu.
     * pacientesToShow is an ArrayList of Paciente objects that are used to store the patients that are displayed in the menu.
     * 
     */
    private Paciente loginPac; 
    private boolean exitSystem = false;
    private Doctor loginDoc;
    private StorageHandler storageHandler;
    private PropertiesFile config = new PropertiesFile();
    private ArrayList<Clinica> clinicToShow;
    private ArrayList<Doctor> doctoresToShow;
    private ArrayList<Paciente> pacientesToShow;
    public final Scanner scanner = new Scanner(System.in);

    /**
     * Starts the program by creating a new instance of Main and calling
     * MenuBegins() on it. If any exception is thrown during the execution of
     * MenuBegins(), it is caught and logged with a SEVERE level. The scanner is
     * always closed in a finally block, and any error that occurs while closing
     * it is logged with a WARNING level.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main app = new Main();
        app.storageHandler = new StorageHandler();
        app.storageHandler.initIds();
        try {
            app.MenuBegins();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            Logger logger = Logger.getLogger(Main.class.getName());
            logger.log(Level.SEVERE, "Unexpected error in main method", e); 
        } finally {
            if (app.scanner != null) {
                try {
                    app.scanner.close();
                } catch (Exception e) {
                    Logger logger = Logger.getLogger(Main.class.getName());
                    logger.log(Level.WARNING, "Error closing scanner", e); 
                }
            }
        }
    }

    /**
     * Initializes and manages the main menu interaction for different user types.
     *
     * The method initializes necessary data from the storage handler and presents
     * a menu for the user to log in as a Patient, Doctor, or Admin. Based on the user
     * type, it directs the user to the appropriate menu handling method.
     * 
     * The method handles exceptions related to invalid input and number format,
     * ensuring the program does not crash due to user errors. It continues to prompt
     * for login until the user chooses to exit the system.
     */
    public void MenuBegins() {
        UserType user = null;
        this.pacientesToShow = this.storageHandler.getAllPacientesForUser();
        this.doctoresToShow = this.storageHandler.getAllDoctorForUser();
        this.clinicToShow = this.storageHandler.getAllClinicas();
        do {
        try {
            user = login();
            while (user != null && !exitSystem) {
                switch (user) {
                    case Paciente:
                        if (loginPac != null) {
                            this.pacienteMenu(loginPac);
                        } else {
                            System.out.println("Error: Patient not found.");
                        }
                        break;
                    case Doctor:
                        if (this.loginDoc != null) {
                            this.doctorMenu(this.loginDoc);
                        } else {
                            System.out.println("Error: Doctor not found.");
                        }
                        break;
                    case Admin:
                        this.adminMenu();
                        break;
                    default:
                        System.out.println("Error: Invalid user type.");
                        break;
                }
                if (!exitSystem) {
                    user = login(); // Prompt for re-login only if the system is not exiting
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input. Please enter a number.");
            scanner.nextLine(); // Clear the invalid input from the scanner
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format. Please enter a valid number.");
        } 
        } while (user != null && !exitSystem);
    }

    /**
     * Handles the login process for the system.
     * 
     * Displays a menu with the options to login as a patient, doctor, or admin.
     * For a patient, it displays a list of all registered patients and allows the user to select a patient to login as.
     * For a doctor, it displays a list of all registered doctors and allows the user to select a doctor to login as.
     * For an admin, it prompts the user to enter the admin username and password. If the credentials are correct, it logs the user into the admin menu.
     * If the user enters 0, it exits the system.
     * 
     * @return UserType of the user that logged in, or null if the user exited the system.
     */
    public UserType login() {
        int input = -1;
        this.pacientesToShow = this.storageHandler.getAllPacientesForUser();
        this.doctoresToShow = this.storageHandler.getAllDoctorForUser();
        this.clinicToShow = this.storageHandler.getAllClinicas();
        do {
            try {
                System.out.println("Mangament for " + config.getHospitalName() +": \n1. Magamente a Patient \n2. Go to Doctor \n3. Go to Administration \n0. Exit System.");
                input = Integer.parseInt(scanner.nextLine());
                switch (input) {
                    case 1:
                        return loginPaciente();
                    case 2:
                        int userInputIdDoctor = -1;
                        do {
                            try {
                                if (doctoresToShow.size() > 0) {
                                    doctoresToShow.sort((d1, d2) -> Integer.compare(d1.getId(), d2.getId()));
                                    for (Doctor doctor : doctoresToShow) {
                                        System.out.println("ID: " + doctor.getId() + ".\n   Dr. " + doctor.getNombre() + ".\n   Clinic: " + doctor.getClinica());
                                    }
                                    System.out.println("0. Return to Main Menu");
                                    System.out.println("Write down the ID of the doctor: ");
                                }
                                userInputIdDoctor = Integer.parseInt(scanner.nextLine());
                                if (userInputIdDoctor == 0) {
                                    System.out.println("Returning to previous menu...");
                                    break;
                                }
                                if (userInputIdDoctor < 0 || userInputIdDoctor > doctoresToShow.size()) {
                                    System.out.println("Error: Doctor with ID: " + userInputIdDoctor + " not found on the system.");
                                }

                            } catch (NumberFormatException e) {
                                System.out.println("Error: only whole numbers allowed.");
                                userInputIdDoctor = -1; // Reset the value so the loop continues
                            }
                        } while (userInputIdDoctor < 0 || userInputIdDoctor > doctoresToShow.size());
                        this.loginDoc = this.storageHandler.getDoctorById(userInputIdDoctor);
                        if (this.loginDoc != null) {
                            return UserType.Doctor;
                        } else {
                            System.out.println("No doctor was found with ID: " + userInputIdDoctor);
                        }
                        break;
                    case 3:
                        System.out.println("\n[Denied Access] Enter the admin username and password: ");
                        if (verifyAdmin()) {
                            System.out.println("Correct credential, loging into the admin menu... ");
                            return UserType.Admin;
                        } else {
                            System.out.println("Incorrect Credentials. Unable to enter the administration of " + config.getHospitalName() + ". Returning previous menu.");
                        }
                        break;
                    case 0:
                        System.out.println("Exiting System...");
                        return null;
                    default:
                        System.out.println("Only enter the numbers shown on the terminal.");
                }
            } catch (NullPointerException e) {
                System.out.println("Error: Null reference encountered.");
            } catch (InputMismatchException e) {
                System.out.println("Error: Enter a whole number: ");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            } 
        } while (input != 0); 
        return null; 
    }

   
    /**
     * Shows the menu for the user to login as a patient.
     * 
     * This method shows a menu with the following options:
     *  1. New Patient
     *  2. Registered Patient
     *  0. Return to the previous menu
     * 
     * Depending on the user's choice this method will either:
     *  - Create a new patient and login as that patient
     *  - Show a list of registered patients and let the user choose one to login as
     *  - Return to the login menu
     * 
     * @return UserType.Paciente if the user logged in as a patient, UserType.Admin if the user logged in as the admin,
     *  UserType.Doctor if the user logged in as a doctor, or null if the user chose to return to the login menu.
     */
    public UserType loginPaciente() {
        int input = -1;
        this.pacientesToShow = this.storageHandler.getAllPacientesForUser();
        this.doctoresToShow = this.storageHandler.getAllDoctorForUser();
        this.clinicToShow = this.storageHandler.getAllClinicas();
        try {
            System.out.println("1. New Patient \n2. Registered Patient\n0. return to previous menu");
            String inputStr = scanner.nextLine();
            if (!inputStr.isEmpty()) {
                input = Integer.parseInt(inputStr);
            }
            switch (input) {
                case 1:
                    System.out.println("Enter the full name of the patient: ");
                    String nombre = scanner.nextLine();
                    this.loginPac = new Paciente(nombre);
                    if (this.storageHandler.createPaciente(this.loginPac)) {
                        System.out.println("Pacient added");
                        return UserType.Paciente;
                    } else {
                        System.out.println("Error: the patient couldn't be added.");
                    }
                    break;
                case 2:
                int userInputIdPaciente = -1;
                boolean patientFound = false;
                do {
                    try {
                        if (pacientesToShow.isEmpty()) {
                            System.out.println("No patients registered.");
                            break;
                        }

                        pacientesToShow.sort((d1, d2) -> Integer.compare(d1.getId(), d2.getId()));
                        for (Paciente paciente : pacientesToShow) {
                            System.out.println("Patient ID: " + paciente.getId() + 
                                               ". \n   Patient Name: " + paciente.getNombre());
                        }
                        System.out.println("0. return to previous menu");
                        System.out.println("Write down the ID of the registered patient: ");
                        
                        inputStr = scanner.nextLine();
                        if (!inputStr.isEmpty()) {
                            userInputIdPaciente = Integer.parseInt(inputStr);
                        }

                        if (userInputIdPaciente == 0) {
                            System.out.println("Returning to previous menu...");
                            return login();
                        }
                        
                        patientFound = false;
                        for (Paciente paciente : pacientesToShow) {
                            if (paciente.getId() == userInputIdPaciente) {
                                patientFound = true;
                                this.loginPac = paciente;
                                System.out.println("Patient found on the system, logging in.");
                                return UserType.Paciente;
                            }
                        }
                        
                        if (!patientFound) {
                            System.out.println("Error: the patient with the entered ID doesn't exist.");
                        }
                        
                    } catch (NumberFormatException e) {
                        System.out.println("Error: only whole numbers allowed.");
                        userInputIdPaciente = -1;
                    }
                } while (userInputIdPaciente != 0);
                break;
                case 0:
                    System.out.println("Returning to previous menu..");
                    return login();
                default:
                    System.out.println("Only enter the numbers on the screen.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: only enter a whole number.");
            input = -1;
            scanner.nextLine(); 
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        } while (input != 0);
        return null;
    }
    


/**
 * Displays and manages the patient menu interaction.
 *
 * Provides options for the logged-in patient to:
 *  1. Add an appointment with a specified date, clinic, and symptoms.
 *  2. Check and list all current appointments.
 *  3. Add to the patient's medical history.
 *  4. Review current prescriptions and display them with doctor and patient names.
 *  0. Return to the previous menu.
 *
 * Handles various exceptions related to input mismatches and null references.
 * Ensures the menu keeps prompting until the patient chooses to return to the previous menu.
 *
 * @param loginPac the currently logged-in patient. If null, prints an error and returns to the original menu.
 */
    public void pacienteMenu(Paciente loginPac) {
        this.pacientesToShow = this.storageHandler.getAllPacientesForUser();
        this.doctoresToShow = this.storageHandler.getAllDoctorForUser();
        this.clinicToShow = this.storageHandler.getAllClinicas();
        if (loginPac == null) {
            System.out.println("Error: Patient not found. returning to the original menu.");
            return;
        }
        int input = -1;
        do {
            System.out.println("Welcome, " + loginPac.getNombre() +  " (ID: " + loginPac.getId() + ")");
            System.out.println("\n 1. Add appointment \n2. Check Appointments \n3. Medical History \n4. Review presctions \n0. return to previous menu");
            try {
                input = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Error: only whole numbers allowed.");
                input = -1;
                scanner.nextLine();
                continue;
            }
            switch (input) {
                case 1:
                    System.out.println("When would you want to have this appointment (YYYY-MM-DD): ");
                    String date = "";
                    boolean isDateValid = true;
                    while (isDateValid) {
                        date = scanner.nextLine();
                        if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            isDateValid = false;
                        } else {
                            System.out.println("Invalid format. Please enter the date in YYYY-MM-DD format (do use \" - \" to seperate each date):");
                        }
                    }
                    System.out.println("Choose the clinic that relates to your sickness: ");
                    if (clinicToShow.size() > 0) {
                        for (int i = 0; i < clinicToShow.size(); i++) {
                            System.out.println((i+1) + ": " + clinicToShow.get(i).getEspecialidad());
                        }
                        System.out.println("0. To cancel the appointment.");
                        
                        // user pone el input una especialidad
                        int userInputIdClinica = Integer.parseInt(scanner.nextLine()) - 1;
                        
                        if (userInputIdClinica < 0 || userInputIdClinica >= clinicToShow.size()) {
                            if (userInputIdClinica == -1) {
                                System.out.println("Cancelling appointment.");
                            } else {
                                System.out.println("Error: The ID from the clinic can't be empty.");
                            }
                            break;
                        }
                        
                        
                        try {
                            Doctor doctor = this.storageHandler.docAddedtoCita(clinicToShow.get(userInputIdClinica).getEspecialidad());
                            System.out.println("What Symptoms do you feel? ");
                            String symptoms = scanner.nextLine();
                            // Create a new appointment and add it to the clinic
                            boolean isCitaAdded = this.storageHandler.drAddCita(doctor, loginPac, clinicToShow.get(userInputIdClinica).getEspecialidad(), date, symptoms);                             
                            if (!isCitaAdded) {
                                System.out.println("Failed to add appointment. Please try again.");
                            } else {
                                System.out.println("Appointment successfully added. With Doctor: " + doctor.getNombre() + "(ID: " + doctor.getId() +")");
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println("The clinic: " + clinicToShow.get(userInputIdClinica).getEspecialidad() + " doesn't have any doctors. Unable to add an appointment.");
                        }
                    }
                    break;
                case 2:
                    //checks the patient appointments.
                    List<Cita> citas = this.storageHandler.getPacienteCitas(loginPac.getId());
                    if (citas == null || citas.isEmpty()) {
                        System.out.println(loginPac.getNombre() + ". Doesn't have any appointment registered.");
                    } else {
                        for (Cita cita : citas) {
                            System.out.println(cita.toString());
                        }
                    }
                    break;
                case 3:
                    //Historial Medico
                    System.out.println("Add the medical history: ");
                    String historial = scanner.nextLine();
                    boolean isHist = this.storageHandler.createHistorialMedico(loginPac.getId(), historial);
                    if (isHist != false) {
                        System.out.println("Medical history added successfully");
                    } else {
                        System.out.println("The Medical history can't be added.");
                    }
                    break;
                case 4:
                    //Revisar Prescripciones (missing the functionaliyy to edit or leave as it is in case that the patient already has a prescription)
                    try {
                        ArrayList<Prescription> prescriptions = this.storageHandler.getPrescriptionsFromPatient(loginPac.getId());
                        if (prescriptions == null || prescriptions.isEmpty()) {
                            System.out.println(loginPac.getNombre() + " doesn't have any prescrition.");
                        } else {
                            for (Prescription p : prescriptions) {
                                Doctor doctor = this.storageHandler.getDoctorById(p.getDoctor()); //calls the name of the doctor
                                Paciente paciente = this.storageHandler.getPacienteById(p.getPaciente()); //calls the name of the patient.
                                p.setDoctorName(doctor.getNombre());
                                p.setPacienteName(paciente.getNombre());
                                System.out.println(p.toString());
                            }
                        }
                    } catch (NullPointerException e) {
                        System.out.println("Error: No patient found.");
                    }
                    break;
                case 0:
                    System.out.println("Return to original menu..");
                    return;
                default:
                    System.out.println("Only enter the numbers on the screen.");
            }
        } while (input != 0);
    }


    
/**
 * Manages the interaction for a logged-in doctor.
 *
 * Provides a menu with the following options for the doctor:
 *  1. Check pending appointments.
 *  2. View the medical history of a patient.
 *  3. Review prescriptions for a patient.
 *  4. Add a prescription for a patient.
 *  5. View all assigned patients.
 *  0. Return to the original menu.
 *
 * Handles input mismatches and ensures that the menu continues to prompt
 * until the doctor chooses to return to the original menu. It verifies if
 * the logged-in doctor object is null and returns to the original menu if so.
 *
 * @param loginDoc the Doctor object representing the logged-in doctor.
 */
    public void doctorMenu(Doctor loginDoc) {
        this.pacientesToShow = this.storageHandler.getAllPacientesForUser();
        this.doctoresToShow = this.storageHandler.getAllDoctorForUser();
        this.clinicToShow = this.storageHandler.getAllClinicas();
        if (loginDoc == null) {
            System.out.println("Error: Doctor not found, returning to original menu.");
            return;
        }
        int input = -1;
        do {
            System.out.println("Welcome Dr." + loginDoc.getNombre() + " (ID: " + loginDoc.getId() + ")");
            System.out.println("\n1. Check pending appointments \n2. Check medical history of a patient \n3. Check presction from a patient \n4. Add a presction to the patient \n5. Check all your patients \n0. Return to original menu");
            try {
                input = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Error: Only whole numbers allowed.");
                input = -1;
                scanner.nextLine();
                continue;
            }
            switch (input) {
                case 1:
                    //revisar citas pendientes (todavia falta)
                    try {
                        ArrayList<Cita> citas = this.storageHandler.drViewCitas(loginDoc);
                        if (citas == null || citas.isEmpty()) {
                            System.out.println("No pending appointments.");
                        } else {
                            for (Cita cita : citas) {
                                System.out.println(cita.toString());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error in obtaining the appointment: " + e.getMessage());
                    }
                    break;
                case 2:
                    // revisar historial médico de un paciente
                    try {
                        if (loginDoc.getPacientesId().size() > 0) {
                            System.out.println("0. return to previous menu");
                            for (int i = 0; i < loginDoc.getPacientesId().size(); i++) {
                                System.out.println("Here is the list of IDs of your patients: ");
                                Paciente pacienteName = this.storageHandler.getPacienteById(loginDoc.getPacientesId().get(i));
                                System.out.println((i+1) + ": Patient ID: " + loginDoc.getPacientesId().get(i) + " Patient Name: " + pacienteName.getNombre());
                            }

                            int userIdDoc = Integer.parseInt(scanner.nextLine()) - 1;
                            if (userIdDoc == -1) {
                                System.out.println("Returning...");
                                return;
                            }

                            int pacienteId = loginDoc.getPacientesId().get(userIdDoc);
                            Paciente paciente = this.storageHandler.getPacienteById(pacienteId);
                            if (paciente == null) {
                                System.out.println("Patient not found.");
                            } else {
                                System.out.println("Medical History from " + paciente.getNombre() + ":");
                                for (String record : paciente.getHistorialMedico()) {
                                    System.out.println(record);
                                }
                            }
                        } else {
                            System.out.println("No patients registered.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Enter a whole number");
                    } catch (Exception e) {
                        System.out.println("Error in obtaining medical history: " + e.getMessage());
                    }
                    break;
                case 3:
                    // revisar prescripción de un paciente
                    try {
                        if (loginDoc.getPacientesId().isEmpty()) {
                            System.out.println("No patients registered.");
                            break;
                        }
                        System.out.println("Enter the ID of the patient: ");
                        System.out.println("0. return to previous menu");
                        for (int i = 0; i < loginDoc.getPacientesId().size(); i++) {
                            Paciente pacienteName = this.storageHandler.getPacienteById(loginDoc.getPacientesId().get(i));
                            System.out.println((i+1) + ": Patient ID: " + loginDoc.getPacientesId().get(i) + " Patient Name: " + pacienteName.getNombre());
                        }

                        int userIdDoc = Integer.parseInt(scanner.nextLine()) - 1;
                        if (userIdDoc == -1) {
                            System.out.println("Returning...");
                            return;
                        }
                        int pacienteId = loginDoc.getPacientesId().get(userIdDoc);
                        ArrayList<Prescription> prescriptions = this.storageHandler.getPrescriptionsFromPatient(pacienteId);
                        if (prescriptions == null || prescriptions.isEmpty()) {
                            System.out.println("No presctiptions found for this patient.");
                        } else {
                            for (Prescription prescription : prescriptions) {
                                Doctor doctor = this.storageHandler.getDoctorById(prescription.getDoctor()); //calls the name of the doctor
                                Paciente paciente = this.storageHandler.getPacienteById(prescription.getPaciente()); //calls the name of the patient.
                                prescription.doctorName = doctor.getNombre();
                                prescription.pacienteName = paciente.getNombre();
                                System.out.println(prescription.toString()); 
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Enter a valid number.");
                    } catch (Exception e) {
                        System.out.println("Error: in obtaining the prescription " + e.getMessage());
                    }
                    break;
                case 4:
                    // agregar prescripción a un paciente
                    if (loginDoc.getPacientesId().isEmpty()) {
                        System.out.println("No patients registered.");
                        break;
                    }
                    System.out.println("Enter the ID of the patient: ");
                    System.out.println("0. return to previous menu");
                    for (int i = 0; i < loginDoc.getPacientesId().size(); i++) {
                        Paciente pacienteName = this.storageHandler.getPacienteById(loginDoc.getPacientesId().get(i));
                        System.out.println((i+1) + ": Patient ID: " + loginDoc.getPacientesId().get(i) + " Patient Name: " + pacienteName.getNombre());
                    }
                    int userIdDoc = Integer.parseInt(scanner.nextLine()) - 1;
                    if (userIdDoc == -1) {
                        System.out.println("Returning...");
                        return;
                    }
                    Paciente paciente = this.storageHandler.getPacienteById(loginDoc.getPacientesId().get(userIdDoc));
                    if (paciente == null) {
                        System.out.println("Error: Patient not found.");
                        break;
                    }
                    System.out.println("Enter Prescription: ");
                    String prescripcion = scanner.nextLine();
                    Prescription newPrescription = new Prescription(loginDoc.getId(), paciente.getId(), loginDoc.getNombre() , paciente.getNombre() , prescripcion);
                    boolean result = this.storageHandler.drPrescribeMedicineToPatient(newPrescription); //you also have to make sure a date is atteached with this prescription...
                    if (result == false) {
                        System.out.println("Error: Presctiption not found.");
                    } else {
                        System.out.println("Prescription added successfully.");
                    }
                    break;
                case 5:
                //this checks all the patients from the doc
                    System.out.println("Patient:");
                    try {
                        ArrayList<Paciente> pacientes = this.storageHandler.getDrPacientes(loginDoc);
                        if (pacientes == null || pacientes.isEmpty()) {
                            System.out.println("There is no patient,  Dr. " + loginDoc.getNombre());
                        } else {
                            int count = 1;
                            for (Paciente p : pacientes.stream().toList()) {
                                System.out.printf("Patient %d: name: %s id: %d%n", count++, p.getNombre(), p.getId());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error in obtaining the patients: " + e.getMessage());
                    }
                    break;
                case 0:
                    System.out.println("Returning to the original menu..");
                    return;
                default:
                    System.out.println("Only enter the numbers in the screen.");
            }
        } while (input != 0);
    }


    /**
     * Displays the menu for the administration and allows the admin to select an option:
     * <ul>
     * <li>1. Administrate Doctors</li>
     * <li>2. Administrate Clinics</li>
     * <li>3. Administrate the patients</li>
     * <li>4. Change the name of the hospital</li>
     * <li>5. Change the username and password of the admin</li>
     * <li>0. Return</li>
     * </ul>
     * The method is a do-while loop and it will keep asking for input until a valid number is entered and the corresponding action is performed.
     * If an invalid input is entered, the program will print an error message and ask for input again.
     */
    public void adminMenu() {
        this.pacientesToShow = this.storageHandler.getAllPacientesForUser();
        this.doctoresToShow = this.storageHandler.getAllDoctorForUser();
        this.clinicToShow = this.storageHandler.getAllClinicas();
        int input = -1;
        do {
            try {
                System.out.println("Menu for the administration: \n1. Administrate Doctors \n2. Administrate Clinics \n3. Administrate the patients \n4. Change the name of the hospital \n5. Change the username and password of the admin \n0. Return");
                input = scanner.nextInt();
                scanner.nextLine();
                switch (input) {
                    case 1:
                        adminDoc();
                        break;
                
                    case 2:
                        adminClinica();
                        break;
                    case 3:
                        adminPaciente();
                        break;
                    case 4:
                        changeNameHospital();
                        break;
                    case 5:
                        changeUserPass(); 
                        break;
                    case 0:
                        System.out.println("Returning to the original menu..");
                        return;
                    default:
                        System.out.println("Only enter the valid number on the screen.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: enter a whole number.");
                input = -1;
                scanner.nextLine();
            } catch (NullPointerException e) {
                System.out.println("Error: The admin data was not found.");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        } while (input != 0);
    }

    /**
     * This method is used to administrate the patients. It provides a menu with the following options:
     * <ul>
     * <li>1. Eliminate the patient from the system</li>
     * <li>0. Return to admin menu</li>
     * </ul>
     * The method is a do-while loop and it will keep asking for input until a valid number is entered and the corresponding action is performed.
     * If an invalid input is entered, the program will print an error message and ask for input again.
     */
    public void adminPaciente() {
        this.pacientesToShow = this.storageHandler.getAllPacientesForUser();
        this.doctoresToShow = this.storageHandler.getAllDoctorForUser();
        this.clinicToShow = this.storageHandler.getAllClinicas();
        boolean inAdminPaciente = true;
        while (inAdminPaciente) {
            System.out.println("Menu to administrate the Patient \n1. Eliminate the patient from the system \n0. Return to admin menu.");
            int input = scanner.nextInt();
            scanner.nextLine();
            switch (input) {
                case 1:
                    try {
                        if (pacientesToShow.isEmpty()) {
                            System.out.println("No patients registered.");
                            break;
                        }
                        System.out.println("Enter the ID of the patient: ");
                        System.out.println("0. return to previous menu");
                        for (int i = 0; i < pacientesToShow.size(); i++) {
                            Paciente paciente = pacientesToShow.get(i);
                            System.out.println((i+1) + ": Patient ID: " + paciente.getId() + " Patient Name: " + paciente.getNombre());
                        }
                        int userIdDoc = Integer.parseInt(scanner.nextLine()) - 1;
                        if (userIdDoc == -1) {
                            System.out.println("Returning...");
                            break;
                        }
                        Paciente selectedPaciente = pacientesToShow.get(userIdDoc);
                        boolean isDeleted = this.storageHandler.deletePatient(selectedPaciente.getId());
                        if (isDeleted) {
                            System.out.println("Patient with id: " + selectedPaciente.getId() + ". Eliminated from the system.");
                        } else {
                            System.out.println("Patient with id: " + selectedPaciente.getId() + ". Doesn't exist in the system.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Error: you must enter a valid whole number.");
                    } catch (NullPointerException e) {
                        System.out.println("Error: The patient numbers were not found.");
                    } catch (Exception e) {
                        System.out.println("An unexpected error occurred: " + e.getMessage());
                    }
                    break;
                case 0:
                    inAdminPaciente = false;
                    System.out.println("Returning to the admin menu..");
                    return;
                default:
                    System.out.println("Only enter the numbers on the terminal.");
            }
        }
    }


    /**
     * This method is used to administrate the doctors. It provides a menu with the following options:
     * <ul>
     * <li>1. Add Doctor</li>
     * <li>2. Eliminate Doctor</li>
     * <li>3. Change password from a doctor</li>
     * <li>0. Return to admin menu</li>
     * </ul>
     * The method is a do-while loop and it will keep asking for input until a valid number is entered and the corresponding action is performed.
     * If an invalid input is entered, the program will print an error message and ask for input again.
     */
    public void adminDoc() {
        this.pacientesToShow = this.storageHandler.getAllPacientesForUser();
        this.doctoresToShow = this.storageHandler.getAllDoctorForUser();
        this.clinicToShow = this.storageHandler.getAllClinicas();
        int input = -1;
        do {
        System.out.println("Menu to Administrate Doctors \n1. Add Doctor \n2. Eliminate Doctor \n3. Change password from a doctor \n0. Return");
        input = scanner.nextInt();
        scanner.nextLine();
        switch (input) {
            case 1:
                System.out.println("Full name of the Doctor:");
                String nombreDoc = scanner.nextLine();
                if (nombreDoc == null || nombreDoc.trim().isEmpty()) {
                    System.out.println("The name can't be empty.");
                    break;
                }
                //scanner lista de clinicas a elejir.
                System.out.println("The Specialities that the Doctor can choose from: ");
                // Agarar todas las clinicas
                Doctor newDoctor;
                if (clinicToShow.size() > 0) {
                    System.out.println("0. Without Speciality");
                    for (int i = 0; i < clinicToShow.size(); i++) {
                        System.out.println((i+1) + ": " + clinicToShow.get(i).getEspecialidad());
                    }

                    // user pone el input una especialidad
                    int userInputIdClinica = Integer.parseInt(scanner.nextLine()) - 1;

                    // Check for input out of range
                    if (userInputIdClinica < -1 || userInputIdClinica >= clinicToShow.size()) {
                        System.out.println("Error: The ID from the clinic can't be empty.");
                        break;
                    }

                    if (userInputIdClinica == -1) { //This exists so that if the admin decides to create a Doc without a speciality.
                        newDoctor = new Doctor(nombreDoc);
                        System.out.println("Dr." + newDoctor.getNombre() + " was not added to a clinic.");
                    } else {
                        newDoctor = new Doctor(nombreDoc, clinicToShow.get(userInputIdClinica).getEspecialidad());
                    }
                } else {
                    // Si no hay clinicas para la especialidad entonces no se agrega la especialidad
                    newDoctor = new Doctor(nombreDoc);
                }
                if (this.storageHandler.createDoctor(newDoctor)) {
                    System.out.println("Doctor added with ID: " + newDoctor.getId());
                } else {
                    System.out.println("The Doctor already exists or there was an error.");
                }
                break;
            case 2:
                System.out.println("The ID to eliminate the Doctor");
                try {
                    int doctorId = scanner.nextInt();
                    scanner.nextLine();
                    this.storageHandler.deleteDoctor(doctorId);
                    System.out.println("Doctor with ID " + doctorId + " Exterminated.");
                } catch (Exception e) {
                    System.out.println("ID not valid. Please enter a number.");
                    scanner.nextLine();
                }
                break;
            case 3:
                System.out.println("The functionality to change the password from a doctor hasn't been implemented yet.");
                System.out.println("\nReturning to the admin menu..");
                break;
            case 0:
                System.out.println("Returning to the admin menu..");
                return;
            default:
                System.out.println("Only enter the numbers on the terminal.");
        }
        } while (input != 0);
    }

    /**
     * Admin menu for managing clinics.
     * 
     * This menu allows the admin to add a clinic, remove a clinic, or move a doctor to a clinic.
     * 
     * The menu continues to loop until the user chooses to return to the admin menu.
     */
    public void adminClinica() {
        int input = -1;
        this.pacientesToShow = this.storageHandler.getAllPacientesForUser();
        this.doctoresToShow = this.storageHandler.getAllDoctorForUser();
        this.clinicToShow = this.storageHandler.getAllClinicas();
        do {
            try {
                System.out.println("Menu to Administrate Clinics  \n1. Add Clinic \n2. Remove Clinic \n3. Move a Doctor to a Clinic \n0. Return to the admin menu");
                input = scanner.nextInt();
                scanner.nextLine();
                switch (input) {
                    case 1:
                        // Add Clinic
                        System.out.println("Add the Speciality of the clinic: ");
                        String especialidad = scanner.nextLine();
                        if (especialidad == null || especialidad.trim().isEmpty()) {
                            System.out.println("Speciality can't be empty.");
                            break;
                        }
                        Clinica newClinica = new Clinica(especialidad);
                        boolean isClinicaCreated = this.storageHandler.createNewClinic(newClinica);
                        if (isClinicaCreated) {
                            System.out.println("Clinic created successfully. ID: " + newClinica.getId());
                        } else {
                            System.out.println("Error in creating the clinic.");
                        }
                        break;
                    case 2:
                        // Remove Clinic
                        if (clinicToShow == null || clinicToShow.isEmpty()) {
                            System.out.println("No clinics available to remove.");
                            break;
                        }
                        System.out.println("Enter the ID of the clinic to remove (0 to cancel): ");
                        for (int i = 0; i < clinicToShow.size(); i++) {
                            System.out.println((i + 1) + ": " + clinicToShow.get(i).getEspecialidad());
                        }
                        int clinicIndex = scanner.nextInt() - 1;
                        scanner.nextLine();
                        
                        if (clinicIndex == -1) {
                            System.out.println("No Clinic removed. Returning...");
                            break;
                        }

                        if (clinicIndex < 0 || clinicIndex >= clinicToShow.size()) {
                            System.out.println("Invalid clinic ID. Please try again.");
                            break;
                        }

                        boolean isClinicRemoved = this.storageHandler.eliminarClinica(clinicToShow.get(clinicIndex).getId());
                        if (isClinicRemoved) {
                            System.out.println("Clinic removed successfully.");
                        } else {
                            System.out.println("Error in removing the clinic.");
                        }
                        break;
                    case 3:
                        // Move Doctor to Clinic
                        if (clinicToShow == null || clinicToShow.isEmpty()) {
                            System.out.println("No clinics available.");
                            break;
                        }
                        System.out.println("Enter the ID of the clinic to move the doctor: ");
                        for (int i = 0; i < clinicToShow.size(); i++) {
                            System.out.println((i + 1) + ": " + clinicToShow.get(i).getEspecialidad());
                        }
                        System.out.println("0. Return to previous menu.");
                        int clinicToMoveTo = scanner.nextInt() - 1;
                        scanner.nextLine();

                        if (clinicToMoveTo == -1) {
                            System.out.println("Operation canceled. Returning...");
                            break;
                        }

                        if (clinicToMoveTo < 0 || clinicToMoveTo >= clinicToShow.size()) {
                            System.out.println("Invalid clinic ID. Please try again.");
                            break;
                        }

                        if (doctoresToShow == null || doctoresToShow.isEmpty()) {
                            System.out.println("No doctors available.");
                            break;
                        }
                        System.out.println("Enter the ID of the doctor to move: ");
                        for (int i = 0; i < doctoresToShow.size(); i++) {
                            System.out.println((i + 1) + ": " + doctoresToShow.get(i).getNombre() + ". Currently at: " + doctoresToShow.get(i).getClinica());
                        }
                        System.out.println("0. Return to previous menu.");
                        int doctorIndex = scanner.nextInt() - 1;
                        scanner.nextLine();

                        if (doctorIndex == -1) {
                            System.out.println("Operation canceled. Returning...");
                            break;
                        }

                        if (doctorIndex < 0 || doctorIndex >= doctoresToShow.size()) {
                            System.out.println("Invalid doctor ID. Please try again.");
                            break;
                        }

                        boolean isDocMoved = this.storageHandler.addClinicToDoctor(clinicToShow.get(clinicToMoveTo).getId(), doctoresToShow.get(doctorIndex).getId());
                        if (isDocMoved) {
                            System.out.println("Doctor moved successfully.");
                        } else {
                            System.out.println("Error in moving the doctor.");
                        }
                        break;
                    case 0:
                        System.out.println("Returning to the admin menu..");
                        return;
                    default:
                        System.out.println("Please enter a valid option.");
                }
            } catch (NullPointerException e) {
                System.out.println("Error: Data not found.");
            } catch (InputMismatchException e) {
                System.out.println("Error: Must enter a valid whole number.");
                input = -1;
                scanner.nextLine(); // clear the invalid input
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        } while (input != 0);
    }

    /**
     * Asks the user to enter the new name of the hospital and then updates
     * the configuration file with the new name.
     */
    private void changeNameHospital() {
        System.out.println("Enter the New name of the hospital: ");
        String newName = scanner.nextLine();

        config.changeHospitalName(newName);
        System.out.println("The new name of the hostpital is " + config.getHospitalName());
    }
    private void changeUserPass() {
        System.out.println("Enter the new username for the admin: ");
        String newUsername = scanner.nextLine();
        
        System.out.println("Enter the new password for the admin: ");
        String newPassword = scanner.nextLine();
        config.changeUsername(newUsername);
        config.changePassword(newPassword);
        System.out.println(config.changeUsername(newUsername));
        System.out.println(config.changePassword(newPassword));
    }
    /**
     * Is the method to verify the admin password,
     * 
     * @return true = correct password; false = incorrect password.
     */
    private boolean verifyAdmin() {
        Authenticator authenticator = new Authenticator();
        int attempts = 2; // the number of times the the user can attempt to answer the password.
        while (attempts > 0) {
            System.out.println("Please enter the admin username.");
            String inputUsername = scanner.nextLine();

            System.out.println("Please enter the admin password.");
            String inputPassword = scanner.nextLine();

            if (authenticator.verifyCredentials(inputUsername, inputPassword)) {
                return true;
            }
            attempts--; // remember the -- is like saying doing a substraction for each loop irritation.
            if (attempts > 0) {
                System.out.println("The password or username are incorrect, try again.");
            }

        }
        System.out.println("The maximum number of attempts has succeded.");
        return false;
    }
}