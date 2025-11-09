package com.grupo7.dsi.tpai.models.interfaces.state.sismografoStates;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateSismografo;

public class Habilitado extends IStateSismografo {

    public Habilitado(){}

    @Override
    public Boolean sosHabilitado() {
        return true;
    }

}
