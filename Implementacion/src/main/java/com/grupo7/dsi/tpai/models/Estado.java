package com.grupo7.dsi.tpai.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@IdClass(EstadoId.class)
public class Estado {

    @Id
    private String nombre;

    @Id
    private String ambito;

    public Estado() {
    }

    public Estado(String nombre, String ambito) {
        this.nombre = nombre;
        this.ambito = ambito;
    }

    // No es cohesivo, agregar esEstadoEspecifico
    public Boolean esNombre(String nombre) {
        return this.nombre.equals(nombre);
    }

    public Boolean esAmbito(String ambito) {
        return this.ambito.equals(ambito);
    }

    public Boolean sosBloqueadoEnRevision() {
        if (esAmbito("Evento sismico") && esNombre("Bloqueado en revision")) {
            return true;
        }
        return false;
    }

    public Boolean sosRechazado(){
        if (esNombre("Rechazado") && esAmbito("Evento sismico")) {
            return true;
        }
        return false;
    }
}