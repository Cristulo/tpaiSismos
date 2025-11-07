package com.grupo7.dsi.tpai.models.interfaces.state.eventoSismicoStates;

import com.grupo7.dsi.tpai.models.CambioEstado;
import com.grupo7.dsi.tpai.models.Empleado;
import com.grupo7.dsi.tpai.models.Estado;
import com.grupo7.dsi.tpai.models.EventoSismico;
import com.grupo7.dsi.tpai.models.interfaces.state.IStateEventoSismico;

import java.time.LocalDateTime;
import java.util.List;

public class BloqueadoEnRevision extends IStateEventoSismico {

    @Override
    public void rechazar(EventoSismico eventoSismico, Empleado empleado) {
        LocalDateTime fechaHoraActual = LocalDateTime.now();

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
        CambioEstado nuevoCambioEstado = new CambioEstado();
        nuevoCambioEstado.setEstadoNombre("Rechazado");
        nuevoCambioEstado.setEstadoAmbito("Evento Sismico");
        nuevoCambioEstado.setFechaHoraInicio(fechaHoraActual);
        nuevoCambioEstado.setResponsableInspeccion(empleado);
        eventoSismico.getCambiosEstado().add(nuevoCambioEstado);

        // Setear el estado actual del evento sismico
        Rechazado estadoRechazado = new Rechazado();
        eventoSismico.setEstadoActualComportamiento(estadoRechazado);
        eventoSismico.setEstadoActual(new Estado("Rechazado", "Evento Sismico"));

    }
}
