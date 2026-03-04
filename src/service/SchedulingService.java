package service;

import exception.CampeonatoRuntimeException;
import exception.TimesInsuficientesException;
import model.CampeonatoMataMata;
import model.Local;
import model.Partida;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulingService {
    public void agendarPartidas (CampeonatoMataMata campeonato) throws TimesInsuficientesException,CampeonatoRuntimeException{

        Map<LocalDate, List<Local>> locaisOcupados = new HashMap<>();

        for(Partida partida : campeonato.getConfrontos()){
            boolean agendada = false;
            LocalDate data = campeonato.getDataDeInicio();

            while (!agendada){
                if(data.isAfter(campeonato.getDataLimite())){
                    throw new CampeonatoRuntimeException("Data Limite ultrapassada, data de campeonato inviavel");
                }
                for(Local local : campeonato.getLocais()){
                    List<Local> ocupados = locaisOcupados.getOrDefault(data, new ArrayList<>());// Retorna uma lista vazia ao inves de Null
                    boolean livre = !ocupados.contains(local);
                    if(livre){
                        partida.setLocal(local);
                        partida.setHorario(local.getAbertura());

                        if (!locaisOcupados.containsKey(data)) {
                            locaisOcupados.put(data, new ArrayList<>());
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
