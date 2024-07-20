package com.teamdimensional.integratedderivative;

import com.teamdimensional.integratedderivative.enums.ShiftClickMode;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Tags.MOD_ID, category = "")
public class IntegratedDerivativeConfig {

    @Config.Comment("Fixes to Integrated Terminals")
    public static IntegratedTerminalsFixes terminalsFixes = new IntegratedTerminalsFixes();

    @Config.Comment("Tweaks to Integrated Terminals")
    public static IntegratedTerminalsTweaks terminalsTweaks = new IntegratedTerminalsTweaks();

    public static class IntegratedTerminalsFixes {
        @Config.Comment("Should we allow shift-clicking a recipe, even if not all ingredients were found? Clientside only.")
        public boolean allowPartialJeiPull = false;

        @Config.Comment("Should we allow shift-clicking recipes from the player's inventory? Required on both sides.")
        public boolean shiftClickFromPlayerInventory = true;

        @Config.Comment("Should we optimize shift-clicking out of the crafting grid? Clientside only, requires that the mod is installed on the server.")
        public boolean optimizeShiftClickCrafting = true;
    }

    public static class IntegratedTerminalsTweaks {
        @Config.Comment("Should the Shift behavior on the Clear button be inverted? Default: shift-click to send into the network, click to send into the inventory. Clientside only.")
        public boolean invertClearButton = true;

        @Config.Comment("How many items should shift-clicking move out of the crafting grid? Only works if shift-click crafting is optimized. Clientside only.")
        public ShiftClickMode shiftClickCraftingBehavior = ShiftClickMode.STACK_ROUNDED_DOWN;

        @Config.Comment("Should shift+clicking items out of the Terminal extract one stack at a time instead of the full inventory? Clientside only. Requires that the mod is installed on the server.")
        public boolean shiftClickOneStack = true;
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
