import ui.Menu;

public class Main {
    public static void main(String[] args) {
        try {
            new Menu().iniciar();
        } catch (Exception e) {
            System.err.println("Erro fatal: " + e.getMessage());
        }
    }
}