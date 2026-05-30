package com.devxkamlesh.autoinventorysorter.util;

import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;

import java.util.*;

public class ItemUtil {

    public static String getStackKey(ItemStack stack) {
        if (stack.isEmpty()) return "empty";
        String base = Registries.ITEM.getId(stack.getItem()).toString();
        // Include damage value for tools so only same-damage tools merge
        if (stack.isDamageable()) {
            return base + "#damage:" + stack.getDamage();
        }
        // Include NBT key for enchanted items
        if (stack.hasEnchantments()) {
            return base + "#enchanted:" + stack.getEnchantments().hashCode();
        }
        return base;
    }

    public static List<ItemStack> mergeGroup(List<ItemStack> group) {
        if (group.isEmpty()) return group;
        Item item = group.get(0).getItem();
        int maxCount = item.getMaxCount();

        List<ItemStack> result = new ArrayList<>();
        int total = group.stream().mapToInt(ItemStack::getCount).sum();

        while (total > 0) {
            int count = Math.min(total, maxCount);
            ItemStack merged = group.get(0).copyWithCount(count);
            result.add(merged);
            total -= count;
        }
        return result;
    }

    public static boolean matchesCategory(ItemStack stack, String category) {
        if (stack.isEmpty()) return false;
        Item item = stack.getItem();
        String id = Registries.ITEM.getId(item).toString();

        return switch (category) {
            case "sword" -> id.contains("_sword");
            case "axe" -> id.contains("_axe");
            case "pickaxe" -> id.contains("_pickaxe");
            case "shovel" -> id.contains("_shovel");
            case "hoe" -> id.contains("_hoe");
            case "bow" -> id.equals("minecraft:bow");
            case "crossbow" -> id.equals("minecraft:crossbow");
            case "trident" -> id.equals("minecraft:trident");
            case "armor" -> id.contains("_helmet") || id.contains("_chestplate") || id.contains("_leggings") || id.contains("_boots");
            case "totem" -> id.equals("minecraft:totem_of_undying");
            case "potion" -> id.contains("potion");
            case "food" -> item.getComponents().contains(net.minecraft.component.DataComponentTypes.FOOD);
            case "torch" -> id.contains("torch");
            case "bucket" -> id.contains("bucket");
            case "arrow" -> id.contains("arrow");
            case "scaffold" -> id.contains("scaffolding") || id.contains("bamboo");
            case "building_block" -> item instanceof BlockItem
                    && !(item instanceof BucketItem)
                    && !id.contains("torch")
                    && !id.contains("sapling")
                    && !id.contains("seed");
            default -> false;
        };
    }

    public static boolean isStackable(ItemStack stack) {
        return stack.getMaxCount() > 1;
    }

    public static String getDisplayName(ItemStack stack) {
        return stack.getName().getString();
    }
}
