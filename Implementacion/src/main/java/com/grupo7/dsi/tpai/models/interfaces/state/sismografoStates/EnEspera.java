package com.grupo7.dsi.tpai.models.interfaces.state.sismografoStates;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateSismografo;

public class EnEspera extends IStateSismografo {

    public  EnEspera() {}

    @Override
    public Boolean sosEnEspera() {
        return true;
    }

}
