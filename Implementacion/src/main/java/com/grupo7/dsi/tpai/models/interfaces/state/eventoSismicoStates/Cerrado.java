package com.grupo7.dsi.tpai.models.interfaces.state.eventoSismicoStates;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateEventoSismico;

public class Cerrado extends IStateEventoSismico {

    public Cerrado() {}

    @Override
    public Boolean sosCerrado() {
        return true;
    }
}
