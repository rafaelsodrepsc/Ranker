package model;

public class Time {

    // Atributos do Time
    private final String nomeTime;       // Nome do time
    private final Local localTime;       // "Casa" do próprio do time

    // Estatísticas do time:
    private int total_pontos;
    private int qnt_vitorias;
    private int qnt_empates;
    private int qnt_derrotas;
    private int qnt_golsPro;
    private int qnt_golsContra;

    public Time( String nome, Local localTime ) {
        this.nomeTime = nome;
        this.localTime = localTime;
        this.total_pontos = 0;
    }

    public void registrarPartida( int golsFeitos, int golsSofridos ) { // Metodo chamado quando o time termina uma partida
        this.qnt_golsPro += golsFeitos;       // Soma gols marcados
        this.qnt_golsContra += golsSofridos;  // Soma gols sofridos

        if (golsFeitos > golsSofridos) {              // Se fez mais gols → vitória
            this.qnt_vitorias++;
            this.total_pontos += Resultado.VITORIA.getPontosAssociados();
        }
        else if (golsFeitos == golsSofridos) {        // Se fez igual número de gols → empate
            this.qnt_empates++;
            this.total_pontos += Resultado.EMPATE.getPontosAssociados();
        }
        else {                                        // Se fez menos gols → derrota
            this.qnt_derrotas++;
            this.total_pontos += Resultado.DERROTA.getPontosAssociados();
        }
    }

    public int getTotal_pontos() {
        return this.total_pontos;
    }

    public int getQnt_vitorias() {
        return this.qnt_vitorias;
    }

    public int getQnt_empates() { return this.qnt_empates; }

    public int getQnt_derrotas() { return this.qnt_derrotas; }

    public int getQnt_golsPro() {
        return this.qnt_golsPro;
    }

    public int getQnt_golsContra() {
        return this.qnt_golsContra;
    }

    public String getNomeTime() {
        return this.nomeTime;
    }

    public Local getLocalTime() {
        return this.localTime;
    }

    public int getSaldoDeGols() { return this.qnt_golsPro - this.qnt_golsContra; } // Retorna saldo de gols (critério de desempate)

    @Override
    public String toString() { // Define o padrão da exibição dos times na tabela
        return this.nomeTime +
                " | Pontos: " + this.total_pontos +
                " | Vitórias: " + this.qnt_vitorias +
                " | Saldo: " + getSaldoDeGols();
    }
}