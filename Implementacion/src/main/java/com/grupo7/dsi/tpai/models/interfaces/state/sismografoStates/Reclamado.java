package com.grupo7.dsi.tpai.models.interfaces.state.sismografoStates;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateSismografo;

public class Reclamado extends IStateSismografo {

    public  Reclamado() {}

    @Override
    public Boolean sosReclamado() {
        return true;
    }

}
