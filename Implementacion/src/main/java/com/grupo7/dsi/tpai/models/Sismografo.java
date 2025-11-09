package com.grupo7.dsi.tpai.models;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateSismografo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Sismografo {

    @Id
    private Integer identificadorSismografo;

    private Integer nroSerie;
    private LocalDate fechaAdquisicion;

    @ManyToOne
    @JoinColumn(name = "codigo_estacion")
    private EstacionSismologica estacionSismologica;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "nombreModelo", referencedColumnName = "nombreModelo"),
            @JoinColumn(name = "nombreFabricante", referencedColumnName = "nombreFabricante")
    })
    private ModeloSismografo modelo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "sismografo_id")
    private List<SerieTemporal> seriesTemporales;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "estado_nombre", referencedColumnName = "nombre"),
            @JoinColumn(name = "estado_ambito", referencedColumnName = "ambito")
    })
    private Estado estadoActual;

    @Transient // Patron State manteniendo el estado actual persistente
    private IStateSismografo estadoActualComportamiento;

    @OneToMany(mappedBy = "sismografo", cascade = CascadeType.ALL)
    private List<CambioEstado> cambiosEstado;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sismografo_id")
    private List<Reparacion> reparaciones;

    @OneToOne(mappedBy = "sismografoAsignado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PlanConstruccionES planesConstruccionES;

    public Sismografo() {
        this.seriesTemporales = new ArrayList<>();
    }

    public Sismografo(Integer identificadorSismografo, Integer nroSerie, LocalDate fechaAdquisicion, ModeloSismografo modelo, SerieTemporal serieTemporal, Estado estadoActual, List<CambioEstado> cambiosEstado, List<Reparacion> reparaciones, PlanConstruccionES planesConstruccionES) {
        this.identificadorSismografo = identificadorSismografo;
        this.nroSerie = nroSerie;
        this.fechaAdquisicion = fechaAdquisicion;
        this.modelo = modelo;
        this.seriesTemporales = new ArrayList<>();
        this.estadoActual = estadoActual;
        this.cambiosEstado = cambiosEstado;
        this.reparaciones = reparaciones;
        this.planesConstruccionES = planesConstruccionES;
    }

    public void reservar(List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        this.estadoActualComportamiento.reservar(this, estadosBDD, responsableCambioEstado);
    }
    public void registrarCertificacionTerreno(List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        this.estadoActualComportamiento.registrarCertificacionTerreno(this, estadosBDD, responsableCambioEstado);
    }
    public void incluirEnPlanDeConstruccion(List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        this.estadoActualComportamiento.incluirEnPlanDeConstruccion(this, estadosBDD, responsableCambioEstado);
    }
    public void enInstalacion(List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        this.estadoActualComportamiento.enInstalacion(this, estadosBDD, responsableCambioEstado);
    }
    public void ponerEnLinea(List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        this.estadoActualComportamiento.ponerEnLinea(this, estadosBDD, responsableCambioEstado);
    }
    public void realizarReclamo(List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        this.estadoActualComportamiento.realizarReclamo(this, estadosBDD, responsableCambioEstado);
    }
    public void darDeBaja(List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        this.estadoActualComportamiento.darDeBaja(this, estadosBDD, responsableCambioEstado);
    }
    public void inspeccionar(List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        this.estadoActualComportamiento.inspeccionar(this, estadosBDD, responsableCambioEstado);
    }
    public void registrarFallo(List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        this.estadoActualComportamiento.registrarFallo(this, estadosBDD, responsableCambioEstado);
    }

    public Boolean sosDeBaja() {return this.estadoActualComportamiento.sosDeBaja();}
    public Boolean sosDisponible() {return this.estadoActualComportamiento.sosDisponible();}
    public Boolean sosEnEspera() {return this.estadoActualComportamiento.sosEnEspera();}
    public Boolean sosEnInstalacion() {return this.estadoActualComportamiento.sosEnInstalacion();}
    public Boolean sosFueraDeServicio() {return this.estadoActualComportamiento.sosFueraDeServicio();}
    public Boolean sosHabilitado() {return this.estadoActualComportamiento.sosHabilitado();}
    public Boolean sosHabilitadoParaInclusion() {return this.estadoActualComportamiento.sosHabilitadoParaInclusion();}
    public Boolean sosIncluidoEnPlanDeConstruccion() {return this.estadoActualComportamiento.sosIncluidoEnPlanDeConstruccion();}
    public Boolean sosInhabilitado() {return this.estadoActualComportamiento.sosInhabilitado();}
    public Boolean sosReclamado() {return this.estadoActualComportamiento.sosReclamado();}

}
