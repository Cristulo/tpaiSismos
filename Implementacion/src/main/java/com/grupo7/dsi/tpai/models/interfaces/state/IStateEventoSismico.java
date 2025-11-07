package com.grupo7.dsi.tpai.models.interfaces.state;

import com.grupo7.dsi.tpai.models.CambioEstado;
import com.grupo7.dsi.tpai.models.Empleado;
import com.grupo7.dsi.tpai.models.Estado;
import com.grupo7.dsi.tpai.models.EventoSismico;

import java.time.LocalDateTime;

public abstract class IStateEventoSismico {

    protected static final String AMBITO = "Evento sismico";

    // Métodos por defecto que lanzan excepción si no son sobreescritos{}
        protected void prohibida(String accion) {
            throw new IllegalStateException("Acción '" + accion + "' no permitida desde " + this.getClass().getSimpleName());}
        public void validarAutoConfirmado(EventoSismico eventoSismico, Empleado responsableCambioEstado) {
            prohibida("validarAutoConfirmado");}
        public void validarAutoDetectado(EventoSismico eventoSismico, Empleado responsableCambioEstado)  {
            prohibida("validarAutoDetectado"); }
        public void bloquearEnRevision(EventoSismico eventoSismico, Empleado responsableCambioEstado)    {
            prohibida("bloquearEnRevision"); }
        public void derivarExperto(EventoSismico eventoSismico, Empleado responsableCambioEstado)        {
            prohibida("derivarExperto"); }
        public void rechazar(EventoSismico eventoSismico, Empleado responsableCambioEstado)              {
            prohibida("rechazar"); }
        public void confirmar(EventoSismico eventoSismico, Empleado responsableCambioEstado)             {
            prohibida("confirmar"); }
        public void validarConfirmado(EventoSismico eeventoSismico, Empleado responsableCambioEstado)     {
            prohibida("validarConfirmado"); }
        public void pendienteDeCierre(EventoSismico eventoSismico, Empleado responsableCambioEstado)     {
            prohibida("pendienteDeCierre"); }
        public void pendienteDeRevision(EventoSismico eventoSismico, Empleado responsableCambioEstado)   {
            prohibida("pendienteDeRevision"); }
        public void sinRevision(EventoSismico eventoSismico, Empleado responsableCambioEstado)           {
            prohibida("sinRevision"); }
        public void cerrar(EventoSismico eventoSismico, Empleado responsableCambioEstado)                {
            prohibida("cerrar"); }


    protected void transicionar(EventoSismico eventoSismico,
                                Empleado responsable,
                                String nuevoNombreEstado,
                                IStateEventoSismico nuevoEstadoComportamiento) {
        var ahora = LocalDateTime.now();
        // 1) cerrar cambio abierto (si hay)
        eventoSismico.getCambiosEstado().stream()
                .filter(CambioEstado::esEstadoActual)
                .findFirst()
                .ifPresent(ce -> {
                    ce.setFechaHoraFin(ahora);
                    ce.setResponsableInspeccion(responsable); // si tu caso de uso lo pide acá
                });
        // 2) crear nuevo cambio
        var nuevo = new CambioEstado();
        nuevo.setEstadoNombre(nuevoNombreEstado);
        nuevo.setEstadoAmbito(AMBITO);
        nuevo.setFechaHoraInicio(ahora);
        nuevo.setResponsableInspeccion(responsable); // responsable visible en el historial
        eventoSismico.getCambiosEstado().add(nuevo);
        // 3) actualizar el comportamiento en memoria
        eventoSismico.setEstadoActualComportamiento(nuevoEstadoComportamiento);
    }


}
