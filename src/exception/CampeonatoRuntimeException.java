package exception;

//Base das Uncheckeds do projeto.
//Existe pra agrupar erros de uso em uma família só,
//permitindo um catch e modificações futuras mais otimizadas.
public class CampeonatoRuntimeException extends RuntimeException{
    public CampeonatoRuntimeException(String message) {
        super(message);
    }

    public CampeonatoRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
