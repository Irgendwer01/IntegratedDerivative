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

        @Config.Comment("Should we allow shift-clicking a recipe, even if not all ingredients were found? Clientside only.")
        public boolean allowPartialJeiPull = false;

        @Config.Comment("Should we allow shift-clicking recipes from the player's inventory? Required on both sides.")
        public boolean shiftClickFromPlayerInventory = true;
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
