import java.time.LocalDate;

public class Partida {
    private StatusPartida status;
    private Time time1;
    private Time time2;
    private LocalDate dataDaPartida;
    private Local localDaPartida;
    
    public Partida(Time time1, Time time2, LocalDate data, Local local) {
        this.time1 = time1;
        this.time2 = time2;
        this.dataDaPartida = data;
        this.localDaPartida = local;
        this.status = status.AGENDADA;
    }
    
    public void definirPlacar(StatusPartida status) {
        this.status = status;
    }
    
    public StatusPartida getStatus() {
        return this.status;
    }
    
    @Override
    public String toString() {
        return this.data + ", " + this.local + " - " + 
               time1.getNome() + " vs " + time2.getNome() + 
               " (" + this.status + ")";
    }
}