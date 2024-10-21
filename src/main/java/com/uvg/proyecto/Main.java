package com.uvg.proyecto;

import java.util.*;

import com.uvg.proyecto.Authenticator.Authenticator;
import com.uvg.proyecto.Classes.Clinica;
import com.uvg.proyecto.Classes.Doctor;
import com.uvg.proyecto.Classes.Paciente;
import com.uvg.proyecto.Data.DataHandler;

/**
 * Clase principal para gestionar pacientes, doctores y clínicas
 * a través de un menú interactivo.
 * -------------------------------------
 * Things to do:
 * Priority:
 * 1. Add java doc documentation
 * 
 * 2. Add more functionalities to paciente and doctor.
 * 
 * 3. Do not allow repeated id for patients or doctors or clinics.
 * 
 * 4. Most methods in Main.java are not using the methods that already exist in DataHandler.
 * 
 * 5. Please move your files to their respective folder.
 * 
 * 6. Implement something that would make sure that if a "," is added the csv wouldn't treated as a new coloumn.
 * 
 * 7. 
 * Extra:
 * 3. When the user enters the showMenuDoctor or showMenuPaciente, and wants to return, it should send him to the main Menu. 
 * 
 * 4. Add a password system to gain access to registered doctors.
 * 
 * 5. If a doctor is to be eliminated, add a password to gain access to this option (Completed but the password should be better secured...).
 * 
 * 6. when the patient or doctor enter their menus, that menu is dedicated to that doctor or that patient, rather than making it a comunual system.
 * 
 * 7. Make some use of inherentence, such like sickness of the patient is managed in a seperated class that is implementing inherentence.
 * 
 */
public class Main {

    public final Scanner scanner = new Scanner(System.in);


    /**
     * this is the system main password to access to some of the features of the system.
     */ 
    //private static final String ADMIN_PASSWORD = "admin"; //please look away. 
    /**
     * Nombre de la clase que guarda los datos CSV.
     */ 
    private final DataHandler data = new DataHandler();

    
    public static void main(String[] args) {
        Main app = new Main();
        app.mainMenu();
    }
    /**
     * Método principal que controla el flujo del programa y muestra un menú interactivo.
     *
     * 
     */
    private void mainMenu() {
        boolean running = true;
        
        while (running) {
            System.out.println("Buenos Dias al sistema Hospitalario.");
            System.out.println("1. Soy Doctor o Administrador"); //Sends to password-user
            System.out.println("2. Administrar a los Paciente del hospital."); //Sends to find for old pentiente or new patient 
            System.out.println("0. Salir del sistema");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    showMenuDoctorGeneral(); //for new and old doctors.
                    break;
                case "2":
                    showMenuPersona(); // for either new or old patients.
                    break;
                case "0":
                    System.out.println("Saliendo...");
                    running = false;
                    break;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }
        //scanner.close(); (finish the menus, then put this at the end.)
    }

    /**
     * Corre Menu para cualquier persona (paciente o no):
     */
    private void showMenuPersona() {
        boolean running = true;
        while (running) {
            System.out.println("Es usted ya paciente? o es usted nuevo al sistema?");
            System.out.println("1. Es Paciente Nuevo"); 
            System.out.println("2. Es Paciente Registrado"); 
            System.out.println("0. regresar");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    agregarPaciente(); //crea nuevo paciente, 
                    showMenuPaciente(); //Manda al menu del paciente.
                    break;
                case "2":
                    showMenuPacienteProtectRegistrado(); //paciente viejo manda a find id.
                    running = false;
                    break;
                case "0":
                    System.out.println("Regresando...");
                    running = false;
                    return;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }
    }   

    /**
     * Muestra el menú para Paciente 'Protect' registrado,
     */
    private void showMenuPacienteProtectRegistrado() {
        boolean running = true;
        while (running) {
            System.out.println("Precione un numero: ");
            System.out.println("1. Tengo un ID: ");
            System.out.println("0. no tengo ID, regresar al menu anterior.: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    if (buscarPaciente()) {
                        showMenuPaciente();
                    }
                    break;
                case "0":
                    System.out.println("Regresando...");
                    running = false;
                    return;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }

    }


