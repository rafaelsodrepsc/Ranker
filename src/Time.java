public class Time {
    private String nome;
    private int pontos;
    
    public Time(String nome) {
        this.nome = nome;
        this.pontos = 0;
    }
    
    public void setPontos(Resultado resultado) {
        this.pontos += resultado.getPontuacao();
    }
    
    public int getPontos() {
        return pontos;
    }
    
    public String getNome() {
        return nome;
    }
    
    @Override
    public String toString() {
        return this.pontos + " - " + this.nome;
    }
}