package model;

public class Local {
    private final String nome; // Def.: Nome do estádio ou local da partida

    public Local(String nome) { this.nome = nome; }

    public String getNome() { return this.nome; } // Retorna o nome do local associado ao time

    @Override
    public String toString() { return this.nome; } // Define como o objeto será exibido ao ser impresso
}