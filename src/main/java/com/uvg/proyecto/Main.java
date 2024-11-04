package com.uvg.proyecto;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger; //Gives a better detail about where the error was found (i.e. class or method)

import com.uvg.proyecto.Authenticator.Authenticator;
import com.uvg.proyecto.Classes.Cita;
import com.uvg.proyecto.Classes.Clinica;
import com.uvg.proyecto.Classes.Doctor;
import com.uvg.proyecto.Classes.Paciente;
import com.uvg.proyecto.Classes.Prescription;
import com.uvg.proyecto.Data.PropertiesFile;
import com.uvg.proyecto.Data.StorageHandler;


/**
 * Clase principal para gestionar pacientes, doctores y clínicas
 * a través de un menú interactivo.
 * -------------------------------------
 * @Todo
 * Priority:
 * - The system only runs one iterriation, then it quits!!! (fixed) 
 * - The system doesn't create new patients or doctors (fixed) change the storageHandler.initIds() now returns +1 to whatever was the highest.
 * - When I run method in admin, rather than returning the previous menu, it send me back to the MenuBegins().  (fixed) added a while loop.
 * - I need to double exit the menu of the admin to fully exit that menu (fixed) I recalled the adminMenu every time I exited a submenu.
 * - When a patient/doctor is removed, its id is removed, but when you create a new doctor/patient rather than refilling the deleted id, it generates the highest id. (  )
 * - Error: Null reference encountered. When enter as a Doctor. (fixed) the issue was in a system.out.println I was calling a loginPac rather than loginDoc.
 * - The try-catch in the menuBegins is not working when the user types something that is not a number (fixed) login() was not closing properly hence I couldn't catch the error.
 * - When NumberFormatException is caught in pacienteMenu or doctorMenu the system returns the user to the previous menu rather than keeping him on the current menu (fixed) added input = -1 in the catch.
 * - There is a odd bug where in the show prescription, the name of the doctor and patient do not appear, instead it shows null (  ).
 * 
 * - Sometimes when a doctor is moved to a clinic, isn't added successfully, it occured with Erick Montenegro couldn't be added to clinica Psychiatry (  )
 * - If a patient wants a new appointment with a new clinic, and that clinic doesn't have a doctor, the program shuts down. (occurs more often than not) (  )
 * - There should be an option to remove an appointment without having to remove the patient. (  )
 * - 
 * Extras:
 * -When the Admin for some reason don't elminates a doctor for whatever reason, allow the admin to have another attempt. (add a while loop [not done yet])
 * - Add a more in-depth Logger to the program, especially that captures any potential error, (  )
 * - Use logger.info, logger.fine, logger.error to better control the errors in the system. (  )
 */

public class Main {

