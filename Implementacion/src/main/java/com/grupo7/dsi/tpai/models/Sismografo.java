package com.grupo7.dsi.tpai.models;

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

}
