package ui.util;

import javafx.scene.Scene;

public class TemaManager {
    private static boolean temaEscuro = false;
    private static Scene sceneAtual;

    public static void setScene(Scene scene) {
        sceneAtual = scene;
        aplicarTema();
    }

    public static void alternarTema() {
        temaEscuro = !temaEscuro;
        aplicarTema();
    }

    public static boolean isTemaEscuro() {
        return temaEscuro;
    }

    private static void aplicarTema() {
        if (sceneAtual == null) return;
        sceneAtual.getStylesheets().clear();
        String css = temaEscuro ? "/css/escuro.css" : "/css/claro.css";
        sceneAtual.getStylesheets().add(
                TemaManager.class.getResource(css).toExternalForm()
        );
    }
}