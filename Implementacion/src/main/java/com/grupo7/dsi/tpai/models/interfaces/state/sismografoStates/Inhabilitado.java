package com.grupo7.dsi.tpai.models.interfaces.state.sismografoStates;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateSismografo;

public class Inhabilitado extends IStateSismografo {

    public Inhabilitado() {}

    @Override
    public Boolean sosInhabilitado() {
        return true;
    }

}