    /**
     * Muestra el menú para Paciente todo paciente.
     */
    private void showMenuPaciente() {
        boolean running = true;
        while (running) {
            System.out.println("Bienvenido al menú de paciente.");
            System.out.println("1. Ver historial médico");
            System.out.println("2. Agregar una cita");
            System.out.println("0. Regresar al menú anterior");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("Mostrando historial médico...");
                    verHistorialMedico();
                    break;
                case "2":
                    System.out.println("Agregando una cita...");
                    // Create a method that adds an appointment to the patient.
                    break;
                case "0":
                    System.out.println("Regresando...");
                    running = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }   
        }
    }
    private void showMenuDoctorGeneral() {
        boolean running = true;
        while (running) {
            System.out.println("Es usted doctor registrado? o es usted nuevo al sistema?");
            System.out.println("1. Soy Doctor registrado"); 
            System.out.println("2. Soy Doctor nuevo"); 
            System.out.println("0. regresar");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    showMenuDoctorProtectRegistrado(); //paciente viejo manda a find id.
                    break;
                case "2":
                    agregarDoctor(); //crea nuevo doctor, 
                    showMenuDoctor(); //Manda a menu de doctores.
                    break;
                case "0":
                    System.out.println("Regresando...");
                    running = false;
                    return;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }
    }

    private void showMenuDoctorProtectRegistrado() {
        boolean running = true;
        while (running) {
            System.out.println("Precione un numero: ");
            System.out.println("1. Tengo un ID: ");
            System.out.println("0. no tengo ID, regresar al menu anterior.: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    if (buscarDoctor()) {
                        showMenuDoctor();
                    }
                    break;
                case "0":
                    System.out.println("Regresando...");
                    running = false;
                    return;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }

    }

     /**
     * Muestra el menú para Paciente todo paciente.
     */
    private void showMenuDoctor() {
        boolean running = true;
        while (running) {
            System.out.println("Bienvenido al menú de doctores y clinicas: ");
            System.out.println("1. Informacion sobre Clinicas: ");
            System.out.println("2. Agregar Clinica: ");
            System.out.println("3. Eliminar un Doctor del sistema: ");
            System.out.println("4. Eliminar a un Paciente del sistema: ");
            System.out.println("5. Informacion general de un paciente en el sistema: ");
            System.out.println("0. Regresar al menú anterior");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    infoClinica();
                    break;
                case "2":
                    agregarClinica();
                    break;
                case "3":
                    System.out.println("\n[Accessed Denied] The admin password is needed.");
                    if (verifyAdmin()) {
                        System.out.println("Correct password, now you may exterminate the doctor. (who?)");
                        eliminarDoctor();
                    } else {
                        System.out.println("The verification can not be completed.");
                    }
                    break;
                case "4":
                    eliminarPaciente();
                    break;
                case "5":
                    buscarPaciente();
                    break;    
                case "0":
                    System.out.println("Regresando...");
                    running = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }   
        }
    }

    /**
     * Busca un paciente por su ID e imprime su información.
     * Se cambia de void a boolean para poder usar este metodo como identificador.
     */
    private boolean buscarPaciente() {
        System.out.print("Ingrese ID del paciente: ");
        String id = scanner.nextLine();
    
        // Call the leerPacientes() method from DataHandler using the instance data.
        List<Paciente> pacientes = data.readPacientes();
        
        // Check if the patient exists
        for (Paciente paciente : pacientes) {
            if (paciente.getId().equals(id)) {
                System.out.println(paciente);
                return true;
            }
        } 
        System.out.println("Paciente no encontrado.");
        return false;
    }

    /**
     * Agrega un nuevo paciente al archivo CSV.
     */
    private void agregarPaciente() {
        System.out.print("Ingrese ID del paciente: ");
        String id = scanner.nextLine();
        System.out.print("Ingrese nombre del paciente: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese ID del doctor a cargo: ");
        String doctorId = scanner.nextLine();
        System.out.print("Ingrese ID de la clínica: ");
        String clinicaId = scanner.nextLine();
        System.out.print("Ingrese historial médico del paciente: ");
        String historialMedico = scanner.nextLine();
        System.out.print("Ingrese enfermedades del paciente: ");
        String enfermedades = scanner.nextLine();
        System.out.print("Ingrese cita médica del paciente: ");
        String citaMedica = scanner.nextLine();

        Paciente paciente = new Paciente(id, nombre, doctorId, clinicaId); //fix this, why is there three methods called like that.
        paciente.agregarHistorialMedico(historialMedico);
        paciente.agregarEnfermedad(enfermedades);
        paciente.agregarCitaMedica(citaMedica);

        data.writePaciente(paciente);
        System.out.println("Paciente agregado con éxito.");
    }

    /**
     * Elimina un paciente del archivo CSV por su ID.
     */
    private void eliminarPaciente() {
        System.out.print("Ingrese ID del paciente a eliminar: ");
        String id = scanner.nextLine();
        List<Paciente> pacientes = data.readPacientes();
        boolean eliminado = false;
        List<Paciente> updatedPacientes = new ArrayList<>();
        for (Paciente paciente : pacientes) {
            if (!paciente.getId().equals(id)) {
                updatedPacientes.add(paciente);
            } else {
                eliminado = true;
            }
        }
        boolean success = data.writePacientes(updatedPacientes);

        if (eliminado && success) {
            System.out.println("Doctor eliminado con éxito.");
        } else if (!eliminado) {
            System.out.println("Doctor no encontrado.");
        } else {
            System.out.println("Ocurrió un error al eliminar el doctor.");
        }
    }
    
    /**
     * Busca un doctor por su ID e imprime su información.
     * type boolean so that it can be used as an identificator.
     */
    private boolean buscarDoctor() {
        System.out.print("Ingrese ID del doctor: ");
        String id = scanner.nextLine();
        List<Doctor> doctores = data.readDoctores();
        for (Doctor doctor : doctores) {
            if (doctor.getId().equals(id)) {
                System.out.println(doctor);
                return true;
            }
        } 
        System.out.println("Doctor no encontrado.");
        return false;
    }

    /**
     * Agrega un nuevo doctor al archivo CSV.
     */
    private void agregarDoctor() {
        System.out.print("Ingrese ID del doctor: ");
        String id = scanner.nextLine();
        System.out.print("Ingrese nombre del doctor: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese ID de la clínica: ");
        String clinicaId = scanner.nextLine();

        Doctor doctor = new Doctor(id, nombre, clinicaId);
        data.writeDoctor(doctor);
        System.out.println("Doctor agregado con éxito.");
    }

    /**
     * Elimina un doctor del archivo CSV por su ID.
     */
    private void eliminarDoctor() {
        System.out.print("Ingrese ID del doctor a eliminar: ");
        String id = scanner.nextLine();
        List<Doctor> doctores = data.readDoctores();
        boolean eliminado = false;
        List<Doctor> updatedDoctores = new ArrayList<>();
        
        for (Doctor doctor : doctores) {
            if (!doctor.getId().equals(id)) {
                updatedDoctores.add(doctor);
            } else {
                eliminado = true;
            }
        }
        boolean success = data.writeDoctores(updatedDoctores); 
        // Use DataHandler to write the updated list of doctors back to the file, and catch any error.

        
        if (eliminado && success) {
            System.out.println("Doctor eliminado con éxito.");
        } else if (!eliminado) {
            System.out.println("Doctor no encontrado.");
        } else {
            System.out.println("Ocurrió un error al eliminar el doctor.");
        }
    }

    /**
     * Muestra el historial médico de un paciente a cargo de un doctor específico.
     */
    private void verHistorialMedico() { //still needs improvement, I think this is not doing what is supposed to be doing.
        System.out.print("Ingrese ID del doctor: ");
        String doctorId = scanner.nextLine();
            
        // Check if doctor ID exists and retrieve the doctor
        Doctor doctor = data.getDoctorById(doctorId);
        if (doctor == null) { //can't this not be done in dataHandler?
            System.out.println("Doctor no encontrado.");
            return;
        }
        
        System.out.print("Ingrese ID del paciente: "); 
        String pacienteId = scanner.nextLine();
            
        // Check if patient ID exists and retrieve the patient
        Paciente paciente = data.getPacienteById(pacienteId);
        if (paciente == null) {
            System.out.println("Paciente no encontrado.");
            return;
        }
        
        // Capture the returned medical history string
        String historialMedico = doctor.verHistorialMedico(paciente);
        
        // Now you can decide where to print it or use it
        System.out.println(historialMedico); // Example: printing it here
    }
    /**
     * Muestra información sobre todas las clínicas. 
     */
    private void infoClinica() {
        List<Clinica> clinicas = data.readClinicas();
        if (clinicas.isEmpty()) {
            System.out.println("No hay clínicas registradas.");
        } else {
            for (Clinica clinica : clinicas) {
                System.out.println(clinica);
            }
        }
    }

    /**
     * Agrega una nueva clínica al archivo CSV.
     */
    private void agregarClinica() {
        System.out.print("Ingrese ID de la clínica: ");
        String id = scanner.nextLine();
        System.out.print("Ingrese nombre de la clínica: ");
        String nombre = scanner.nextLine();
        System.out.println("La direccion de la clincia: ");
        String direccion = scanner.nextLine();

        Clinica clinica = new Clinica(id, nombre, direccion);
        data.writeClinica(clinica);
        System.out.println("Clínica agregada con éxito.");
    }



    /**
     * Is the method to verify the admin password,
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
            attempts--; //remember the -- is like saying doing a substraction for each loop irritation.
            if (attempts > 0) {
                System.out.println("The password or username are incorrect, try again.");
            }

        }
        System.out.println("The maximum number of attempts has succeded.");
        return false;
    }



}