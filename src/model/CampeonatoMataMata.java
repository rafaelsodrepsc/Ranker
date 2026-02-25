package model;

import exception.TimesInsuficientesException;

import java.time.LocalDate;
import java.util.*;


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
}