    public enum UserType {
        Paciente,
        Doctor,
        Admin,
    }
    private Paciente loginPac;
    private boolean exitSystem = false;
    private Doctor loginDoc;
    private StorageHandler storageHandler;
    private PropertiesFile config = new PropertiesFile();
    private ArrayList<Clinica> clinicToShow;
    private ArrayList<Doctor> doctoresToShow;
    private ArrayList<Paciente> pacientesToShow;
    public final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Main.class.getName()); //used to catch any unprecented error that is in the program, especially useful when running by "real" users. 
        Main app = new Main();
        try {
            app.MenuBegins();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            logger.log(Level.SEVERE, "Unexpected error in main method", e); //In case a SEVERE error is counter, (i.e. one that requires immidiate attention.)
        } finally {
            if (app.scanner != null) {
                try {
                    app.scanner.close();
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error closing scanner", e); //In case that the scanner isn't fully closed, this captures and logs the error for future fixing.
                }
            }
        }
    }

    public void MenuBegins() {
        UserType user = null;
        this.storageHandler = new StorageHandler();
        this.storageHandler.initIds();
        clinicToShow = this.storageHandler.getAllClinicas();
        doctoresToShow = this.storageHandler.getAllDoctorForUser();
        pacientesToShow = this.storageHandler.getAllPacientesForUser();
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

    public UserType login() {
        int input = -1;
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
     * Handles the login process for a patient.
     * 
     * Displays a menu to either register a new patient or login as an existing patient. 
     * For a new patient, prompts for the patient's name and creates a new Paciente object.
     * For an existing patient, prompts for the patient ID and retrieves the corresponding Paciente object.
     * 
     * @return UserType.Paciente if the login or registration is successful, null otherwise.
     */
    public UserType loginPaciente() {
        int input = -1;
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
            scanner.nextLine(); // clear the invalid input
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        } while (input != 0);
        return null;
    }
    


    public void pacienteMenu(Paciente loginPac) {
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
                    System.out.println("When would you want to have this appointment: ");
                    String date = scanner.nextLine();
                    System.out.println("Choose the clinic that relates to your sickness: ");
                    if (clinicToShow.size() > 0) {
                        for (int i = 0; i < clinicToShow.size(); i++) {
                            System.out.println((i+1) + ": " + clinicToShow.get(i).getEspecialidad());
                        }
        
                        // user pone el input una especialidad
                        int userInputIdClinica = Integer.parseInt(scanner.nextLine()) -1 ;
                        
                        if (userInputIdClinica < 0 || userInputIdClinica >= clinicToShow.size()) {
                            System.out.println("Error: The ID from the clinic can't be empty.");
                            break;
                        }
                        Doctor doctor = this.storageHandler.docAddedtoCita(clinicToShow.get(userInputIdClinica).getEspecialidad());
                        if (doctor == null) {
                            System.out.println("Unfortunely " + config.getHospitalName() + ". Doesn't have any doctors in that clinic."); 
                            break;
                        }
                            System.out.println("What Symptoms do you feel? ");
                            String symptoms = scanner.nextLine();
                            // Create a new appointment and add it to the clinic
                            boolean isCitaAdded = this.storageHandler.drAddCita(doctor, loginPac, clinicToShow.get(userInputIdClinica).getEspecialidad(), date, symptoms);                             if (!isCitaAdded) {
                                System.out.println("Failed to add appointment. Please try again.");
                            } else {
                                System.out.println("Appointment successfully added. With Doctor: " + doctor.getNombre() + "(ID: " + doctor.getId() +")");
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
     * Handles the menu for a doctor user.
     * 
     * Displays a menu with the following options:
     *  1. Revisar Citas Pendientes
     *  2. Ver historial Médico de un Paciente
     *  3. Revisar Prescripciones de un Paciente
     *  4. Agregar prescripción a un paciente
     *  5. Ver a todos mis pacientes
     *  0. Regresar
     * 
     * @param loginDoc the doctor object of the logged in doctor. If null, prints an error and returns.
     */
    public void doctorMenu(Doctor loginDoc) {
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


    public void adminMenu() {
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

    public void adminPaciente() {
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


    public void adminDoc() {
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
                break;
            case 0:
                System.out.println("Returning to the admin menu..");
                return;
            default:
                System.out.println("Only enter the numbers on the terminal.");
        }
        } while (input != 0);
    }

    public void adminClinica() {
        int input = -1;
        do {
            try {
                System.out.println("Menu to Administrate Clinics  \n1. Add Clinic \n2. Remove Clinic \n3. Move a Doctor to a Clinic \n0. Return to the admin menu");
                input = scanner.nextInt();
                scanner.nextLine();
                switch (input) {
                    case 1:
                        // Agregar Clinica
                        System.out.println("Add the Speciality of the clinic: ");
                        String especialidad = scanner.nextLine();
                        Clinica newClinca = new Clinica(especialidad);
                        boolean isClinicaCreated = this.storageHandler.createNewClinic(newClinca);
                        if (isClinicaCreated) {
                            System.out.println("Clinic created without error. With ID: " + newClinca.getId());
                        } else {
                            System.out.println("Error in creating the clinic.");
                        }
                        break;
                    case 2:
                        // Eliminar Clinica
                        System.out.println("The ID from the clinic to remove ");
                        if (clinicToShow.size() > 0) {
                            System.out.println("0. return to previous menu");
                            for (int i = 0; i < clinicToShow.size(); i++) {
                                System.out.println((i+1) + ": " + clinicToShow.get(i).getEspecialidad());
                            }
                            
                            // user pone el input una especialidad
                            int userIdClinica = Integer.parseInt(scanner.nextLine()) - 1;

                            if (userIdClinica == -1) { //This exists so that if the admin decides to create a Doc without a speciality.
                                System.out.println("No Clinic removed. Returning...");
                                return;
                            }

                            this.storageHandler.eliminarClinica(userIdClinica);
                            System.out.println("Clinic with ID " + userIdClinica + " eliminated.");    
                        }
                        break;
                    case 3:
                        // Add Doctor to clinic
                        System.out.println("The ID of the clinic to move the Doctor.");
                        
                        int idDoctorClinica = -1;
                        int userIdDoc = -1;

                        if (clinicToShow.size() > 0) {
                            System.out.println("0. return to previous menu");
                            for (int i = 0; i < clinicToShow.size(); i++) {
                                System.out.println((i+1) + ": " + clinicToShow.get(i).getEspecialidad());
                            }
                            
                            // user pone el input una especialidad
                            idDoctorClinica = Integer.parseInt(scanner.nextLine()) - 1;

                            if (idDoctorClinica == -1) { //This exists so that if the admin decides to create a Doc without a speciality.
                                System.out.println("No Clinic removed. Returning...");
                                return;
                            }
                            
                        }
                        System.out.println("The ID of the doctor to move to: ");
                        if (doctoresToShow.size() > 0) {
                            System.out.println("0. return to previous menu");
                            for (int i = 0; i < doctoresToShow.size(); i++) {
                                System.out.println((i+1) + ": " + doctoresToShow.get(i).getNombre() + ". Currently at: " + doctoresToShow.get(i).getClinica());
                            }
                            
                            // user pone el input una especialidad
                            userIdDoc = Integer.parseInt(scanner.nextLine()) - 1;

                            if (userIdDoc == -1) { //This exists so that if the admin decides to create a Doc without a speciality.
                                System.out.println("No Clinic removed. Returning...");
                                return;
                            }  
                        }
                        boolean isDocMoved = this.storageHandler.addClinicToDoctor(clinicToShow.get(idDoctorClinica).getId(), doctoresToShow.get(userIdDoc).getId());
                        if (isDocMoved) {
                            System.out.println("The doctor was moved without problem.");
                        } else {
                            System.out.println("Error in moving the doctor.");
                        }
                        break;
                    case 0:
                        System.out.println("Returning to the admin menu..");
                        return;
                    default:
                        System.out.println("Only enter the numbers on the terminal.");
                }
            } catch (NullPointerException e) {
                System.out.println("Error: Error the numbers in the clinics wheren't found.");
            } catch (InputMismatchException e) {
                System.out.println("Error: must enter a valid whole number.");
                input = -1;
                scanner.nextLine(); // clear the invalid input
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        } while (input != 0);
    }
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