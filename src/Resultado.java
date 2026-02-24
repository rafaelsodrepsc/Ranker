public enum Resultado {

    VITORIA(3),  // Vitória vale 3 pontos
    EMPATE(1),   // Empate vale 1 ponto
    DERROTA(0);  // Derrota vale 0 pontos

    private int pontuacao;
    // Guarda quantos pontos aquele resultado vale

    private Resultado(int pontuacao) {
        // Construtor do enum
        this.pontuacao = pontuacao;
    }

    public int getPontuacao() {
        // Retorna a pontuação associada ao resultado
        return pontuacao;
    }
}