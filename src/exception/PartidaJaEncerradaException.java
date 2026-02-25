package exception;

//Unchecked -  O erro consiste de tentar encerrar uma partida que ja foi encerrada.
public class PartidaJaEncerradaException extends CampeonatoRuntimeException {
    public PartidaJaEncerradaException(String message) {
        super(message);
    }
}
