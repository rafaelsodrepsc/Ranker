package exception;

//Checked -  O erro consiste de tentar criar uma partida mas não ter times suficientes
//oque força a ter um tratamento dentro para que seja criada a partida.
public class TimesInsuficientesException extends CampeonatoException {
    public TimesInsuficientesException(String message) {
        super(message);
    }
}
