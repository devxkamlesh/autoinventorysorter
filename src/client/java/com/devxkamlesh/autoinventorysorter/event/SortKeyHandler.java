package com.devxkamlesh.autoinventorysorter.event;

import com.devxkamlesh.autoinventorysorter.AutoInventorySorter;
import com.devxkamlesh.autoinventorysorter.inventory.ChestSorter;
import com.devxkamlesh.autoinventorysorter.sorting.InventorySorter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class SortKeyHandler {

    private static KeyBinding sortKey;

    public static KeyBinding getSortKey() {
        return sortKey;
    }

    public static void register() {
        sortKey = new KeyBinding(
                "key.autoinventorysorter.sort",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.autoinventorysorter"
        );

        // Register tick event
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (sortKey.wasPressed()) {
                handleSortKey(client);
            }
        });
    }

    private static void handleSortKey(MinecraftClient client) {
        if (client.player == null) return;
        if (!AutoInventorySorter.getConfig().enabled) return;

        // Check if player is viewing a chest/container
        if (client.player.currentScreenHandler != null && 
            client.currentScreen != null &&
            AutoInventorySorter.getConfig().sortChestsWithKey) {
            
            // Try to sort the container
            var screenHandler = client.player.currentScreenHandler;
            if (screenHandler.slots.size() > 36) { // Has container slots
                // Get the container inventory (first inventory that's not player's)
                var inventory = screenHandler.slots.get(0).inventory;
                if (inventory != client.player.getInventory()) {
                    ChestSorter chestSorter = new ChestSorter(AutoInventorySorter.getConfig());
                    chestSorter.sort(inventory);
                    AutoInventorySorter.LOGGER.info("[AutoInventorySorter] Sorted chest/container");
                    return;
                }
            }
        }

        InventorySorter sorter = new InventorySorter(AutoInventorySorter.getConfig());

        // Sort main inventory
        sorter.sort(client.player.getInventory());

        // Sort hotbar if configured
        if (AutoInventorySorter.getConfig().organizeHotbar) {
            sorter.sortHotbar(client.player.getInventory());
        }

        AutoInventorySorter.LOGGER.info("[AutoInventorySorter] Sort triggered by key");
    }
}
