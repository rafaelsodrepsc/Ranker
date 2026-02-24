import java.util.List;              // Importa a interface List
import java.util.ArrayList;         // Importa a implementação ArrayList

public abstract class Campeonato {  // Classe abstrata (não pode ser instanciada diretamente)

    protected List<Partida> confrontos = new ArrayList<>();
    // Lista que armazena todas as partidas do campeonato

    protected List<Local> locais_dos_confrontos = new ArrayList<>();
    // Lista com todos os estádios utilizados

    protected List<Time> times = new ArrayList<>();
    // Lista com todos os times participantes

    public void adicionarTime(Time time) {
        // Metodo para adicionar um time ao campeonato
        times.add(time);
    }

    public List<Time> getTimes() {
        // Retorna a lista de times
        return times;
    }

    public List<Partida> getConfrontos() {
        // Retorna a lista de partidas
        return confrontos;
    }
}