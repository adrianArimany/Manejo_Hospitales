package com.uvg.proyecto;

import java.util.Scanner;

import org.antlr.v4.runtime.InputMismatchException;

import com.uvg.proyecto.Authenticator.Authenticator;
import com.uvg.proyecto.Classes.Doctor;
import com.uvg.proyecto.Classes.Paciente;
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
 * - I need to double exit the menu of the admin to fully exit that menu ( )
 * - When a patient/doctor is removed, its id is removed, but when you create a new doctor/patient rather than refilling the deleted id, it generates the highest id. (  )
 * - Error: Null reference encountered. When enter as a Doctor. ( )
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

    //private final StorageHandler data = new StorageHandler();

    public static void main(String[] args) {
        Main app = new Main();
        app.MenuBegins();
        
    }

    public void MenuBegins() {
        try {
            this.storageHandler = new StorageHandler();
            this.storageHandler.initIds();
            UserType user = login();
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
            System.out.println("Error: Invalid input.");
            scanner.nextLine(); // clear the invalid input
        } catch (NullPointerException e) {
            System.out.println("Error: Null reference encountered.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Login and returns us the UserType
    public UserType login() {
        boolean running = true;
        while (running) {
            System.out.println("Gestión para " + hospitalName +": \n1. Administrate a Paciente \n2. Soy un Doctor \n3. Soy el Admininistrador \n0. Salir del Sistema.");
            int input = scanner.nextInt();
            scanner.nextLine(); //remember that with this is NEEDED for the switch to function.
            switch (input) {
                case 1:                    
                    return loginPaciente();
                    
                case 2:
                    System.out.println("Escribe su ID del doctor: ");
                    int idDoctor = Integer.parseInt(scanner.nextLine());
                    this.loginDoc = this.storageHandler.getDoctorById(idDoctor);
                    if (this.loginDoc == null) { 
                        System.out.println("No se encontró un doctor con ID: " + idDoctor);
                        break;  // Allow the user to reattempt login
                    }
                    return UserType.Doctor;

                case 3:
                    System.out.println("\n[Accesso Denegado] Ingrese clave y usuario del administrador: ");
                    try {
                        if (verifyAdmin()) {
                            System.out.println("Credenciales correctos, ingresando al Menu del Admin: ");
                            return UserType.Admin;
                        } else {
                            System.out.println("Credenciales incorrectos, saliendo del sistema.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                case 0:
                    System.out.println("Saliendo del Systema...");
                    running = false;
                    return null;
                default:
                    System.out.println("Solo ingrese los numeros en la pantalla.");
            }
        }
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
        try {
            System.out.println("1. Paciente Nuevo \n2. Paciente Registrado");
            int input = scanner.nextInt();
            scanner.nextLine();
            switch (input) {
                case 1:
                    System.out.println("Ingrese el nombre del paciente: ");
                    String nombre = scanner.nextLine();
                    this.loginPac = new Paciente(nombre);
                    this.storageHandler.createPaciente(this.loginPac);
                    System.out.println("Paciente agregado");
                    return UserType.Paciente;
                case 2:
                    System.out.println("Escribe el ID del paciente: "); 
                    int idPaceinte = scanner.nextInt();
                    this.loginPac = this.storageHandler.getPacienteById(idPaceinte);
                    return UserType.Paciente;
                default:
                    System.out.println("Solo ingrese los numeros en la pantalla.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un numero entero.");
        }
        return null;
    }
    


    public void pacienteMenu(Paciente loginPac) {
        boolean inPaciente = true;
        while (inPaciente) {
        System.out.println("Bienbenido, " + loginPac.getNombre() +  " (ID: " + loginPac.getId() + ")");
        System.out.println("\n 1. Agendar Cita \n2. Revisar Citas \n3. Historial medico \n4. Revisar Prescripciones \n0. Regresar");
        int input = scanner.nextInt();
        scanner.nextLine();
        switch (input) {
            case 1:
                //Agendar Citas
                break;
            case 2:
                this.storageHandler.getPacienteCitas(loginPac.getId());
                break;
                
            case 3:
                //Historial Medico
                break;

            case 4:
                this.storageHandler.getPrescriptionsFromPatient(loginPac.getId());
                break;
            case 0:
                inPaciente = false;
                return;
            default:
                System.out.println("Solo ingrese los numeros en la pantalla.");
        }
        }
    }

    public void doctorMenu(Doctor loginDoc) {
        boolean inDoctor = true;
        while (inDoctor) {
        System.out.println("Bienbenido Dr." + loginDoc.getNombre() +  " (ID: " + loginDoc.getId() + ")");
        System.out.println("\n1. Revisar Citas Pendientes \n2. Ver historal Medico de un Paciente \n3. Revisar Prescripciones de un Paciente \n4. Agregar prescripcion a un paciente \n5. Ver a todos mis paceintes.  \n0. Regresar");
        int input = scanner.nextInt();
        scanner.nextLine();
        switch (input) {
            case 1:
                this.storageHandler.drViewCitas(loginDoc);
                break;
            case 2:
                //revisar historial medico de un paciente
                break;
            case 3:
                //revisar prescripcion de un paciente
                break;
            case 4:
                //agregar prescripcion a un paciente
                break;
            case 5:
                System.out.println("Pacientes:");
                System.out.println(this.storageHandler.getDrPacientes(this.loginDoc));
            case 0:
                inDoctor = false;
                return;
            default:
                System.out.println("Solo ingrese los numeros en la pantalla.");
        }
        }
    }


    public void adminMenu() {
        boolean inAdminMenu = true;
        while (inAdminMenu) {
            System.out.println("Menu para el Administrador \n1. Administrar Doctores \n2. Administrar Clinicas \n3. Administrar a los Pacientes \n0. Regresar");
            int input = scanner.nextInt();
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
                    inAdminMenu = false;
                    return;
                default:
                    System.out.println("Solo ingrese los numeros en la pantalla.");
            }
        }
        }

    public void adminPaciente() {
        boolean inAdminPaciente = true;
        while (inAdminPaciente) {
        System.out.println("Menu para el Administrador Pacientes \n1. Eliminar Paciente del sistema \n0. Regresar");
        int input = scanner.nextInt();
        scanner.nextLine();
        switch (input) {
            case 1:
                System.out.println("Cual es el ID del paciente a Eliminar: ");
                int id = scanner.nextInt();
                boolean isDeleted  = this.storageHandler.deletePatient(id);
                if (isDeleted) {
                    System.out.println("Paciente con id: " + id + ". Eliminado del sistema.");
                } else {
                    System.out.println("Paciente con id: " + id + ". No existe en el sistema.");
                }
                break;
            case 0:
                inAdminPaciente = false;
                adminMenu();
                return;
            default:
                System.out.println("Solo ingrese los numeros en la pantalla.");
        }
        }
    }


    public void adminDoc() {
        boolean inAdminDoc = true;
        while (inAdminDoc) {
        System.out.println("Administracion para Doctores: \n1. Agregar Doctores \n2. Eliminar Doctores \n3. Editar info de un Doctor \n0. Regresar");
        int input = scanner.nextInt();
        scanner.nextLine();
        switch (input) {
            case 1:
                System.out.println("Nombre del Doctor:");
                String nombre = scanner.nextLine();
                if (nombre == null || nombre.trim().isEmpty()) {
                    System.out.println("Nombre no puede estar vacío.");
                    break;
                }
                Doctor newDoctor = new Doctor(nombre);
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
                } catch (InputMismatchException e) {
                    System.out.println("ID inválido. Por favor, ingrese un número.");
                    scanner.nextLine(); // clear the invalid input
                }
                break;
            case 3:
                System.out.println("Funcionalidad Editar a un Doctor no implementada");
                break;
            case 0:
                inAdminDoc = false;
                adminMenu();
                return;
            default:
                System.out.println("Solo ingrese los numeros en la pantalla.");
        }
        }
    }

    public void adminClinica() {
        boolean inAdminClincia = true;
        while (inAdminClincia) {
        System.out.println("Administracion para Clinicas  \n1. Agregar Clinca \n2. Eliminar Clinica \n3. Mover a un Doctor \n0. regresar");
        int input = scanner.nextInt();
        scanner.nextLine();
        switch (input) {
            case 1:
                //Agregar Clinica
                break;
            case 2:
                //Eliminar Clinica
                break;

            case 3:
                //Mover Doctor.
                break;
            case 0:
                inAdminClincia = false;
                adminMenu();
                return;
            default:
                System.out.println("Solo ingrese los numeros en la pantalla.");
        }
        }
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