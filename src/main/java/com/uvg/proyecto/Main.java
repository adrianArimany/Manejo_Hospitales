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
 * - When I run method in admin, rather than returning the previous menu, it send me back to the MenuBegins(). 
 * - When a patient/doctor is removed, its id is removed, but when you create a new doctor/patient rather than refilling the deleted id, it generates the highest id.
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
                        if (loginDoc != null) {
                            this.doctorMenu(loginDoc);
                        } else {
                            System.out.println("Error: Doctor not found.");
                        }
                        break;
                    case Admin:
                        adminMenu();
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
                    int idDoctor = scanner.nextInt();
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
        System.out.println("Bienbenido, " + loginPac.getNombre() +  " (ID: " + loginPac.getId() + ")");
        System.out.println("\n 1. Agendar Cita \n2. Revisar Citas \n3. Historial medico \n4. Revisar Prescripciones \n0. Regresar");
        int input = scanner.nextInt();
        scanner.nextLine();
        switch (input) {
            case 1:
                //Agendar Citas
                break;
            case 2:
                //Revisar Citas
                break;
                
            case 3:
                //Historial Medico
                break;

            case 4:
                //Revisar Prescripciones
                break;
            case 0:
                return;
            default:
                System.out.println("Solo ingrese los numeros en la pantalla.");
        }
    }

    public void doctorMenu(Doctor loginDoc) {
        System.out.println("Bienbenido Dr." + loginDoc.getNombre() +  " (ID: " + loginPac.getId() + ")");
        System.out.println("\n 1. Revisar Citas Pendientes \n2. Ver historal Medico de un Paciente \n3. Revisar Prescripciones de un Paciente \n4. Agregar prescripcion a un paciente  \n0. Regresar");
        int input = scanner.nextInt();
        scanner.nextLine();
        switch (input) {
            case 1:
                //Revisar citas pendientes
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
            case 0:
                return;
            default:
                System.out.println("Solo ingrese los numeros en la pantalla.");
        }
    }


    public void adminMenu() {
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
                return;
            default:
                System.out.println("Solo ingrese los numeros en la pantalla.");
        }
    }

    public void adminPaciente() {
        System.out.println("Menu para el Administrador Pacientes \n1. Eliminar Paciente del sistema \n0. Regresar");
        int input = scanner.nextInt();
        scanner.nextLine();
        switch (input) {
            case 1:
                System.out.println("Cual es el ID del paciente a Eliminar: ");
                int id = scanner.nextInt();
                boolean isDeleted  = storageHandler.deletePatient(id);
                if (isDeleted) {
                    System.out.println("Paciente con id: " + id + ". Eliminado del sistema.");
                } else {
                    System.out.println("Paciente con id: " + id + ". No existe en el sistema.");
                }
                break;
            case 0:
                adminMenu();
                return;
            default:
                System.out.println("Solo ingrese los numeros en la pantalla.");
        }
    }


    public void adminDoc() {
        System.out.println("Administracion para Doctores: \n1. Agregar Doctores \n2. Eliminar Doctores \n3. Mover a un Doctor \n0. Regresar");
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
                System.out.println("Funcionalidad mover doctor no implementada.");
                break;
            case 0:
                adminMenu();
                return;
            default:
                System.out.println("Solo ingrese los numeros en la pantalla.");
        }
    }

    public void adminClinica() {
        System.out.println("Administracion para Clinicas  \n1. Agregar Clinca \n2. Eliminar Clinica \n0. regresar");
        int input = scanner.nextInt();
        scanner.nextLine();
        switch (input) {
            case 1:
                
                break;
            case 2:

                break;

            case 3:
                
                break;
            case 0:
                adminMenu();
                return;
            default:
                System.out.println("Solo ingrese los numeros en la pantalla.");
        }
    }


    /**
     * Método principal que controla el flujo del programa y muestra un menú
     * interactivo.
     *
     * 
     */
    // private void mainMenu() {
    //     boolean running = true;

    //     while (running) {
    //         System.out.println("Buenos Dias al sistema Hospitalario.");
    //         System.out.println("1. Soy Doctor o Administrador"); // Sends to password-user
    //         System.out.println("2. Administrar a los Paciente del hospital."); // Sends to find for old pentiente or new
    //                                                                            // patient
    //         System.out.println("0. Salir del sistema");
    //         String choice = scanner.nextLine();
    //         switch (choice) {
    //             case "1":
    //                 showMenuDoctorGeneral(); // for new and old doctors.
    //                 break;
    //             case "2":
    //                 showMenuPersona(); // for either new or old patients.
    //                 break;
    //             case "0":
    //                 System.out.println("Saliendo...");
    //                 running = false;
    //                 break;
    //             default:
    //                 System.out.println("Opción no válida. Intenta de nuevo.");
    //         }
    //     }
    //     // scanner.close(); (finish the menus, then put this at the end.)
    // }

    /**
     * Corre Menu para cualquier persona (paciente o no):
     */
    // private void showMenuPersona() {
    //     boolean running = true;
    //     while (running) {
    //         System.out.println("Es usted ya paciente? o es usted nuevo al sistema?");
    //         System.out.println("1. Es Paciente Nuevo");
    //         System.out.println("2. Es Paciente Registrado");
    //         System.out.println("0. regresar");
    //         String choice = scanner.nextLine();
    //         switch (choice) {
    //             case "1":
    //                 agregarPaciente(); // crea nuevo paciente,
    //                 showMenuPaciente(); // Manda al menu del paciente.
    //                 break;
    //             case "2":
    //                 showMenuPacienteProtectRegistrado(); // paciente viejo manda a find id.
    //                 running = false;
    //                 break;
    //             case "0":
    //                 System.out.println("Regresando...");
    //                 running = false;
    //                 return;
    //             default:
    //                 System.out.println("Opción no válida. Intenta de nuevo.");
    //         }
    //     }
    // }

    // /**
    //  * Muestra el menú para Paciente 'Protect' registrado,
    //  */
    // private void showMenuPacienteProtectRegistrado() {
    //     boolean running = true;
    //     while (running) {
    //         System.out.println("Precione un numero: ");
    //         System.out.println("1. Tengo un ID: ");
    //         System.out.println("0. no tengo ID, regresar al menu anterior.: ");
    //         String choice = scanner.nextLine();
    //         switch (choice) {
    //             case "1":
    //                 if (buscarPaciente()) {
    //                     showMenuPaciente();
    //                 }
    //                 break;
    //             case "0":
    //                 System.out.println("Regresando...");
    //                 running = false;
    //                 return;
    //             default:
    //                 System.out.println("Opción no válida. Intenta de nuevo.");
    //         }
    //     }

    // }

    // /**
    //  * Muestra el menú para Paciente todo paciente.
    //  */
    // private void showMenuPaciente() {
    //     boolean running = true;
    //     while (running) {
    //         System.out.println("Bienvenido al menú de paciente.");
    //         System.out.println("1. Ver historial médico");
    //         System.out.println("2. Agregar una cita");
    //         System.out.println("0. Regresar al menú anterior");
    //         String choice = scanner.nextLine();
    //         switch (choice) {
    //             case "1":
    //                 System.out.println("Mostrando historial médico...");
    //                 verHistorialMedico();
    //                 break;
    //             case "2":
    //                 System.out.println("Agregando una cita...");
    //                 // Create a method that adds an appointment to the patient.
    //                 break;
    //             case "0":
    //                 System.out.println("Regresando...");
    //                 running = false;
    //                 break;
    //             default:
    //                 System.out.println("Opción no válida.");
    //         }
    //     }
    // }

    // private void showMenuDoctorGeneral() {
    //     boolean running = true;
    //     while (running) {
    //         System.out.println("Es usted doctor registrado? o es usted nuevo al sistema?");
    //         System.out.println("1. Soy Doctor registrado");
    //         System.out.println("2. Soy Doctor nuevo");
    //         System.out.println("0. regresar");
    //         String choice = scanner.nextLine();
    //         switch (choice) {
    //             case "1":
    //                 showMenuDoctorProtectRegistrado(); // paciente viejo manda a find id.
    //                 break;
    //             case "2":
    //                 agregarDoctor(); // crea nuevo doctor,
    //                 showMenuDoctor(); // Manda a menu de doctores.
    //                 break;
    //             case "0":
    //                 System.out.println("Regresando...");
    //                 running = false;
    //                 return;
    //             default:
    //                 System.out.println("Opción no válida. Intenta de nuevo.");
    //         }
    //     }
    // }

    // private void showMenuDoctorProtectRegistrado() {
    //     boolean running = true;
    //     while (running) {
    //         System.out.println("Precione un numero: ");
    //         System.out.println("1. Tengo un ID: ");
    //         System.out.println("0. no tengo ID, regresar al menu anterior.: ");
    //         String choice = scanner.nextLine();
    //         switch (choice) {
    //             case "1":
    //                 if (buscarDoctor()) {
    //                     showMenuDoctor();
    //                 }
    //                 break;
    //             case "0":
    //                 System.out.println("Regresando...");
    //                 running = false;
    //                 return;
    //             default:
    //                 System.out.println("Opción no válida. Intenta de nuevo.");
    //         }
    //     }

    // }

    // /**
    //  * Muestra el menú para Paciente todo paciente.
    //  */
    // private void showMenuDoctor() {
    //     boolean running = true;
    //     while (running) {
    //         System.out.println("Bienvenido al menú de doctores y clinicas: ");
    //         System.out.println("1. Informacion sobre Clinicas: ");
    //         System.out.println("2. Agregar Clinica: ");
    //         System.out.println("3. Eliminar un Doctor del sistema: ");
    //         System.out.println("4. Eliminar a un Paciente del sistema: ");
    //         System.out.println("5. Informacion general de un paciente en el sistema: ");
    //         System.out.println("0. Regresar al menú anterior");
    //         String choice = scanner.nextLine();
    //         switch (choice) {
    //             case "1":
    //                 infoClinica();
    //                 break;
    //             case "2":
    //                 agregarClinica();
    //                 break;
    //             case "3":
    //                 System.out.println("\n[Accessed Denied] The admin password is needed.");
    //                 if (verifyAdmin()) {
    //                     System.out.println("Correct password, now you may exterminate the doctor. (who?)");
    //                     eliminarDoctor();
    //                 } else {
    //                     System.out.println("The verification can not be completed.");
    //                 }
    //                 break;
    //             case "4":
    //                 eliminarPaciente();
    //                 break;
    //             case "5":
    //                 buscarPaciente();
    //                 break;
    //             case "0":
    //                 System.out.println("Regresando...");
    //                 running = false;
    //                 break;
    //             default:
    //                 System.out.println("Opción no válida.");
    //         }
    //     }
    // }

    // /**
    //  * Busca un paciente por su ID e imprime su información.
    //  * Se cambia de void a boolean para poder usar este metodo como identificador.
    //  */
    // private boolean buscarPaciente() {
    //     System.out.print("Ingrese ID del paciente: ");
    //     String id = scanner.nextLine();

    //     // Call the leerPacientes() method from DataHandler using the instance data.
    //     List<Paciente> pacientes = data.readPacientes();

    //     // Check if the patient exists
    //     for (Paciente paciente : pacientes) {
    //         if (paciente.getId().equals(id)) {
    //             System.out.println(paciente);
    //             return true;
    //         }
    //     }
    //     System.out.println("Paciente no encontrado.");
    //     return false;
    // }

    // /**
    //  * Agrega un nuevo paciente al archivo CSV.
    //  */
    // private void agregarPaciente() {
    //     System.out.print("Ingrese ID del paciente: ");
    //     String id = scanner.nextLine();
    //     System.out.print("Ingrese nombre del paciente: ");
    //     String nombre = scanner.nextLine();
    //     System.out.print("Ingrese ID del doctor a cargo: ");
    //     String doctorId = scanner.nextLine();
    //     System.out.print("Ingrese ID de la clínica: ");
    //     String clinicaId = scanner.nextLine();
    //     System.out.print("Ingrese historial médico del paciente: ");
    //     String historialMedico = scanner.nextLine();
    //     System.out.print("Ingrese enfermedades del paciente: ");
    //     String enfermedades = scanner.nextLine();
    //     System.out.print("Ingrese cita médica del paciente: ");
    //     String citaMedica = scanner.nextLine();

    //     Paciente paciente = new Paciente(id, nombre, doctorId, clinicaId); // fix this, why is there three methods
    //                                                                        // called like that.
    //     paciente.agregarHistorialMedico(historialMedico);
    //     paciente.agregarEnfermedad(enfermedades);
    //     paciente.agregarCitaMedica(citaMedica);

    //     data.writePaciente(paciente);
    //     System.out.println("Paciente agregado con éxito.");
    // }

    // /**
    //  * Elimina un paciente del archivo CSV por su ID.
    //  */
    // private void eliminarPaciente() {
    //     System.out.print("Ingrese ID del paciente a eliminar: ");
    //     String id = scanner.nextLine();
    //     List<Paciente> pacientes = data.readPacientes();
    //     boolean eliminado = false;
    //     List<Paciente> updatedPacientes = new ArrayList<>();
    //     for (Paciente paciente : pacientes) {
    //         if (!paciente.getId().equals(id)) {
    //             updatedPacientes.add(paciente);
    //         } else {
    //             eliminado = true;
    //         }
    //     }
    //     boolean success = data.writePacientes(updatedPacientes);

    //     if (eliminado && success) {
    //         System.out.println("Doctor eliminado con éxito.");
    //     } else if (!eliminado) {
    //         System.out.println("Doctor no encontrado.");
    //     } else {
    //         System.out.println("Ocurrió un error al eliminar el doctor.");
    //     }
    // }

    // /**
    //  * Busca un doctor por su ID e imprime su información.
    //  * type boolean so that it can be used as an identificator.
    //  */
    // private boolean buscarDoctor() {
    //     System.out.print("Ingrese ID del doctor: ");
    //     String id = scanner.nextLine();
    //     List<Doctor> doctores = data.readDoctores();
    //     for (Doctor doctor : doctores) {
    //         if (doctor.getId().equals(id)) {
    //             System.out.println(doctor);
    //             return true;
    //         }
    //     }
    //     System.out.println("Doctor no encontrado.");
    //     return false;
    // }

    // /**
    //  * Agrega un nuevo doctor al archivo CSV.
    //  */
    // private void agregarDoctor() {
    //     System.out.print("Ingrese ID del doctor: ");
    //     String id = scanner.nextLine();
    //     System.out.print("Ingrese nombre del doctor: ");
    //     String nombre = scanner.nextLine();
    //     System.out.print("Ingrese ID de la clínica: ");
    //     String clinicaId = scanner.nextLine();

    //     Doctor doctor = new Doctor(id, nombre, clinicaId);
    //     data.writeDoctor(doctor);
    //     System.out.println("Doctor agregado con éxito.");
    // }

    // /**
    //  * Elimina un doctor del archivo CSV por su ID.
    //  */
    // private void eliminarDoctor() {
    //     System.out.print("Ingrese ID del doctor a eliminar: ");
    //     String id = scanner.nextLine();
    //     List<Doctor> doctores = data.readDoctores();
    //     boolean eliminado = false;
    //     List<Doctor> updatedDoctores = new ArrayList<>();

    //     for (Doctor doctor : doctores) {
    //         if (!doctor.getId().equals(id)) {
    //             updatedDoctores.add(doctor);
    //         } else {
    //             eliminado = true;
    //         }
    //     }
    //     boolean success = data.writeDoctores(updatedDoctores);
    //     // Use DataHandler to write the updated list of doctors back to the file, and
    //     // catch any error.

    //     if (eliminado && success) {
    //         System.out.println("Doctor eliminado con éxito.");
    //     } else if (!eliminado) {
    //         System.out.println("Doctor no encontrado.");
    //     } else {
    //         System.out.println("Ocurrió un error al eliminar el doctor.");
    //     }
    // }

    // /**
    //  * Muestra el historial médico de un paciente a cargo de un doctor específico.
    //  */
    // private void verHistorialMedico() { // still needs improvement, I think this is not doing what is supposed to be
    //                                     // doing.
    //     System.out.print("Ingrese ID del doctor: ");
    //     String doctorId = scanner.nextLine();

    //     // Check if doctor ID exists and retrieve the doctor
    //     Doctor doctor = data.getDoctorById(doctorId);
    //     if (doctor == null) { // can't this not be done in dataHandler?
    //         System.out.println("Doctor no encontrado.");
    //         return;
    //     }

    //     System.out.print("Ingrese ID del paciente: ");
    //     String pacienteId = scanner.nextLine();

    //     // Check if patient ID exists and retrieve the patient
    //     Paciente paciente = data.getPacienteById(pacienteId);
    //     if (paciente == null) {
    //         System.out.println("Paciente no encontrado.");
    //         return;
    //     }

    //     // Capture the returned medical history string
    //     String historialMedico = doctor.verHistorialMedico(paciente);

    //     // Now you can decide where to print it or use it
    //     System.out.println(historialMedico); // Example: printing it here
    // }

    // /**
    //  * Muestra información sobre todas las clínicas.
    //  */
    // private void infoClinica() {
    //     List<Clinica> clinicas = data.readClinicas();
    //     if (clinicas.isEmpty()) {
    //         System.out.println("No hay clínicas registradas.");
    //     } else {
    //         for (Clinica clinica : clinicas) {
    //             System.out.println(clinica);
    //         }
    //     }
    // }

    // /**
    //  * Agrega una nueva clínica al archivo CSV.
    //  */
    // private void agregarClinica() {
    //     System.out.print("Ingrese ID de la clínica: ");
    //     String id = scanner.nextLine();
    //     System.out.print("Ingrese nombre de la clínica: ");
    //     String nombre = scanner.nextLine();

    //     Clinica clinica = new Clinica(id, nombre);
    //     data.writeClinica(clinica);
    //     System.out.println("Clínica agregada con éxito.");
    // }

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