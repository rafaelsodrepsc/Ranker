package model;

import exception.CampeonatoRuntimeException;
import exception.TimesInsuficientesException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CampeonatoMataMata extends Campeonato {
    private LocalDate dataLimite;

    public CampeonatoMataMata( String nome, int diasDeDescanso, LocalDate dataDeInicio, LocalDate dataLimite) {
        super(nome, diasDeDescanso, dataDeInicio);
        this.dataLimite = dataLimite;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    @Override
    public void gerarConfrontos() throws TimesInsuficientesException {

        //Metodo bit a bit para ver se é potencia de 2
        if (this.times.isEmpty() || (this.times.size() & (this.times.size() - 1)) != 0 ) { throw new TimesInsuficientesException("Numero de times impossivel de se criar um mata mata"); }

        List<Time>lista = new ArrayList<>(this.times);
        Collections.shuffle(lista); //Embaralha a lista "aleatoriamente"

        LocalDate dataRodada = this.dataDeInicio;
        for ( int i = 0; i < lista.size()/2; i++ ) {
            Time timeA = lista.get(i*2);
            Time timeB = lista.get(i*2 + 1);
            Partida partida = new Partida(timeA,timeB,1, dataRodada,timeA.getLocalTime());
            this.confrontos.add(partida);
        }
    }

    public boolean avancarFase(int rodadaDaVez){
        List<Partida> faseSeparada = this.confrontos.stream().filter(p -> p.getRodadaAtual() == rodadaDaVez).toList();

        for ( Partida p : faseSeparada ) {
            if (p.getStatus() != StatusPartida.CONCLUIDA){ throw new CampeonatoRuntimeException("Partida não foi concluida"); }
        }

        List<Time> vencedores = faseSeparada.stream()
                .map(Partida::getVencedor)  // cada Partida vira um Time
                .toList();

        if( vencedores.size() == 1 ){
            System.out.println("Campeão: " + vencedores.get(0).getNomeTime());
            return true;
        }
        LocalDate proximaData = this.dataDeInicio.plusDays((long) rodadaDaVez * this.diasDeDescanso);
        int proximaRodada = rodadaDaVez + 1;

        for ( int i = 0; i < vencedores.size() / 2; i++ ) {
            Time timeA = vencedores.get(i * 2);
            Time timeB = vencedores.get(i * 2 + 1);
            this.confrontos.add(new Partida(timeA, timeB, proximaRodada, proximaData, timeA.getLocalTime()));
        }
        return false;
    }

    public void exibirTabela() {return;} //TO DO
}