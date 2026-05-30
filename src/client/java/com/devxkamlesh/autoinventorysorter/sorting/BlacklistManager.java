package com.devxkamlesh.autoinventorysorter.sorting;

import com.devxkamlesh.autoinventorysorter.config.SorterConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

import java.util.HashSet;
import java.util.Set;

public class BlacklistManager {
    
    private final SorterConfig config;
    private final Set<String> blacklistedItems;
    private final Set<Integer> blacklistedSlots;
    
    public BlacklistManager(SorterConfig config) {
        this.config = config;
        this.blacklistedItems = new HashSet<>(config.blacklistedItems);
        this.blacklistedSlots = new HashSet<>(config.blacklistedSlots);
    }
    
    public boolean isItemBlacklisted(ItemStack stack) {
        if (!config.enableBlacklist || stack.isEmpty()) {
            return false;
        }
        
        String itemId = Registries.ITEM.getId(stack.getItem()).toString();
        return blacklistedItems.contains(itemId);
    }
    
    public boolean isSlotBlacklisted(int slot) {
        if (!config.enableBlacklist) {
            return false;
        }
        
        return blacklistedSlots.contains(slot);
    }
    
    public void addItemToBlacklist(String itemId) {
        blacklistedItems.add(itemId);
        config.blacklistedItems.add(itemId);
        config.save();
    }
    
    public void removeItemFromBlacklist(String itemId) {
        blacklistedItems.remove(itemId);
        config.blacklistedItems.remove(itemId);
        config.save();
    }
    
    public void addSlotToBlacklist(int slot) {
        blacklistedSlots.add(slot);
        config.blacklistedSlots.add(slot);
        config.save();
    }
    
    public void removeSlotFromBlacklist(int slot) {
        blacklistedSlots.remove(slot);
        config.blacklistedSlots.remove(slot);
        config.save();
    }
    
    public boolean canSort(ItemStack stack, int slot) {
        return !isItemBlacklisted(stack) && !isSlotBlacklisted(slot);
    }
    
    public Set<String> getBlacklistedItems() {
        return new HashSet<>(blacklistedItems);
    }
    
    public Set<Integer> getBlacklistedSlots() {
        return new HashSet<>(blacklistedSlots);
    }
}
