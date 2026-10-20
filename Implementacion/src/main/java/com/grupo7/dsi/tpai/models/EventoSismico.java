package com.grupo7.dsi.tpai.models;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateEventoSismico;
import com.grupo7.dsi.tpai.models.interfaces.state.eventoSismicoStates.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@IdClass(EventoSismicoId.class)
@Getter @Setter
public class EventoSismico {

    @Id
    private LocalDateTime fechaHoraOcurrencia;

    @Id
    @ManyToOne
    @JoinColumn(name = "origenGeneracion", referencedColumnName = "nombre")
    private OrigenDeGeneracion origenGeneracion;

    private LocalDateTime fechaHoraFin;
    private String latitudEpicentro;
    private String latitudHipocentro;
    private String longitudEpicentro;
    private String longitudHipocentro;
    private Float valorMagnitud;

    @ManyToOne
    @JoinColumn(name = "magnitud_richter", referencedColumnName = "numero")
    private MagnitudRichter magnitud;

    @ManyToOne
    @JoinColumn(name = "clasificacion", referencedColumnName = "nombre")
    private ClasificacionSismo clasificacion;

    @ManyToOne
    @JoinColumn(name = "alcance_sismo", referencedColumnName = "nombre")
    private AlcanceSismo alcanceSismo;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "estado_nombre", referencedColumnName = "nombre"),
            @JoinColumn(name = "estado_ambito", referencedColumnName = "ambito")
    })
    private Estado estadoActual;

    @Transient
    private IStateEventoSismico estadoActualComportamiento;

    @ManyToOne
    @JoinColumn(name = "analista_supervisor_legajo", referencedColumnName = "id")
    private Empleado analistaSupervisor;

    @OneToMany (fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "origen_generacion", referencedColumnName = "origenGeneracion"),
            @JoinColumn(name = "fecha_hora_ocurrencia", referencedColumnName = "fechaHoraOcurrencia")
    })
    private List<CambioEstado> cambiosEstado;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "origen_generacion", referencedColumnName = "origenGeneracion"),
            @JoinColumn(name = "fecha_hora_ocurrencia", referencedColumnName = "fechaHoraOcurrencia")
    })
    private List<SerieTemporal> seriesTemporal;

    public EventoSismico() {}

    public EventoSismico(LocalDateTime fechaHoraFin, LocalDateTime fechaHoraOcurrencia, String latitudEpicentro, String latitudHipocentro, String longitudEpicentro, String longitudHipocentro, Float valorMagnitud, ClasificacionSismo clasificacion, MagnitudRichter magnitud, OrigenDeGeneracion origenGeneracion, AlcanceSismo alcanceSismo, Estado estadoActual, Empleado analistaSupervisor, List<CambioEstado> cambiosEstado, List<SerieTemporal> seriesTemporal) {
        this.fechaHoraFin = fechaHoraFin;
        this.fechaHoraOcurrencia = fechaHoraOcurrencia;
        this.latitudEpicentro = latitudEpicentro;
        this.latitudHipocentro = latitudHipocentro;
        this.longitudEpicentro = longitudEpicentro;
        this.longitudHipocentro = longitudHipocentro;
        this.valorMagnitud = valorMagnitud;
        this.clasificacion = clasificacion;
        this.magnitud = magnitud;
        this.origenGeneracion = origenGeneracion;
        this.alcanceSismo = alcanceSismo;
        this.estadoActual = estadoActual;
        this.analistaSupervisor = analistaSupervisor;
        this.cambiosEstado = cambiosEstado;
        this.seriesTemporal = seriesTemporal;
    }

    public void agregarCambioEstadoAEvento(CambioEstado cambioEstadoBloqueadoEnRevision) {
        this.cambiosEstado.add(cambioEstadoBloqueadoEnRevision);
    }

    @PostLoad
    private void inicializarEstadoState() {
        if (estadoActual == null) return;
        switch (estadoActual.getNombre()) {
            case "Auto confirmado" -> this.estadoActualComportamiento = new AutoConfirmado();
            case "Auto detectado" -> this.estadoActualComportamiento = new AutoDetectado();
            case "Bloqueado en revision" -> this.estadoActualComportamiento = new BloqueadoEnRevision();
            case "Cerrado" ->  this.estadoActualComportamiento = new Cerrado();
            case "Confirmado" -> this.estadoActualComportamiento = new Confirmado();
            case "Derivado a experto" -> this.estadoActualComportamiento = new DerivadoAExperto();
            case "Pendiente de cierre" -> this.estadoActualComportamiento = new PendienteDeCierre();
            case "Pendiente revision" -> this.estadoActualComportamiento = new PendienteDeRevision();
            case "Rechazado" -> this.estadoActualComportamiento = new Rechazado();
            case "Sin revision" -> this.estadoActualComportamiento = new SinRevision();
            default -> this.estadoActualComportamiento = null;
        }
    }

    public Boolean sosPendienteDeRevision() {
        if (this.estadoActual.sosPendienteDeRevision()) {
            return true;
        }
        return false;
    }

    public Boolean sosAutoDetectado(){
        if (this.estadoActual.sosAutoDetectado()) {
            return true;
        }
        return false;
    }

    // Metodos que delegan a los estados desde el evento sismico

    public void bloquearEnRevision(Empleado responsableCambioEstado) {
        if (estadoActualComportamiento == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActualComportamiento.bloquearEnRevision(this, responsableCambioEstado);
    }

    public void rechazar(Empleado responsableInspeccion) {
        if (estadoActualComportamiento == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActualComportamiento.rechazar(this, responsableInspeccion);
    }

    public void validarAutoConfirmado(Empleado responsableCambioEstado){
        if (estadoActualComportamiento == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActualComportamiento.validarAutoConfirmado(this, responsableCambioEstado);
    }

    public void validarAutoDetectado(Empleado responsableCambioEstado){
        if (estadoActualComportamiento == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActualComportamiento.validarAutoDetectado(this, responsableCambioEstado);
    }

    public void derivarExperto(Empleado responsableCambioEstado){
        if (estadoActualComportamiento == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActualComportamiento.derivarExperto(this, responsableCambioEstado);
    }

    public void confirmar(Empleado responsableCambioEstado){
        if (estadoActualComportamiento == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActualComportamiento.confirmar(this, responsableCambioEstado);
    }

    public void validarConfirmado(Empleado responsableCambioEstado){
        if (estadoActualComportamiento == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActualComportamiento.validarConfirmado(this, responsableCambioEstado);
    }

    public void pendienteDeCierre(Empleado responsableCambioEstado){
        if (estadoActualComportamiento == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActualComportamiento.pendienteDeCierre(this, responsableCambioEstado);
    }

    public void pendienteDeRevision(Empleado responsableCambioEstado){
        if (estadoActualComportamiento == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActualComportamiento.pendienteDeRevision(this, responsableCambioEstado);
    }

    public void sinRevision(Empleado responsableCambioEstado){
        if (estadoActualComportamiento == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActualComportamiento.sinRevision(this, responsableCambioEstado);
    }

    public void cerrar(Empleado responsableCambioEstado){
        if (estadoActualComportamiento == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActualComportamiento.cerrar(this, responsableCambioEstado);
    }

    public List<String> getAlcanceClasificacionOrigen() {
        List<String> datos = new ArrayList<>();
        datos.add("Alcance: " + alcanceSismo.getNombre() + " - " + alcanceSismo.getDescripcion());
        datos.add("Clasificación: " + clasificacion.getNombre() + " (" +
                clasificacion.getKmProfundidadDesde() + "km - " +
                clasificacion.getKmProfundidadHasta() + "km)");
        datos.add("Origen: " + origenGeneracion.getNombre() + " - " + origenGeneracion.getDescripcion());
        return datos;
    }

    public Map<EstacionSismologica, List<SerieTemporal>> getSeriesTemporalClasificadasPorEstacionSismologica(
            Map<EstacionSismologica, List<SerieTemporal>> seriesTemporalesPorEstacion) {
        // Recorrer el map y quitar las series temporales que no pertenezcan a este evento sismico
        for (EstacionSismologica estacion : seriesTemporalesPorEstacion.keySet()) {
            List<SerieTemporal> seriesTemporalesBDD = seriesTemporalesPorEstacion.get(estacion);
            List<SerieTemporal> seriesTemporalesDelEventoSismisco = this.seriesTemporal;
            // de las series temporales bdd quitar las que no esten en las series temporales del evento sismico
            seriesTemporalesBDD.removeIf(serieTemporal -> !seriesTemporalesDelEventoSismisco.contains(serieTemporal));
        }
        return seriesTemporalesPorEstacion;
    }

}
