package com.devxkamlesh.autoinventorysorter.client;

import com.devxkamlesh.autoinventorysorter.AutoInventorySorter;
import com.devxkamlesh.autoinventorysorter.event.SortKeyHandler;
import com.devxkamlesh.autoinventorysorter.hotbar.HotbarRefiller;
import com.devxkamlesh.autoinventorysorter.sorting.InventorySorter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class AutoInventorySorterClient implements ClientModInitializer {

    private static int tickCounter = 0;
    private static final int HOTBAR_SYNC_INTERVAL = 40; // every 2 seconds
    private static final int REFILL_CHECK_INTERVAL = 5; // check every 5 ticks
    private static final Map<Integer, ItemStack> previousHotbarStacks = new HashMap<>();

    @Override
    public void onInitializeClient() {
        AutoInventorySorter.LOGGER.info("[AutoInventorySorter] Client initializing...");

        // Register sort key handler first (this creates the keybinding)
        SortKeyHandler.register();

        // Now register the keybinding with Fabric
        KeyBindingHelper.registerKeyBinding(SortKeyHandler.getSortKey());

        // Register client tick event for hotbar organization and auto-refill
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            if (!AutoInventorySorter.getConfig().enabled) return;

            tickCounter++;
            
            // Auto-refill check (more frequent)
            if (AutoInventorySorter.getConfig().autoRefillHotbar && tickCounter % REFILL_CHECK_INTERVAL == 0) {
                checkAndRefillHotbar(client);
            }
            
            // Hotbar organization (less frequent)
            if (AutoInventorySorter.getConfig().organizeHotbar && tickCounter >= HOTBAR_SYNC_INTERVAL) {
                tickCounter = 0;
                InventorySorter sorter = new InventorySorter(AutoInventorySorter.getConfig());
                sorter.sortHotbar(client.player.getInventory());
            }
        });

        // Register screen open event for auto-sort on inventory open
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof InventoryScreen)) return;
            if (!AutoInventorySorter.getConfig().autoSortOnOpen) return;
            if (!AutoInventorySorter.getConfig().enabled) return;
            if (client.player == null) return;

            InventorySorter sorter = new InventorySorter(AutoInventorySorter.getConfig());
            sorter.sort(client.player.getInventory());

            AutoInventorySorter.LOGGER.debug("[AutoInventorySorter] Auto-sorted on inventory open");
        });

        AutoInventorySorter.LOGGER.info("[AutoInventorySorter] Client ready. Press [R] to sort!");
        AutoInventorySorter.LOGGER.info("[AutoInventorySorter] Features: Slot Locking, Auto-Refill, Chest Sorting, Blacklist");
    }
    
    private static void checkAndRefillHotbar(net.minecraft.client.MinecraftClient client) {
        if (client.player == null) return;
        
        HotbarRefiller refiller = new HotbarRefiller(AutoInventorySorter.getConfig());
        
        // Check each hotbar slot for consumed items
        for (int slot = 0; slot < 9; slot++) {
            ItemStack currentStack = client.player.getInventory().getStack(slot);
            ItemStack previousStack = previousHotbarStacks.get(slot);
            
            if (previousStack != null && !previousStack.isEmpty()) {
                // Check if item was consumed
                if (currentStack.isEmpty() || 
                    (currentStack.getItem() == previousStack.getItem() && 
                     currentStack.getCount() < previousStack.getCount())) {
                    refiller.refillConsumedItem(client.player.getInventory(), slot, previousStack);
                }
            }
            
            // Update previous stack
            previousHotbarStacks.put(slot, currentStack.copy());
        }
    }
}
