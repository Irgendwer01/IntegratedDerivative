package com.teamdimensional.integratedderivative.network;

import com.teamdimensional.integratedderivative.IntegratedDerivative;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.integratedterminals.api.terminalstorage.ITerminalStorageTabServer;
import org.cyclops.integratedterminals.core.terminalstorage.TerminalStorageTabIngredientComponentServer;
import org.cyclops.integratedterminals.inventory.container.ContainerTerminalStorage;

public class TerminalPacketExtractOneStack extends PacketCodec {
    @CodecField
    private String tabId;
    @CodecField
    private int channel;
    @CodecField
    private NBTTagCompound serialized;

    public TerminalPacketExtractOneStack() {
    }

    public TerminalPacketExtractOneStack(String tabId, int channel, ItemStack stack) {
        this.tabId = tabId;
        this.channel = channel;
        this.serialized = new NBTTagCompound();
        this.serialized.setTag("i", IngredientComponents.ITEMSTACK.getSerializer().serializeInstance(stack));
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public void actionClient(World world, EntityPlayer player) {}

    @Override
    @SuppressWarnings("unchecked")
    public void actionServer(World world, EntityPlayerMP player) {

        if(player.openContainer instanceof ContainerTerminalStorage) {
            ContainerTerminalStorage container = ((ContainerTerminalStorage) player.openContainer);
            ITerminalStorageTabServer tab1 = container.getTabServer(tabId);
            if (tab1 instanceof TerminalStorageTabIngredientComponentServer) {
                TerminalStorageTabIngredientComponentServer<ItemStack, Integer> tab = (TerminalStorageTabIngredientComponentServer<ItemStack, Integer>) tab1;
                IIngredientComponentStorage<ItemStack, Integer> storage = tab.getIngredientNetwork()
                    .getChannel(container.getSelectedChannel());
                ItemStack stack = IngredientComponents.ITEMSTACK.getSerializer().deserializeInstance(this.serialized.getTag("i"));
                ItemStack copy = stack.copy();
                stack.setCount(Math.min(stack.getMaxStackSize(), stack.getCount()));
                if (storage.extract(stack, ItemMatch.EXACT & ~ItemMatch.STACKSIZE, true).isEmpty()) {
                    IntegratedDerivative.LOGGER.warn("Unable to find the shift-clicked stack: {}!", stack);
                    return;
                }
                int currentCount = stack.getCount();
                player.inventory.addItemStackToInventory(stack);
                copy.setCount(currentCount - stack.getCount());
                storage.extract(copy, ItemMatch.EXACT & ~ItemMatch.STACKSIZE, false);
            }
        }
    }
}
