package com.grupo7.dsi.tpai.models;

import jakarta.persistence.*;

@Entity
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAmbito() {
        return ambito;
    }

    // No es cohesivo, agregar esEstadoEspecifico
    public Boolean esNombre(String nombre) {
        return this.nombre.equals(nombre);
    }

    public Boolean esAmbito(String ambito) {
        return this.ambito.equals(ambito);
    }

    public Boolean sosPendienteDeRevision() {
        if (esAmbito("Evento sismico") && esNombre("Pendiente revision")) {
            return true;
        }
        return false;
    }

    public Boolean sosAutoDetectado(){
        if (esAmbito("Evento sismico") && esNombre("Auto detectado")) {
            return true;
        }
        return false;
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