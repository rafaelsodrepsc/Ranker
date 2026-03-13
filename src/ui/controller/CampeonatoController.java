package ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Campeonato;
import model.CampeonatoMataMata;
import model.CampeonatoPontosCorridos;
import model.Time;
import model.Local;
import java.time.LocalTime;
import service.SchedulingService;
import exception.TimesInsuficientesException;
import exception.CampeonatoRuntimeException;

public class CampeonatoController {

    @FXML private Label lblNomeCampeonato;
    @FXML private Label lblTipoCampeonato;

    @FXML private Button btnCadastrarLocal;
    @FXML private Button btnExibirTabela;
    @FXML private Button btnSimular;
    @FXML private Button btnVoltar;

    private Campeonato campeonato;

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
        lblNomeCampeonato.setText(campeonato.getNome());

        if (campeonato instanceof CampeonatoPontosCorridos) {
            lblTipoCampeonato.setText("Pontos Corridos");
            btnCadastrarLocal.setVisible(false);
        } else {
            lblTipoCampeonato.setText("Mata-Mata");
            btnExibirTabela.setVisible(false);
        }
    }

    @FXML private void handleCadastrarTime() { System.out.println("Cadastrar Time"); }
    @FXML private void handleCadastrarLocal() { System.out.println("Cadastrar Local"); }
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
    @FXML private void handleSimular() { System.out.println("Simular"); }
    @FXML private void handleExibirTabela() { System.out.println("Exibir Tabela"); }

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
}