package com.teamdimensional.integratedderivative.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;

public class LPPacketJEIDragging extends PacketCodec {
    @CodecField
    private int slotIndex;
    @CodecField
    private ItemStack itemStack;

    public LPPacketJEIDragging() {
    }

    public LPPacketJEIDragging(int slotIndex, ItemStack itemStack) {
        this.slotIndex = slotIndex;
        this.itemStack = itemStack;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public void actionClient(World world, EntityPlayer entityPlayer) {

    }

    @Override
    public void actionServer(World world, EntityPlayerMP player) {
        if (player.openContainer instanceof ContainerLogicProgrammerBase) {
            IInventory temporaryInputSlots = ((ContainerLogicProgrammerBase) player.openContainer).getTemporaryInputSlots();
            temporaryInputSlots.setInventorySlotContents(this.slotIndex, this.itemStack);
        }
    }
}
