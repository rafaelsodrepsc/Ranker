public enum Resultado {
    VITORIA(3), 
    DERROTA(0), 
    EMPATE(1);
    
    private int pontuacao;
    private Resultado(int pontuacao) {
        this.pontuacao = pontuacao;
    }
    
    public void setPontuacao(int nova_pontuacao) {
        this.pontuacao = nova_pontuacao;
    }
    
    public int getPontuacao() {
        return this.pontuacao;
    }
}