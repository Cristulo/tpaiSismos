package com.grupo7.dsi.tpai.models.interfaces.state.eventoSismicoStates;

import com.grupo7.dsi.tpai.models.*;
import com.grupo7.dsi.tpai.models.interfaces.state.IStateEventoSismico;

import java.time.LocalDateTime;
import java.util.List;

public class PendienteDeRevision extends IStateEventoSismico {

    public PendienteDeRevision() {}

    @Override
    public Boolean sosPendienteDeRevision() {
        return true;
    }

    @Override
    public void bloquearEnRevision(EventoSismico evento,List<Estado> estadosBDD, Usuario usuario) {

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
        cambioEstadoBloqueadoEnRevision.setFechaHoraInicio(fechaHoraActual);
        evento.agregarCambioEstadoAEvento(cambioEstadoBloqueadoEnRevision);
        // Setear el estado actual del evento sismico
        BloqueadoEnRevision estadoBloqueadoState = new BloqueadoEnRevision();
        evento.setEstadoActual(estadoBloqueadoState);
    }
}
