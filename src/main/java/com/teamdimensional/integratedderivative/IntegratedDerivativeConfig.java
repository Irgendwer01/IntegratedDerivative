package com.teamdimensional.integratedderivative;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Tags.MOD_ID, category = "")
public class IntegratedDerivativeConfig {

    @Config.Comment("Fixes to Integrated Terminals")
    public static IntegratedTerminalsFixes terminalsFixes = new IntegratedTerminalsFixes();

    public static class IntegratedTerminalsFixes {
        @Config.Comment("Should the Shift behavior on the Clear button be inverted? Default: shift-click to send into the network, click to send into the inventory. Clientside only.")
        public boolean invertClearButton = true;
    }

    @Mod.EventBusSubscriber(modid = Tags.MOD_ID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Tags.MOD_ID)) {
                ConfigManager.sync(Tags.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }

}
