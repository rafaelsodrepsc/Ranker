package model;

import exception.PartidaJaEncerradaException;

import java.time.LocalDate;

public class Partida {

    private final Time time1;              // Mandante
    private final Time time2;              // Visitante
    private final int rodada;
    private final Local local;
    private final LocalDate dataRodada;
    private int golsTime1;
    private int golsTime2;
    private StatusPartida status;

    public Partida(Time time1, Time time2, int rodada, LocalDate dataRodada) {

        this.time1 = time1;
        this.time2 = time2;
        this.rodada = rodada;
        this.dataRodada = dataRodada;
        this.local = time1.getEstadio(); // O estádio será o do mandante
        this.status = StatusPartida.AGENDADA;
    }

    public void encerrarPartida(int gols1, int gols2) {
        if (status == StatusPartida.CONCLUIDA) {
            throw new  PartidaJaEncerradaException("Partida ja encerrada!! ");
        }

        this.golsTime1 = gols1;
        this.golsTime2 = gols2;

        time1.registrarPartida(gols1, gols2);
        time2.registrarPartida(gols2, gols1);

        this.status = StatusPartida.CONCLUIDA;
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

    public int getRodada(){return rodada;}

    public Local getLocal() {return local;}

    public LocalDate getDataRodada() {return dataRodada;}

    public int getGolsTime1() {return golsTime1;}

    public int getGolsTime2() {return golsTime2;}

    @Override
    public String toString() {
        String placar = (status == StatusPartida.CONCLUIDA)
                ? golsTime1 + "x" + golsTime2 : "x";
        return "Rodada " + rodada + " | " + dataRodada + "|" +
                local + " | " +
                time1.getNome() + " " + placar + " " +
                time2.getNome() +
                " (" + status + ")";
    }
}