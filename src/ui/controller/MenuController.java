package ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import ui.util.TemaManager;

public class MenuController {

    @FXML private Button btnPontosCorridos;
    @FXML private Button btnMataMata;

    @FXML
    private void handlePontosCorridos() {
        abrirTela("/fxml/config.fxml", "PC");
    }
    @FXML
    private void handleMataMata() {
        abrirTela("/fxml/config.fxml", "MM");
    }

    private void abrirTela(String caminho, String tipo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            Scene scene = new Scene(loader.load());
            TemaManager.setScene(scene);  // ← adiciona aqui
            ConfigController controller = loader.getController();
            controller.setTipo(tipo);
            Stage stage = (Stage) btnPontosCorridos.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println("Erro ao abrir tela: " + e.getMessage());
        }
    }
    @FXML private javafx.scene.control.ToggleButton toggleTema;

    @FXML
    private void handleToggleTema() {
        TemaManager.alternarTema();
        toggleTema.setText(TemaManager.isTemaEscuro() ? "🌙 Escuro" : "☀ Claro");
    }
}