package com.grupo7.dsi.tpai.models.interfaces.state.eventoSismicoStates;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateEventoSismico;

public final class EstadoEventoRegistry {
    private EstadoEventoRegistry(){}

    public static IStateEventoSismico fromCode(String code) {
        return switch (code) {
            case "PENDIENTE_REV" -> new PendienteDeRevision();
            case "BLOQUEADO_REV" -> new BloqueadoEnRevision();
            case "RECHAZADO"     -> new Rechazado();
            case "AUTO_DET"      -> new AutoDetectado();
            case "AUTO_CONF"     -> new AutoConfirmado();
            default              -> new SinRevision();
        };
    }

    public static String codeOf(IStateEventoSismico s) {
        if (s instanceof PendienteDeRevision) return "PENDIENTE_REV";
        if (s instanceof BloqueadoEnRevision)  return "BLOQUEADO_REV";
        if (s instanceof Rechazado)            return "RECHAZADO";
        if (s instanceof AutoDetectado)        return "AUTO_DET";
        if (s instanceof AutoConfirmado)       return "AUTO_CONF";
        return "SIN_REV";
    }
}