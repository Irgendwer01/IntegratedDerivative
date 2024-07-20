package com.teamdimensional.integratedderivative.util;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StackUtil {

    public static List<ItemStack> compactStacks(List<ItemStack> stacks, boolean resetSize) {
        List<ItemStack> output = new ArrayList<>();
        for (ItemStack s : stacks) {
            if (s.isEmpty()) continue;
            boolean grew = false;
            for (ItemStack u : output) {
                if (s.isItemEqual(u) && (Objects.equals(s.getTagCompound(), u.getTagCompound()))) {
                    u.grow(resetSize ? 1 : s.getCount());
                    grew = true;
                    break;
                }
            }
            if (!grew) {
                ItemStack copy = s.copy();
                if (resetSize) copy.setCount(1);
                output.add(copy);
            }
        }
        return output;
    }

}
