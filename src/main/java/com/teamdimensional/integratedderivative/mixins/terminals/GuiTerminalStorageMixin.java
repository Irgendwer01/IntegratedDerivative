package com.teamdimensional.integratedderivative.mixins.terminals;

import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.cyclopscore.client.gui.container.GuiContainerExtended;
import org.cyclops.cyclopscore.inventory.container.ExtendedInventoryContainer;
import org.cyclops.integratedterminals.IntegratedTerminals;
import org.cyclops.integratedterminals.api.terminalstorage.ITerminalStorageTabClient;
import org.cyclops.integratedterminals.api.terminalstorage.TerminalClickType;
import org.cyclops.integratedterminals.client.gui.container.GuiTerminalStorage;
import org.cyclops.integratedterminals.core.terminalstorage.TerminalStorageTabIngredientComponentClient;
import org.cyclops.integratedterminals.inventory.SlotCraftingAutoRefill;
import org.cyclops.integratedterminals.inventory.container.ContainerTerminalStorage;
import org.cyclops.integratedterminals.network.packet.TerminalStorageIngredientSlotClickPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import yalter.mousetweaks.api.IMTModGuiContainer2Ex;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mixin(value = GuiTerminalStorage.class, remap = false)
public abstract class GuiTerminalStorageMixin extends GuiContainerExtended implements IMTModGuiContainer2Ex {
    public GuiTerminalStorageMixin(ExtendedInventoryContainer container) {
        super(container);
    }

    @Shadow public abstract ContainerTerminalStorage getContainer();

    @Shadow @Nullable public abstract Slot getSlotUnderMouse();

    @Shadow private int terminalDragSplittingButton;

    @Shadow protected boolean terminalDragSplitting;

    @Override
    public boolean MT_isMouseTweaksDisabled() {
        return false;
    }

    @Override
    public boolean MT_isWheelTweakDisabled() {
        return true;
    }

    @Override
    public List<Slot> MT_getSlots() {
        // There are no slots in the inventory itself, because of a custom renderer from ITer
        return new ArrayList<>();
    }

    @Override
    public Slot MT_getSlotUnderMouse() {
        return getSlotUnderMouse();
    }

    @Override
    public boolean MT_isCraftingOutput(Slot slot) {
        return slot instanceof SlotCraftingAutoRefill;
    }

    @Override
    public boolean MT_isIgnored(Slot slot) {
        return false;
    }

    @Override
    public boolean MT_disableRMBDraggingFunctionality() {
        if (terminalDragSplitting) {
            if (terminalDragSplittingButton == 1) {
                terminalDragSplitting = false;
                return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void MT_clickSlot(Slot slot, int mouseButton, ClickType clickType) {
        if (clickType != ClickType.QUICK_MOVE) {
            handleMouseClick(slot, slot.slotNumber, mouseButton, clickType);
            return;
        }

        int slotNumber = slot.slotNumber;
        ContainerTerminalStorage terminalContainer = (ContainerTerminalStorage) container;
        int channel = terminalContainer.getSelectedChannel();
        String tab = terminalContainer.getSelectedTab();
        if (tab == null || !tab.startsWith("minecraft:itemstack")) return;

        ITerminalStorageTabClient<?> tabClient = terminalContainer.getTabClient(tab);

        if (tabClient instanceof TerminalStorageTabIngredientComponentClient) {
            TerminalStorageTabIngredientComponentClient<ItemStack, Integer> tabClient2 = (TerminalStorageTabIngredientComponentClient<ItemStack, Integer>) tabClient;
            IngredientComponent<ItemStack, Integer> component = tabClient2.getIngredientComponent();
            IIngredientMatcher<ItemStack, Integer> matcher = component.getMatcher();
            TerminalStorageIngredientSlotClickPacket<ItemStack> packet = new TerminalStorageIngredientSlotClickPacket<>(
                tab, component, TerminalClickType.PLAYER_QUICK_MOVE, channel,
                matcher.getEmptyInstance(), slotNumber, 0L, matcher.getEmptyInstance(), true);
            IntegratedTerminals._instance.getPacketHandler().sendToServer(packet);
        }
    }

    @Override
    public int MT_scrollIsItemPrioritizedForSlot(Slot slot, ItemStack stack) {
        // This only applies to mouse wheel, which we have disabled
        return 0;
    }
}
