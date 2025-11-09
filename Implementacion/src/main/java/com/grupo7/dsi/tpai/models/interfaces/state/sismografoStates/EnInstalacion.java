package com.grupo7.dsi.tpai.models.interfaces.state.sismografoStates;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateSismografo;

public class EnInstalacion extends IStateSismografo {

    public  EnInstalacion() {}

    @Override
    public Boolean sosEnInstalacion() {
        return true;
    }

}
