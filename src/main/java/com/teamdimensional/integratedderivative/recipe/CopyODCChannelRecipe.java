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
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.cyclops.integrateddynamics.core.part.PartTypes;

import javax.annotation.Nonnull;
import java.util.Objects;

public class CopyODCChannelRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    public static Item connector() {
        return PartTypes.CONNECTOR_OMNI.getItem();
    }

    private int getConnectorChannel(InventoryCrafting inv) {
        ItemStack channeled = null;
        boolean foundEmpty = false;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() != connector()) return -1;

            boolean isEmpty = !stack.hasTagCompound() || !Objects.requireNonNull(stack.getTagCompound()).hasKey("omnidir-group-key");
            if (isEmpty) {
                if (foundEmpty) return -1;
                foundEmpty = true;
            } else {
                if (channeled != null) return -1;
                channeled = stack;
            }
        }

        if (channeled == null || !foundEmpty) return -1;
        return channeled.getTagCompound().getInteger("omnidir-group-key");
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        return getConnectorChannel(inv) != -1;
    }

    @Override
    public @Nonnull ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        int newChannel = getConnectorChannel(inv);
        ItemStack stack = new ItemStack(connector(), 2);
        NBTTagCompound cmp = new NBTTagCompound();
        cmp.setInteger("omnidir-group-key", newChannel);
        stack.setTagCompound(cmp);
        return stack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @Nonnull ItemStack getRecipeOutput() {
        ItemStack stack = new ItemStack(connector(), 2);
        NBTTagCompound cmp = new NBTTagCompound();
        NBTTagCompound display = new NBTTagCompound();
        NBTTagList lore = new NBTTagList();
        lore.appendTag(new NBTTagString(I18n.format("tooltip.omni_duplicate.name")));
        cmp.setTag("display", display);
        display.setTag("Lore", lore);
        stack.setTagCompound(cmp);
        return stack;
    }

    @Override
    public @Nonnull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ing = NonNullList.create();
        ing.add(Ingredient.fromStacks(new ItemStack(connector())));
        ing.add(Ingredient.fromStacks(new ItemStack(connector())));
        return ing;
    }
}
