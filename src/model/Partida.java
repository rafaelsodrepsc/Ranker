package model;

import exception.CampeonatoRuntimeException;
import exception.PartidaJaEncerradaException;
import exception.PenaltisEmpatadadosException;

import java.time.LocalDate;

public class Partida {

    // Atributos da partida
    private StatusPartida status;
    private final int rodadaAtual;
    private final Local local;
    private final LocalDate dataDaRodada;

    private final Time timeMandante;
    private int golsTimeMandante;

    private final Time timeVisitante;
    private int golsTimeVisitante;

    private boolean foiParaPenaltis;
    private int golsPenaltisTimeMandante;
    private int golsPenaltisTimeVisitante;

    public Partida( Time time1, Time time2, int rodada, LocalDate dataDaRodada ) {
        this.timeMandante = time1;
        this.timeVisitante = time2;
        this.rodadaAtual = rodada;
        this.dataDaRodada = dataDaRodada;
        this.local = time1.getLocalTime(); // O estádio será o do mandante
        this.status = StatusPartida.AGENDADA;
    }

    public void encerrarPartida( int gols1, int gols2 ) {

        if ( this.status == StatusPartida.CONCLUIDA ) { throw new  PartidaJaEncerradaException("Partida ja encerrada!! "); }

        if ( gols1 < 0 || gols2 < 0 ) {
            System.out.println("Quantidade inválida de gols fornecida. (Por favor, coloque valores corretos)");
            return;
        }

        this.golsTimeMandante = gols1;
        this.golsTimeVisitante = gols2;

        this.timeMandante.registrarPartida(gols1, gols2);
        this.timeVisitante.registrarPartida(gols2, gols1);

        if ( this.golsTimeMandante != this.golsTimeVisitante ) { this.status = StatusPartida.CONCLUIDA; }    // tem vencedor, encerra a partida
        else { this.status = StatusPartida.EM_ANDAMENTO; }                                                   // empate, aguarda a decisão por pênaltis
    }

    public void encerrarPenaltis( int gols1, int gols2 ) throws PenaltisEmpatadadosException {

        if ( this.status != StatusPartida.EM_ANDAMENTO ) { throw new CampeonatoRuntimeException("Partida finalizada sem penaltis."); }

        if ( this.golsTimeMandante != this.golsTimeVisitante ) { throw new CampeonatoRuntimeException("Partida não necessita de penaltis"); }

        if( this.golsPenaltisTimeMandante == this.golsPenaltisTimeVisitante ) { throw new PenaltisEmpatadadosException("Penaltis empatados, continue a cobrança"); }

        this.golsPenaltisTimeMandante = gols1;
        this.golsPenaltisTimeVisitante = gols2;

        this.foiParaPenaltis = true;
        this.status = StatusPartida.CONCLUIDA;
    }

    public Time getVencedor() {
        if ( this.status != StatusPartida.CONCLUIDA ) { throw new PartidaJaEncerradaException("Partida ainda não foi encerrada."); }

        if ( this.foiParaPenaltis ) {
            if ( this.golsPenaltisTimeMandante > this.golsPenaltisTimeVisitante ) { return this.timeMandante; }
            else { return this.timeVisitante; }
        }
        else {
            if ( this.golsTimeMandante > this.golsTimeVisitante ) { return this.timeMandante; }
            else { return this.timeVisitante; }
        }
    }

    public StatusPartida getStatus() { return this.status; }

    public Time getTimeMandante() { return this.timeMandante; }

    public Time getTimeVisitante() { return this.timeVisitante; }

    public int getRodadaAtual(){ return this.rodadaAtual; }

    public Local getLocal() { return this.local; }

    public LocalDate getDataRodada() { return this.dataDaRodada; }

    public int getGolsTimeMandante() { return this.golsTimeMandante; }

    public int getGolsTimeVisitante() { return this.golsTimeVisitante; }

    @Override
    public String toString() {
        String placar = ( this.status == StatusPartida.CONCLUIDA )
                ? this.golsTimeMandante + "x" + this.golsTimeVisitante : "x";

        return "Rodada " + this.rodadaAtual + " | " + this.dataDaRodada + "|" +
                this.local + " | " +
                this.timeMandante.getNomeTime() + " " + placar + " " +
                this.timeVisitante.getNomeTime() +
                " (" + this.status + ")";
    }
}