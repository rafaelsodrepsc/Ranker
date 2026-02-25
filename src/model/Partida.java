package model;

import exception.CampeonatoRuntimeException;
import exception.PartidaJaEncerradaException;
import exception.PenaltisEmpatadadosException;

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
    private boolean foiParaPenaltis;
    private int golsPenaltisTime1;
    private int golsPenaltisTime2;

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

        if (golsTime1 != golsTime2) {
            this.status = StatusPartida.CONCLUIDA; // tem vencedor, encerra
        } else {
            this.status = StatusPartida.EM_ANDAMENTO; // empate, aguarda pênaltis
        }
    }

    public void encerrarPenaltis(int gols1,int gols2) throws PenaltisEmpatadadosException {
        if (status != StatusPartida.EM_ANDAMENTO){
            throw new CampeonatoRuntimeException("Partida finalizada sem penaltis.");
        }
        if (golsTime1 != golsTime2){
            throw new CampeonatoRuntimeException("Partida não necessita de penaltis");
        }
        if(golsPenaltisTime1 == golsPenaltisTime2){
            throw new PenaltisEmpatadadosException("Penaltis empatados, continue a cobrança");
        }
        this.golsPenaltisTime1 = gols1;
        this.golsPenaltisTime2 = gols2;

        foiParaPenaltis = true;
        this.status = StatusPartida.CONCLUIDA;
    }
    public Time getVencedor(){
        if (status != StatusPartida.CONCLUIDA){
            throw new PartidaJaEncerradaException("Partida ainda não foi encerrada.");
        }
        if (foiParaPenaltis){
            if (golsPenaltisTime1>golsPenaltisTime2){
                return time1;
            }else {
                return time2;
            }
        }else {
            if (golsTime1 > golsTime2) {
                return time1;
            } else {
                return time2;
            }
        }
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