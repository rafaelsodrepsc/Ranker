package model;

public class Local {
    private final String nome;

    public Local(String nome) {
        this.nome = nome;
    }
    public String getNome() {
        return nome;
    }
    @Override
    public String toString() {
        return nome;
    }
}