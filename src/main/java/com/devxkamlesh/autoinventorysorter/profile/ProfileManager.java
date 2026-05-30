package com.devxkamlesh.autoinventorysorter.profile;

import com.devxkamlesh.autoinventorysorter.AutoInventorySorter;
import com.devxkamlesh.autoinventorysorter.config.ProfileConfig;
import com.devxkamlesh.autoinventorysorter.config.SorterConfig;

import java.util.*;

public class ProfileManager {

    private final SorterConfig config;
    public static final List<String> BUILT_IN_PROFILES = List.of("survival", "pvp", "mining", "building");

    public ProfileManager(SorterConfig config) {
        this.config = config;
        // Ensure defaults exist
        if (config.profiles.isEmpty()) {
            config.initDefaultProfiles();
            config.save();
        }
    }

    public void switchProfile(String profileId) {
        if (!config.profiles.containsKey(profileId)) {
            AutoInventorySorter.LOGGER.warn("[AutoInventorySorter] Profile '{}' not found!", profileId);
            return;
        }
        config.activeProfile = profileId;
        applyProfileSettings(config.profiles.get(profileId));
        config.save();
        AutoInventorySorter.LOGGER.info("[AutoInventorySorter] Switched to profile: {}", profileId);
    }

    private void applyProfileSettings(ProfileConfig profile) {
        config.sortMode = profile.sortMode;
        config.organizeHotbar = profile.organizeHotbar;
        config.autoCleanJunk = profile.autoCleanJunk;
        config.moveValuablesToTop = profile.moveValuablesToTop;
        config.crystalPvpPreset = profile.crystalPvpPreset;
        config.potionQuickAccess = profile.potionQuickAccess;
    }

    public void createProfile(String id, String name) {
        if (config.profiles.containsKey(id)) {
            AutoInventorySorter.LOGGER.warn("[AutoInventorySorter] Profile '{}' already exists!", id);
            return;
        }
        ProfileConfig newProfile = ProfileConfig.survivalDefaults();
        newProfile.name = name;
        config.profiles.put(id, newProfile);
        config.save();
    }

    public void deleteProfile(String id) {
        if (BUILT_IN_PROFILES.contains(id)) {
            AutoInventorySorter.LOGGER.warn("[AutoInventorySorter] Cannot delete built-in profile: {}", id);
            return;
        }
        config.profiles.remove(id);
        if (config.activeProfile.equals(id)) {
            config.activeProfile = "survival";
        }
        config.save();
    }

    public Map<String, ProfileConfig> getAllProfiles() {
        return Collections.unmodifiableMap(config.profiles);
    }

    public String getActiveProfile() {
        return config.activeProfile;
    }

    public ProfileConfig getActiveProfileConfig() {
        return config.getActiveProfileConfig();
    }

    public void exportProfile(String id) {
        // Future: export to JSON file
        AutoInventorySorter.LOGGER.info("[AutoInventorySorter] Export profile: {} (coming soon)", id);
    }

    public void importProfile(String path) {
        // Future: import from JSON file
        AutoInventorySorter.LOGGER.info("[AutoInventorySorter] Import profile from: {} (coming soon)", path);
    }
}
