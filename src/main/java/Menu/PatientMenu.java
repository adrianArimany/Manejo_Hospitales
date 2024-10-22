package Menu;

import java.util.ArrayList;
import java.util.HashMap;

import com.uvg.proyecto.Main;
import com.uvg.proyecto.Main.UserType;

/**
 * MainMenu
 */
public abstract class PatientMenu {
    public HashMap<String, Integer[]> estado = new HashMap();
    public ArrayList<Integer> tranciciones = new ArrayList();

    public int currentState = 0;


    public void trancition(int userInput) {
        
    }


    

    // public Main.UserType menuPacientConditional1(int userInput, String scanner) {
        
    //     switch (userInput) {
    //         System.out.println("Opciones: \n1 Agregar Paciente \n2 Administar Paciente Registrado");
    //         case 1:
    //             System.out.println("Escribe el ID del paciente: ");
    //             String idPaceinte = scanner.nextLine();
    //             Paciente paciente = this.data.getPacienteById(idPaceinte);    
    //             return UserType.Paciente;

    //         case 2:
    //             System.out.println("Escribe el ID del paciente: ");
    //             String idPaceinte = scanner.nextLine();
    //             Paciente paciente = this.data.getPacienteById(idPaceinte);    
    //             return UserType.Paciente;
    //         default:
    //             break;
    //     }

    // }

}