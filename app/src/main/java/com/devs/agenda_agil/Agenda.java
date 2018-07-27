package com.devs.agenda_agil;

import com.devs.src.DateUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

class Agenda {
    private List<Evento> eventos = new ArrayList<>();
    private List<EventoCiclico> eventosCiclicos = new ArrayList<>();
    private List<Tarea> backlog = new ArrayList<>();
    private List<Tarea> historialTareas = new ArrayList<>();
    private List<TareaPlanificada> planificado = new ArrayList<>();
    private DateUtil dateSupplier;

    public Agenda(DateUtil dateSupplier) {
        this.dateSupplier = dateSupplier;
    }

    public Agenda() {
        this.dateSupplier = new DateUtil();
    }

    public void agregar(Evento evento) {
        this.eventos.add(evento);
    }

    public void agregar(EventoCiclico eventoCiclico) {
        this.eventosCiclicos.add(eventoCiclico);
    }

    public void eliminar(Evento evento) {
        assert eventos.contains(evento);

        this.eventos.remove(evento);
    }

    public void agregar(Tarea tarea) {
        this.backlog.add(tarea);
    }

    public void eliminar(Tarea tarea) {
        assert backlog.contains(tarea);

        this.backlog.remove(tarea);
    }

    public boolean pertenece(Evento evento) {
        return this.eventos.contains(evento);
    }

    public boolean pertenece(Tarea tarea) {
        return this.backlog.contains(tarea);
    }

    public List<Tarea> backlog() {
        Collections.sort(this.backlog);
        return this.backlog;
    }

    public void realizar(Tarea tarea) {
        assert(this.backlog().contains(tarea));

        this.backlog.remove(tarea);
        this.historialTareas.add(tarea);
    }

    public List<Tarea> historialDeTareas() {
        return this.historialTareas;
    }

    /**
     * La utilidad de este método la vemos cuando en la aplicación tengamos la view
     * planificarDia llamemos a planificar()
     * @param tarea
     */
    public void planificar(Tarea tarea) {
        assert(this.backlog().contains(tarea));

        this.backlog.remove(tarea);
        Calendar diaDeMañana = this.getTomorrowDate();
        TareaPlanificada tareaPlanificada = new TareaPlanificada(tarea.nombre(), diaDeMañana);
        this.planificado.add(tareaPlanificada);
    }

    private Calendar getTomorrowDate() {
        Calendar diaDeMañana = this.dateSupplier.getDate();
        diaDeMañana.add(Calendar.DATE, 1);
        return diaDeMañana;
    }

    public boolean planificada(Tarea tarea) {
        boolean pertenece = false;
        for(TareaPlanificada planificada : this.planificado){
            if(planificada.nombre().equals(tarea.nombre())){
                pertenece = true;
            }
        }
        return pertenece;
    }

    public DiaDeAgenda mostrarDia(Calendar diaAMostrar) {
        DiaDeAgenda obligacionesDelDia = new DiaDeAgenda();

        for(Evento evento : this.eventos){
            if( evento.fecha().equals(diaAMostrar) ){
                obligacionesDelDia.add(evento);
            }
        }

        for(TareaPlanificada tarea : this.planificado){
            if( tarea.fecha().equals(diaAMostrar) ){
                obligacionesDelDia.add(tarea);
            }
        }

        for (EventoCiclico eventoCiclico : this.eventosCiclicos) {
            assert(eventosCiclicos.size() > 0);
            for (Calendar fecha : eventoCiclico.getFechas()) {
                int año_de_fecha = fecha.get(Calendar.YEAR);
                int dia_del_año_de_fecha = fecha.get(Calendar.DAY_OF_YEAR);
                int dia_del_año_de_diaAMostrar = diaAMostrar.get(Calendar.DAY_OF_YEAR);
                int año_de_diaAMostrar = diaAMostrar.get(Calendar.YEAR);

                if (dia_del_año_de_fecha == dia_del_año_de_diaAMostrar && año_de_fecha == año_de_diaAMostrar) {
                    Evento evento = new Evento(fecha);
                    obligacionesDelDia.add(evento);
                }
            }
        }
        return obligacionesDelDia;
    }

    public void realizar(Evento evento) {
        assert this.eventos.contains(evento);

        this.eventos.remove(evento);
        Evento eventoRealizado = new Evento(evento);
        eventoRealizado.setRealizado(true);
        this.eventos.add(eventoRealizado);
    }

    public List<Evento> eventosRealizados() {
        List<Evento> eventos = new ArrayList<>();
        for(Evento evento : this.eventos){
            if( evento.realizado() ){
                eventos.add(evento);
            }
        }
        return eventos;
    }

    public List<Evento> eventos() {
        return this.eventos;
    }

    public void rePlanificar(Evento evento, Calendar nuevaFecha) {
        assert this.eventos.contains(evento);

        this.eventos.remove(evento);
        Evento eventoRePlanificado = new Evento(evento);
        eventoRePlanificado.setFecha(nuevaFecha);
        this.eventos.add(eventoRePlanificado);
    }

    public List<EventoCiclico> eventosCiclicos() {
        return this.eventosCiclicos;
    }
}
