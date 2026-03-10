package service;

import exception.CampeonatoRuntimeException;
import exception.TimesInsuficientesException;
import model.CampeonatoMataMata;
import model.Local;
import model.Partida;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class SchedulingService {
    public void agendarPartidas (CampeonatoMataMata campeonato) throws TimesInsuficientesException,CampeonatoRuntimeException{

        Map<LocalDate, Set<Local>> locaisOcupados = new HashMap<>();

        for(Partida partida : campeonato.getConfrontos()){
            boolean agendada = false;
            LocalDate data = campeonato.getDataDeInicio();
            LocalDate dataAtual = data;

            while (!agendada){
                if(data.isAfter(campeonato.getDataLimite())){
                    throw new CampeonatoRuntimeException("Data Limite ultrapassada, data de campeonato inviavel");
                }

                List<Local> locaisOrdenados = campeonato.getLocais().stream().sorted((a, b) -> {
                    Set<Local> ocupados = locaisOcupados.getOrDefault(dataAtual, new HashSet<Local>());
                    long jogosA = ocupados.stream().filter(l -> l.equals(a)).count();
                    long jogosB = ocupados.stream().filter(l -> l.equals(b)).count();
                    return Long.compare(jogosA, jogosB);
                }).toList();

                for(Local local : locaisOrdenados){
                    Set<Local> ocupados = locaisOcupados.getOrDefault(data, new HashSet<Local>());// Retorna uma lista vazia ao inves de Null

                    long jogosAgendados = ocupados.stream()
                            .filter(l -> l.equals(local))
                            .count();

                    long capacidade = ChronoUnit.HOURS.between(local.getAbertura(), local.getFechamento()) / 2;
                    boolean livre = jogosAgendados < capacidade;

                    if(livre){
                        LocalTime horario = local.getAbertura().plusHours(jogosAgendados * 2);
                        partida.setLocal(local);
                        partida.setHorario(horario);

                        if (!locaisOcupados.containsKey(data)) {
                            locaisOcupados.put(data, new HashSet<Local>());
                        }
                        locaisOcupados.get(data).add(local);
                        agendada = true;
                        break;
                    }
                }
                if(!agendada){
                    data = data.plusDays(1);
                }
            }
        }
    }
}
