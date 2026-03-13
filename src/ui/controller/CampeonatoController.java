package ui.controller;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.time.format.DateTimeParseException;
import javafx.scene.control.TextInputDialog;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.*;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Random;

import exception.PenaltisEmpatadadosException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import java.util.stream.Collectors;

import service.SchedulingService;
import exception.TimesInsuficientesException;
import exception.CampeonatoRuntimeException;

public class CampeonatoController {
    @FXML private Label lblNomeCampeonato;
    @FXML private Label lblTipoCampeonato;
    @FXML private Pane paneChaveamento;

    @FXML private Button btnCadastrarLocal;
    @FXML private Button btnExibirTabela;
    @FXML private Button btnSimular;
    @FXML private Button btnVoltar;

    @FXML private TableView<Time> tabelaClassificacao;
    @FXML private TableColumn<Time, String> colPosicao;
    @FXML private TableColumn<Time, String> colTime;
    @FXML private TableColumn<Time, Integer> colPontos;
    @FXML private TableColumn<Time, Integer> colVitorias;
    @FXML private TableColumn<Time, Integer> colEmpates;
    @FXML private TableColumn<Time, Integer> colDerrotas;
    @FXML private TableColumn<Time, Integer> colSaldo;

    private Campeonato campeonato;
    private int faseAtual = 1;

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
        lblNomeCampeonato.setText(campeonato.getNome());

        if (campeonato instanceof CampeonatoPontosCorridos) {
            lblTipoCampeonato.setText("Pontos Corridos");
            btnCadastrarLocal.setVisible(false);
            btnCadastrarLocal.setManaged(false);
        } else {
            lblTipoCampeonato.setText("Mata-Mata");
            btnExibirTabela.setVisible(false);
            btnExibirTabela.setManaged(false);
            tabelaClassificacao.setVisible(false);
            paneChaveamento.setVisible(true);
        }

