package model;

public class CampeonatoPontosCorridos extends Campeonato {
    // A classe model.CampeonatoPontosCorridos herda de model.Campeonato.
    // Isso significa que ela já possui:
    // - A lista de times (times)
    // - A lista de partidas (confrontos)
    // Ela representa a fase de pontos corridos.

    // ---------------------------------------------------------
    // METODO RESPONSÁVEL POR GERAR TODOS OS CONFRONTOS
    // ---------------------------------------------------------
    public void gerarConfrontos() {

        // Primeiro laço percorre todos os times da lista
        for (int i = 0; i < times.size(); i++) {

            // Segundo laço começa sempre depois do i
            // Isso evita repetir confrontos.
            // Exemplo: se já fez A x B, não fará B x A aqui.
            for (int j = i + 1; j < times.size(); j++) {

                // Pega o time da posição i
                Time timeA = times.get(i);

                // Pega o time da posição j
                Time timeB = times.get(j);

                // -----------------------------
                // JOGO DE IDA
                // -----------------------------
                // timeA será o mandante
                // O estádio usado será o do timeA
                Partida ida = new Partida(timeA, timeB, null);

                // Adiciona o jogo de ida na lista de confrontos
                confrontos.add(ida);

                // -----------------------------
                // JOGO DE VOLTA
                // -----------------------------
                // Agora invertemos o mando de campo
                // timeB será o mandante
                Partida volta = new Partida(timeB, timeA, null);

                // Adiciona o jogo de volta
                confrontos.add(volta);
            }
        }
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

    // ---------------------------------------------------------
    // METODO QUE RETORNA A MELHOR DEFESA
    // (model.Time que sofreu menos gols)
    // ---------------------------------------------------------
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