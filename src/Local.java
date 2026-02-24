public class Local {

    private String nomeLocal;
    // Nome do estádio/local da partida

    public Local(String nome) {
        // Construtor que recebe o nome do estádio
        this.nomeLocal = nome;
    }

    public String getNomeLocal() {
        // Retorna o nome do estádio
        return nomeLocal;
    }

    @Override
    public String toString() {
        // Define como o objeto será exibido ao ser impresso
        return nomeLocal;
    }
}