package com.teamdimensional.integratedderivative.util;

import com.google.common.collect.Iterators;
import net.minecraft.item.ItemStack;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Iterator;

public class CombinedIngredientStorage implements IIngredientComponentStorage<ItemStack, Integer> {

    private final IIngredientComponentStorage<ItemStack, Integer>[] parts;

    @SafeVarargs
    public CombinedIngredientStorage(IIngredientComponentStorage<ItemStack, Integer> ...parts) {
        this.parts = parts;
        assert parts.length > 0;
    }

    @Override
    public IngredientComponent<ItemStack, Integer> getComponent() {
        return parts[0].getComponent();
    }

    @Override
    public @Nonnull Iterator<ItemStack> iterator() {
        return Iterators.concat(Arrays.stream(parts).map(IIngredientComponentStorage::iterator).iterator());
    }

    @Override
    public Iterator<ItemStack> iterator(@Nonnull ItemStack stack, Integer m) {
        return Iterators.concat(Arrays.stream(parts).map(r -> r.iterator(stack, m)).iterator());
    }

    @Override
    public long getMaxQuantity() {
        return Arrays.stream(parts).mapToLong(IIngredientComponentStorage::getMaxQuantity).sum();
    }

    @Override
    public ItemStack insert(@Nonnull ItemStack stack, boolean simulate) {
        for (IIngredientComponentStorage<ItemStack, Integer> part : parts) {
            stack = part.insert(stack, simulate);
        }
        return stack;
    }

    @Override
    public ItemStack extract(long maxQuantity, boolean simulate) {
        for (IIngredientComponentStorage<ItemStack, Integer> part : parts) {
            ItemStack extracted = part.extract(maxQuantity, simulate);
            if (!extracted.isEmpty()) return extracted;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extract(@Nonnull ItemStack stack, Integer matchCondition, boolean simulate) {
        for (IIngredientComponentStorage<ItemStack, Integer> part : parts) {
            ItemStack extracted = part.extract(stack, matchCondition, simulate);
            if (!extracted.isEmpty()) return extracted;
        }
        return ItemStack.EMPTY;
    }
}
