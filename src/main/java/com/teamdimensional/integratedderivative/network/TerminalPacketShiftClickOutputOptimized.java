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

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
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

    private boolean extractItems(List<ItemStack> grid, IIngredientComponentStorage<ItemStack, Integer> extraction, int multiplicity, boolean simulate) {
        return grid.stream().allMatch(s -> {
            ItemStack copy = s.copy();
            copy.setCount(copy.getCount() * multiplicity);
            return extraction.extract(copy, ItemMatch.EXACT, simulate) != ItemStack.EMPTY;
        });
    }

    private int computeMultiplicityAndExtract(List<ItemStack> grid, IIngredientComponentStorage<ItemStack, Integer> extraction, int maxMultiplicity) {
        int minMultiplicity = 1;
        if (!extractItems(grid, extraction, 1, true)) return 1;
        while (minMultiplicity < maxMultiplicity) {
            int midMultiplicity = (maxMultiplicity + minMultiplicity + 1) / 2;
            // We subtract 1 because the first batch is extracted from the grid itself
            if (extractItems(grid, extraction, midMultiplicity - 1, true)) {
                minMultiplicity = midMultiplicity;
            } else {
                maxMultiplicity = midMultiplicity - 1;
            }
        }
        // at this point both values are the same
        extractItems(grid, extraction, minMultiplicity - 1, false);
        return minMultiplicity;
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

        List<ItemStack> stacks = slots.subList(1, 10).stream().map(Slot::getStack).collect(Collectors.toList());
        // Now comes the problem that we have to modify stack sizes for equivalent stacks
        List<ItemStack> taken = new LinkedList<>();
        for (ItemStack s : stacks) {
            if (s.isEmpty()) continue;
            boolean grew = false;
            for (ItemStack u : taken) {
                if (s.isItemEqual(u) && (Objects.equals(s.getTagCompound(), u.getTagCompound()))) {
                    u.grow(1);
                    grew = true;
                    break;
                }
            }
            if (!grew) {
                ItemStack copy = s.copy();
                copy.setCount(1);
                taken.add(copy);
            }
        }

        boolean hasRateLimits =
            storage instanceof IngredientChannelAdapter && ((IngredientChannelAdapterMixin) storage).getLimitsEnabled();
        if (hasRateLimits) {
            IngredientChannelAdapter<ItemStack, Integer> indexedStorage = (IngredientChannelAdapter<ItemStack, Integer>) storage;
            indexedStorage.disableLimits();
            int multiplicity = computeMultiplicityAndExtract(taken, extraction, maxMultiplicity);
            indexedStorage.disableLimits();
            return multiplicity;
        } else {
            return computeMultiplicityAndExtract(taken, extraction, maxMultiplicity);
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

                TerminalStorageTabIngredientComponentItemStackCraftingCommon tabCommonCrafting = (TerminalStorageTabIngredientComponentItemStackCraftingCommon)tabCommon;
                PartTypeTerminalStorage.State partState = container.getPartState();
                SlotCrafting slotCrafting = tabCommonCrafting.getSlotCrafting();

                int multiplicity = extractComponents(inputs, container, player, tabCommonCrafting);
                ItemStack resultStack = slotCrafting.onTake(player, slotCrafting.decrStackSize(64));
                resultStack.setCount(resultStack.getCount() * multiplicity);
                player.inventory.placeItemBackInInventory(world, resultStack);
                tabCommonCrafting.updateCraftingResult(player, player.openContainer, partState);
            }
        }

    }
}
