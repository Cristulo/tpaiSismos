package com.grupo7.dsi.tpai.models.interfaces.state.eventoSismicoStates;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateEventoSismico;

public class Confirmado extends IStateEventoSismico {

    public Confirmado() {}

    @Override
    public Boolean sosConfirmado() {
        return true;
    }
}
