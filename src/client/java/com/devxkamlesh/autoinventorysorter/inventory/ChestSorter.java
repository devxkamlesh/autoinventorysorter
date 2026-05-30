package com.devxkamlesh.autoinventorysorter.inventory;

import com.devxkamlesh.autoinventorysorter.AutoInventorySorter;
import com.devxkamlesh.autoinventorysorter.config.SorterConfig;
import com.devxkamlesh.autoinventorysorter.sorting.ItemCategory;
import com.devxkamlesh.autoinventorysorter.util.ItemUtil;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class ChestSorter {

    private final SorterConfig config;

    public ChestSorter(SorterConfig config) {
        this.config = config;
    }

    public void sort(Inventory inventory) {
        if (!config.enableChestSorting) return;

        int size = inventory.size();
        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ItemStack s = inventory.getStack(i);
            if (!s.isEmpty()) {
                items.add(s.copy());
                inventory.setStack(i, ItemStack.EMPTY);
            }
        }

        // Merge stacks
        if (config.autoMergeStacks) {
            items = mergeAll(items);
        }

        // Sort by category
        items.sort(Comparator.comparingInt(s -> ItemCategory.of(s).priority));

        // Write back
        for (int i = 0; i < size && i < items.size(); i++) {
            inventory.setStack(i, items.get(i));
        }
        
        AutoInventorySorter.LOGGER.debug("[AutoInventorySorter] Sorted chest with {} items", items.size());
    }

    private List<ItemStack> mergeAll(List<ItemStack> items) {
        Map<String, List<ItemStack>> grouped = new LinkedHashMap<>();
        for (ItemStack s : items) {
            grouped.computeIfAbsent(ItemUtil.getStackKey(s), k -> new ArrayList<>()).add(s);
        }
        List<ItemStack> merged = new ArrayList<>();
        for (List<ItemStack> group : grouped.values()) {
            merged.addAll(ItemUtil.mergeGroup(group));
        }
        return merged;
    }
}
