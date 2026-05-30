package com.devxkamlesh.autoinventorysorter.hotbar;

import com.devxkamlesh.autoinventorysorter.config.SorterConfig;
import com.devxkamlesh.autoinventorysorter.util.ItemUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class HotbarSlotLocker {
    
    private final SorterConfig config;
    private final Map<Integer, String> lockedSlots = new HashMap<>();
    
    public HotbarSlotLocker(SorterConfig config) {
        this.config = config;
        initializeDefaultLocks();
    }
    
    private void initializeDefaultLocks() {
        if (config.lockSword) {
            lockedSlots.put(config.swordSlot, "sword");
        }
        if (config.lockPickaxe) {
            lockedSlots.put(config.pickaxeSlot, "pickaxe");
        }
        if (config.lockAxe) {
            lockedSlots.put(config.axeSlot, "axe");
        }
        if (config.lockTotem) {
            lockedSlots.put(config.totemSlot, "totem");
        }
        
        // Add custom locked slots from config
        if (config.lockedSlotCategories != null) {
            lockedSlots.putAll(config.lockedSlotCategories);
        }
    }
    
    public boolean isSlotLocked(int slot) {
        return config.enableSlotLocking && lockedSlots.containsKey(slot);
    }
    
    public String getLockedCategory(int slot) {
        return lockedSlots.get(slot);
    }
    
    public void lockSlot(int slot, String category) {
        lockedSlots.put(slot, category);
        if (config.lockedSlotCategories == null) {
            config.lockedSlotCategories = new HashMap<>();
        }
        config.lockedSlotCategories.put(slot, category);
        config.save();
    }
    
    public void unlockSlot(int slot) {
        lockedSlots.remove(slot);
        if (config.lockedSlotCategories != null) {
            config.lockedSlotCategories.remove(slot);
            config.save();
        }
    }
    
    public boolean shouldItemStayInSlot(ItemStack stack, int slot) {
        if (!isSlotLocked(slot)) {
            return false;
        }
        
        String category = getLockedCategory(slot);
        return ItemUtil.matchesCategory(stack, category);
    }
    
    public void enforceLockedSlots(PlayerInventory inventory) {
        if (!config.enableSlotLocking) {
            return;
        }
        
        for (Map.Entry<Integer, String> entry : lockedSlots.entrySet()) {
            int slot = entry.getKey();
            String category = entry.getValue();
            
            if (slot < 0 || slot >= 9) {
                continue; // Only hotbar slots
            }
            
            ItemStack currentStack = inventory.getStack(slot);
            
            // If slot is empty or has wrong item, try to find correct item
            if (currentStack.isEmpty() || !ItemUtil.matchesCategory(currentStack, category)) {
                // Search inventory for matching item
                for (int i = 9; i < inventory.size(); i++) {
                    ItemStack stack = inventory.getStack(i);
                    if (!stack.isEmpty() && ItemUtil.matchesCategory(stack, category)) {
                        // Swap items
                        inventory.setStack(slot, stack.copy());
                        inventory.setStack(i, currentStack.copy());
                        break;
                    }
                }
            }
        }
    }
    
    public Map<Integer, String> getLockedSlots() {
        return new HashMap<>(lockedSlots);
    }
}
