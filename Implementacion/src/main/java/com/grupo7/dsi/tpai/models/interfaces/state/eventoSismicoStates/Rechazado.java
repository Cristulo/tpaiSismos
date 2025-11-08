package com.grupo7.dsi.tpai.models.interfaces.state.eventoSismicoStates;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateEventoSismico;

public class Rechazado extends IStateEventoSismico {

    public Rechazado() {}

    @Override
    public Boolean sosRechazado() {
        return true;
    }

}
