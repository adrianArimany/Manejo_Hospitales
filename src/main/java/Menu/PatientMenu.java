package Menu;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * MainMenu
 */
public abstract class PatientMenu {
    public HashMap<String, Integer[]> estado = new HashMap();
    public ArrayList<Integer> tranciciones = new ArrayList();

    public int currentState = 0;


    public void trancition(int userInput) {
        
    }


    
}