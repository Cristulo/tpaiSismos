package com.grupo7.dsi.tpai.models.interfaces.state.sismografoStates;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateSismografo;

public class Disponible extends IStateSismografo {

    public Disponible() {}

    @Override
    public Boolean sosDisponible() {
        return true;
    }


}
