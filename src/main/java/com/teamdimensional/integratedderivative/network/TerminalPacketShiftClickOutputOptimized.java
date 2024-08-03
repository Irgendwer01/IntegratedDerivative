package com.teamdimensional.integratedderivative.network;

import com.teamdimensional.integratedderivative.enums.ShiftClickMode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.integratedterminals.api.terminalstorage.ITerminalStorageTabCommon;
import org.cyclops.integratedterminals.core.terminalstorage.TerminalStorageTabIngredientComponentItemStackCraftingCommon;
import org.cyclops.integratedterminals.inventory.container.ContainerTerminalStorage;
import org.cyclops.integratedterminals.part.PartTypeTerminalStorage;

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

    public void actionServer(World world, EntityPlayerMP player) {
        if (player.openContainer instanceof ContainerTerminalStorage) {
            ContainerTerminalStorage container = (ContainerTerminalStorage)player.openContainer;
            ITerminalStorageTabCommon tabCommon = container.getTabCommon(this.tabId);
            if (!(tabCommon instanceof TerminalStorageTabIngredientComponentItemStackCraftingCommon)) return;

            TerminalStorageTabIngredientComponentItemStackCraftingCommon tabCommonCrafting = (TerminalStorageTabIngredientComponentItemStackCraftingCommon)tabCommon;
            PartTypeTerminalStorage.State partState = container.getPartState();
            SlotCrafting slotCrafting = tabCommonCrafting.getSlotCrafting();
            ItemStack resultStack = slotCrafting.getStack().copy();
            if (resultStack.isEmpty()) return;  // nothing can be crafted

            int maxCapacity = resultStack.getCount();

            boolean checkBeforeCrafting = true;
            boolean firstOperation = true;
            if (mode == ShiftClickMode.STACK_ROUNDED_DOWN.value || mode == ShiftClickMode.STACK_ROUNDED_UP.value) {
                maxCapacity = resultStack.getMaxStackSize();
                checkBeforeCrafting = mode == ShiftClickMode.STACK_ROUNDED_UP.value;
            }

            do {
                if (checkBeforeCrafting && !firstOperation && maxCapacity <= resultStack.getCount()) break;
                ItemStack localResult = slotCrafting.onTake(player, slotCrafting.decrStackSize(64));
                tabCommonCrafting.updateCraftingResult(player, player.openContainer, partState);
                if (!firstOperation) {
                    resultStack.grow(localResult.getCount());
                } else {
                    firstOperation = false;
                }
                if (!checkBeforeCrafting && maxCapacity <= resultStack.getCount() + slotCrafting.getStack().getCount()) break;
            } while (ItemHandlerHelper.canItemStacksStack(slotCrafting.getStack(), resultStack));

            player.inventory.placeItemBackInInventory(world, resultStack);
        }

    }
}
