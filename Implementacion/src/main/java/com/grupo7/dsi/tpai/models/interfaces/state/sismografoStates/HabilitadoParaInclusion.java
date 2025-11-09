package com.grupo7.dsi.tpai.models.interfaces.state.sismografoStates;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateSismografo;

public class HabilitadoParaInclusion extends IStateSismografo {

    public HabilitadoParaInclusion() {}

    @Override
    public Boolean sosHabilitadoParaInclusion() {
        return true;
    }

}
