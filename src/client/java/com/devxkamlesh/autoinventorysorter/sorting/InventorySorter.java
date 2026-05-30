package com.devxkamlesh.autoinventorysorter.sorting;

import com.devxkamlesh.autoinventorysorter.AutoInventorySorter;
import com.devxkamlesh.autoinventorysorter.config.SorterConfig;
import com.devxkamlesh.autoinventorysorter.hotbar.HotbarSlotLocker;
import com.devxkamlesh.autoinventorysorter.hotbar.HotbarRefiller;
import com.devxkamlesh.autoinventorysorter.util.ItemUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

import java.util.*;
import java.util.stream.Collectors;

public class InventorySorter {

    private final SorterConfig config;
    private final BlacklistManager blacklistManager;
    private final HotbarSlotLocker slotLocker;
    private final HotbarRefiller hotbarRefiller;

    public InventorySorter(SorterConfig config) {
        this.config = config;
        this.blacklistManager = new BlacklistManager(config);
        this.slotLocker = new HotbarSlotLocker(config);
        this.hotbarRefiller = new HotbarRefiller(config);
    }

    /**
     * Main sort entry point. Sorts slots 9-35 (main inventory, excluding hotbar).
     */
    public void sort(PlayerInventory inventory) {
        if (!config.enabled) return;

        // Step 1: Collect non-locked items from main inventory (slots 9-35)
        List<ItemStack> items = collectMainInventory(inventory);

        // Step 2: Auto-merge partial stacks
        if (config.autoMergeStacks) {
            items = mergeStacks(items);
        }

        // Step 3: Remove junk
        if (config.autoCleanJunk) {
            items = removeJunk(items);
        }

        // Step 4: Sort
        items = applySortMode(items, config.sortMode);

        // Step 5: Handle priority groups (valuables to top, food together, etc.)
        if (config.moveValuablesToTop) {
            items = promoteCategory(items, ItemCategory.VALUABLE);
        }

        // Step 6: Write back to inventory
        writeMainInventory(inventory, items);
        
        // Step 7: Enforce locked hotbar slots
        slotLocker.enforceLockedSlots(inventory);
        
        // Step 8: Refill hotbar if needed
        hotbarRefiller.refillHotbar(inventory);

        AutoInventorySorter.LOGGER.debug("[AutoInventorySorter] Sorted main inventory");
    }

    /**
     * Sort hotbar (slots 0-8) according to profile fixed slots.
     */
    public void sortHotbar(PlayerInventory inventory) {
        if (!config.organizeHotbar) return;

        // First, enforce locked slots
        slotLocker.enforceLockedSlots(inventory);

        Map<Integer, String> fixedSlots = config.getActiveProfileConfig().fixedSlots;

        // Collect hotbar items not locked to a slot
        List<ItemStack> hotbar = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (!fixedSlots.containsKey(i) && !config.lockedSlots.contains(i) && !slotLocker.isSlotLocked(i)) {
                hotbar.add(inventory.getStack(i).copy());
                inventory.setStack(i, ItemStack.EMPTY);
            }
        }

        // Place fixed-category items first
        for (Map.Entry<Integer, String> entry : fixedSlots.entrySet()) {
            int slot = entry.getKey();
            String category = entry.getValue();
            
            // Skip if slot is locked
            if (slotLocker.isSlotLocked(slot)) {
                continue;
            }
            
            ItemStack best = findBestForCategory(inventory, category);
            if (!best.isEmpty()) {
                inventory.setStack(slot, best);
            }
        }

        // Fill remaining open hotbar slots with sorted remaining items
        List<ItemStack> remaining = hotbar.stream()
                .filter(s -> !s.isEmpty())
                .sorted(Comparator.comparingInt(s -> ItemCategory.of(s).priority))
                .collect(Collectors.toList());

        int idx = 0;
        for (int i = 0; i < 9; i++) {
            if (inventory.getStack(i).isEmpty() && idx < remaining.size()) {
                inventory.setStack(i, remaining.get(idx++));
            }
        }
        
