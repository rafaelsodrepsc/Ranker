package model;

import exception.CampeonatoRuntimeException;
import exception.TimesInsuficientesException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CampeonatoMataMata extends Campeonato {
    public CampeonatoMataMata(String nome, int diasDeDescanso, LocalDate dataDeInicio) {
        super(nome, diasDeDescanso, dataDeInicio);
    }
    @Override
    public void gerarConfrontos() throws TimesInsuficientesException {
        //Metodo bit a bit para ver se é potencia de 2
        if (times.size() == 0 || (times.size() & (times.size() - 1)) != 0) {
            throw new TimesInsuficientesException("Numero de times impossivel de se criar um mata mata");
        }
        List<Time>lista = new ArrayList<>(times);
        Collections.shuffle(lista); //Embaralha a lista "aleatoriamente"

        LocalDate dataRodada = dataDeInicio;
        for (int i = 0; i < lista.size()/2; i++) {
            Time timeA = lista.get(i*2);
            Time timeB = lista.get(i*2 + 1);
            Partida partida = new Partida(timeA,timeB,1, dataRodada);
            confrontos.add(partida);
        }
    }
    public void avancarFase(int rodadaAtual){
        List<Partida> faseSeparada = confrontos.stream().filter(p -> p.getRodada() == rodadaAtual).toList();

        for (Partida p : faseSeparada) {
            if (p.getStatus() != StatusPartida.CONCLUIDA){
                throw new CampeonatoRuntimeException("Partida não foi concluida");
            }
        }

        List<Time> vencedores = faseSeparada.stream()
                .map(Partida::getVencedor)  // cada Partida vira um Time
                .toList();
        if(vencedores.size() == 1){
            return;
        }
        LocalDate proximaData = dataDeInicio.plusDays((long) rodadaAtual * diasDeDescanso);
        int proximaRodada = rodadaAtual + 1;

        for (int i = 0; i < vencedores.size() / 2; i++) {
            Time timeA = vencedores.get(i * 2);
            Time timeB = vencedores.get(i * 2 + 1);
            confrontos.add(new Partida(timeA, timeB, proximaRodada, proximaData));
        }
    }
}

