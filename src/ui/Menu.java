package ui;

import exception.TimesInsuficientesException;
import model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;
import java.util.Scanner;

public class Menu {
    private Scanner scanner = new Scanner(System.in);
    private Campeonato campeonatoAtivo = null;

    public static void main(String[] args) {
        try {
            new Menu().iniciar();
        } catch (TimesInsuficientesException e) {
            System.err.println("Erro fatal: " + e.getMessage());
        }
    }

    public void iniciar() throws TimesInsuficientesException {
        while (true) {
            System.out.println("\n+++++ Ranker - Sistema de campeonatos +++++");
            System.out.println("1. Pontos Corridos");
            System.out.println("2. Mata-Mata (WIP)");
            System.out.println("0. Sair");

            System.out.print("Digite uma opção: ");
            int escolha = scanner.nextInt();
            scanner.nextLine();

            switch (escolha) {
                case 1:
                    loopPontosCorridos();
                    break;
                case 2:
                    System.out.println("Funcionalidade Mata-Mata ainda não implementada completamente.");
                    break;
                case 0:
                    System.out.println("Obrigado por utilizar nosso sistema!!");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    protected void loopPontosCorridos() {
        System.out.println("\n===== Informações iniciais ======");
        System.out.print("Digite o nome do seu Campeonato: ");
        String nome = scanner.nextLine();
        System.out.print("Dias de descanso: ");
        int diasDescanso = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Data de inicio (AAAA-MM-DD): ");
        LocalDate dataI = LocalDate.parse(scanner.nextLine());

        CampeonatoPontosCorridos campeonato = new CampeonatoPontosCorridos(nome, diasDescanso, dataI);

        while (true) {
            menuPontosCorridos();
            System.out.print("Digite uma opção: ");
            int escolha = scanner.nextInt();
            scanner.nextLine();

            switch (escolha) {
                case 1:
                    cadastrarTimeManual(campeonato);
                    break;
                case 2:
                    gerarDadosTeste(campeonato);
                    break;
                case 3:
                    try {
                        campeonato.gerarConfrontos();
                        System.out.println("Confrontos gerados com sucesso!");
                    } catch (TimesInsuficientesException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;
                case 4:
                    if (campeonato.getConfrontos() == null || campeonato.getConfrontos().isEmpty()) {
                        System.out.println("Nenhum confronto gerado ainda.");
                    } else {
                        System.out.println("--- Rodadas Agendadas ---");
                        campeonato.getConfrontos().forEach(System.out::println);
                    }
                    break;
                case 5:
                    simularCampeonato(campeonato);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    // Métodos auxiliares para manter o codigo limpo
    private void cadastrarTimeManual(CampeonatoPontosCorridos cp) {
        System.out.print("Nome do Time: ");
        String nomeTime = scanner.nextLine();
        System.out.print("Nome do Local: ");
        String localTime = scanner.nextLine();
        System.out.print("Horário Abertura (HH:MM): ");
        LocalTime horarioA = LocalTime.parse(scanner.nextLine());
        System.out.print("Horário Fechamento (HH:MM): ");
        LocalTime horarioF = LocalTime.parse(scanner.nextLine());

        cp.adicionarTime(new Time(nomeTime, new Local(localTime, horarioA, horarioF)));
    }

    private void gerarDadosTeste(CampeonatoPontosCorridos cp) {
        cp.adicionarTime(new Time("Flamengo", new Local("Maracanã", LocalTime.of(16, 0), LocalTime.of(22, 0))));
        cp.adicionarTime(new Time("Vasco", new Local("São Januário", LocalTime.of(15, 0), LocalTime.of(21, 0))));
        cp.adicionarTime(new Time("Palmeiras", new Local("Allianz Parque", LocalTime.of(14, 0), LocalTime.of(20, 0))));
        cp.adicionarTime(new Time("Corinthians", new Local("Neo Química", LocalTime.of(16, 0), LocalTime.of(22, 0))));
        System.out.println("Times de teste criados!");
    }

    private void simularCampeonato(CampeonatoPontosCorridos cp) {
        if (cp.getConfrontos() == null || cp.getConfrontos().isEmpty()) {
            System.out.println("Gere os confrontos (opção 3) antes de simular!");
            return;
        }
        Random random = new Random();
        for (Partida partida : cp.getConfrontos()) {
            partida.encerrarPartida(random.nextInt(5), random.nextInt(5));
            System.out.println(partida);
        }
        System.out.println("\nCampeonato simulado com sucesso!");
    }

    private void menuPontosCorridos() {
        System.out.println("\n++++++ Menu: Pontos Corridos +++++++");
        System.out.println("1. Cadastrar Time e Local");
        System.out.println("2. Gerar Dados Teste");
        System.out.println("3. Gerar Confrontos");
        System.out.println("4. Mostrar Confrontos");
        System.out.println("5. Simular campeonato inteiro");
        System.out.println("0. Voltar");
    }
}