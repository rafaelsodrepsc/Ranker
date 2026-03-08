package ui;

import exception.CampeonatoRuntimeException;
import exception.PenaltisEmpatadadosException;
import exception.TimesInsuficientesException;
import model.*;
import service.SchedulingService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;
import java.util.Scanner;

public class Menu {
    private Scanner scanner = new Scanner(System.in);

    public void iniciar() throws TimesInsuficientesException {
        while (true) {
            System.out.println("\n+++++ Ranker - Sistema de campeonatos +++++");
            System.out.println("1. Pontos Corridos");
            System.out.println("2. Mata-Mata");
            System.out.println("0. Sair");

            System.out.print("Digite uma opção: ");
            int escolha = scanner.nextInt();
            scanner.nextLine();

            switch (escolha) {
                case 1:
                    loopPontosCorridos();
                    break;
                case 2:
                    loopMataMata();
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
        System.out.print("Data de fim (AAAA-MM-DD): ");
        LocalDate dataF = LocalDate.parse(scanner.nextLine());

        CampeonatoPontosCorridos campeonato = new CampeonatoPontosCorridos(nome, diasDescanso, dataI);

        while (true) {
            menuPontosCorridos();
            System.out.print("Digite uma opção: ");
            int escolha = scanner.nextInt();
            scanner.nextLine();

            switch (escolha) {
                case 1:
                    cadastrarTime(campeonato);
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
                case 6:
                    campeonato.exibirTabela();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    protected void loopMataMata() throws TimesInsuficientesException {
        System.out.println("\n===== Informações iniciais ======");
        System.out.print("Digite o nome do seu Campeonato: ");
        String nome = scanner.nextLine();
        System.out.print("Dias de descanso: ");
        int diasDescanso = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Data de inicio (AAAA-MM-DD): ");
        LocalDate dataI = LocalDate.parse(scanner.nextLine());
        System.out.print("Data de fim (AAAA-MM-DD): ");
        LocalDate dataF = LocalDate.parse(scanner.nextLine());

        CampeonatoMataMata campeonato = new CampeonatoMataMata(nome, diasDescanso, dataI,dataF);
        int faseAtual = 1;

        while (true) {
            menuMataMata();
            System.out.print("Digite uma opção: ");
            int escolha = scanner.nextInt();
            scanner.nextLine();

            switch (escolha) {
                case 1:
                    cadastrarTimeMM(campeonato);
                    break;
                case 2:
                    cadastrarLocalMM(campeonato);
                    break;
                case 3:
                    gerarDadosTesteMM(campeonato);
                    break;
                case 4:
                    try {
                        campeonato.gerarConfrontos();
                        new SchedulingService().agendarPartidas(campeonato);
                        System.out.println("Confrontos gerados e agendados com sucesso!");
                    } catch (TimesInsuficientesException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }catch (CampeonatoRuntimeException e){
                        System.out.println("Erro no agendamento: " + e.getMessage());
                    }
                    break;
                case 5:
                    if (campeonato.getConfrontos() == null || campeonato.getConfrontos().isEmpty()) {
                        System.out.println("Nenhum confronto gerado ainda.");
                    } else {
                        System.out.println("\n--- Confrontos Agendados ---\n");
                        campeonato.getConfrontos().forEach(System.out::println);
                    }
                    break;
                case 6:
                    simularFase(campeonato,faseAtual);
                    boolean encerrado = campeonato.avancarFase(faseAtual);
                    faseAtual++;
                    if (encerrado){
                        return;
                    }else{
                        new SchedulingService().agendarPartidas(campeonato);
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    // Métodos auxiliares para manter o codigo limpo
    private void cadastrarTime(Campeonato cp) {
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
    private void cadastrarTimeMM(CampeonatoMataMata cp){
        System.out.print("Nome do Time: ");
        String nomeTime = scanner.nextLine();

        cp.adicionarTime(new Time(nomeTime,null));
    }
    private void cadastrarLocalMM(CampeonatoMataMata cp){
        System.out.print("Nome do Local: ");
        String localNome = scanner.nextLine();
        System.out.print("Horário Abertura (HH:MM): ");
        LocalTime horarioA = LocalTime.parse(scanner.nextLine());
        System.out.print("Horário Fechamento (HH:MM): ");
        LocalTime horarioF = LocalTime.parse(scanner.nextLine());

        cp.adicionarLocal(new Local(localNome,horarioA,horarioF));
    }
    private void gerarDadosTeste(CampeonatoPontosCorridos cp) {
        cp.adicionarTime(new Time("Flamengo", new Local("Maracanã", LocalTime.of(16, 0), LocalTime.of(22, 0))));
        cp.adicionarTime(new Time("Vasco", new Local("São Januário", LocalTime.of(15, 0), LocalTime.of(21, 0))));
        cp.adicionarTime(new Time("Palmeiras", new Local("Allianz Parque", LocalTime.of(14, 0), LocalTime.of(20, 0))));
        cp.adicionarTime(new Time("Corinthians", new Local("Neo Química", LocalTime.of(16, 0), LocalTime.of(22, 0))));
        System.out.println("Times de teste criados!");
    }

    private void gerarDadosTesteMM(CampeonatoMataMata cp) {
        cp.adicionarTime(new Time("Flamengo", null));
        cp.adicionarTime(new Time("Vasco", null));
        cp.adicionarTime(new Time("Sport", null));
        cp.adicionarTime(new Time("Palmeiras", null));
        cp.adicionarTime(new Time("Internacional", null));
        cp.adicionarTime(new Time("Gremio", null));
        cp.adicionarTime(new Time("Cruzeiro", null));
        cp.adicionarTime(new Time("Atletico Mineiro", null));

        cp.adicionarLocal(new Local("Maracanã", LocalTime.of(16, 0), LocalTime.of(22, 0)));
        cp.adicionarLocal(new Local("São Januário", LocalTime.of(15, 0), LocalTime.of(21, 0)));
        cp.adicionarLocal(new Local("Allianz Parque", LocalTime.of(14, 0), LocalTime.of(20, 0)));
        System.out.println("\nTimes e locais de teste criados!");
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

    private void simularFase(CampeonatoMataMata cp, int faseAtual) {
        if (cp.getConfrontos() == null || cp.getConfrontos().isEmpty()) {
            System.out.println("Gere os confrontos (opção 3) antes de simular!");
            return;
        }
        Random random = new Random();

        cp.getConfrontos().stream()
                .filter(partida -> partida.getRodadaAtual() == faseAtual)
                .forEach(partida -> {
                    int gols1 = random.nextInt(5);
                    int gols2 = random.nextInt(5);

                    partida.encerrarPartida(gols1,gols2);

                    if (gols1 == gols2){
                        int p1,p2;

                        do {
                            p1 = random.nextInt(6) + 3;
                            p2 = random.nextInt(6) + 3;
                        }while (p1 == p2);

                        try {
                            partida.encerrarPenaltis(p1,p2);
                        } catch (PenaltisEmpatadadosException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println(partida);
                });
                System.out.println("\nFase " + faseAtual + " simulada com sucesso!");
    }

    private void menuPontosCorridos() {
        System.out.println("\n++++++ Menu: Pontos Corridos +++++++");
        System.out.println("1. Cadastrar Time e Local");
        System.out.println("2. Gerar Dados Teste");
        System.out.println("3. Gerar Confrontos");
        System.out.println("4. Mostrar Confrontos");
        System.out.println("5. Simular campeonato inteiro");
        System.out.println("6. Exibir Tabela Geral");
        System.out.println("0. Voltar");
    }
    private void menuMataMata() {
        System.out.println("\n++++++ Menu: Mata Mata +++++++");
        System.out.println("1. Cadastrar Times");
        System.out.println("2. Cadastrar Locais");
        System.out.println("3. Gerar Dados Teste");
        System.out.println("4. Gerar Confrontos");
        System.out.println("5. Mostrar Confrontos");
        System.out.println("6. Simular Fase Atual");
        System.out.println("0. Voltar");
    }
}