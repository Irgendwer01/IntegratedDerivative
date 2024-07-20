package com.teamdimensional.integratedderivative.integration.jei;

import com.teamdimensional.integratedderivative.IntegratedDerivative;
import com.teamdimensional.integratedderivative.network.LPPacketJEIDragging;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IGhostIngredientHandler;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.integrateddynamics.client.gui.GuiLogicProgrammerBase;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class IntegratedDerivativeJEIPlugin implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        IntegratedDerivative.LOGGER.info("Registering our JEI plugin");
        registry.addGhostIngredientHandler(GuiLogicProgrammerBase.class, new LPGhostIngredientHandler<>());
    }

    private static class LPGhostIngredientHandler<T extends GuiLogicProgrammerBase> implements IGhostIngredientHandler<T> {

        @Override
        public <I> @Nonnull List<Target<I>> getTargets(@Nonnull T gui, @Nonnull I ingredient, boolean doStart) {
            List<Target<I>> targets = new ArrayList<>();
            if (ingredient instanceof ItemStack || ingredient instanceof FluidStack) {
                int size = gui.getContainer().inventorySlots.size();
                for (int i = 4; i < size; i++) {
                    Slot slot = gui.getContainer().inventorySlots.get(i);
                    if (slot.inventory instanceof SimpleInventory) {
                        targets.add(new IGhostIngredientHandler.Target<I>() {
                            @Override
                            public @Nonnull Rectangle getArea() {
                                return new Rectangle(gui.getGuiLeft() + slot.xPos, gui.getGuiTop() + slot.yPos, 16, 16);
                            }

                            @Override
                            public void accept(@Nonnull I ingredient) {
                                if (ingredient instanceof ItemStack) {
                                    IntegratedDerivative.INSTANCE.getPacketHandler().sendToServer(new LPPacketJEIDragging(slot.getSlotIndex(), (ItemStack) ingredient));
                                } else if (ingredient instanceof FluidStack) {
                                    ItemStack s = FluidUtil.getFilledBucket((FluidStack) ingredient);
                                    IntegratedDerivative.INSTANCE.getPacketHandler().sendToServer(new LPPacketJEIDragging(slot.getSlotIndex(), s));
                                }
                            }
                        });
                    }
                }
            }

            return targets;
        }

        @Override
        public void onComplete() {

        }
    }

}
