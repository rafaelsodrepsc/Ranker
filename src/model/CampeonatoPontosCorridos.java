package model;

import exception.TimesInsuficientesException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CampeonatoPontosCorridos extends Campeonato {
    public CampeonatoPontosCorridos( String nome, int diasDeDescanso, LocalDate dataDeInicio ) {
        super(nome, diasDeDescanso, dataDeInicio);
    }

    @Override
    public void gerarConfrontos() throws TimesInsuficientesException {

        if ( this.times.size() < 4 ){ throw new TimesInsuficientesException("Minimo de 4 times para o campeonato."); }

        //Algoritmo Round Robin de torneios - comumente usado na criação de pontos corridos
        int numRodadas = times.size()-1;
        int metade = times.size()/2;

        List<Time> listaIda = new ArrayList<>(this.times); //Jogos de ida
        for( int i = 1; i <= numRodadas; i++ ){
            LocalDate dataRodada = this.dataDeInicio.plusDays((long) i * this.diasDeDescanso);
            for ( int j = 0; j < metade; j++ ){
                Time timeA = listaIda.get(j);
                Time timeB = listaIda.get(listaIda.size()- 1 - j);

                Partida partida = new Partida(timeA,timeB,i ,dataRodada);
                this.confrontos.add(partida);
            }
            Time ultimo = listaIda.removeLast();
            listaIda.add(1, ultimo);
        }

        List<Time>listaVolta = new ArrayList<>(this.times); //Jogo da volta
        for ( int i = 1; i <= numRodadas; i++ ){
            LocalDate dataRodada = this.dataDeInicio.plusDays((long) (numRodadas + i) * this.diasDeDescanso);
            for( int j = 0; j < metade; j++ ){
                Time timeA = listaVolta.get(j);
                Time timeB = listaVolta.get(listaVolta.size() - 1 - j);

                Partida partida = new Partida(timeB,timeA, numRodadas + i, dataRodada);
                this.confrontos.add(partida);
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

    /* ------------------------------------------------------------
    /* METODO QUE RETORNA O MELHOR ATAQUE (Time que fez mais gols)
    /* ------------------------------------------------------------ */
    public Time melhorAtaque() {

        Time melhor = this.times.getFirst(); // Assume inicialmente que o primeiro time é o melhor

        for ( int i = 1; i < this.times.size(); i++ ) { // Começa do segundo time (posição 1)

            if ( this.times.get(i).getQnt_golsPro() > melhor.getQnt_golsPro() ) { // Testa se o time atual fez mais gols que o melhor atual
                melhor = this.times.get(i);
            }
        }

        return melhor; // Retorna o time que mais marcou gols
    }

    /* ------------------------------------------------------------
    /* METODO QUE RETORNA A MELHOR DEFESA (Time que sofreu menos gols)
    /* ------------------------------------------------------------ */
    public Time melhorDefesa() {

        Time melhor = this.times.getFirst(); // Assume inicialmente que o primeiro time é a melhor defesa

        for ( int i = 1; i < this.times.size(); i++ ) { // Percorre os demais times

            if ( this.times.get(i).getQnt_golsContra() < melhor.getQnt_golsContra() ) { // Testa se o time atual sofreu menos gols que o melhor atual
                melhor = this.times.get(i); // Atualiza referência
            }
        }

        return melhor; // Retorna o time que sofreu menos gols
    }
}