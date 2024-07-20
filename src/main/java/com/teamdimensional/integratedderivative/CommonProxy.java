package com.teamdimensional.integratedderivative;

import com.teamdimensional.integratedderivative.network.TerminalPacketExtractOneStack;
import com.teamdimensional.integratedderivative.network.TerminalPacketShiftClickOutputOptimized;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.cyclopscore.proxy.CommonProxyComponent;

public class CommonProxy extends CommonProxyComponent {
    @Override
    public ModBase getMod() {
        return IntegratedDerivative.INSTANCE;
    }

    @Override
    public void registerPacketHandlers(PacketHandler packetHandler) {
        super.registerPacketHandlers(packetHandler);
        packetHandler.register(TerminalPacketShiftClickOutputOptimized.class);
        packetHandler.register(TerminalPacketExtractOneStack.class);
        IntegratedDerivative.LOGGER.info("Registered packet handlers");
    }
}
