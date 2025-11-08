package com.grupo7.dsi.tpai.models.interfaces.state.eventoSismicoStates;

import com.grupo7.dsi.tpai.models.*;
import com.grupo7.dsi.tpai.models.interfaces.state.IStateEventoSismico;
import com.grupo7.dsi.tpai.service.EstadoService;

import java.time.LocalDateTime;
import java.util.List;

public class BloqueadoEnRevision extends IStateEventoSismico {

    public BloqueadoEnRevision() {}

    @Override
    public Boolean sosBloqueadoEnRevision() {
        return true;
    }

    @Override
    public void rechazar(EventoSismico eventoSismico, List<Estado> estados, Usuario usuarioDeLaSesion) {

        Empleado empleado = usuarioDeLaSesion.getEmpleado();
        LocalDateTime fechaHoraActual = LocalDateTime.now();

        Estado estadoRechazado = null;

        // Buscar y cerrar el ultimo cambio de estado
        List<CambioEstado> cambiosEstadoDelEventoSismico = eventoSismico.getCambiosEstado();
        CambioEstado cambioEstadoAbierto = null;
        for (CambioEstado cambioEstado : cambiosEstadoDelEventoSismico) {
            if (cambioEstado.esEstadoActual()){cambioEstadoAbierto = cambioEstado;}
        }
        if (cambioEstadoAbierto != null) {
            cambioEstadoAbierto.setFechaHoraFin(fechaHoraActual);
        }

        // Crear un nuevo cambio de estado con el estado "Rechazado" y lo agrego al evento sismico
        for (Estado estado : estados) {
            if (estado.sosRechazado()) {
                estadoRechazado = estado;
            }
        }

        CambioEstado nuevoCambioEstado = new CambioEstado(estadoRechazado);
        nuevoCambioEstado.setFechaHoraInicio(fechaHoraActual);
        nuevoCambioEstado.setResponsableInspeccion(empleado);
        eventoSismico.agregarCambioEstadoAEvento(nuevoCambioEstado);

        // Setear el estado actual del evento sismico
        Rechazado estadoRechazadoComportamiento = new Rechazado();
        eventoSismico.setEstadoActual(estadoRechazadoComportamiento);

    }
}
