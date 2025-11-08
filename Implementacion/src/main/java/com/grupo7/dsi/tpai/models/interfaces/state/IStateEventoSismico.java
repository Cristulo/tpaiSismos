package com.grupo7.dsi.tpai.models.interfaces.state;

import com.grupo7.dsi.tpai.models.CambioEstado;
import com.grupo7.dsi.tpai.models.Empleado;
import com.grupo7.dsi.tpai.models.Estado;
import com.grupo7.dsi.tpai.models.EventoSismico;

import java.time.LocalDateTime;
import java.util.List;

public abstract class IStateEventoSismico {

    // Métodos por defecto que lanzan excepción si no son sobreescritos
        protected void prohibida(String accion) {
            throw new IllegalStateException("Acción '" + accion + "' no permitida desde " + this.getClass().getSimpleName());}
        public void validarAutoConfirmado(EventoSismico eventoSismico,List<Estado> estadosBDD, Empleado responsableCambioEstado) {
            prohibida("validarAutoConfirmado");}
        public void validarAutoDetectado(EventoSismico eventoSismico,List<Estado> estadosBDD, Empleado responsableCambioEstado)  {
            prohibida("validarAutoDetectado"); }
        public void bloquearEnRevision(EventoSismico eventoSismico,List<Estado> estadosBDD, Empleado responsableCambioEstado)    {
            prohibida("bloquearEnRevision"); }
        public void derivarExperto(EventoSismico eventoSismico,List<Estado> estadosBDD, Empleado responsableCambioEstado)        {
            prohibida("derivarExperto"); }
        public void rechazar(EventoSismico eventoSismico,List<Estado> estadosBDD, List<Estado> estados, Empleado responsableCambioEstado)              {
            prohibida("rechazar"); }
        public void confirmar(EventoSismico eventoSismico,List<Estado> estadosBDD, Empleado responsableCambioEstado)             {
            prohibida("confirmar"); }
        public void validarConfirmado(EventoSismico eeventoSismico,List<Estado> estadosBDD, Empleado responsableCambioEstado)     {
            prohibida("validarConfirmado"); }
        public void pendienteDeCierre(EventoSismico eventoSismico,List<Estado> estadosBDD, Empleado responsableCambioEstado)     {
            prohibida("pendienteDeCierre"); }
        public void pendienteDeRevision(EventoSismico eventoSismico,List<Estado> estadosBDD, Empleado responsableCambioEstado)   {
            prohibida("pendienteDeRevision"); }
        public void sinRevision(EventoSismico eventoSismico,List<Estado> estadosBDD, Empleado responsableCambioEstado)           {
            prohibida("sinRevision"); }
        public void cerrar(EventoSismico eventoSismico,List<Estado> estadosBDD, Empleado responsableCambioEstado)                {
            prohibida("cerrar"); }

    /*
    protected static final String AMBITO = "Evento sismico";
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
    */
}
