import model.Cores;
import ui.Menu;

public class Main {
    public static void main(String[] args) {
        try {
            new Menu().iniciar();
        } catch (Exception e) {
            System.err.println(Cores.VERMELHO + "[ErroFatal] " + Cores.RESET + e.getMessage());
        }
    }
}