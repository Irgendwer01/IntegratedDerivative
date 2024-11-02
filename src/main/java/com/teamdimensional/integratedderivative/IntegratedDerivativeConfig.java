package com.teamdimensional.integratedderivative;

import com.teamdimensional.integratedderivative.enums.JEICompactingMode;
import com.teamdimensional.integratedderivative.enums.ShiftClickMode;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Tags.MOD_ID, category = "")
public class IntegratedDerivativeConfig {

    @Config.Comment("Fixes for Integrated Dynamics")
    public static IntegratedDynamicsFixes dynamicsFixes = new IntegratedDynamicsFixes();

    @Config.Comment("Tweaks to Integrated Dynamics")
    public static IntegratedDynamicsTweaks dynamicsTweaks = new IntegratedDynamicsTweaks();

    @Config.Comment("Fixes for Integrated Terminals")
    public static IntegratedTerminalsFixes terminalsFixes = new IntegratedTerminalsFixes();

    @Config.Comment("Tweaks to Integrated Terminals")
    public static IntegratedTerminalsTweaks terminalsTweaks = new IntegratedTerminalsTweaks();

    @Config.Comment("Fixes for Integrated Tunnels")
    public static IntegratedTunnelsFixes tunnelsFixes = new IntegratedTunnelsFixes();

    public static class IntegratedDynamicsTweaks {
        @Config.Comment("How should we compact the recipes? " +
            "Large means the recipe has more than 9 filled input slots and/or more than 3 filled output slots. " +
            "Lossy means the recipe will be compacted even when it has more than 9 distinct inputs and/or more than 3 distinct outputs. " +
            "Blacklisted recipes will never be compacted. Clientside only.")
        public JEICompactingMode jeiCompactingMode = JEICompactingMode.COMPACT_ALWAYS;

        @Config.Comment("Should we remove the first ingredient from Carpenter and Thermionic Fabricator when applicable? Clientside only.")
        public boolean autoCompactForestryRecipes = true;

        @Config.Comment("These recipe categories should never be compacted when JEI recipes are pulled into the Logic Programmer. " +
            "These are using fully-qualified class names.")
        public String[] recipeCompactingBlacklist = new String[]{
            "mezz.jei.plugins.vanilla.crafting.CraftingRecipeCategory",
            "forestry.factory.recipes.jei.carpenter.CarpenterRecipeCategory",
            "forestry.factory.recipes.jei.fabricator.FabricatorRecipeCategory"
        };
    }

    public static class IntegratedDynamicsFixes {
        @Config.Comment("Should we replace Omni-Directional Connector 'channel copy' crafting with a less janky version?")
        public boolean fixODCCrafting = true;
    }

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

    public static class IntegratedTunnelsFixes {
        @Config.Comment("Should we fix World Block Importer being able to break indestructible blocks?")
        public boolean fixBedrockBreaker = true;
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
