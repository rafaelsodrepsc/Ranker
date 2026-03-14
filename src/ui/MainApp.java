package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.util.TemaManager;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
        Scene scene = new Scene(loader.load());
        TemaManager.setScene(scene);  // ← aplica o tema inicial
        stage.setTitle("Ranker - Sistema de Campeonatos");
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}