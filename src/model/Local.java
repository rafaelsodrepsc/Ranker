package model;

public class Local {
    private final String nome;
    // Nome do estádio/local da partida
    public Local(String nome) {
        // Construtor que recebe o nome do estádio
        this.nome = nome;
    }
    public String getNome() {
        // Retorna o nome do estádio
        return nome;
    }
    @Override
    public String toString() {
        // Define como o objeto será exibido ao ser impresso
        return nome;
    }
}