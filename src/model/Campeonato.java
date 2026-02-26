package model;

import exception.TimeJaCadastradoException;
import exception.TimesInsuficientesException;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;              // Importa a interface List
import java.util.ArrayList;         // Importa a implementação ArrayList

public abstract class Campeonato {  // Classe abstrata (não pode ser instanciada diretamente)

    // Atributos do Campeonato
    protected String nome;
    protected List<Partida> confrontos = new ArrayList<>();
    protected List<Local> locais = new ArrayList<>();
    protected List<Time> times = new ArrayList<>();
    protected LocalDate dataDeInicio;
    protected int diasDeDescanso;

    public Campeonato( String nome, int diasDeDescanso, LocalDate dataDeInicio ) {
        this.nome = nome;
        this.diasDeDescanso = diasDeDescanso;
        this.dataDeInicio = dataDeInicio;
    }

    public void adicionarTime( Time time ) {
        boolean jaExiste = this.times.stream()
                .anyMatch(t -> t.getNomeTime().equals(time.getNomeTime()));
        if(jaExiste){ throw new TimeJaCadastradoException("Time ja cadastrado no campeonato"); }
        this.times.add(time);
    }
    public void adicionarLocal(Local local){
        this.locais.add(local);
    }

    public List<Local> getLocais() { return this.locais; }

    public String getNome() { return this.nome; }

    public LocalDate getDataDeInicio() { return this.dataDeInicio; }

    public int getDiasDeDescanso() { return this.diasDeDescanso; }

    public List<Time> getTimes() { return this.times; }

    public List<Partida> getConfrontos() { return this.confrontos; }

    public abstract void gerarConfrontos() throws TimesInsuficientesException;
}