        configurarTabela();
    }
    private void configurarTabela() {
        colPosicao.setCellValueFactory(data -> {
            int pos = tabelaClassificacao.getItems().indexOf(data.getValue()) + 1;
            return new SimpleStringProperty(pos + "º");
        });
        colTime.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNomeTime()));
        colPontos.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getTotal_pontos()).asObject());
        colVitorias.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQnt_vitorias()).asObject());
        colEmpates.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQnt_empates()).asObject());
        colDerrotas.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQnt_derrotas()).asObject());
        colSaldo.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSaldoDeGols()).asObject());
    }

    private void atualizarTabela() {
        if (!(campeonato instanceof CampeonatoPontosCorridos pc)) return;

        List<Time> timesOrdenados = pc.getTimes().stream()
                .sorted(Comparator.comparingInt(Time::getTotal_pontos)
                        .thenComparingInt(Time::getQnt_vitorias)
                        .thenComparingInt(Time::getSaldoDeGols)
                        .reversed())
                .collect(Collectors.toList());

        tabelaClassificacao.setItems(FXCollections.observableArrayList(timesOrdenados));
    }

    @FXML
    private void handleCadastrarTime() {
        if (campeonato instanceof CampeonatoPontosCorridos) {
            Dialog<Time> dialog = new Dialog<>();
            dialog.setTitle("Cadastrar Time");
            dialog.setHeaderText("Informações do Time");

            ButtonType confirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmar, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField txtNome = new TextField();
            TextField txtLocal = new TextField();
            TextField txtAbertura = new TextField();
            TextField txtFechamento = new TextField();

            grid.add(new Label("Nome do time:"), 0, 0);
            grid.add(txtNome, 1, 0);
            grid.add(new Label("Nome do local:"), 0, 1);
            grid.add(txtLocal, 1, 1);
            grid.add(new Label("Abertura (HH:MM):"), 0, 2);
            grid.add(txtAbertura, 1, 2);
            grid.add(new Label("Fechamento (HH:MM):"), 0, 3);
            grid.add(txtFechamento, 1, 3);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(btn -> {
                if (btn == confirmar) {
                    try {
                        String nome = txtNome.getText().trim();
                        String local = txtLocal.getText().trim();
                        LocalTime abertura = LocalTime.parse(txtAbertura.getText().trim());
                        LocalTime fechamento = LocalTime.parse(txtFechamento.getText().trim());

                        if (nome.isEmpty() || local.isEmpty()) return null;
                        if (fechamento.isBefore(abertura) || fechamento.equals(abertura)) return null;

                        return new Time(nome, new Local(local, abertura, fechamento));
                    } catch (DateTimeParseException e) {
                        return null;
                    }
                }
                return null;
            });

            dialog.showAndWait().ifPresent(time -> {
                campeonato.adicionarTime(time);
                System.out.println("Time adicionado: " + time.getNomeTime());
            });

        } else {
            TextInputDialog d = new TextInputDialog();
            d.setTitle("Cadastrar Time");
            d.setHeaderText("Novo Time");
            d.setContentText("Nome do time:");
            d.showAndWait().ifPresent(nome -> {
                if (!nome.trim().isEmpty()) {
                    campeonato.adicionarTime(new Time(nome.trim(), null));
                    System.out.println("Time adicionado: " + nome);
                }
            });
        }
    }
    @FXML
    private void handleCadastrarLocal() {
        Dialog<Local> dialog = new Dialog<>();
        dialog.setTitle("Cadastrar Local");
        dialog.setHeaderText("Informações do Local");

        ButtonType confirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField txtNome = new TextField();
        TextField txtAbertura = new TextField();
        TextField txtFechamento = new TextField();

        grid.add(new Label("Nome do local:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("Abertura (HH:MM):"), 0, 1);
        grid.add(txtAbertura, 1, 1);
        grid.add(new Label("Fechamento (HH:MM):"), 0, 2);
        grid.add(txtFechamento, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == confirmar) {
                try {
                    String nome = txtNome.getText().trim();
                    LocalTime abertura = LocalTime.parse(txtAbertura.getText().trim());
                    LocalTime fechamento = LocalTime.parse(txtFechamento.getText().trim());

                    if (nome.isEmpty()) return null;
                    if (fechamento.isBefore(abertura) || fechamento.equals(abertura)) return null;

                    return new Local(nome, abertura, fechamento);
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(local -> {
            campeonato.adicionarLocal(local);
            System.out.println("Local adicionado: " + local.getNome());
        });
    }
    @FXML
    private void handleDadosTeste() {
        if (campeonato instanceof CampeonatoPontosCorridos pc) {
            pc.adicionarTime(new Time("Flamengo", new Local("Maracanã", LocalTime.of(16, 0), LocalTime.of(22, 0))));
            pc.adicionarTime(new Time("Vasco", new Local("São Januário", LocalTime.of(15, 0), LocalTime.of(21, 0))));
            pc.adicionarTime(new Time("Palmeiras", new Local("Allianz Parque", LocalTime.of(14, 0), LocalTime.of(20, 0))));
            pc.adicionarTime(new Time("Corinthians", new Local("Neo Química", LocalTime.of(16, 0), LocalTime.of(22, 0))));
        } else if (campeonato instanceof CampeonatoMataMata mm) {
            mm.adicionarTime(new Time("Flamengo", null));
            mm.adicionarTime(new Time("Vasco", null));
            mm.adicionarTime(new Time("Sport", null));
            mm.adicionarTime(new Time("Palmeiras", null));
            mm.adicionarTime(new Time("Internacional", null));
            mm.adicionarTime(new Time("Gremio", null));
            mm.adicionarTime(new Time("Cruzeiro", null));
            mm.adicionarTime(new Time("Atletico Mineiro", null));
            mm.adicionarLocal(new Local("Maracanã", LocalTime.of(16, 0), LocalTime.of(22, 0)));
            mm.adicionarLocal(new Local("São Januário", LocalTime.of(15, 0), LocalTime.of(21, 0)));
            mm.adicionarLocal(new Local("Allianz Parque", LocalTime.of(14, 0), LocalTime.of(20, 0)));
        }
        System.out.println("Dados de teste adicionados!");
    }
    @FXML
    private void handleGerarConfrontos() {
        try {
            campeonato.gerarConfrontos();
            if (campeonato instanceof CampeonatoMataMata mm) {
                new SchedulingService().agendarPartidas(mm);
                desenharChaveamento();
            }
            System.out.println("Confrontos gerados com sucesso!");
        } catch (TimesInsuficientesException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (CampeonatoRuntimeException e) {
            System.out.println("Erro no agendamento: " + e.getMessage());
        }
    }
    @FXML
    private void handleMostrarConfrontos() {
        if (campeonato.getConfrontos() == null || campeonato.getConfrontos().isEmpty()) {
            System.out.println("Nenhum confronto gerado ainda.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        campeonato.getConfrontos().forEach(p -> sb.append(p.toString()).append("\n"));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confrontos");
        alert.setHeaderText(campeonato.getNome());
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }
    @FXML
    private void handleSimular() throws TimesInsuficientesException {
        if (campeonato.getConfrontos() == null || campeonato.getConfrontos().isEmpty()) {
            System.out.println("Gere os confrontos antes de simular!");
            return;
        }

        Random random = new Random();

        if (campeonato instanceof CampeonatoPontosCorridos) {
            campeonato.getConfrontos().forEach(p -> p.encerrarPartida(random.nextInt(5), random.nextInt(5)));
            atualizarTabela();
            System.out.println("Campeonato simulado!");
        }else if (campeonato instanceof CampeonatoMataMata mm) {
        mm.getConfrontos().stream()
                .filter(p -> p.getRodadaAtual() == faseAtual && p.getStatus() != StatusPartida.CONCLUIDA)
                .forEach(p -> {
                    int g1 = random.nextInt(5);
                    int g2 = random.nextInt(5);
                    p.encerrarPartida(g1, g2);

                    if (g1 == g2) {
                        int p1, p2;
                        do {
                            p1 = random.nextInt(6) + 3;
                            p2 = random.nextInt(6) + 3;
                        } while (p1 == p2);
                        try {
                            p.encerrarPenaltis(p1, p2);
                        } catch (PenaltisEmpatadadosException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        boolean encerrado = mm.avancarFase(faseAtual);
        faseAtual++;

        desenharChaveamento();

        if (!encerrado) {
            new SchedulingService().agendarPartidas(mm);
        } else {
            System.out.println("Campeonato encerrado!");
        }
    }
    }

    @FXML
    private void handleExibirTabela() {
        if (!(campeonato instanceof CampeonatoPontosCorridos pc)) {
            System.out.println("Tabela disponível apenas para Pontos Corridos!");
            return;
        }

        if (pc.getConfrontos() == null || pc.getConfrontos().isEmpty()) {
            System.out.println("Nenhum confronto gerado ainda.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        pc.getTimes().stream()
                .sorted(java.util.Comparator.comparingInt(model.Time::getTotal_pontos)
                        .thenComparingInt(model.Time::getQnt_vitorias)
                        .thenComparingInt(model.Time::getSaldoDeGols)
                        .reversed())
                .forEach(t -> sb.append(t.toString()).append("\n"));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tabela");
        alert.setHeaderText(campeonato.getNome());
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }

    @FXML
    private void handleVoltar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) btnVoltar.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println("Erro ao voltar: " + e.getMessage());
        }
    }
    private void desenharChaveamento() {
        paneChaveamento.getChildren().clear();

        int totalTimes = campeonato.getTimes().size();
        int numFases = (int) (Math.log(totalTimes) / Math.log(2));

        double alturaTotal = 560;
        double larguraColuna = 160;
        double alturaLabel = 25;
        double larguraLabel = 130;

        for (int fase = 1; fase <= numFases; fase++) {
            int confrontosDaFase = totalTimes / (int) Math.pow(2, fase);
            if (confrontosDaFase == 0) confrontosDaFase = 1;

            double espacamento = alturaTotal / confrontosDaFase;
            double x = (fase - 1) * larguraColuna + 10;

            int faseCapture = fase;
            List<Partida> partidasDaFase = campeonato.getConfrontos().stream()
                    .filter(p -> p.getRodadaAtual() == faseCapture)
                    .collect(Collectors.toList());

            for (int i = 0; i < partidasDaFase.size(); i++) {
                Partida partida = partidasDaFase.get(i);

                double yBase = espacamento * i;
                double yMandante = yBase + espacamento * 0.25 - alturaLabel / 2;
                double yVisitante = yBase + espacamento * 0.75 - alturaLabel / 2;
                double yMeio = yBase + espacamento * 0.5;

                System.out.println("Fase " + fase + " | Confronto " + i +
                        " | yMandante=" + yMandante +
                        " | yVisitante=" + yVisitante +
                        " | yMeio=" + yMeio +
                        " | x=" + x);

                // Labels
                Label lblMandante = criarLabel(partida.getTimeMandante().getNomeTime(), x, yMandante, larguraLabel, alturaLabel);
                Label lblVisitante = criarLabel(partida.getTimeVisitante().getNomeTime(), x, yVisitante, larguraLabel, alturaLabel);

                // Linha vertical conectando os dois
                Line linhaV = new Line(x + larguraLabel, yMandante + alturaLabel / 2, x + larguraLabel, yVisitante + alturaLabel / 2);
                linhaV.setStroke(javafx.scene.paint.Color.GRAY);

                // ← substitui a linhaH
                Line linhaH = new Line(
                        x + larguraLabel,
                        yMeio,
                        x + larguraColuna,
                        yMeio
                );
                linhaH.setStroke(javafx.scene.paint.Color.GRAY);

                // Destaca vencedor
                if (partida.getStatus() == StatusPartida.CONCLUIDA) {
                    Time vencedor = partida.getVencedor();
                    if (vencedor == partida.getTimeMandante()) {
                        lblMandante.setStyle("-fx-border-color: green; -fx-padding: 4; -fx-font-weight: bold; -fx-background-color: #e8f5e9;");
                    } else {
                        lblVisitante.setStyle("-fx-border-color: green; -fx-padding: 4; -fx-font-weight: bold; -fx-background-color: #e8f5e9;");
                    }
                }

                paneChaveamento.getChildren().addAll(lblMandante, lblVisitante, linhaV, linhaH);
            }
        }

        // Label do campeão na última coluna
        double xCampeao = numFases * larguraColuna + 10;
        double yFinalMeio = alturaTotal / 2 - alturaLabel / 2;

        campeonato.getConfrontos().stream()
                .filter(p -> p.getRodadaAtual() == numFases && p.getStatus() == StatusPartida.CONCLUIDA)
                .findFirst()
                .ifPresent(ultima -> {
                    Label lblCampeao = criarLabel("🏆 " + ultima.getVencedor().getNomeTime(), xCampeao, yFinalMeio, larguraLabel, alturaLabel);
                    lblCampeao.setStyle("-fx-border-color: gold; -fx-padding: 4; -fx-font-weight: bold; -fx-background-color: #fff9c4;");
                    paneChaveamento.getChildren().add(lblCampeao);
                });
    }

    private Label criarLabel(String texto, double x, double y, double largura, double altura) {
        Label label = new Label(texto);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setPrefWidth(largura);
        label.setPrefHeight(altura);
        label.setStyle("-fx-border-color: gray; -fx-padding: 4;");
        return label;
    }
}