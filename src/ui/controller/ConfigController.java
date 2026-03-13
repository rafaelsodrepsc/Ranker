package ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.CampeonatoMataMata;
import model.CampeonatoPontosCorridos;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Campeonato;
import model.CampeonatoPontosCorridos;
import model.CampeonatoMataMata;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ConfigController {

    @FXML private TextField txtNome;
    @FXML private TextField txtDiasDescanso;
    @FXML private TextField txtDataInicio;
    @FXML private TextField txtDataFim;
    @FXML private TextField txtNumTimes;

    @FXML
    private void handleConfirmar() {
        try {
            String nome = txtNome.getText();
            int dias = Integer.parseInt(txtDiasDescanso.getText());
            LocalDate dataI = LocalDate.parse(txtDataInicio.getText());
            LocalDate dataF = LocalDate.parse(txtDataFim.getText());
            int numTimes = Integer.parseInt(txtNumTimes.getText());

            if (dataI.isBefore(LocalDate.now())) {
                System.out.println("Data de início no passado!");
                return;
            }
            if (dataF.isBefore(dataI)) {
                System.out.println("Data de fim anterior à data de início!");
                return;
            }
            if (tipoCampeonato.equals("MM") && (numTimes & (numTimes - 1)) != 0) {
                System.out.println("Mata-Mata exige potência de 2!");
                return;
            }

            if (tipoCampeonato.equals("PC")) {
                CampeonatoPontosCorridos campeonato = new CampeonatoPontosCorridos(nome, dias, dataI);
                abrirTelaCampeonato(campeonato);
            } else {
                CampeonatoMataMata campeonato = new CampeonatoMataMata(nome, dias, dataI, dataF);
                abrirTelaCampeonato(campeonato);
            }

        } catch (NumberFormatException e) {
            System.out.println("Dias de descanso e número de times devem ser números!");
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido! Use AAAA-MM-DD");
        }
    }

    @FXML
    private void handleVoltar() {
        System.out.println("Voltar clicado!");
    }

    private String tipoCampeonato;

    public void setTipo(String tipo) {
        this.tipoCampeonato = tipo;
    }

    private void abrirTelaCampeonato(Campeonato campeonato) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/campeonato.fxml"));
            Scene scene = new Scene(loader.load());
            CampeonatoController controller = loader.getController();
            controller.setCampeonato(campeonato);
            Stage stage = (Stage) txtNome.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println("Erro ao abrir tela: " + e.getMessage());
        }
    }

}