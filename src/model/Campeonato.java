package model;

import exception.TimeJaCadastradoException;
import exception.TimesInsuficientesException;

import java.time.LocalDate;
import java.util.List;              // Importa a interface List
import java.util.ArrayList;         // Importa a implementação ArrayList

public abstract class Campeonato {  // Classe abstrata (não pode ser instanciada diretamente)

    protected List<Partida> confrontos = new ArrayList<>();
    protected List<Local> locais= new ArrayList<>();
    protected List<Time> times = new ArrayList<>();
    protected String nome;
    protected LocalDate dataDeInicio;
    protected int diasDeDescanso;

    public Campeonato(String nome, int diasDeDescanso, LocalDate dataDeInicio) {
        this.nome = nome;
        this.diasDeDescanso = diasDeDescanso;
        this.dataDeInicio = dataDeInicio;
    }

    public void adicionarTime(Time time) {
        boolean jaExiste = times.stream()
                .anyMatch(t -> t.getNome().equals(time.getNome()));
        if(jaExiste){
            throw new TimeJaCadastradoException("Time ja cadastrado no campeonato");
        }
        times.add(time);
    }
    public void adicionarLocal(Local local){
        locais.add(local);
    }
    public abstract void gerarConfrontos() throws TimesInsuficientesException;

    public List<Local> getLocais() {return locais;}

    public String getNome() {return nome;}

    public LocalDate getDataDeInicio() {return dataDeInicio;}

    public int getDiasDeDescanso() {return diasDeDescanso;}

    public List<Time> getTimes() {return times;}

    public List<Partida> getConfrontos() {return confrontos;}
}