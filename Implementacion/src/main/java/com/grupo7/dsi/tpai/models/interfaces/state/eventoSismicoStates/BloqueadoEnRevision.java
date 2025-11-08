package com.grupo7.dsi.tpai.models.interfaces.state.eventoSismicoStates;

import com.grupo7.dsi.tpai.models.CambioEstado;
import com.grupo7.dsi.tpai.models.Empleado;
import com.grupo7.dsi.tpai.models.Estado;
import com.grupo7.dsi.tpai.models.EventoSismico;
import com.grupo7.dsi.tpai.models.interfaces.state.IStateEventoSismico;
import com.grupo7.dsi.tpai.service.EstadoService;

import java.time.LocalDateTime;
import java.util.List;

public class BloqueadoEnRevision extends IStateEventoSismico {

    public BloqueadoEnRevision() {}

    private EstadoService estadoService = new EstadoService();

    private List<Estado> estadosEventoSismico = estadoService.findAllByAmbito("Evento sismico");

    @Override
    public void rechazar(EventoSismico eventoSismico, List<Estado> estados, Empleado empleado) {
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        Estado estadoRechazado = null;

        // Buscar el cambio de estado abierto
        List<CambioEstado> cambiosEstadoDelEventoSismico = eventoSismico.getCambiosEstado();
        CambioEstado cambioEstadoAbierto = null;
        for (CambioEstado cambioEstado : cambiosEstadoDelEventoSismico) {
            if (cambioEstado.esEstadoActual()){cambioEstadoAbierto = cambioEstado;}
        }
        // Cerrar el cambio de estado abierto
        if (cambioEstadoAbierto != null) {
            cambioEstadoAbierto.setFechaHoraFin(fechaHoraActual);
        }

        // Crear un nuevo cambio de estado con el estado "Rechazado" y lo agrego al evento sismico
        for (Estado estado : estados) {
            if (estado.sosRechazado()) {
                estadoRechazado = estado;
            }
        }
        CambioEstado nuevoCambioEstado = new CambioEstado();
        nuevoCambioEstado.setFechaHoraInicio(fechaHoraActual);
        nuevoCambioEstado.setEstado(estadoRechazado);
        nuevoCambioEstado.setResponsableInspeccion(empleado);
        eventoSismico.getCambiosEstado().add(nuevoCambioEstado);

        // Setear el estado actual del evento sismico
        Rechazado estadoRechazadoComportamiento = new Rechazado();
        eventoSismico.setEstadoActualComportamiento(estadoRechazadoComportamiento);
        eventoSismico.setEstadoActual(new Estado("Rechazado", "Evento Sismico"));

    }
}
