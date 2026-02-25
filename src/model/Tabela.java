package model;

import java.util.List;

public class Tabela {

    private List<Time> times;

    public Tabela(List<Time> times) {
        this.times = times;
    }

    // Metodo que organiza os times manualmente
    public void classificar() {

        for (int i = 0; i < times.size(); i++) {

            for (int j = i + 1; j < times.size(); j++) {

                Time t1 = times.get(i);
                Time t2 = times.get(j);

                // Critério 1: Pontos
                if (t2.getPontos() > t1.getPontos()) {
                    trocar(i, j);
                }

                // Critério 2: Vitórias
                else if (t2.getPontos() == t1.getPontos() &&
                        t2.getVitorias() > t1.getVitorias()) {
                    trocar(i, j);
                }

                // Critério 3: Saldo de gols
                else if (t2.getPontos() == t1.getPontos() &&
                        t2.getVitorias() == t1.getVitorias() &&
                        t2.getSaldoDeGols() > t1.getSaldoDeGols()) {
                    trocar(i, j);
                }
            }
        }
    }

    // Metodo auxiliar para trocar posições
    private void trocar(int i, int j) {

        Time temp = times.get(i);
        times.set(i, times.get(j));
        times.set(j, temp);
    }

    public List<Time> getClassificados(int quantidade) {
        classificar();
        return times.subList(0, quantidade);
    }
}