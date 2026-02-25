package model;

public class Time {

    private final String nome;       // Nome do time
    private int pontos;        // Total de pontos na tabela
    private int vitorias;      // Quantidade de vitórias
    private int empates;       // Quantidade de empates
    private int derrotas;      // Quantidade de derrotas
    private int golsPro;       // Gols marcados
    private int golsContra;    // Gols sofridos
    private final Local estadio;     // Estádio próprio do time

    public Time(String nome, Local estadio) {
        // Construtor do time
        this.nome = nome;
        this.estadio = estadio;
        this.pontos = 0;  // Começa com zero pontos
    }

    public void registrarPartida(int golsFeitos, int golsSofridos) {
        // Metodo chamado quando o time termina uma partida
        golsPro += golsFeitos;       // Soma gols marcados
        golsContra += golsSofridos;  // Soma gols sofridos

        if (golsFeitos > golsSofridos) {
            // Se fez mais gols → vitória
            vitorias++;
            pontos += 3;
        }
        else if (golsFeitos == golsSofridos) {
            // Se fez igual número de gols → empate
            empates++;
            pontos += 1;
        }
        else {
            // Se fez menos gols → derrota
            derrotas++;
        }
    }

    public int getSaldoDeGols() {
        // Retorna saldo de gols (critério de desempate)
        return golsPro - golsContra;
    }

    public int getPontos() {
        return pontos;
    }

    public int getVitorias() {
        return vitorias;
    }

    public int getGolsPro() {
        return golsPro;
    }

    public int getGolsContra() {
        return golsContra;
    }

    public String getNome() {
        return nome;
    }

    public Local getEstadio() {
        return estadio;
    }

    @Override
    public String toString() {
        // Define como o time aparece na tabela
        return nome +
                " | Pontos: " + pontos +
                " | Vitórias: " + vitorias +
                " | Saldo: " + getSaldoDeGols();
    }
}