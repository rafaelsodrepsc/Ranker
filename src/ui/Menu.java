package ui;

import exception.CampeonatoRuntimeException;
import exception.PenaltisEmpatadadosException;
import exception.TimesInsuficientesException;
import model.*;
import service.SchedulingService;
import service.RelatorioService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
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

            int escolha = lerInteiro("Digite uma opção: ");

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
                    System.out.println(Cores.VERMELHO + "[Erro1] " + Cores.RESET
                            + "Opção inválida!");
            }
        }
    }

    protected void loopPontosCorridos() {
        System.out.println("\n===== Informações iniciais ======");
        System.out.print("Digite o nome do seu Campeonato: ");
        String nome = scanner.nextLine();
        int diasDescanso = 0;
        LocalDate dataI = null;
        LocalDate dataF;
        int numTimes;

        var flag = false;
        while (!flag) { // loop de validação das entradas
            diasDescanso = lerInteiro("Dias de descanso: ");
            dataI = lerData("Data de inicio (AAAA-MM-DD): ");
            dataF = lerData("Data de término (AAAA-MM-DD): ");
            numTimes = lerInteiro("Qual a quantidade de times desejados: ");
            if (validarDadosCampeonato(diasDescanso, dataI, dataF, numTimes, false)) {flag = true;}
        }

        CampeonatoPontosCorridos campeonato = new CampeonatoPontosCorridos(nome, diasDescanso, dataI);

        while (true) {
            menuPontosCorridos();
            int escolha = lerInteiro("Digite uma opção: ");

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
                        System.out.println(Cores.VERMELHO + "[Erro2] " + Cores.RESET
                                +  e.getMessage());
                    }
                    break;
                case 4:
                    if (campeonato.getConfrontos() == null || campeonato.getConfrontos().isEmpty()) {
                        System.out.println(Cores.VERMELHO + "[Erro3] " + Cores.RESET
                                + "Nenhum confronto gerado ainda.");
                    } else {
                        System.out.println("--- Rodadas Agendadas ---");
                        campeonato.getConfrontos().forEach(System.out::println);
                    }
                    break;
                case 5:
                    simularCampeonato(campeonato);
                    break;
                case 6:
                    RelatorioService.exibirTabela(campeonato);
                    break;
                case 0:
                    return;
                default:
                    System.out.println(Cores.VERMELHO + "[Erro1] " + Cores.RESET
                            + "Opção inválida.");
            }
        }
    }

    protected void loopMataMata() {
        System.out.println("\n===== Informações iniciais ======");
        System.out.print("Digite o nome do seu Campeonato: ");
        String nome = scanner.nextLine();
        int diasDescanso = 0;
        LocalDate dataI = LocalDate.now();
        LocalDate dataF = LocalDate.now();
        int numTimes = 0;

        var flag = false;
        while (!flag) { // loop de validação das entradas
            diasDescanso = lerInteiro("Dias de descanso: ");
            dataI = lerData("Data de inicio (AAAA-MM-DD): ");
            dataF = lerData("Data de término (AAAA-MM-DD): ");
            numTimes = lerInteiro("Qual a quantidade de times desejados: ");
            if (validarDadosCampeonato(diasDescanso, dataI, dataF, numTimes, true)) {
                flag = true;
            }
        }

        CampeonatoMataMata campeonato = new CampeonatoMataMata(nome, diasDescanso, dataI, dataF);
        int faseAtual = 1;

        while (true) {
            menuMataMata();
            int escolha = lerInteiro("Digite uma opção: ");

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
                        System.out.println(Cores.VERMELHO + "[Erro2] " + Cores.RESET
                                +  e.getMessage());
                    } catch (CampeonatoRuntimeException e) {
                        System.out.println(Cores.VERMELHO + "[Erro4] " + Cores.RESET
                                +  e.getMessage());
                    }
                    break;
                case 5:
                    if (campeonato.getConfrontos() == null || campeonato.getConfrontos().isEmpty()) {
                        System.out.println(Cores.VERMELHO + "[Erro3] " + Cores.RESET
                                + "Nenhum confronto gerado ainda.");
                    } else {
                        System.out.println("\n--- Confrontos Agendados ---\n");
                        campeonato.getConfrontos().forEach(System.out::println);
                    }
                    break;
                case 6:
                    simularFase(campeonato, faseAtual);
                    boolean encerrado = campeonato.avancarFase(faseAtual);
                    faseAtual++;
                    if (encerrado) {
                        return;
                    } else {
                        try {
                            new SchedulingService().agendarPartidas(campeonato);
                        } catch (TimesInsuficientesException | CampeonatoRuntimeException e) {
                            System.out.println(Cores.VERMELHO + "[Erro5] " + Cores.RESET
                                    +  e.getMessage());
                        }
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println(Cores.VERMELHO + "[Erro1] " + Cores.RESET
                            + "Opção inválida.");
            }
        }
    }

    // Métodos auxiliares para manter o codigo limpo
    private void cadastrarTime(Campeonato cp) {
        System.out.print("Nome do Time: ");
        String nomeTime = scanner.nextLine();
        System.out.print("Nome do Local: ");
        String localTime = scanner.nextLine();

        LocalTime horarioA = lerHorario("Horário Abertura (HH:MM): ");
        LocalTime horarioF = validarDadosCadastramento(horarioA);

        cp.adicionarTime(new Time(nomeTime, new Local(localTime, horarioA, horarioF)));
    }

    private void cadastrarTimeMM(CampeonatoMataMata cp) {
        System.out.print("Nome do Time: ");
        String nomeTime = scanner.nextLine();

        cp.adicionarTime(new Time(nomeTime, null));
    }

    private void cadastrarLocalMM(CampeonatoMataMata cp) {
        System.out.print("Nome do Local: ");
        String localNome = scanner.nextLine();

        LocalTime horarioA = lerHorario("Horário Abertura (HH:MM): ");
        LocalTime horarioF = validarDadosCadastramento(horarioA);

        cp.adicionarLocal(new Local(localNome, horarioA, horarioF));
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
            System.out.println(Cores.VERMELHO + "[Erro6] " + Cores.RESET
                    + "Gere os confrontos (opção 3) antes de simular!");
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
            System.out.println(Cores.VERMELHO + "[Erro6] " + Cores.RESET
                    + "Gere os confrontos (opção 3) antes de simular!");
            return;
        }
        Random random = new Random();

        cp.getConfrontos().stream()
                .filter(partida -> partida.getRodadaAtual() == faseAtual)
                .forEach(partida -> {
                    int gols1 = random.nextInt(5);
                    int gols2 = random.nextInt(5);

                    partida.encerrarPartida(gols1, gols2);

                    if (gols1 == gols2) {
                        int p1, p2;

                        do {
                            p1 = random.nextInt(6) + 3;
                            p2 = random.nextInt(6) + 3;
                        } while (p1 == p2);

                        try {
                            partida.encerrarPenaltis(p1, p2);
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

    private int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                int valor = scanner.nextInt();
                scanner.nextLine();
                if (valor >= 0) {
                    return valor;
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println(Cores.VERMELHO + "[Erro7] " + Cores.RESET + "Valor inválido. Digite um número inteiro positivo.");
            }
        }
    }

    private LocalDate lerData(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                LocalDate data = LocalDate.parse(scanner.nextLine());
                if (data.isBefore(LocalDate.now())) {
                    System.out.println(Cores.VERMELHO + "[Erro8] " + Cores.RESET + "Esta data esta no passado. Digite uma data válida");
                } else {
                    return data;
                }
            } catch (DateTimeParseException e) {
                System.out.println(Cores.VERMELHO + "[Erro9] " + Cores.RESET + "Formato inválido! Use AAAA-MM-DD");
            }
        }
    }

    private LocalTime lerHorario(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return LocalTime.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println(Cores.VERMELHO + "[Erro10] " + Cores.RESET + "Formato inválido! Use HH:MM");
            }
        }
    }

    public boolean validarDadosCampeonato(int qdiasd, LocalDate dI, LocalDate dF, int qtimes, boolean isMM) {
        String erro = "";
        String initialError = Cores.VERMELHO + "[Erro";
        int numFases = (int) (Math.log(qtimes) / Math.log(2));
        int diasNecessarios = numFases * qdiasd;

        if (qdiasd < 0 || qdiasd > 7) {erro += initialError + "11]: " + Cores.RESET + " Dias de descanso fora do limite (de até 7 dias).\n";}
        else if (dF.isBefore(dI.plusDays(diasNecessarios))) {erro += initialError + "12]: " + Cores.RESET + " Periodo de tempo insuficiente\n";}
        if ((qtimes & (qtimes - 1)) != 0 && isMM) {erro += initialError + "13]: " + Cores.RESET
                + " Mata-Mata exige que a qnt. de times seja uma potência de 2! (2, 4, 8, 16...)\n";}
        if (!dF.isAfter(dI) || !dF.isBefore(dI.plusYears(1))) {erro += initialError + "14]: " + Cores.RESET
                + " Data de término inválida.\n";}

        if (!erro.isEmpty()) {
            erro += "\nDigite os dados novamente\n";
            System.out.print(erro);
            return false;
        }
        return true;
    }

    public LocalTime validarDadosCadastramento(LocalTime horaI) {
        boolean horaOK = false;
        LocalTime horaTemp = lerHorario("Horário de Fechamento (HH:MM): ");
        while (!horaOK) {
            LocalTime limite = horaI.plusHours(9); // Caso padrão: o intervalo está dentro do mesmo dia
            if (limite.isAfter(horaI) && horaTemp.isAfter(horaI) && horaTemp.isBefore(limite)) {
                horaOK = true;
            } else if (horaTemp.isAfter(horaI) || horaTemp.isBefore(limite)) {
                // Caso de virada: o intervalo cruza a meia-noite
                horaOK = true;
            }
            else {
                String erro = Cores.VERMELHO + "[Erro15] " + Cores.RESET
                        + "Horário fora do escopo (de até 8 hrs após o início). Digite novamente\n";
                System.out.println(erro);
                horaTemp = lerHorario("Horário de Fechamento (HH:MM): ");
            }
        }
        return horaTemp;
    }
}