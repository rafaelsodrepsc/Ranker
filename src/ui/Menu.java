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
            menuPrincipal();
            int escolha = lerInteiro("  Opção: ");

            switch (escolha) {
                case 1: loopPontosCorridos(); break;
                case 2: loopMataMata(); break;
                case 0:
                    imprimirSucesso("Obrigado por utilizar o Ranker!");
                    return;
                default:
                    imprimirErro("Opção inválida!");
            }
        }
    }

    protected void loopPontosCorridos() {
        imprimirCabecalho("INFORMAÇÕES INICIAIS");
        System.out.print("  Nome do Campeonato: ");
        String nome = scanner.nextLine();

        int diasDescanso = lerInteiroDias("  Dias de descanso (1-7): ");
        LocalDate dataI = lerData("  Data de início (AAAA-MM-DD): ");
        LocalDate dataF = lerDataFim("  Data de fim (AAAA-MM-DD): ", dataI);
        int numTimes = lerInteiroPC("  Quantidade de times: ", diasDescanso, dataI, dataF);

        CampeonatoPontosCorridos campeonato = new CampeonatoPontosCorridos(nome, diasDescanso, dataI);

        while (true) {
            menuPontosCorridos();
            int escolha = lerInteiro("  Opção: ");

            switch (escolha) {
                case 1: cadastrarTime(campeonato); break;
                case 2: gerarDadosTeste(campeonato); break;
                case 3:
                    try {
                        campeonato.gerarConfrontos();
                        imprimirSucesso("Confrontos gerados com sucesso!");
                    } catch (TimesInsuficientesException e) {
                        imprimirErro(e.getMessage());
                    }
                    break;
                case 4:
                    if (campeonato.getConfrontos() == null || campeonato.getConfrontos().isEmpty()) {
                        imprimirInfo("Nenhum confronto gerado ainda.");
                    } else {
                        imprimirCabecalho("CONFRONTOS AGENDADOS");
                        campeonato.getConfrontos().forEach(p -> System.out.println("  " + p));
                        imprimirSeparador();
                    }
                    break;
                case 5: simularCampeonato(campeonato); break;
                case 6: RelatorioService.exibirTabela(campeonato); break;
                case 0: return;
                default: imprimirErro("Opção inválida.");
            }
        }
    }

    protected void loopMataMata() {
        imprimirCabecalho("INFORMAÇÕES INICIAIS");
        System.out.print("  Nome do Campeonato: ");
        String nome = scanner.nextLine();

        int diasDescanso = lerInteiroDias("  Dias de descanso (1-7): ");
        LocalDate dataI = lerData("  Data de início (AAAA-MM-DD): ");
        LocalDate dataF = lerDataFim("  Data de fim (AAAA-MM-DD): ", dataI);
        int numTimes = lerInteiroMM("  Quantidade de times (potência de 2): ", diasDescanso, dataI, dataF);

        CampeonatoMataMata campeonato = new CampeonatoMataMata(nome, diasDescanso, dataI, dataF);
        int faseAtual = 1;

        while (true) {
            menuMataMata();
            int escolha = lerInteiro("  Opção: ");

            switch (escolha) {
                case 1: cadastrarTimeMM(campeonato); break;
                case 2: cadastrarLocalMM(campeonato); break;
                case 3: gerarDadosTesteMM(campeonato); break;
                case 4:
                    try {
                        campeonato.gerarConfrontos();
                        new SchedulingService().agendarPartidas(campeonato);
                        imprimirSucesso("Confrontos gerados e agendados com sucesso!");
                    } catch (TimesInsuficientesException | CampeonatoRuntimeException e) {
                        imprimirErro(e.getMessage());
                    }
                    break;
                case 5:
                    if (campeonato.getConfrontos() == null || campeonato.getConfrontos().isEmpty()) {
                        imprimirInfo("Nenhum confronto gerado ainda.");
                    } else {
                        imprimirCabecalho("CONFRONTOS AGENDADOS");
                        campeonato.getConfrontos().forEach(p -> System.out.println("  " + p));
                        imprimirSeparador();
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
                            imprimirErro(e.getMessage());
                        }
                    }
                    break;
                case 0: return;
                default: imprimirErro("Opção inválida.");
            }
        }
    }

    private void cadastrarTime(Campeonato cp) {
        imprimirCabecalho("CADASTRAR TIME");
        System.out.print("  Nome do Time: ");
        String nomeTime = scanner.nextLine();
        System.out.print("  Nome do Local: ");
        String localTime = scanner.nextLine();
        LocalTime horarioA = lerHorario("  Horário Abertura (HH:MM): ");
        LocalTime horarioF = lerHorarioFechamento("  Horário Fechamento (HH:MM): ", horarioA);
        cp.adicionarTime(new Time(nomeTime, new Local(localTime, horarioA, horarioF)));
        imprimirSucesso("Time cadastrado com sucesso!");
    }

    private void cadastrarTimeMM(CampeonatoMataMata cp) {
        imprimirCabecalho("CADASTRAR TIME");
        System.out.print("  Nome do Time: ");
        String nomeTime = scanner.nextLine();
        cp.adicionarTime(new Time(nomeTime, null));
        imprimirSucesso("Time cadastrado com sucesso!");
    }

    private void cadastrarLocalMM(CampeonatoMataMata cp) {
        imprimirCabecalho("CADASTRAR LOCAL");
        System.out.print("  Nome do Local: ");
        String localNome = scanner.nextLine();
        LocalTime horarioA = lerHorario("  Horário Abertura (HH:MM): ");
        LocalTime horarioF = lerHorarioFechamento("  Horário Fechamento (HH:MM): ", horarioA);
        cp.adicionarLocal(new Local(localNome, horarioA, horarioF));
        imprimirSucesso("Local cadastrado com sucesso!");
    }

    private void gerarDadosTeste(CampeonatoPontosCorridos cp) {
        cp.adicionarTime(new Time("Flamengo",    new Local("Maracanã",       LocalTime.of(16, 0), LocalTime.of(22, 0))));
        cp.adicionarTime(new Time("Vasco",       new Local("São Januário",   LocalTime.of(15, 0), LocalTime.of(21, 0))));
        cp.adicionarTime(new Time("Palmeiras",   new Local("Allianz Parque", LocalTime.of(14, 0), LocalTime.of(20, 0))));
        cp.adicionarTime(new Time("Corinthians", new Local("Neo Química",    LocalTime.of(16, 0), LocalTime.of(22, 0))));
        imprimirSucesso("4 times de teste criados!");
    }

    private void gerarDadosTesteMM(CampeonatoMataMata cp) {
        cp.adicionarTime(new Time("Flamengo",         null));
        cp.adicionarTime(new Time("Vasco",            null));
        cp.adicionarTime(new Time("Sport",            null));
        cp.adicionarTime(new Time("Palmeiras",        null));
        cp.adicionarTime(new Time("Internacional",    null));
        cp.adicionarTime(new Time("Grêmio",           null));
        cp.adicionarTime(new Time("Cruzeiro",         null));
        cp.adicionarTime(new Time("Atlético Mineiro", null));
        cp.adicionarLocal(new Local("Maracanã",       LocalTime.of(16, 0), LocalTime.of(22, 0)));
        cp.adicionarLocal(new Local("São Januário",   LocalTime.of(15, 0), LocalTime.of(21, 0)));
        cp.adicionarLocal(new Local("Allianz Parque", LocalTime.of(14, 0), LocalTime.of(20, 0)));
        imprimirSucesso("8 times e 3 locais de teste criados!");
    }

    private void simularCampeonato(CampeonatoPontosCorridos cp) {
        if (cp.getConfrontos() == null || cp.getConfrontos().isEmpty()) {
            imprimirInfo("Gere os confrontos (opção 3) antes de simular!");
            return;
        }
        imprimirCabecalho("SIMULAÇÃO DO CAMPEONATO");
        Random random = new Random();
        for (Partida partida : cp.getConfrontos()) {
            partida.encerrarPartida(random.nextInt(5), random.nextInt(5));
            System.out.println("  " + partida);
        }
        imprimirSeparador();
        imprimirSucesso("Campeonato simulado com sucesso!");
    }

    private void simularFase(CampeonatoMataMata cp, int faseAtual) {
        if (cp.getConfrontos() == null || cp.getConfrontos().isEmpty()) {
            imprimirInfo("Gere os confrontos (opção 4) antes de simular!");
            return;
        }
        imprimirCabecalho("FASE " + faseAtual);
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
                    System.out.println("  " + partida);
                });
        imprimirSeparador();
        imprimirSucesso("Fase " + faseAtual + " simulada com sucesso!");
    }

    private void menuPrincipal() {
        System.out.println();
        imprimirCabecalho("RANKER — SISTEMA DE CAMPEONATOS");
        System.out.println("  1. Pontos Corridos");
        System.out.println("  2. Mata-Mata");
        imprimirSeparador();
        System.out.println("  0. Sair");
        imprimirSeparador();
    }

    private void menuPontosCorridos() {
        System.out.println();
        imprimirCabecalho("PONTOS CORRIDOS");
        System.out.println("  1. Cadastrar Time e Local");
        System.out.println("  2. Gerar Dados Teste");
        System.out.println("  3. Gerar Confrontos");
        System.out.println("  4. Mostrar Confrontos");
        System.out.println("  5. Simular Campeonato");
        System.out.println("  6. Exibir Tabela");
        imprimirSeparador();
        System.out.println("  0. Voltar");
        imprimirSeparador();
    }

    private void menuMataMata() {
        System.out.println();
        imprimirCabecalho("MATA-MATA");
        System.out.println("  1. Cadastrar Times");
        System.out.println("  2. Cadastrar Locais");
        System.out.println("  3. Gerar Dados Teste");
        System.out.println("  4. Gerar Confrontos");
        System.out.println("  5. Mostrar Confrontos");
        System.out.println("  6. Simular Fase Atual");
        imprimirSeparador();
        System.out.println("  0. Voltar");
        imprimirSeparador();
    }

    private int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                int valor = scanner.nextInt();
                scanner.nextLine();
                if (valor >= 0) return valor;
                imprimirErro("Digite um número positivo!");
            } catch (InputMismatchException e) {
                scanner.nextLine();
                imprimirErro("Digite um número válido!");
            }
        }
    }

    private int lerInteiroDias(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                int valor = scanner.nextInt();
                scanner.nextLine();
                if (valor >= 1 && valor <= 7) return valor;
                imprimirErro("Dias de descanso deve ser entre 1 e 7!");
            } catch (InputMismatchException e) {
                scanner.nextLine();
                imprimirErro("Digite um número válido!");
            }
        }
    }

    private int lerInteiroPC(String mensagem, int diasDescanso, LocalDate dataI, LocalDate dataF) {
        while (true) {
            System.out.print(mensagem);
            try {
                int valor = scanner.nextInt();
                scanner.nextLine();
                if (valor < 2) {
                    imprimirErro("Mínimo de 2 times!");
                    continue;
                }
                int timesEfetivos = (valor % 2 != 0) ? valor + 1 : valor;
                int numRodadas = (timesEfetivos - 1) * 2;
                int diasNecessarios = numRodadas * diasDescanso;
                if (dataF.isBefore(dataI.plusDays(diasNecessarios))) {
                    imprimirErro("Período insuficiente para " + valor + " times! Necessário " + diasNecessarios + " dias.");
                    continue;
                }
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                imprimirErro("Digite um número válido!");
            }
        }
    }

    private int lerInteiroMM(String mensagem, int diasDescanso, LocalDate dataI, LocalDate dataF) {
        while (true) {
            System.out.print(mensagem);
            try {
                int valor = scanner.nextInt();
                scanner.nextLine();
                if (valor < 2 || (valor & (valor - 1)) != 0) {
                    imprimirErro("Mata-Mata exige potência de 2! (2, 4, 8, 16...)");
                    continue;
                }
                int numFases = (int) (Math.log(valor) / Math.log(2));
                int diasNecessarios = numFases * diasDescanso;
                if (dataF.isBefore(dataI.plusDays(diasNecessarios))) {
                    imprimirErro("Período insuficiente para " + valor + " times! Necessário " + diasNecessarios + " dias.");
                    continue;
                }
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                imprimirErro("Digite um número válido!");
            }
        }
    }

    private LocalDate lerData(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                LocalDate data = LocalDate.parse(scanner.nextLine());
                if (data.isBefore(LocalDate.now())) {
                    imprimirInfo("Data inválida! Não pode ser no passado.");
                } else {
                    return data;
                }
            } catch (DateTimeParseException e) {
                imprimirErro("Formato inválido! Use AAAA-MM-DD");
            }
        }
    }

    private LocalDate lerDataFim(String mensagem, LocalDate dataInicio) {
        while (true) {
            System.out.print(mensagem);
            try {
                LocalDate data = LocalDate.parse(scanner.nextLine());
                if (data.isBefore(LocalDate.now())) {
                    imprimirInfo("Data inválida! Não pode ser no passado.");
                } else if (!data.isAfter(dataInicio)) {
                    imprimirInfo("Data inválida! Não pode ser anterior ou igual à data de início.");
                } else if (!data.isBefore(dataInicio.plusYears(1))) {
                    imprimirInfo("Campeonato não pode durar mais de 1 ano!");
                } else {
                    return data;
                }
            } catch (DateTimeParseException e) {
                imprimirErro("Formato inválido! Use AAAA-MM-DD");
            }
        }
    }

    private LocalTime lerHorario(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return LocalTime.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                imprimirErro("Formato inválido! Use HH:MM");
            }
        }
    }

    private LocalTime lerHorarioFechamento(String mensagem, LocalTime abertura) {
        while (true) {
            System.out.print(mensagem);
            try {
                LocalTime horario = LocalTime.parse(scanner.nextLine());
                if (horario.isBefore(abertura) || horario.equals(abertura)) {
                    imprimirInfo("Horário de fechamento deve ser depois da abertura!");
                } else if (horario.isBefore(abertura.plusHours(2))) {
                    imprimirInfo("Intervalo mínimo de 2 horas entre abertura e fechamento!");
                } else {
                    return horario;
                }
            } catch (DateTimeParseException e) {
                imprimirErro("Formato inválido! Use HH:MM");
            }
        }
    }

    private void imprimirCabecalho(String titulo) {
        int largura = 45;
        String linha = Cores.NEGRITO + "=".repeat(largura) + Cores.RESET;
        int espacos = (largura - titulo.length()) / 2;
        String tituloCentralizado = " ".repeat(Math.max(0, espacos)) + Cores.NEGRITO + titulo + Cores.RESET;
        System.out.println(linha);
        System.out.println(tituloCentralizado);
        System.out.println(linha);
    }

    private void imprimirSeparador() {
        System.out.println("-".repeat(45));
    }

    private void imprimirSucesso(String mensagem) {
        System.out.println(Cores.VERDE + "[ ✓ ] " + mensagem + Cores.RESET);
    }

    private void imprimirErro(String mensagem) {
        System.out.println(Cores.VERMELHO + "[ ✗ ] " + mensagem + Cores.RESET);
    }

    private void imprimirInfo(String mensagem) {
        System.out.println(Cores.AMARELO + "[ ! ] " + mensagem + Cores.RESET);
    }
}