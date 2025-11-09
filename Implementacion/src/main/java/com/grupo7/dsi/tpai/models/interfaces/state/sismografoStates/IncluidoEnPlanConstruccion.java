package com.grupo7.dsi.tpai.models.interfaces.state.sismografoStates;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateSismografo;

public class IncluidoEnPlanConstruccion extends IStateSismografo {

    public IncluidoEnPlanConstruccion() {}

    @Override
    public Boolean sosIncluidoEnPlanDeConstruccion() {
        return true;
    }
}
