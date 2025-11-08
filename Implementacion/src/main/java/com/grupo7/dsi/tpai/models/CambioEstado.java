package com.grupo7.dsi.tpai.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@IdClass(CambioEstadoId.class)
public class CambioEstado {

    @Id
    private LocalDateTime fechaHoraInicio;

    @Id
    @Column(name = "estado_nombre")
    private String estadoNombre;

    @Id
    @Column(name = "estado_ambito")
    private String estadoAmbito;

    private LocalDateTime fechaHoraFin;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "estado_nombre", referencedColumnName = "nombre", insertable = false, updatable = false),
            @JoinColumn(name = "estado_ambito", referencedColumnName = "ambito", insertable = false, updatable = false)
    })
    private Estado estado;

    @ManyToOne
    private Empleado responsableInspeccion;

    @ManyToMany
    private List<MotivoFueraServicio> motivoFueraServicio;

    @ManyToOne
    @JoinColumn(name = "sismografo_id")
    private Sismografo sismografo; //Sismografos que tienen este cambio de estado

    public CambioEstado() {}

    public CambioEstado(Estado estadoParametro){
        this.estadoNombre = estadoParametro.getNombre();
        this.estadoAmbito = estadoParametro.getAmbito();
        this.estado = estadoParametro;
        this.fechaHoraInicio = LocalDateTime.now();
    }

    public List<MotivoFueraServicio> getMotivoFueraServicio() {
        return motivoFueraServicio;
    }
    public void setMotivoFueraServicio(List<MotivoFueraServicio> motivoFueraServicio) {
        this.motivoFueraServicio = motivoFueraServicio;
    }
    public Boolean esEstadoActual(){
        if (this.fechaHoraFin == null) {
            return true;
        }
        return false;
    }

    // getters, setters, constructor vacío
}