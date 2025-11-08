package com.grupo7.dsi.tpai.models.interfaces.state.eventoSismicoStates;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateEventoSismico;

public class DerivadoAExperto extends IStateEventoSismico {

    public DerivadoAExperto() {}

    @Override
    public Boolean sosDerivadoAExperto() {
        return true;
    }
}
