package com.grupo7.dsi.tpai.models.interfaces.state;

import com.grupo7.dsi.tpai.models.Estado;
import com.grupo7.dsi.tpai.models.EventoSismico;
import com.grupo7.dsi.tpai.models.Sismografo;
import com.grupo7.dsi.tpai.models.Usuario;
import com.grupo7.dsi.tpai.models.interfaces.state.sismografoStates.*;

import java.util.List;

public abstract class IStateSismografo {

    /* ESTADOS:
     DeBaja
     Disponible
     EnEspera
     EnInstalacion
     FueraDeServicio
     Habilitado
     HabilitadoParaInclusion
     IncluidoEnPlanDeConstruccion
     Inhabilitado
     Reclamado
    */

    protected void prohibida(String accion) {
        throw new IllegalStateException("Acción '" + accion + "' no permitida desde " + this.getClass().getSimpleName());}

    public void reservar(Sismografo sismografo, List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        prohibida("reservar");}
    public void registrarCertificacionTerreno(Sismografo sismografo, List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        prohibida("registrarCertificacionTerreno");}
    public void incluirEnPlanDeConstruccion(Sismografo sismografo, List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        prohibida("incluirEnPlanDeConstruccion");}
    public void enInstalacion(Sismografo sismografo, List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        prohibida("enInstalacion");}
    public void ponerEnLinea(Sismografo sismografo, List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        prohibida("ponerEnLinea");}
    public void realizarReclamo(Sismografo sismografo, List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        prohibida("realizarReclamo");}
    public void darDeBaja(Sismografo sismografo, List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        prohibida("darDeBaja");}
    public void inspeccionar(Sismografo sismografo, List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        prohibida("inspeccionar");}
    public void registrarFallo(Sismografo sismografo, List<Estado> estadosBDD, Usuario responsableCambioEstado) {
        prohibida("registrarFallo");}


    public Boolean sosDeBaja() {return false;}
    public Boolean sosDisponible() {return false;}
    public Boolean sosEnEspera() {return false;}
    public Boolean sosEnInstalacion() {return false;}
    public Boolean sosFueraDeServicio() {return false;}
    public Boolean sosHabilitado() {return false;}
    public Boolean sosHabilitadoParaInclusion() {return false;}
    public Boolean sosIncluidoEnPlanDeConstruccion() {return false;}
    public Boolean sosInhabilitado() {return false;}
    public Boolean sosReclamado() {return false;}

}
