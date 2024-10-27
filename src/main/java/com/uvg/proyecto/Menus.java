// package com.uvg.proyecto;
// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;
// import java.util.HashMap;
// import java.util.Scanner;

// import com.uvg.proyecto.Classes.Cita;
// import com.uvg.proyecto.Classes.Doctor;
// import com.uvg.proyecto.Classes.Paciente;
// import com.uvg.proyecto.Data.StorageHandler;

// public class Menus {
//     private StorageHandler storageHandler;
//     private HashMap<Estados, String> menu;
//     private final String hospitalName = "Hospital Chapintenco";
//     private Estados currEstado;
//     private final String SECRET_PASS = "admin";
//     public enum Estados {
//         welcome,
//         Doctor,
//         LoggedInDoctor,
//         LoggedInPaciente,
//         Paciente,
//         Admin
//     }

//     private Doctor loginDoc;
//     private Paciente loginPac;
//     // private Admin loginAdmin;

//     public final Scanner scanner = new Scanner(System.in);

//     public static void main(String[] args) {
//         Menus menus = new Menus();
//         while (true) {
//             System.out.println(menus.printMenu());
//             try {
//                 int decision = Integer.parseInt(menus.scanner.nextLine());
//                 menus.makeDecisionToState(decision);
//             } catch (Exception e) {
//                 System.err.println("Opción no valida. Intenta de nuevo.");
//             } 

//         }
//     }

//     /**
//      * Print the current menu.
//      * @return the current menu
//      */
//     public String printMenu() {
//         return menu.get(currEstado);
//     }


//     public Menus() {
//         this.currEstado = Estados.welcome;
//         this.storageHandler = new StorageHandler();

//         // Conseguir los ultimos ids;
//         this.storageHandler.initIds();

//         this.menu= new HashMap<>(){
//             {
//                 put(Estados.welcome, chooseUserMenu());
//                 put(Estados.Doctor, chooseDoctorMenu());
//                 put(Estados.LoggedInDoctor, chooseDoctorMenu());
//                 put(Estados.LoggedInPaciente, chooseLoginPaciente());
//                 put(Estados.Paciente, choosePaciente());
//                 put(Estados.Admin, chooseUserMenu());
//             }
//         };
//     }
    
//     public String chooseUserMenu() {
//         StringBuilder stringBuilder = new StringBuilder();
//         stringBuilder.append("Gestión para " + hospitalName  +". \n");
//         stringBuilder.append("1. Soy un Doctor\n");
//         stringBuilder.append("2. Administar a un Paciente\n");
//         stringBuilder.append("3. Soy el Administrador\n");
//         stringBuilder.append("4. Salir\n");
//         return stringBuilder.toString();
//     }

//     /**
//      * Imprime el menú de paciente
//      * @return String del menú
//      */
//     public String choosePaciente(){
//         StringBuilder stringBuilder = new StringBuilder();
//         stringBuilder.append("1. Mostrar todos los pacientes\n");
//         stringBuilder.append("2. Agregar paciente nuevo\n");
//         stringBuilder.append("4. Usar paciente existente\n");
//         stringBuilder.append("5. Salir\n");
//         return stringBuilder.toString();
//     }

//     public String chooseDoctorMenu() {
//         StringBuilder stringBuilder = new StringBuilder();
//         stringBuilder.append("1. Agregar Doctor\n");
//         stringBuilder.append("2. Mostrar todos los Doctores\n");
//         stringBuilder.append("3. Buscar Doctor\n");
//         stringBuilder.append("4. User Doctor Existente\n");
//         stringBuilder.append("5. Salir\n");
//         return stringBuilder.toString();
//     }

//     public String loginDoctorMenu() {
//         StringBuilder stringBuilder = new StringBuilder();
//         stringBuilder.append("1. Agregar Paciente\n");
//         stringBuilder.append("2. Mostrar todos los Pacientes\n");
//         stringBuilder.append("3. Mostrar todos mis Pacientes\n");
//         stringBuilder.append("4. Hacer cita con Paciente\n");
//         stringBuilder.append("5. Buscar Paciente\n");
//         stringBuilder.append("6. Remover Paciente\n");
//         stringBuilder.append("7. Salir\n");
//         return stringBuilder.toString();
//     }

