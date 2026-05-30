package com.devxkamlesh.autoinventorysorter;

import com.devxkamlesh.autoinventorysorter.config.SorterConfig;
import com.devxkamlesh.autoinventorysorter.profile.ProfileManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoInventorySorter implements ModInitializer {

    public static final String MOD_ID = "autoinventorysorter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static SorterConfig config;
    private static ProfileManager profileManager;

    @Override
    public void onInitialize() {
        LOGGER.info("[AutoInventorySorter] Initializing...");

        config = SorterConfig.load();
        profileManager = new ProfileManager(config);

        LOGGER.info("[AutoInventorySorter] Loaded profile: {}", config.getActiveProfile());
        LOGGER.info("[AutoInventorySorter] Ready!");
    }

    public static SorterConfig getConfig() {
        return config;
    }

    public static ProfileManager getProfileManager() {
        return profileManager;
    }
}
