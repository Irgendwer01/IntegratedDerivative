package com.teamdimensional.integratedderivative.recipe;

import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.cyclops.integrateddynamics.core.part.PartTypes;

import javax.annotation.Nonnull;
import java.util.Objects;

public class CopyODCChannelRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    public static Item connector() {
        return PartTypes.CONNECTOR_OMNI.getItem();
    }

    private final IRecipe other;

    public CopyODCChannelRecipe(IRecipe other) {
        this.other = other;
    }

    private int getConnectorChannel(InventoryCrafting inv) {
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() != connector()) continue;

            boolean isEmpty = !stack.hasTagCompound() || !Objects.requireNonNull(stack.getTagCompound()).hasKey("omnidir-group-key");
            if (!isEmpty) {
                return stack.getTagCompound().getInteger("omnidir-group-key");
            }
        }

        return -1;
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        return other.matches(inv, worldIn);
    }

    @Override
    public @Nonnull ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        int newChannel = getConnectorChannel(inv);
        ItemStack stack = other.getCraftingResult(inv);
        if (newChannel != -1) {
            NBTTagCompound cmp = new NBTTagCompound();
            cmp.setInteger("omnidir-group-key", newChannel);
            stack.setTagCompound(cmp);
        }
        return stack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return other.canFit(width, height);
    }

    @Override
    public @Nonnull ItemStack getRecipeOutput() {
        ItemStack stack = other.getRecipeOutput();

        if (FMLCommonHandler.instance().getSide().isClient()) {
            NBTTagCompound cmp = new NBTTagCompound();
            NBTTagCompound display = new NBTTagCompound();
            NBTTagList lore = new NBTTagList();
            lore.appendTag(new NBTTagString(I18n.format("tooltip.omni_duplicate.name")));
            cmp.setTag("display", display);
            display.setTag("Lore", lore);
            stack.setTagCompound(cmp);
        }
        return stack;
    }

    @Override
    public @Nonnull NonNullList<Ingredient> getIngredients() {
        return other.getIngredients();
    }
}
