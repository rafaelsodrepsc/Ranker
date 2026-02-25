package exception;

//Unchecked -  O erro consiste de tentar colocar um time que ja existe.
public class TimeJaCadastradoException extends CampeonatoRuntimeException {
    public TimeJaCadastradoException(String message) {
        super(message);
    }
}
