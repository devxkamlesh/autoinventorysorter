package com.devxkamlesh.autoinventorysorter.config;

import com.devxkamlesh.autoinventorysorter.AutoInventorySorter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SorterConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("autoinventorysorter.json");

    // General
    public boolean enabled = true;
    public boolean autoSortOnOpen = true;
    public boolean showSortButton = true;
    public SortMode sortMode = SortMode.ITEM_TYPE;

    // Hotbar
    public boolean organizeHotbar = true;
    public boolean lockSword = true;
    public int swordSlot = 0;
    public boolean lockPickaxe = true;
    public int pickaxeSlot = 1;
    public boolean lockAxe = true;
    public int axeSlot = 2;
    public boolean lockTotem = true;
    public int totemSlot = 8;
    
    // Hotbar Slot Locking (new feature)
    public boolean enableSlotLocking = true;
    public Map<Integer, String> lockedSlotCategories = new HashMap<>(); // slot -> category (e.g., "sword", "pickaxe")
    
    // Auto-refill Hotbar (new feature)
    public boolean autoRefillHotbar = true;
    public boolean refillFood = true;
    public boolean refillBlocks = true;
    public boolean refillTorches = true;
    public boolean refillArrows = true;

    // Smart Features
    public boolean autoStack = true;
    public boolean autoMergeStacks = true;
    public boolean compactEmptySlots = true;
    public boolean autoCleanJunk = false;
    public Set<String> junkItems = new HashSet<>(Arrays.asList(
        "minecraft:rotten_flesh",
        "minecraft:poisonous_potato",
        "minecraft:spider_eye",
        "minecraft:bone",
        "minecraft:gravel",
        "minecraft:flint"
    ));

    // Survival
    public boolean moveFoodTogether = true;
    public boolean moveOresToSlots = true;
    public boolean movePotionsTogether = true;
    public boolean moveArrowsTogether = true;
    public boolean moveBuildingBlocksTogether = true;
    public boolean moveValuablesToTop = true;
    public Set<String> valuableItems = new HashSet<>(Arrays.asList(
        "minecraft:diamond",
        "minecraft:emerald",
        "minecraft:netherite_ingot",
        "minecraft:netherite_scrap",
        "minecraft:ancient_debris",
        "minecraft:gold_ingot",
        "minecraft:iron_ingot"
    ));

    // Locked/Favorite items (won't be moved)
    public Set<String> lockedItems = new HashSet<>();
    public Set<Integer> lockedSlots = new HashSet<>();

    // Profiles
    public String activeProfile = "survival";
    public Map<String, ProfileConfig> profiles = new HashMap<>();

    // PvP
    public boolean crystalPvpPreset = false;
    public boolean potionQuickAccess = true;

    // Chest/Shulker
    public boolean smartChestOrganization = false;
    public boolean smartShulkerOrganization = false;
    
    // Chest Sorting (new feature)
    public boolean enableChestSorting = true;
    public boolean sortChestsOnOpen = false; // auto-sort when opening
    public boolean sortChestsWithKey = true; // sort with R key
    
    // Blacklist System (new feature)
    public boolean enableBlacklist = true;
    public Set<String> blacklistedItems = new HashSet<>(Arrays.asList(
        "minecraft:compass",
        "minecraft:clock",
        "minecraft:map"
    ));
    public Set<Integer> blacklistedSlots = new HashSet<>(); // slots that won't be sorted
    
    // UI Features (new feature)
    public boolean showSortButtonInInventory = true;
    public int sortButtonX = 130;
    public int sortButtonY = 60;

    // Keybinds
    public String sortKey = "key.keyboard.r";

    public enum SortMode {
        ITEM_TYPE, RARITY, NAME_AZ, QUANTITY, DURABILITY
    }

    public static SorterConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                SorterConfig cfg = GSON.fromJson(reader, SorterConfig.class);
                if (cfg != null) {
                    AutoInventorySorter.LOGGER.info("[AutoInventorySorter] Config loaded.");
                    return cfg;
                }
            } catch (Exception e) {
                AutoInventorySorter.LOGGER.error("[AutoInventorySorter] Failed to load config, using defaults.", e);
            }
        }
        SorterConfig defaults = new SorterConfig();
        defaults.initDefaultProfiles();
        defaults.save();
        return defaults;
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(this, writer);
            }
        } catch (Exception e) {
            AutoInventorySorter.LOGGER.error("[AutoInventorySorter] Failed to save config.", e);
        }
    }

    public void initDefaultProfiles() {
        profiles.put("survival", ProfileConfig.survivalDefaults());
        profiles.put("pvp", ProfileConfig.pvpDefaults());
        profiles.put("mining", ProfileConfig.miningDefaults());
        profiles.put("building", ProfileConfig.buildingDefaults());
    }

    public String getActiveProfile() {
        return activeProfile;
    }

    public ProfileConfig getActiveProfileConfig() {
        return profiles.getOrDefault(activeProfile, ProfileConfig.survivalDefaults());
    }
}
