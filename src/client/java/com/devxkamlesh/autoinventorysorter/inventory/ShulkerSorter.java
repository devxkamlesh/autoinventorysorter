package com.devxkamlesh.autoinventorysorter.inventory;

import com.devxkamlesh.autoinventorysorter.config.SorterConfig;
import com.devxkamlesh.autoinventorysorter.sorting.ItemCategory;
import com.devxkamlesh.autoinventorysorter.util.ItemUtil;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.item.ItemStack;

import java.util.*;

public class ShulkerSorter {

    private final SorterConfig config;

    public ShulkerSorter(SorterConfig config) {
        this.config = config;
    }

    public void sort(ShulkerBoxBlockEntity shulker) {
        if (!config.smartShulkerOrganization) return;

        int size = shulker.size();
        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ItemStack s = shulker.getStack(i);
            if (!s.isEmpty()) {
                items.add(s.copy());
                shulker.setStack(i, ItemStack.EMPTY);
            }
        }

        if (config.autoMergeStacks) {
            Map<String, List<ItemStack>> grouped = new LinkedHashMap<>();
            for (ItemStack s : items) {
                grouped.computeIfAbsent(ItemUtil.getStackKey(s), k -> new ArrayList<>()).add(s);
            }
            items = new ArrayList<>();
            for (List<ItemStack> group : grouped.values()) {
                items.addAll(ItemUtil.mergeGroup(group));
            }
        }

        items.sort(Comparator.comparingInt(s -> ItemCategory.of(s).priority));

        for (int i = 0; i < size && i < items.size(); i++) {
            shulker.setStack(i, items.get(i));
        }
    }
}
