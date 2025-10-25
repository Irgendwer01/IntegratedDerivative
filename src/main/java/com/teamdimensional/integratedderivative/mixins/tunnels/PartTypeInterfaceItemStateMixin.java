package com.teamdimensional.integratedderivative.mixins.tunnels;

import com.google.common.collect.Iterators;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;
import org.cyclops.commoncapabilities.IngredientComponents;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ISlotlessItemHandler;
import org.cyclops.cyclopscore.ingredient.collection.FilteredIngredientCollectionIterator;
import org.cyclops.integratedtunnels.api.network.IItemNetwork;
import org.cyclops.integratedtunnels.core.part.PartTypeInterfacePositionedAddon;
import org.cyclops.integratedtunnels.part.PartTypeInterfaceItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;
import java.util.Iterator;

@Mixin(value = PartTypeInterfaceItem.State.class, remap = false)
public class PartTypeInterfaceItemStateMixin extends PartTypeInterfacePositionedAddon.State<PartTypeInterfaceItem, IItemNetwork, IItemHandler> implements IItemHandler, ISlotlessItemHandler {

    /**
     * @author Irgendwer01
     * @reason Backport CyclopsMC/IntegratedTunnels@495d61b
     */
    @Overwrite
    public Iterator<ItemStack> getItems() {
        if (!this.isNetworkAndPositionValid()) {
            return Iterators.forArray();
        }
        this.disablePosition();
        Iterator<ItemStack> ret;
        if (!this.getPositionedAddonsNetwork().getChannel(this.getChannelInterface()).iterator().hasNext()) {
            // If the target is empty, we can safely forward this call to our index.
            ret = this.getPositionedAddonsNetwork().getChannel(this.getChannelInterface()).iterator();
        } else {
            // If the target is not empty, iterate over all positions except for the target to determine items.
            // If we would not do this, this would result in duplication of index contents, see CyclopsMC/IntegratedTerminals#109
            IItemNetwork network = this.getPositionedAddonsNetwork();
            ret = Iterators.concat(network.getPositions(this.getChannel()).stream()
                    .filter(pos -> !network.isPositionDisabled(pos))
                    .map(network::getRawInstances)
                    .toArray(Iterator[]::new));
        }
        this.enablePosition();
        return ret;
    }

    /**
     * @author Irgendwer01
     * @reason Backport CyclopsMC/IntegratedTunnels@495d61b
     */
    @Overwrite
    public Iterator<ItemStack> findItems(@Nonnull ItemStack stack, int matchFlags) {
        if (!this.isNetworkAndPositionValid()) {
            return Iterators.forArray();
        }
        this.disablePosition();
        Iterator<ItemStack> ret;
        if (!this.getPositionedAddonsNetwork().getChannel(this.getChannelInterface()).iterator(stack, matchFlags).hasNext()) {
            // If the target is empty, we can safely forward this call to our index.
            ret = this.getPositionedAddonsNetwork().getChannel(this.getChannelInterface()).iterator(stack, matchFlags);
        } else {
            // If the target is not empty, iterate over all positions except for the target to determine items.
            // If we would not do this, this would result in duplication of index contents, see CyclopsMC/IntegratedTerminals#109
            ret = new FilteredIngredientCollectionIterator<>(getItems(), IngredientComponents.ITEMSTACK.getMatcher(), stack, matchFlags);
        }
        this.enablePosition();
        return ret;
    }


    @Nonnull
    @Shadow
    public ItemStack insertItem(@Nonnull ItemStack itemStack, boolean b) {
        return null;
    }

    @Nonnull
    @Shadow
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return null;
    }


    @Nonnull
    @Shadow
    public ItemStack extractItem(int i, boolean b) {
        return null;
    }

    @Nonnull
    @Shadow
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }


    @Nonnull
    @Shadow
    public ItemStack extractItem(@Nonnull ItemStack itemStack, int i, boolean b) {
        return null;
    }

    @Shadow
    public int getLimit() {
        return 0;
    }

    @Shadow
    public int getSlots() {
        return 0;
    }

    @Nonnull
    @Shadow
    public ItemStack getStackInSlot(int slot) {
        return null;
    }

    @Shadow
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Shadow
    protected Capability<IItemHandler> getTargetCapability() {
        return null;
    }
}
