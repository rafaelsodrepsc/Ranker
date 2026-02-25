package exception;

//Base das Checkeds do projeto.
//Existe pra agrupar erros de uso em uma família só,
//permitindo um catch e modificações futuras mais otimizadas.

//Oque difere da Unchecked é que nas Checked são casos que vão demandar um tratamento.
public class CampeonatoException extends Exception {
    public CampeonatoException(String message) {
        super(message);
    }

    public CampeonatoException(String message, Throwable cause) {
        super(message, cause);
    }
}
