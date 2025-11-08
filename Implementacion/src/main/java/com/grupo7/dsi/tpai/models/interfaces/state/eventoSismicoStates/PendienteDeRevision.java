package com.grupo7.dsi.tpai.models.interfaces.state.eventoSismicoStates;

import com.grupo7.dsi.tpai.models.CambioEstado;
import com.grupo7.dsi.tpai.models.Empleado;
import com.grupo7.dsi.tpai.models.Estado;
import com.grupo7.dsi.tpai.models.EventoSismico;
import com.grupo7.dsi.tpai.models.interfaces.state.IStateEventoSismico;
import com.grupo7.dsi.tpai.service.EstadoService;

import java.time.LocalDateTime;
import java.util.List;

public class PendienteDeRevision extends IStateEventoSismico {

    public PendienteDeRevision() {}

    @Override
    public void bloquearEnRevision(EventoSismico evento,List<Estado> estadosBDD, Empleado responsable) {
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        List<CambioEstado> cambiosEstadoEventoSismico = evento.getCambiosEstado();

        // Buscar el cambio de estado actual y setear su fechaHoraFin
        for (CambioEstado cambioEstado : cambiosEstadoEventoSismico) {
            if (cambioEstado.esEstadoActual()){
                cambioEstado.setFechaHoraFin(fechaHoraActual);
                break;
            }
        }

        // Buscar el estado "Bloqueado en revision" para el evento sismico.
        Estado estadoBloqueado = null;
        for (Estado estado : estadosBDD) {
            if (estado.sosBloqueadoEnRevision()) {
                estadoBloqueado = estado;
                break;
            }
        }

        // Crear el nuevo cambio de estado "Bloqueado en revision", se utilizan la clase estado generica y la de comportamiento del patron
        CambioEstado cambioEstadoBloqueadoEnRevision = new CambioEstado(estadoBloqueado);
        cambioEstadoBloqueadoEnRevision.setResponsableInspeccion(responsable);
        cambioEstadoBloqueadoEnRevision.setFechaHoraInicio(fechaHoraActual);
        evento.agregarCambioEstadoAEvento(cambioEstadoBloqueadoEnRevision);
        // Setear el estado actual del evento sismico
        evento.setEstadoActual(estadoBloqueado);
        evento.setEstadoActualComportamiento(new BloqueadoEnRevision()); // Esto lo quiero cambiar.
    }

}
