package com.uvg.proyecto;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger; //Gives a better detail about where the error was found (i.e. class or method)
import java.util.logging.Level;

import com.uvg.proyecto.Authenticator.Authenticator;
import com.uvg.proyecto.Classes.Cita;
import com.uvg.proyecto.Classes.Clinica;
import com.uvg.proyecto.Classes.Doctor;
import com.uvg.proyecto.Classes.Paciente;
import com.uvg.proyecto.Classes.Prescription;
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
 * 
 * 
 * Extras:
 * -When the Admin for some reason don't elminates a doctor for whatever reason, allow the admin to have another attempt. (add a while loop)
 */
public class Main {

    public enum UserType {
        Paciente,
        Doctor,
        Admin,
    }
    private final String hospitalName = "Hospital Chapintenco"; //THis should be moved to the menu for the admin so that the admin can change the name of the hospital if needed.
    private Paciente loginPac;
    private boolean exitSystem = false;
    private Doctor loginDoc;
    private StorageHandler storageHandler;


    public final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Main.class.getName()); //used to catch any unprecented error in the program, especially useful when running by "real" users. 
        Main app = new Main();
        try {
            app.MenuBegins();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            logger.log(Level.SEVERE, "Unexpected error in main method", e); //In case a SEBVERE error is counter, (i.e. one that requires immidiate attention.)
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
        do {
        try {
            user = login();
            while (user != null && !exitSystem) {
                switch (user) {
                    case Paciente:
                        if (loginPac != null) {
                            this.pacienteMenu(loginPac);
                        } else {
                            System.out.println("Error: Paciente not found.");
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
                System.out.println("Gestión para " + hospitalName +": \n1. Administrate a Paciente \n2. Soy un Doctor \n3. Soy el Admininistrador \n0. Salir del Sistema.");
                input = Integer.parseInt(scanner.nextLine());
                switch (input) {
                    case 1:
                        return loginPaciente();
                    case 2:
                        boolean validInput = false;
                        System.out.println("Escribe su ID del doctor: ");
                        do {
                        try {
                            int idDoctor = Integer.parseInt(scanner.nextLine());  // Use parseInt for this specific case
                            this.loginDoc = this.storageHandler.getDoctorById(idDoctor);
                            if (this.loginDoc == null) {
                                System.out.println("No se encontró un doctor con ID: " + idDoctor);
                                System.out.print("Escribe su ID del doctor: ");
                            } else {
                                validInput = true;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Solo se permiten numeros enteros.");
                            System.out.print("Escribe su ID del doctor: "); 
                        } if (validInput) {
                            return UserType.Doctor;  // Return if a valid ID and doctor are found
                        }
                        } while (!validInput && this.loginDoc == null);
                        break;
                    case 3:
                        System.out.println("\n[Accesso Denegado] Ingrese clave y usuario del administrador: ");
                        if (verifyAdmin()) {
                            System.out.println("Credenciales correctos, ingresando al Menu del Admin: ");
                            return UserType.Admin;
                        } else {
                            System.out.println("Credenciales incorrectos, por terminos de seguridad para " + hospitalName + " el sistema se va a desconectar.");
                        }
                        break;
                    case 0:
                        System.out.println("Saliendo del Systema...");
                        return null;
                    default:
                        System.out.println("Solo ingrese los numeros en la pantalla.");
                }
            } catch (NullPointerException e) {
                System.out.println("Error: Null reference encountered.");
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número entero.");
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
            System.out.println("1. Paciente Nuevo \n2. Paciente Registrado\n0. Regresar al menu Anterior");
            input = scanner.nextInt();
            scanner.nextLine();
            switch (input) {
                case 1:
                    System.out.println("Ingrese el nombre del paciente: ");
                    String nombre = scanner.nextLine();
                    this.loginPac = new Paciente(nombre);
                    if (this.storageHandler.createPaciente(this.loginPac)) {
                        System.out.println("Paciente agregado");
                        return UserType.Paciente;
                    } else {
                        System.out.println("Error: No se pudo agregar el paciente.");
                    }
                    break;
                case 2:
                    System.out.println("Escribe el ID del paciente: ");
                    int idPaciente = scanner.nextInt();
                    this.loginPac = this.storageHandler.getPacienteById(idPaciente);
                    if (this.loginPac != null) {
                        System.out.println("Login exitoso");
                        return UserType.Paciente;
                    } else {
                        System.out.println("Paciente no encontrado.");
                    }
                    break;
                case 0:
                    System.out.println("Regresando al menu anterior..");
                    return login();
                default:
                    System.out.println("Solo ingrese los numeros en la pantalla.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un numero entero.");
            input = -1;
            scanner.nextLine(); // clear the invalid input
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        } while (input != 0);
        return null;
    }
    


    public void pacienteMenu(Paciente loginPac) {
        if (loginPac == null) {
            System.out.println("Error: Paciente no encontrado. Regresando al menu principal.");
            return;
        }
        int input = -1;
        do {
            System.out.println("Bienvenido, " + loginPac.getNombre() +  " (ID: " + loginPac.getId() + ")");
            System.out.println("\n 1. Agendar Cita \n2. Revisar Citas \n3. Historial medico \n4. Revisar Prescripciones \n0. Regresar");
            try {
                input = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un numero entero.");
                input = -1;
                scanner.nextLine();
                continue;
            }
            switch (input) {
                case 1:
                    //Agendar Citas
                    break;
                case 2:
                    //revisar citas (todavia falta)
                    this.storageHandler.getPacienteCitas(loginPac.getId());
                    break;
                case 3:
                    //Historial Medico
                    System.out.println("Agregar Historial Medico: ");
                    String historial = scanner.nextLine();
                    boolean isHist = this.storageHandler.createHistorialMedico(loginPac.getId(), historial);
                    if (isHist != false) {
                        System.out.println("Historial Medico Agregado");
                    } else {
                        System.out.println("No se pudo agregar historial Medico");
                    }
                    break;
                case 4:
                    //Revisar Prescripciones
                    try {
                        ArrayList<Prescription> prescriptions = this.storageHandler.getPrescriptionsFromPatient(loginPac.getId());
                        if (prescriptions == null || prescriptions.isEmpty()) {
                            System.out.println("No hay prescripciones para este paciente.");
                        } else {
                            for (Prescription p : prescriptions) {
                                System.out.println(p.toString());
                            }
                        }
                    } catch (NullPointerException e) {
                        System.out.println("Error: No se encontraron prescripciones para este paciente.");
                    }
                    break;
                case 0:
                    System.out.println("Regresando al menu Pricipal..");
                    return;
                default:
                    System.out.println("Solo ingrese los numeros en la pantalla.");
            }
        } while (input != 0);
    }

    public void doctorMenu(Doctor loginDoc) {
        if (loginDoc == null) {
            System.out.println("Error: Doctor no encontrado. Regresando al menu principal.");
            return;
        }
        int input = -1;
        do {
            System.out.println("Bienvenido Dr." + loginDoc.getNombre() + " (ID: " + loginDoc.getId() + ")");
            System.out.println("\n1. Revisar Citas Pendientes \n2. Ver historial Médico de un Paciente \n3. Revisar Prescripciones de un Paciente \n4. Agregar prescripción a un paciente \n5. Ver a todos mis pacientes \n0. Regresar");
            try {
                input = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número entero.");
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
                            System.out.println("No hay citas pendientes.");
                        } else {
                            for (Cita cita : citas) {
                                System.out.println(cita.toString());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error al obtener las citas: " + e.getMessage());
                    }
                    break;
                case 2:
                    // revisar historial médico de un paciente
                    System.out.println("Ingrese el ID del paciente: ");
                    try {
                        int pacienteHistorialId = Integer.parseInt(scanner.nextLine());
                        Paciente paciente = this.storageHandler.getPacienteById(pacienteHistorialId);
                        if (paciente != null) {
                            System.out.println("Historial Médico de " + paciente.getNombre() + ":");
                            for (String record : paciente.getHistorialMedico()) {
                                System.out.println(record);
                            }
                        } else {
                            System.out.println("Paciente no encontrado.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Debe ingresar un número válido.");
                    } catch (Exception e) {
                        System.out.println("Error al obtener el historial médico: " + e.getMessage());
                    }
                    break;
                case 3:
                    // revisar prescripción de un paciente
                    System.out.println("Ingrese el ID del paciente: ");
                    try {
                        int pacientePrescripcionId = Integer.parseInt(scanner.nextLine());
                        ArrayList<Prescription> prescriptions = this.storageHandler.getPrescriptionsFromPatient(pacientePrescripcionId);
                        if (prescriptions == null || prescriptions.isEmpty()) {
                            System.out.println("No hay prescripciones para este paciente.");
                        } else {
                            for (Prescription prescription : prescriptions) {
                                System.out.println(prescription.toString());
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Debe ingresar un número válido.");
                    } catch (Exception e) {
                        System.out.println("Error al obtener las prescripciones: " + e.getMessage());
                    }
                    break;
                case 4:
                    // agregar prescripción a un paciente
                    System.out.println("Ingrese el ID del paciente: ");
                    try {
                        int pacienteId = Integer.parseInt(scanner.nextLine());
                        System.out.println("Ingrese la prescripción: ");
                        String prescripcion = scanner.nextLine();
                        Prescription newPrescription = new Prescription(loginDoc.getId(), pacienteId, prescripcion);
                        boolean result = this.storageHandler.drPrescribeMedicineToPatient(newPrescription);
                        if (result) {
                            System.out.println("Prescripción agregada exitosamente.");
                        } else {
                            System.out.println("Error: No se pudo agregar la prescripción.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Debe ingresar un número válido.");
                    } catch (Exception e) {
                        System.out.println("Error al agregar la prescripción: " + e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("Pacientes:");
                    try {
                        ArrayList<Paciente> pacientes = this.storageHandler.getDrPacientes(loginDoc);
                        if (pacientes == null || pacientes.isEmpty()) {
                            System.out.println("No hay pacientes asignados a este doctor.");
                        } else {
                            int count = 1;
                            for (Paciente p : pacientes.stream().toList()) {
                                    System.out.printf("Patient %d: %s%n", count++, p);
                                }
                        }
                    } catch (Exception e) {
                        System.out.println("Error al obtener los pacientes: " + e.getMessage());
                    }
                    break;
                case 0:
                    System.out.println("Regresando al menú Principal..");
                    return;
                default:
                    System.out.println("Solo ingrese los números en la pantalla.");
            }
        } while (input != 0);
    }


    public void adminMenu() {
        int input = -1;
        do {
            try {
                System.out.println("Mewu para el Administrador \n1. Administrar Doctores \n2. Administrar Clinicas \n3. Administrar a los Pacientes \n0. Regresar");
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

                    case 0:
                        System.out.println("Regresando al menu Pricipal..");
                        return;
                    default:
                        System.out.println("Solo ingrese los numeros en la pantalla.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un numero entero.");
                input = -1;
                scanner.nextLine();
            } catch (NullPointerException e) {
                System.out.println("Error: No se encontraron los datos del administrador.");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        } while (input != 0);
    }

    public void adminPaciente() {
        boolean inAdminPaciente = true;
        while (inAdminPaciente) {
            System.out.println("Menu para el Administrador Pacientes \n1. Eliminar Paciente del sistema \n0. Regresar");
            int input = scanner.nextInt();
            scanner.nextLine();
            switch (input) {
                case 1:
                    try {
                        System.out.println("Cual es el ID del paciente a Eliminar: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        boolean isDeleted  = this.storageHandler.deletePatient(id);
                        if (isDeleted) {
                            System.out.println("Paciente con id: " + id + ". Eliminado del sistema.");
                        } else {
                            System.out.println("Paciente con id: " + id + ". No existe en el sistema.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Error: Debe ingresar un numero entero.");
                    } catch (NullPointerException e) {
                        System.out.println("Error: No se encontraron los datos del paciente.");
                    } catch (Exception e) {
                        System.out.println("An unexpected error occurred: " + e.getMessage());
                    }
                    break;
                case 0:
                    inAdminPaciente = false;
                    System.out.println("Regresando al Menu Administrador..");
                    return;
                default:
                    System.out.println("Solo ingrese los numeros en la pantalla.");
            }
        }
    }


    public void adminDoc() {
        int input = -1;
        do {
        System.out.println("Administracion para Doctores: \n1. Agregar Doctores \n2. Eliminar Doctores \n3. Editar info de un Doctor \n0. Regresar");
        input = scanner.nextInt();
        scanner.nextLine();
        switch (input) {
            case 1:
                System.out.println("Nombre del Doctor:");
                String nombreDoc = scanner.nextLine();
                if (nombreDoc == null || nombreDoc.trim().isEmpty()) {
                    System.out.println("Nombre no puede estar vacío.");
                    break;
                }
                //scanner lista de clinicas a elejir.
                System.out.println("Especialidad del Doctor: ");
                // Agarar todas las clinicas
                ArrayList<Clinica> clinicasParaMostrarAlUsuario = this.storageHandler.getAllClinicas();
                Doctor newDoctor;
                if (clinicasParaMostrarAlUsuario.size() > 0){
                    for (int i = 0; i < clinicasParaMostrarAlUsuario.size(); i++) {
                        System.out.println((i+1) + ": " + clinicasParaMostrarAlUsuario.get(i).getEspecialidad());
                    }

                    // user pone el input una especilaidad
                    int userInputIdClinica = Integer.parseInt(scanner.nextLine()) - 1 ;          
                    
                    newDoctor = new Doctor(nombreDoc, clinicasParaMostrarAlUsuario.get(userInputIdClinica).getEspecialidad());
                }else{
                    // Si no hay clinicas para la especialidad entonces no se agrega la especialidad
                    newDoctor = new Doctor(nombreDoc);
                }
                if (this.storageHandler.createDoctor(newDoctor)) {
                    System.out.println("Doctor agregado");
                } else {
                    System.out.println("El doctor ya existe o hubo un error al agregar.");
                }
                break;
            case 2:
                System.out.println("ID del Doctor a eliminar:");
                try {
                    int doctorId = scanner.nextInt();
                    scanner.nextLine();
                    this.storageHandler.deleteDoctor(doctorId);
                    System.out.println("Doctor con ID " + doctorId + " eliminado.");
                } catch (Exception e) {
                    System.out.println("ID inválido. Por favor, ingrese un número.");
                    scanner.nextLine();
                }
                break;
            case 3:
                System.out.println("Funcionalidad Editar a un Doctor no implementada");
                break;
            case 0:
                System.out.println("Regresando al Menu Administrador..");
                return;
            default:
                System.out.println("Solo ingrese los numeros en la pantalla.");
        }
        } while (input != 0);
    }

    public void adminClinica() {
        int input = -1;
        do {
            try {
                System.out.println("Administracion para Clinicas  \n1. Agregar Clinca \n2. Eliminar Clinica \n3. Mover a un Doctor \n0. regresar");
                input = scanner.nextInt();
                scanner.nextLine();
                switch (input) {
                    case 1:
                        // Agregar Clinica
                        System.out.println("Agregar Clinica Especialidad: ");
                        String especialidad = scanner.nextLine();
                        Clinica newClinca = new Clinica(especialidad);
                        boolean isClinicaCreated = this.storageHandler.createNewClinic(newClinca);
                        if (isClinicaCreated) {
                            System.out.println("Clinica creada con exito.");
                        } else {
                            System.out.println("Error al crear la clinica.");
                        }
                        break;
                    case 2:
                        // Eliminar Clinica
                        System.out.println("ID de la Clinica a Eliminar: ");
                        int idClinica = Integer.parseInt(scanner.nextLine());
                        this.storageHandler.eliminarClinica(idClinica);
                        System.out.println("Clinica con ID " + idClinica + " eliminada.");
                        break;
                    case 3:
                        // Add Doctor to clinic
                        System.out.println("ID de la clinica: ");
                        int idClinicaDoc = Integer.parseInt(scanner.nextLine());
                        System.out.println("ID del doctor a mover: ");
                        int idDoctorClinica = Integer.parseInt(scanner.nextLine());
                        boolean isDocMoved = this.storageHandler.addClinicToDoctor(idDoctorClinica, idClinicaDoc);
                        if (isDocMoved) {
                            System.out.println("El doctor se movio de clinica con exito.");
                        } else {
                            System.out.println("Error al mover el doctor");
                        }
                        break;
                    case 0:
                        System.out.println("Regresando al Menu Administrador..");
                        return;
                    default:
                        System.out.println("Solo ingrese los numeros en la pantalla.");
                }
            } catch (NullPointerException e) {
                System.out.println("Error: No se encontraron los datos de las clinicas.");
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un numero entero.");
                input = -1;
                scanner.nextLine(); // clear the invalid input
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        } while (input != 0);
    }

    /**
     * Is the method to verify the admin password,
     * 
     * @return true = correct password; false = incorrect password.
     */
    private boolean verifyAdmin() {
        int attempts = 2; // the number of times the the user can attempt to answer the password.
        while (attempts > 0) {
            System.out.println("Please enter the admin username.");
            String inputUsername = scanner.nextLine();

            System.out.println("Please enter the admin password.");
            String inputPassword = scanner.nextLine();

            if (Authenticator.verifyCredentials(inputUsername, inputPassword)) {
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