//     public String chooseLoginPaciente(){
//         StringBuilder stringBuilder = new StringBuilder();
//         stringBuilder.append("1. Mostrar todos los pacientes\n");
//         stringBuilder.append("2. Agregar paciente nuevo\n");
//         stringBuilder.append("4. Usar paciente existente\n");
//         stringBuilder.append("5. Salir\n");
//         return stringBuilder.toString();
//     }

//     public void makeDecisionToState(int decision) {
//         switch (currEstado) {
//             case welcome:
//                 welcomeDecision(decision);
//                 break;
//             case Doctor:
//                 doctMenuDecision(decision);
//                 break;
//             case LoggedInDoctor:
//                 loginDoctorMenuDecision(decision);
//                 break;
//             case Paciente:
//                 pacienteMenuDecision(decision);
//                 break;
//             case Admin:
//                 // adminMenuDecision(decision);
//                 break;
//             default:
//                 System.out.println("Opción no válida. Intenta de nuevo.");
//         }
//     }

//     private void pacienteMenuDecision(int decision) {
// 		switch (decision) {
//             case 1:
//                 // Mostrar todos los pacientes
//                 System.out.println("Pacientes:");
//                 System.out.println(this.storageHandler.showAllPacientes());
//                 break;
//             case 2:
//                 // Agregar paciente nuevo
//                 System.out.println("Nombre:");
//                 String nombre = scanner.nextLine();
//                 this.loginPac = new Paciente(nombre);
//                 this.storageHandler.createPaciente(this.loginPac);
//                 System.out.println("Paciente agregado");
//                 this.currEstado = Estados.LoggedInPaciente;
//                 System.out.println(String.format("Bienvenido %s", this.loginPac.getNombre()));
//                 break;
//             case 4:
//                 // Usar paciente existente
//                 System.out.println("ID:");
//                 int id = Integer.parseInt(scanner.nextLine());
//                 this.loginPac = this.storageHandler.getPacienteById(id);
//                 if (loginPac != null) {
//                     this.currEstado = Estados.LoggedInPaciente;

//                     System.out.println("Login exitoso");
//                     System.out.println(String.format("Bienvenido %s", loginPac.getNombre()));
//                 } else {
//                     System.out.println("Paciente no encontrado");
//                 }
//                 break;
//             case 5:
//                 // Salir
//                 System.out.println("Saliendo...");
//                 break;
//             default:
//                 System.out.println("Opción no válida. Intenta de nuevo.");
//                 break;
//         }
// 	}

//     /**
//      * Handle the user's decision at the welcome screen.
//      *
//      * @param decision The user's decision.
//      */
// 	public void welcomeDecision(int decision) {
//         switch (decision) {
//             case 1:
//                 currEstado = Estados.Doctor;
//                 System.out.println("Bienvenido Doctor!");
//                 break;
//             case 2:
//                 currEstado = Estados.Paciente;
//                 System.out.println("Bienvenido Paciente!");
//                 break;
//             case 3:
//                 currEstado = Estados.Admin;
//                 System.out.println("Para ingresar como administrador favor ingresar contrasena");
//                 String contrasena = scanner.nextLine();
//                 if (contrasena.equals(SECRET_PASS)) {
//                     System.out.println("Bienvenido Administrador!");
//                 } else {
//                     System.out.println("Contrasena incorrecta");
//                     currEstado = Estados.welcome;
//                 }
//                 break;
//             case 4:
//                 System.out.println("Saliendo...");
//                 // Add exit logic if needed
//                 break;
//             default:
//                 System.out.println("Opción no válida. Intenta de nuevo.");
//                 break;
//         }
//     }

