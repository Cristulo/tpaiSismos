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

    @Convert(converter = EstadoEventoConverter.class) // Esto esta feo, revisar
    @Column(name = "estado_codigo", length = 32, nullable = false)
    private IStateEventoSismico estadoActual;

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

    public EventoSismico(LocalDateTime fechaHoraFin, LocalDateTime fechaHoraOcurrencia, String latitudEpicentro,
                         String latitudHipocentro, String longitudEpicentro, String longitudHipocentro,
                         Float valorMagnitud, ClasificacionSismo clasificacion, MagnitudRichter magnitud,
                         OrigenDeGeneracion origenGeneracion, AlcanceSismo alcanceSismo, IStateEventoSismico estadoActual,
                         Empleado analistaSupervisor, List<CambioEstado> cambiosEstado,
                         List<SerieTemporal> seriesTemporal, Boolean esAutoDetectado) {
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
        this.analistaSupervisor = analistaSupervisor;
        this.cambiosEstado = cambiosEstado;
        this.seriesTemporal = seriesTemporal;
        if (esAutoDetectado) {
            this.estadoActual = new AutoDetectado();
        } else if (!esAutoDetectado) {
            this.estadoActual = new AutoConfirmado();
        } else {
            this.estadoActual = estadoActual;
        }
    }



    public void agregarCambioEstadoAEvento(CambioEstado cambioEstado) {
        this.cambiosEstado.add(cambioEstado);
    }

    public Boolean sosPendienteDeRevision() {
        return this.estadoActual.sosPendienteDeRevision();
    }

    public Boolean sosAutoDetectado(){
        return this.estadoActual.sosAutoDetectado();
    }

    // Metodos que delegan a los estados desde el evento sismico
    public void bloquearEnRevision(List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        if (estadoActual == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActual.bloquearEnRevision(this, estadosBDD, responsableCambioEstado);
    }
    public void rechazar(List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        if (estadoActual == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActual.rechazar(this, estadosBDD, responsableCambioEstado);
    }
    public void validarAutoConfirmado(List<Estado> estadosBDD, Usuario responsableCambioEstado){
        if (estadoActual == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActual.validarAutoConfirmado(this, estadosBDD, responsableCambioEstado);
    }
    public void validarAutoDetectado(List<Estado> estadosBDD, Usuario responsableCambioEstado){
        if (estadoActual == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActual.validarAutoDetectado(this, estadosBDD, responsableCambioEstado);
    }
    public void derivarExperto(List<Estado> estadosBDD, Usuario responsableCambioEstado){
        if (estadoActual == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActual.derivarExperto(this, estadosBDD, responsableCambioEstado);
    }
    public void confirmar(List<Estado> estadosBDD, Usuario responsableCambioEstado){
        if (estadoActual == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActual.confirmar(this, estadosBDD, responsableCambioEstado);
    }
    public void validarConfirmado(List<Estado> estadosBDD, Usuario responsableCambioEstado){
        if (estadoActual == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActual.validarConfirmado(this, estadosBDD, responsableCambioEstado);
    }
    public void pendienteDeCierre(List<Estado> estadosBDD, Usuario responsableCambioEstado){
        if (estadoActual == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActual.pendienteDeCierre(this, estadosBDD, responsableCambioEstado);
    }
    public void pendienteDeRevision(List<Estado> estadosBDD, Usuario responsableCambioEstado){
        if (estadoActual == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActual.pendienteDeRevision(this, estadosBDD, responsableCambioEstado);
    }
    public void sinRevision(List<Estado> estadosBDD, Usuario responsableCambioEstado){
        if (estadoActual == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActual.sinRevision(this, estadosBDD, responsableCambioEstado);
    }
    public void cerrar(List<Estado> estadosBDD, Usuario responsableCambioEstado){
        if (estadoActual == null)
            throw new IllegalStateException("No hay estado lógico inicializado.");
        estadoActual.cerrar(this, estadosBDD, responsableCambioEstado);
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
