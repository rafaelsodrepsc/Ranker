package service;

import model.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RelatorioService {

    public static String ultimasPartidas(Campeonato cp, Time t) { //código feito com auxílio de IA
        AtomicInteger index = new AtomicInteger(0);

        List<Partida> confrontos = cp.getConfrontos().reversed();
        List<String> resultados = confrontos.stream()
                .filter(p -> p.getStatus() == StatusPartida.CONCLUIDA)
                .filter(p -> p.getTimeMandante() == t || p.getTimeVisitante() == t)
                .limit(5) // Pega apenas os 5 primeiros (mais recentes)
                .map(partida -> {
                    int i = index.getAndIncrement();
                    String simbolo;
                    String cor = Cores.CINZA;

                    if (partida.getTimeVisitante() == t && partida.ResultadoMandante() == -1) {
                        simbolo = "V";
                        cor = Cores.VERDE;
                    }else if(partida.getTimeVisitante() == t && partida.ResultadoMandante() == 1){
                        simbolo = "D";
                        cor = Cores.VERMELHO;
                    } else if (partida.getTimeMandante() == t && partida.ResultadoMandante() == 1) {
                        simbolo = "V";
                        cor = Cores.VERDE;
                    } else if (partida.getTimeMandante() == t && partida.ResultadoMandante() == -1){
                        simbolo = "D";
                        cor = Cores.VERMELHO;
                    }else {
                        simbolo = "E";
                    }

                    return (i == 0)
                            ? cor + "((" + simbolo + "))" + Cores.RESET
                            : cor + "("  + simbolo + ")" + Cores.RESET;
                })
                .collect(Collectors.toList());

        if (resultados.isEmpty()) {resultados.add(Cores.CINZA + "((.))" + Cores.RESET);}

        while (resultados.size() < 5) { // Caso o time não tenha 5 partidas concluidas
            resultados.add(Cores.CINZA + "(.)" + Cores.RESET);
        }
        Collections.reverse(resultados); // Inverte a lista para que o "index 0" (ultimo evento) fique por último

        return String.join(" ", resultados); // junta separando por espaço
    }

    public static String centralizar(String texto, int largura) { //código feito com auxílio de IA
        if (texto.length() > largura) {
            texto = texto.substring(0, largura - 3) + "...";
        }

        texto = texto.toUpperCase();
        if (texto.length() >= largura) {
            return texto.substring(0, largura); // Corta se for maior que o limite
        }

        int espacosTotal = largura - texto.length();
        int antes = espacosTotal / 2;
        int depois = espacosTotal - antes;

        return " ".repeat(antes) + texto + " ".repeat(depois);
    }

    public static void exibirTabela(CampeonatoPontosCorridos cp) { //código feito com auxílio de IA
        List<Time> timesOrdenados = cp.getTimes().stream().sorted(Comparator.comparingInt(Time::getTotal_pontos) // Ordenar por ptn/vit/saldo de gols
                .thenComparingInt(Time::getQnt_vitorias)
                .thenComparingInt(Time::getQnt_golsPro)
                .reversed()).toList();

        String nomeFormatado = centralizar(cp.getNome(), 89);
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│" + Cores.NEGRITO + nomeFormatado + Cores.RESET + "│");
        /*
        ┌─────────────────────────────────────────────────────────────────────────────────────────┐
        │                              TABELA GERAL DO CAMPEONATO                                 │
        ├──────┬───────────────┬──────┬─────┬─────┬─────┬─────┬─────┬─────┬───────────────────────┤
        │ Pos  │ Time          │ Pts  │ VIT │ EMP │ DER │ GP  │ GC  │ SG  │    Últimas Partidas   │
        ├──────┼───────────────┼──────┼─────┼─────┼─────┼─────┼─────┼─────┼───────────────────────┤
        │ 1º   │ Nome do time1 │ pnt1 │ vit1│ emp1│ der1│ gp1 │ gc1 │ sg1 │ (.) (.) (.) (.) ((.)) │   (V) -> VITORIA
        │ 2º   │ Nome do time2 │ pnt2 │ vit2│ emp2│ der2│ gp2 │ gc2 │ sg2 │ (.) (.) (.) (.) ((.)) │   (D) -> DERROTA
        │ .    │ .             │ .    │ .   │ .   │ .   │ .   │ .   │ .   │ .                     │   (E) -> EMPATE
        │ .    │ .             │ .    │ .   │ .   │ .   │ .   │ .   │ .   │ .                     │   (.) -> PENDENTE
        │ .    │ .             │ .    │ .   │ .   │ .   │ .   │ .   │ .   │ .                     │   ((x)) -> ÚLTIMO EVENTO
        │ nº   │ Nome do timeN │ pntN │ vitN│ empN│ derN│ gpN │ gcN │ sgN │ (.) (.) (.) (.) ((.)) │
        └──────┴───────────────┴──────┴─────┴─────┴─────┴─────┴─────┴─────┴───────────────────────┘
        */
        System.out.println("├──────┬───────────────┬──────┬─────┬─────┬─────┬─────┬─────┬─────┬───────────────────────┤");
        System.out.printf("│ %-4s │ %-13s │ %s%-4s%s │ %-3s │ %-3s │ %-3s │ %-3s │ %-3s │ %-3s │    %-16s   │\n"
                , "Pos", "Time", Cores.NEGRITO, "Pts", Cores.RESET , "VIT", "EMP", "DER", "GP", "GC", "SG", "Últimas Partidas");
        System.out.println("├──────┼───────────────┼──────┼─────┼─────┼─────┼─────┼─────┼─────┼───────────────────────┤");

        for (int i = 0; i < timesOrdenados.size(); i++) {
            Time t = timesOrdenados.get(i);
            String cor = "";
            String corPts = Cores.NEGRITO;

            // Lógica de cores baseada na posição
            if (i == 0) {
                cor = Cores.AMARELO; // Campeão
                corPts = cor;
            } else if (i == timesOrdenados.size()-1) {
                cor = Cores.VERMELHO; // Laterna do Campeonato
                corPts = cor;
            }

            String pontosFormatados = String.valueOf(t.getTotal_pontos());
            String qntVitoriasFormatados = String.valueOf(t.getQnt_vitorias());
            String qntEmpatesFormatados = String.valueOf(t.getQnt_empates());
            String qntDerrotasFormatadas = String.valueOf(t.getQnt_derrotas());
            String qntGolsProFormatados = String.valueOf(t.getQnt_golsPro());
            String qntGolsContraFormatados = String.valueOf(t.getQnt_golsContra());
            String saldoDeGolsFormatados = String.valueOf(t.getSaldoDeGols());

            System.out.printf("│ %s%-4s%s │ %s%-13s%s │ %s%4s%s │ %s%-3s%s │ %s%-3s%s │ %s%-3s%s │ %s%-3s%s │ %s%-3s%s │ %s%-3s%s │ %21s │\n", // %-13s garante que o nome do time ocupe exatamente 13 espaços
                    cor, (i + 1) + "º", Cores.RESET,
                    cor, (t.getNomeTime().length() > 13 ? t.getNomeTime().substring(0, 13) : t.getNomeTime()), Cores.RESET,
                    corPts, pontosFormatados, Cores.RESET,
                    cor, qntVitoriasFormatados, Cores.RESET,
                    cor, qntEmpatesFormatados, Cores.RESET,
                    cor, qntDerrotasFormatadas, Cores.RESET,
                    cor, qntGolsProFormatados, Cores.RESET,
                    cor, qntGolsContraFormatados, Cores.RESET,
                    cor, saldoDeGolsFormatados, Cores.RESET,
                    ultimasPartidas(cp,t)
            );
        }

        System.out.println("└──────┴───────────────┴──────┴─────┴─────┴─────┴─────┴─────┴─────┴───────────────────────┘");
    }
    public Time melhorDefesa(Campeonato cp) {

        Time melhor = cp.getTimes().getFirst(); // Assume inicialmente que o primeiro time é a melhor defesa

        for ( int i = 1; i < cp.getTimes().size(); i++ ) { // Percorre os demais times

            if ( cp.getTimes().get(i).getQnt_golsContra() < melhor.getQnt_golsContra() ) { // Testa se o time atual sofreu menos gols que o melhor atual
                melhor = cp.getTimes().get(i); // Atualiza referência
            }
        }

        return melhor; // Retorna o time que sofreu menos gols
    }
}

