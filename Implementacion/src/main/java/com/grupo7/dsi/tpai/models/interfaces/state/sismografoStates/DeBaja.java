package com.grupo7.dsi.tpai.models.interfaces.state.sismografoStates;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateSismografo;

public class DeBaja extends IStateSismografo {

    public DeBaja() {}

    @Override
    public Boolean sosDeBaja() {
        return true;
    }

}