        // Refill hotbar after sorting
        hotbarRefiller.refillHotbar(inventory);
    }

    // ─── Internals ────────────────────────────────────────────────────

    private List<ItemStack> collectMainInventory(PlayerInventory inventory) {
        List<ItemStack> items = new ArrayList<>();
        for (int slot = 9; slot < 36; slot++) {
            ItemStack stack = inventory.getStack(slot);
            if (!stack.isEmpty() && !config.lockedSlots.contains(slot)) {
                String itemId = Registries.ITEM.getId(stack.getItem()).toString();
                
                // Check blacklist
                if (blacklistManager.isItemBlacklisted(stack) || blacklistManager.isSlotBlacklisted(slot)) {
                    continue;
                }
                
                if (!config.lockedItems.contains(itemId)) {
                    items.add(stack.copy());
                    inventory.setStack(slot, ItemStack.EMPTY);
                }
            }
        }
        return items;
    }

    private List<ItemStack> mergeStacks(List<ItemStack> items) {
        Map<String, List<ItemStack>> grouped = new LinkedHashMap<>();
        for (ItemStack stack : items) {
            String key = ItemUtil.getStackKey(stack);
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(stack);
        }

        List<ItemStack> merged = new ArrayList<>();
        for (List<ItemStack> group : grouped.values()) {
            merged.addAll(ItemUtil.mergeGroup(group));
        }
        return merged;
    }

    private List<ItemStack> removeJunk(List<ItemStack> items) {
        return items.stream()
                .filter(s -> {
                    String id = Registries.ITEM.getId(s.getItem()).toString();
                    return !config.junkItems.contains(id);
                })
                .collect(Collectors.toList());
    }

    private List<ItemStack> applySortMode(List<ItemStack> items, SorterConfig.SortMode mode) {
        Comparator<ItemStack> comparator = switch (mode) {
            case ITEM_TYPE -> Comparator.comparingInt(s -> ItemCategory.of(s).priority);
            case RARITY -> Comparator.comparingInt((ItemStack s) -> ItemRarity.getRarityPriority(s)).reversed();
            case NAME_AZ -> Comparator.comparing(s -> s.getName().getString());
            case QUANTITY -> Comparator.comparingInt((ItemStack s) -> s.getCount()).reversed();
            case DURABILITY -> Comparator.comparingInt((ItemStack s) -> {
                if (!s.isDamageable()) return Integer.MAX_VALUE;
                return s.getMaxDamage() - s.getDamage();
            }).reversed();
        };

        return items.stream().sorted(comparator).collect(Collectors.toList());
    }

    private List<ItemStack> promoteCategory(List<ItemStack> items, ItemCategory cat) {
        List<ItemStack> promoted = new ArrayList<>();
        List<ItemStack> rest = new ArrayList<>();
        for (ItemStack s : items) {
            if (ItemCategory.of(s) == cat) promoted.add(s);
            else rest.add(s);
        }
        promoted.addAll(rest);
        return promoted;
    }

    private void writeMainInventory(PlayerInventory inventory, List<ItemStack> items) {
        int idx = 0;
        for (int slot = 9; slot < 36; slot++) {
            if (inventory.getStack(slot).isEmpty()) {
                if (idx < items.size()) {
                    inventory.setStack(slot, items.get(idx++));
                }
            }
        }
        // If there are more items than empty slots (shouldn't happen, but safety)
        while (idx < items.size()) {
            inventory.player.dropItem(items.get(idx++), false);
        }
    }

    private ItemStack findBestForCategory(PlayerInventory inventory, String category) {
        for (int slot = 9; slot < 36; slot++) {
            ItemStack s = inventory.getStack(slot);
            if (!s.isEmpty() && ItemUtil.matchesCategory(s, category)) {
                ItemStack found = s.copy();
                inventory.setStack(slot, ItemStack.EMPTY);
                return found;
            }
        }
        return ItemStack.EMPTY;
    }
    
    // ─── Public Accessors for New Features ────────────────────────────
    
    public BlacklistManager getBlacklistManager() {
        return blacklistManager;
    }
    
    public HotbarSlotLocker getSlotLocker() {
        return slotLocker;
    }
    
    public HotbarRefiller getHotbarRefiller() {
        return hotbarRefiller;
    }
}
