package model;

import exception.TimesInsuficientesException;

import java.time.LocalDate;
import java.util.*;

public class CampeonatoPontosCorridos extends Campeonato {
    public CampeonatoPontosCorridos(String nome, int diasDeDescanso, LocalDate dataDeInicio) {
        super(nome, diasDeDescanso, dataDeInicio);
    }

    @Override
    public void gerarConfrontos() throws TimesInsuficientesException {

        if (this.times.size() < 4) {
            throw new TimesInsuficientesException("Minimo de 4 times para o campeonato.");
        }

        //Algoritmo Round Robin de torneios - comumente usado na criação de pontos corridos
        List<Time> listaIda = new ArrayList<>(this.times);

        if (listaIda.size() % 2 != 0) {
            listaIda.add(null); // time fantasma
        }
        int numRodadas = listaIda.size() - 1;
        int metade = listaIda.size() / 2;

        //Jogos de ida
        for (int i = 1; i <= numRodadas; i++) {
            LocalDate dataRodada = this.dataDeInicio.plusDays((long) i * this.diasDeDescanso);
            for (int j = 0; j < metade; j++) {
                Time timeA = listaIda.get(j);
                Time timeB = listaIda.get(listaIda.size() - 1 - j);

                if (timeA != null && timeB != null) {
                    Partida partida = new Partida(timeA, timeB, i, dataRodada, timeA.getLocalTime());
                    this.confrontos.add(partida);
                }
            }
            Time ultimo = listaIda.removeLast();
            listaIda.add(1, ultimo);
        }
        List<Time> listaVolta = new ArrayList<>(this.times); //Jogo da volta

        if (listaVolta.size() % 2 != 0) {
            listaVolta.add(null); // time fantasma
        }

        for (int i = 1; i <= numRodadas; i++) {
            LocalDate dataRodada = this.dataDeInicio.plusDays((long) (numRodadas + i) * this.diasDeDescanso);
            for (int j = 0; j < metade; j++) {
                Time timeA = listaVolta.get(j);
                Time timeB = listaVolta.get(listaVolta.size() - 1 - j);

                if (timeA != null && timeB != null) {

                    Partida partida = new Partida(timeB, timeA, numRodadas + i, dataRodada, timeB.getLocalTime());
                    this.confrontos.add(partida);
                }
            }
            Time ultimo = listaVolta.removeLast();
            listaVolta.add(1, ultimo);
        }
    }

    /* ---------------------------------------------------------
    /*        METODO QUE DEFINE O CAMPEÃO DA LIGA
    // ---------------------------------------------------------
       Ordena os times de acordo com os critérios:
       1º Pontos
       2º Vitórias
       3º Saldo de gols
    */
    public Time campeao() {

        Tabela tabela = new Tabela(this.times); // Cria um objeto Tabela usando a lista atual de times
        tabela.classificar();

        return tabela.getClassificados(1).getFirst(); // Pega o primeiro colocado (posição 0)
    }
}

