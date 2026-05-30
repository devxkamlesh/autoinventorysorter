package com.devxkamlesh.autoinventorysorter.hotbar;

import com.devxkamlesh.autoinventorysorter.AutoInventorySorter;
import com.devxkamlesh.autoinventorysorter.config.SorterConfig;
import com.devxkamlesh.autoinventorysorter.util.ItemUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

public class HotbarRefiller {
    
    private final SorterConfig config;
    
    public HotbarRefiller(SorterConfig config) {
        this.config = config;
    }
    
    public void refillHotbar(PlayerInventory inventory) {
        if (!config.autoRefillHotbar) {
            return;
        }
        
        // Check each hotbar slot
        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            ItemStack hotbarStack = inventory.getStack(hotbarSlot);
            
            // Skip if slot is not empty
            if (!hotbarStack.isEmpty()) {
                continue;
            }
            
            // Try to refill from main inventory
            refillSlotFromInventory(inventory, hotbarSlot);
        }
    }
    
    public void refillSlotFromInventory(PlayerInventory inventory, int hotbarSlot) {
        // Look for the same item type in main inventory
        for (int i = 9; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            
            if (stack.isEmpty()) {
                continue;
            }
            
            String itemId = Registries.ITEM.getId(stack.getItem()).toString();
            
            // Check if this item should be refilled based on config
            if (!shouldRefillItem(itemId, stack)) {
                continue;
            }
            
            // Move item to hotbar
            inventory.setStack(hotbarSlot, stack.copy());
            inventory.setStack(i, ItemStack.EMPTY);
            
            AutoInventorySorter.LOGGER.debug("[AutoInventorySorter] Refilled hotbar slot {} with {}", 
                hotbarSlot, itemId);
            break;
        }
    }
    
    public void refillConsumedItem(PlayerInventory inventory, int hotbarSlot, ItemStack previousStack) {
        if (!config.autoRefillHotbar) {
            return;
        }
        
        ItemStack currentStack = inventory.getStack(hotbarSlot);
        
        // Check if item was consumed (slot is now empty or stack size decreased significantly)
        boolean wasConsumed = currentStack.isEmpty() || 
            (currentStack.getItem() == previousStack.getItem() && 
             currentStack.getCount() < previousStack.getCount());
        
        if (!wasConsumed) {
            return;
        }
        
        String itemId = Registries.ITEM.getId(previousStack.getItem()).toString();
        
        // Search for same item in main inventory
        for (int i = 9; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            
            if (stack.isEmpty() || stack.getItem() != previousStack.getItem()) {
                continue;
            }
            
            // If current slot is empty, move entire stack
            if (currentStack.isEmpty()) {
                inventory.setStack(hotbarSlot, stack.copy());
                inventory.setStack(i, ItemStack.EMPTY);
                AutoInventorySorter.LOGGER.debug("[AutoInventorySorter] Auto-refilled {} in slot {}", 
                    itemId, hotbarSlot);
                return;
            }
            
            // If current slot has same item but less, try to merge
            if (currentStack.getItem() == stack.getItem()) {
                int maxCount = currentStack.getMaxCount();
                int needed = maxCount - currentStack.getCount();
                int available = stack.getCount();
                int toTransfer = Math.min(needed, available);
                
                if (toTransfer > 0) {
                    currentStack.setCount(currentStack.getCount() + toTransfer);
                    stack.setCount(stack.getCount() - toTransfer);
                    
                    if (stack.getCount() <= 0) {
                        inventory.setStack(i, ItemStack.EMPTY);
                    }
                    
                    AutoInventorySorter.LOGGER.debug("[AutoInventorySorter] Refilled {} x{} in slot {}", 
                        itemId, toTransfer, hotbarSlot);
                    return;
                }
            }
        }
    }
    
    private boolean shouldRefillItem(String itemId, ItemStack stack) {
        // Check config settings for what to refill
        if (config.refillFood && ItemUtil.matchesCategory(stack, "food")) {
            return true;
        }
        
        if (config.refillBlocks && ItemUtil.matchesCategory(stack, "building_block")) {
            return true;
        }
        
        if (config.refillTorches && itemId.contains("torch")) {
            return true;
        }
        
        if (config.refillArrows && ItemUtil.matchesCategory(stack, "arrow")) {
            return true;
        }
        
        return false;
    }
}
