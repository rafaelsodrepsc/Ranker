package model;

import java.util.List;

public class Tabela {

    private final List<Time> times;

    public Tabela(List<Time> times) {
        this.times = times;
    }

    public void classificar() { // Metodo que organiza os times manualmente

        for ( int i = 0; i < this.times.size(); i++ ) {

            for ( int j = i + 1; j < this.times.size(); j++ ) {

                Time t1 = this.times.get(i);
                Time t2 = this.times.get(j);

                // Critério 1: Pontos
                if ( t2.getTotal_pontos() > t1.getTotal_pontos() ) {
                    trocar(i, j);
                }

                // Critério 2: Vitórias
                else if ( t2.getTotal_pontos() == t1.getTotal_pontos() &&
                        t2.getQnt_vitorias() > t1.getQnt_vitorias() ) {
                    trocar(i, j);
                }

                // Critério 3: Saldo de gols
                else if ( t2.getTotal_pontos() == t1.getTotal_pontos() &&
                        t2.getQnt_vitorias() == t1.getQnt_vitorias() &&
                        t2.getSaldoDeGols() > t1.getSaldoDeGols() ) {
                    trocar(i, j);
                }
            }
        }
    }

    private void trocar( int i, int j ) { // Metodo auxiliar para trocar posições

        Time temp = this.times.get(i);
        this.times.set(i, this.times.get(j));
        this.times.set(j, temp);
    }

    public List<Time> getClassificados( int quantidade ) {
        classificar();
        return this.times.subList(0, quantidade);
    }
}