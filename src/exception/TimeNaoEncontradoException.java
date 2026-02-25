package exception;

//Unchecked -  O erro consiste de tentar encontrar um time que não existe.
public class TimeNaoEncontradoException extends CampeonatoRuntimeException {
    public TimeNaoEncontradoException(String message) {
        super(message);
    }
}
