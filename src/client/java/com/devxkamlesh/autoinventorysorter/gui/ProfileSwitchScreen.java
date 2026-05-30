package com.devxkamlesh.autoinventorysorter.gui;

import com.devxkamlesh.autoinventorysorter.AutoInventorySorter;
import com.devxkamlesh.autoinventorysorter.config.ProfileConfig;
import com.devxkamlesh.autoinventorysorter.profile.ProfileManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.Map;

public class ProfileSwitchScreen extends Screen {

    private final Screen parent;
    private final ProfileManager profileManager;

    public ProfileSwitchScreen(Screen parent, ProfileManager profileManager) {
        super(Text.literal("Select Profile"));
        this.parent = parent;
        this.profileManager = profileManager;
    }

    @Override
    protected void init() {
        super.init();

        Map<String, ProfileConfig> profiles = profileManager.getAllProfiles();
        int yStart = height / 2 - (profiles.size() * 25) / 2;
        int i = 0;

        for (Map.Entry<String, ProfileConfig> entry : profiles.entrySet()) {
            final String profileId = entry.getKey();
            String displayName = entry.getValue().name;
            boolean isActive = profileId.equals(profileManager.getActiveProfile());

            String label = (isActive ? "§a✔ " : "") + displayName;

            addDrawableChild(ButtonWidget.builder(Text.literal(label), btn -> {
                profileManager.switchProfile(profileId);
                client.setScreen(parent);
            }).dimensions(width / 2 - 80, yStart + i * 25, 160, 20).build());

            i++;
        }

        addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), btn -> {
            client.setScreen(parent);
        }).dimensions(width / 2 - 50, yStart + profiles.size() * 25 + 10, 100, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, "Select Inventory Profile", width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
