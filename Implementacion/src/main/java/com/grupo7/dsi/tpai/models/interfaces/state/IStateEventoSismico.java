package com.grupo7.dsi.tpai.models.interfaces.state;

import com.grupo7.dsi.tpai.models.*;

import java.time.LocalDateTime;
import java.util.List;

public abstract class IStateEventoSismico {

    // Métodos por defecto que lanzan excepción si no son sobreescritos
        protected void prohibida(String accion) {
            throw new IllegalStateException("Acción '" + accion + "' no permitida desde " + this.getClass().getSimpleName());}

        public void validarAutoConfirmado(EventoSismico eventoSismico,List<Estado> estadosBDD, Usuario responsableCambioEstado) {
            prohibida("validarAutoConfirmado");}
        public void validarAutoDetectado(EventoSismico eventoSismico,List<Estado> estadosBDD, Usuario responsableCambioEstado)  {
            prohibida("validarAutoDetectado"); }
        public void bloquearEnRevision(EventoSismico eventoSismico,List<Estado> estadosBDD, Usuario responsableCambioEstado)    {
            prohibida("bloquearEnRevision"); }
        public void derivarExperto(EventoSismico eventoSismico,List<Estado> estadosBDD, Usuario responsableCambioEstado)        {
            prohibida("derivarExperto"); }
        public void rechazar(EventoSismico eventoSismico,List<Estado> estadosBDD, Usuario responsableCambioEstado)              {
            prohibida("rechazar"); }
        public void confirmar(EventoSismico eventoSismico,List<Estado> estadosBDD, Usuario responsableCambioEstado)             {
            prohibida("confirmar"); }
        public void validarConfirmado(EventoSismico eeventoSismico,List<Estado> estadosBDD, Usuario responsableCambioEstado)     {
            prohibida("validarConfirmado"); }
        public void pendienteDeCierre(EventoSismico eventoSismico,List<Estado> estadosBDD, Usuario responsableCambioEstado)     {
            prohibida("pendienteDeCierre"); }
        public void pendienteDeRevision(EventoSismico eventoSismico,List<Estado> estadosBDD, Usuario responsableCambioEstado)   {
            prohibida("pendienteDeRevision"); }
        public void sinRevision(EventoSismico eventoSismico,List<Estado> estadosBDD, Usuario responsableCambioEstado)           {
            prohibida("sinRevision"); }
        public void cerrar(EventoSismico eventoSismico,List<Estado> estadosBDD, Usuario responsableCambioEstado)                {
            prohibida("cerrar"); }

        public Boolean sosAutoConfirmado() {return false;}
        public Boolean sosAutoDetectado() {return false;}
        public Boolean sosBloqueadoEnRevision() {return false;}
        public Boolean sosDerivadoAExperto() {return false;}
        public Boolean sosRechazado() {return false;}
        public Boolean sosConfirmado() {return false;}
        public Boolean sosValidadoConfirmado() {return false;}
        public Boolean sosPendienteDeCierre() {return false;}
        public Boolean sosPendienteDeRevision() {return false;}
        public Boolean sosSinRevision() {return false;}
        public Boolean sosCerrado() {return false;}


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
