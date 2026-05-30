package com.devxkamlesh.autoinventorysorter.sorting;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;

public class ItemRarity {

    public static int getRarityPriority(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        return switch (stack.getRarity()) {
            case EPIC -> 4;
            case RARE -> 3;
            case UNCOMMON -> 2;
            case COMMON -> 1;
        };
    }

    public static String getRarityName(ItemStack stack) {
        if (stack.isEmpty()) return "Common";
        return switch (stack.getRarity()) {
            case EPIC -> "Epic";
            case RARE -> "Rare";
            case UNCOMMON -> "Uncommon";
            case COMMON -> "Common";
        };
    }
}
