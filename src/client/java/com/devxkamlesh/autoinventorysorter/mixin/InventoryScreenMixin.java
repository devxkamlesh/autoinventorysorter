package com.devxkamlesh.autoinventorysorter.mixin;

import com.devxkamlesh.autoinventorysorter.AutoInventorySorter;
import com.devxkamlesh.autoinventorysorter.sorting.InventorySorter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends HandledScreen<PlayerScreenHandler> {

    @Unique
    private static final int BUTTON_SIZE = 20;
    @Unique
    private int sortButtonX;
    @Unique
    private int sortButtonY;
    @Unique
    private boolean isHoveringButton = false;

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        // Position button relative to inventory
        sortButtonX = this.x + AutoInventorySorter.getConfig().sortButtonX;
        sortButtonY = this.y + AutoInventorySorter.getConfig().sortButtonY;
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!AutoInventorySorter.getConfig().showSortButtonInInventory) {
            return;
        }

        // Check if mouse is hovering over button
        isHoveringButton = mouseX >= sortButtonX && mouseX < sortButtonX + BUTTON_SIZE &&
                          mouseY >= sortButtonY && mouseY < sortButtonY + BUTTON_SIZE;

        // Draw button background
        int color = isHoveringButton ? 0x80FFFFFF : 0x80808080;
        context.fill(sortButtonX, sortButtonY, sortButtonX + BUTTON_SIZE, sortButtonY + BUTTON_SIZE, color);
        
        // Draw button border
        context.drawBorder(sortButtonX, sortButtonY, BUTTON_SIZE, BUTTON_SIZE, isHoveringButton ? 0xFFFFFFFF : 0xFF404040);

        // Draw sort icon (simple arrows)
        drawSortIcon(context, sortButtonX + 2, sortButtonY + 2);

        // Draw tooltip if hovering
        if (isHoveringButton) {
            context.drawTooltip(this.textRenderer, Text.literal("Sort Inventory (R)"), mouseX, mouseY);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir) {
        if (!AutoInventorySorter.getConfig().showSortButtonInInventory) {
            return;
        }

        if (button == 0 && isHoveringButton) { // Left click
            // Trigger sort
            if (this.client != null && this.client.player != null) {
                InventorySorter sorter = new InventorySorter(AutoInventorySorter.getConfig());
                sorter.sort(this.client.player.getInventory());
                
                if (AutoInventorySorter.getConfig().organizeHotbar) {
                    sorter.sortHotbar(this.client.player.getInventory());
                }
                
                AutoInventorySorter.LOGGER.info("[AutoInventorySorter] Sort triggered by button click");
            }
            cir.setReturnValue(true);
        }
    }

    @Unique
    private void drawSortIcon(DrawContext context, int x, int y) {
        // Draw simple sort arrows icon
        int color = isHoveringButton ? 0xFFFFFFFF : 0xFFAAAAAA;
        
        // Up arrow
        context.fill(x + 7, y + 2, x + 9, y + 3, color);
        context.fill(x + 6, y + 3, x + 10, y + 4, color);
        context.fill(x + 5, y + 4, x + 11, y + 5, color);
        
        // Down arrow
        context.fill(x + 5, y + 11, x + 11, y + 12, color);
        context.fill(x + 6, y + 12, x + 10, y + 13, color);
        context.fill(x + 7, y + 13, x + 9, y + 14, color);
    }
}
