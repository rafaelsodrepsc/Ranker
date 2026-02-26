package model;

public enum Resultado {

    VITORIA(3),  // (Valor associado) = Pontos por Vítoria
    EMPATE(1),   // (Valor associado) = Pontos por Empate
    DERROTA(0);  // (Valor associado) = Pontos por Derrota

    private final int pontosAssociados;  // Guarda quantos pontos um resultado qualquer vale

    Resultado( int pontosAssociados ) { this.pontosAssociados = pontosAssociados; }

    public int getPontosAssociados() { return this.pontosAssociados; }   // Retorna a pontuação associada ao resultado
}