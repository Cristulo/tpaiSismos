package com.grupo7.dsi.tpai.models.interfaces.state.sismografoStates;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateSismografo;

public class FueraDeServicio extends IStateSismografo {

    public FueraDeServicio() {}

    @Override
    public Boolean sosFueraDeServicio() {
        return true;
    }

}
