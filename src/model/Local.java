package model;

import java.time.LocalTime;

public class Local {
    // Def.: Nome do estádio ou local da partida
    private final String nome;
    private final LocalTime abertura;
    private final LocalTime fechamento;

    public Local(String nome, LocalTime abertura, LocalTime fechamento) {
        this.nome = nome;
        this.abertura = abertura;
        this.fechamento = fechamento;
    }

    public String getNome() { return this.nome; } // Retorna o nome do local associado ao time

    public LocalTime getAbertura() {
        return this.abertura;
    }
    public LocalTime getFechamento() {
        return this.fechamento;
    }

    @Override
    public String toString() { return this.nome + " | Horário de funcionamento: " + this.abertura + " às " + this.fechamento; } // Define como o objeto será exibido ao ser impresso
}