package Menu;

import java.util.HashMap;

/**
 * Estado
 */
public class Estado {

    public String menu;
    public HashMap<String, Estado> transition;

    public Estado nextState(String transitionString) {
        return transition.get(transitionString);
    }


}