//     /**
//      * Handles the user's decision at the doctor menu.
//      *
//      * @param decision The user's decision.
//      */
//     public void doctMenuDecision(int decision){
//         switch (decision) {
//             case 1:
//                 // agregar doctor
//                 System.out.println("Nombre:");
//                 String nombre = scanner.nextLine();
//                 this.loginDoc = new Doctor(nombre);
//                 this.storageHandler.createDoctor(this.loginDoc);
//                 System.out.println("Doctor agregado");
//                 this.currEstado = Estados.LoggedInDoctor;
//                 System.out.println(String.format("Bienvenido Dr %s", this.loginDoc.getNombre()));
//                 break;
//             case 2:
//                 System.out.println("Doctores:");
//                 // this.storageHandler.
//                 break;
//             case 3:
//                 // buscar doctor
//                 System.out.println("ID:");
//                 int id = Integer.parseInt(scanner.nextLine());
//                 Doctor doc = this.storageHandler.getDoctorById(id);
//                 if (doc != null) {
//                     System.out.println(doc);
//                 } else {
//                     System.out.println("Doctor no encontrado");
//                 }
//                 break;
//             case 4:
//                 // user doctor existente
//                 System.out.println("ID:");
//                 int idDoc = Integer.parseInt(scanner.nextLine());
//                 this.loginDoc = this.storageHandler.getDoctorById(idDoc);
//                 if (loginDoc != null) {
//                     this.currEstado = Estados.LoggedInDoctor;

//                     System.out.println("Login exitoso");
//                     System.out.println(String.format("Bienvenido Dr %s", loginDoc.getNombre()));
//                 } else {
//                     System.out.println("Doctor no encontrado");
//                 }
//                 break;
//             case 5:
//                 // salir
//                 System.out.println("Saliendo...");
//                 currEstado = Estados.welcome;
//                 break;
//             default:
//                 System.out.println("Opción no válida. Intenta de nuevo.");
//                 break;
//         }
//     }

//     public void loginDoctorMenuDecision(int decision) {
//         switch (decision) {
//             case 1:
//                 // agregar paciente
//                 System.out.println("ID del paciente:");
//                 int id = Integer.parseInt(scanner.nextLine());
//                 Paciente paciente =this.storageHandler.getPacienteById(id);
//                 if (paciente != null) {
//                     boolean isPacienteAdded = this.storageHandler.addPacienteToDoctor(this.loginDoc, paciente);
//                     if (isPacienteAdded) {
//                         System.out.println("Paciente agregado");
//                     }else {
//                         System.out.println("Paciente no agregado");
//                     }

//                 } else {
//                     System.out.println("Paciente no encontrado");
//                 }
//                 break;
//             case 2:
//                 // mostrar todos los pacientes
//                 System.out.println("Pacientes:");
//                 System.out.println(this.storageHandler.showAllPacientes());
//                 break;
//             case 3:
//                 // mostrar todos mis pacientes
//                 System.out.println("Pacientes:");
//                 System.out.println(this.storageHandler.getDrPacientes(this.loginDoc));
//                 break;
//             case 4:
//                 // hacer cita con paciente
//                 System.out.println("ID del paciente:");
//                 int idPaciente = Integer.parseInt(scanner.nextLine());
//                 Paciente pacienteCita =this.storageHandler.getPacienteById(idPaciente);
//                 if (pacienteCita != null) {
//                     System.out.println("Fecha de la cita:");
//                     String fecha = scanner.nextLine();
//                     DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MMM-dd");

//                     System.out.println("Motivo de la cita:");
//                     String motivo = scanner.nextLine();
                    
//                     // Cita cita = new Cita(idPaciente, this.loginDoc.getId(), dtf);
//                     // boolean isCitaAdded = this.storageHandler.addCitaToPaciente(pacienteCita, cita);
//                     // if (isCitaAdded) {
//                     //     System.out.println("Cita agregada");
//                     // }else {
//                     //     System.out.println("Cita no agregada");
//                     // }

//                 } else {
//                     System.out.println("Paciente no encontrado");
//                 }
//                 break;
//             case 5:
//                 // buscar paciente
//                 break;
//             case 6:
//                 // remover paciente
//                 break;
//             case 7:
//                 // salir
//                 break;
//             default:
//                 System.out.println("Opción no válida. Intenta de nuevo.");
//                 break;
//         }
//     }
// }
