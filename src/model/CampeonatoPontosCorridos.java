package model;

import exception.TimesInsuficientesException;

import java.time.LocalDate;

public class CampeonatoPontosCorridos extends Campeonato {
    public CampeonatoPontosCorridos(String nome, int diasDeDescanso, LocalDate dataDeInicio) {
        super(nome, diasDeDescanso, dataDeInicio);
    }

    public void gerarConfrontos() throws TimesInsuficientesException{
        if (times.size() < 4){
            throw new TimesInsuficientesException("Minimo de 4 times para o campeonato.");
        }

        //Algoritmo Round Robin de torneios - comumente usado na criação de pontos corridos

    }

    // ---------------------------------------------------------
    // METODO QUE DEFINE O CAMPEÃO DA LIGA
    // ---------------------------------------------------------
    public Time campeao() {

        // Cria um objeto model.Tabela usando a lista atual de times
        Tabela tabela = new Tabela(times);

        // Ordena os times de acordo com os critérios:
        // 1º Pontos
        // 2º Vitórias
        // 3º Saldo de gols
        tabela.classificar();

        // Pega o primeiro colocado (posição 0)
        return tabela.getClassificados(1).get(0);
    }

    // ---------------------------------------------------------
    // METODO QUE RETORNA O MELHOR ATAQUE
    // (model.Time que fez mais gols)
    // ---------------------------------------------------------
    public Time melhorAtaque() {

        // Assume inicialmente que o primeiro time é o melhor
        Time melhor = times.get(0);

        // Começa do segundo time (posição 1)
        for (int i = 1; i < times.size(); i++) {

            // Se o time atual fez mais gols que o melhor atual
            if (times.get(i).getGolsPro() > melhor.getGolsPro()) {

                // Atualiza a referência de melhor ataque
                melhor = times.get(i);
            }
        }

        // Retorna o time que mais marcou gols
        return melhor;
    }

    public Time melhorDefesa() {

        // Assume inicialmente que o primeiro time é a melhor defesa
        Time melhor = times.get(0);

        // Percorre os demais times
        for (int i = 1; i < times.size(); i++) {

            // Se o time atual sofreu menos gols que o melhor atual
            if (times.get(i).getGolsContra() < melhor.getGolsContra()) {

                // Atualiza referência
                melhor = times.get(i);
            }
        }

        // Retorna o time que sofreu menos gols
        return melhor;
    }
}