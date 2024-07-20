package com.teamdimensional.integratedderivative;

import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;

public class ClientProxy extends ClientProxyComponent {
    public ClientProxy() {
        super(new CommonProxy());
    }

    @Override
    public ModBase getMod() {
        return IntegratedDerivative.INSTANCE;
    }
}
