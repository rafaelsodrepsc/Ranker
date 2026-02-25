package model;

import java.time.LocalDate; // Importa classe de data

public class Partida {

    private StatusPartida status;    // Situação da partida
    private Time time1;              // Mandante
    private Time time2;              // Visitante
    private LocalDate dataDaPartida; // Data do jogo
    private Local localDaPartida;    // Estádio do jogo
    private int golsTime1;           // Gols do mandante
    private int golsTime2;           // Gols do visitante

    public Partida(Time time1, Time time2, LocalDate data) {
        // Construtor da partida

        this.time1 = time1;                 // Define mandante
        this.time2 = time2;                 // Define visitante
        this.dataDaPartida = data;          // Define data
        this.localDaPartida = time1.getEstadio();
        // O estádio será o do mandante

        this.status = StatusPartida.AGENDADA;
        // Começa como agendada
    }

    public void encerrarPartida(int gols1, int gols2) {

        if (status == StatusPartida.CONCLUIDA) {
            // Evita que a partida seja encerrada duas vezes
            return;
        }

        this.golsTime1 = gols1;  // Define gols do mandante
        this.golsTime2 = gols2;  // Define gols do visitante

        // Atualiza estatísticas dos times
        time1.registrarPartida(gols1, gols2);
        time2.registrarPartida(gols2, gols1);

        this.status = StatusPartida.CONCLUIDA;
        // Marca a partida como concluída
    }

    public StatusPartida getStatus() {
        return status;
    }

    public Time getTime1() {
        return time1;
    }

    public Time getTime2() {
        return time2;
    }

    @Override
    public String toString() {
        // Representação textual da partida
        return dataDaPartida + " - " +
                localDaPartida + " | " +
                time1.getNome() + " " + golsTime1 +
                " x " + golsTime2 + " " +
                time2.getNome() +
                " (" + status + ")";
    }
}