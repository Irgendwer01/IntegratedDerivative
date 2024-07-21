package com.teamdimensional.integratedderivative.network;

import com.teamdimensional.integratedderivative.enums.ShiftClickMode;
import com.teamdimensional.integratedderivative.mixins.dynamics.IngredientChannelAdapterMixin;
import com.teamdimensional.integratedderivative.util.CombinedIngredientStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.commoncapabilities.ingredient.storage.IngredientComponentStorageWrapperHandlerItemStack;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.integrateddynamics.core.network.IngredientChannelAdapter;
import org.cyclops.integratedterminals.api.terminalstorage.ITerminalStorageTabCommon;
import org.cyclops.integratedterminals.core.terminalstorage.TerminalStorageTabIngredientComponentItemStackCraftingCommon;
import org.cyclops.integratedterminals.core.terminalstorage.TerminalStorageTabIngredientComponentServer;
import org.cyclops.integratedterminals.core.terminalstorage.button.TerminalButtonItemStackCraftingGridAutoRefill;
import org.cyclops.integratedterminals.inventory.container.ContainerTerminalStorage;
import org.cyclops.integratedterminals.part.PartTypeTerminalStorage;

import java.util.List;
import java.util.stream.Collectors;

public class TerminalPacketShiftClickOutputOptimized extends PacketCodec {
    @CodecField
    private String tabId;
    @CodecField
    private int channel;
    @CodecField
    private int mode;

    public TerminalPacketShiftClickOutputOptimized() {
    }

    public TerminalPacketShiftClickOutputOptimized(String tabId, int channel, int mode) {
        this.tabId = tabId;
        this.channel = channel;
        this.mode = mode;
    }

    public boolean isAsync() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void actionClient(World world, EntityPlayer player) {
    }

    private int computeMultiplicityAndExtract(List<Slot> grid, IIngredientComponentStorage<ItemStack, Integer> extraction, int maxMultiplicity) {
        int multiplicity = maxMultiplicity - 1;
        for (int i = 0; i < maxMultiplicity; i++) {
            boolean shouldContinue = true;
            for (Slot slot : grid) {
                ItemStack stack = slot.getStack();

                if (stack.isEmpty()) continue;
                if (stack.getCount() > 1) {
                    stack.shrink(1);
                } else if (extraction.extract(slot.getStack(), ItemMatch.EXACT, false).isEmpty()) {
                    slot.inventory.setInventorySlotContents(slot.getSlotIndex(), ItemStack.EMPTY);
                    shouldContinue = false;
                }
            }
            if (!shouldContinue) {
                multiplicity = i;
                break;
            }
        }
        return multiplicity + 1;
    }

    @SuppressWarnings("unchecked")
    private int extractComponents(
        List<Slot> slots, ContainerTerminalStorage container, EntityPlayer thePlayer, TerminalStorageTabIngredientComponentItemStackCraftingCommon tabCommon) {
        TerminalStorageTabIngredientComponentServer<ItemStack, Integer> tabServer =
            (TerminalStorageTabIngredientComponentServer<ItemStack, Integer>) container.getTabServer(tabCommon.getName().toString());
        if (tabServer == null) return 0;

        TerminalButtonItemStackCraftingGridAutoRefill.AutoRefillType autoRefill = tabCommon.getAutoRefill();

        IIngredientComponentStorage<ItemStack, Integer> storage = tabServer.getIngredientNetwork()
            .getChannel(container.getSelectedChannel());
        IIngredientComponentStorage<ItemStack, Integer> player = new IngredientComponentStorageWrapperHandlerItemStack.ComponentStorageWrapper(
            IngredientComponent.ITEMSTACK, new PlayerInvWrapper(thePlayer.inventory));

        IIngredientComponentStorage<ItemStack, Integer> extraction = null;
        switch (autoRefill) {
            case STORAGE:
                extraction = storage;
                break;
            case PLAYER:
                extraction = player;
                break;
            case STORAGE_PLAYER:
                extraction = new CombinedIngredientStorage(storage, player);
                break;
            case PLAYER_STORAGE:
                extraction = new CombinedIngredientStorage(player, storage);
                break;
        }
        if (extraction == null) return 1;

        Slot grid = slots.get(0);
        int craftingOutputSize = grid.getStack().getCount();
        int maxStackSize = grid.getStack().getMaxStackSize();
        int maxMultiplicity = 1;
        if (mode == ShiftClickMode.STACK_ROUNDED_DOWN.value) {
            maxMultiplicity = maxStackSize / craftingOutputSize;
        } else if (mode == ShiftClickMode.STACK_ROUNDED_UP.value) {
            maxMultiplicity = (maxStackSize - 1) / craftingOutputSize + 1;
        }

        List<Slot> inputs = slots.subList(1, 10);

        boolean hasRateLimits =
            storage instanceof IngredientChannelAdapter && ((IngredientChannelAdapterMixin) storage).getLimitsEnabled();
        if (hasRateLimits) {
            IngredientChannelAdapter<ItemStack, Integer> indexedStorage = (IngredientChannelAdapter<ItemStack, Integer>) storage;
            indexedStorage.disableLimits();
            int multiplicity = computeMultiplicityAndExtract(inputs, extraction, maxMultiplicity);
            indexedStorage.disableLimits();
            return multiplicity;
        } else {
            return computeMultiplicityAndExtract(inputs, extraction, maxMultiplicity);
        }
    }

    public void actionServer(World world, EntityPlayerMP player) {
        if (player.openContainer instanceof ContainerTerminalStorage) {
            ContainerTerminalStorage container = (ContainerTerminalStorage)player.openContainer;
            ITerminalStorageTabCommon tabCommon = container.getTabCommon(this.tabId);
            if (tabCommon instanceof TerminalStorageTabIngredientComponentItemStackCraftingCommon) {
                List<Triple<Slot, Integer, Integer>> tabSlots = container.getTabSlots(this.tabId);
                // slot 0 = output, slots 1-9 = inputs
                List<Slot> inputs = tabSlots.subList(0, 10).stream().map(Triple::getLeft).collect(Collectors.toList());
                if (!inputs.get(0).getHasStack()) return;  // nothing can be crafted

                TerminalStorageTabIngredientComponentItemStackCraftingCommon tabCommonCrafting = (TerminalStorageTabIngredientComponentItemStackCraftingCommon)tabCommon;
                PartTypeTerminalStorage.State partState = container.getPartState();
                SlotCrafting slotCrafting = tabCommonCrafting.getSlotCrafting();

                ItemStack resultStack = slotCrafting.getStack();
                int multiplicity = extractComponents(inputs, container, player, tabCommonCrafting);
                resultStack.setCount(resultStack.getCount() * multiplicity);
                player.inventory.placeItemBackInInventory(world, resultStack);
                tabCommonCrafting.updateCraftingResult(player, player.openContainer, partState);
            }
        }

    }
}
