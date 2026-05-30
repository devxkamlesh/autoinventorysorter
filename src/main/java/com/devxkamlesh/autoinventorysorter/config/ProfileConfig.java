package com.devxkamlesh.autoinventorysorter.config;

import java.util.*;

public class ProfileConfig {

    public String name;
    public SorterConfig.SortMode sortMode;
    public boolean organizeHotbar;
    public boolean autoCleanJunk;
    public boolean moveValuablesToTop;
    public boolean crystalPvpPreset;
    public boolean potionQuickAccess;
    public Map<Integer, String> fixedSlots = new HashMap<>();

    public static ProfileConfig survivalDefaults() {
        ProfileConfig p = new ProfileConfig();
        p.name = "Survival";
        p.sortMode = SorterConfig.SortMode.ITEM_TYPE;
        p.organizeHotbar = true;
        p.autoCleanJunk = false;
        p.moveValuablesToTop = true;
        p.crystalPvpPreset = false;
        p.potionQuickAccess = false;
        // Slot 0=Sword, 1=Pickaxe, 2=Axe, 3=Shovel, 4=Food, 8=Totem
        p.fixedSlots.put(0, "sword");
        p.fixedSlots.put(1, "pickaxe");
        p.fixedSlots.put(2, "axe");
        p.fixedSlots.put(3, "shovel");
        p.fixedSlots.put(4, "food");
        p.fixedSlots.put(8, "totem");
        return p;
    }

    public static ProfileConfig pvpDefaults() {
        ProfileConfig p = new ProfileConfig();
        p.name = "PvP";
        p.sortMode = SorterConfig.SortMode.ITEM_TYPE;
        p.organizeHotbar = true;
        p.autoCleanJunk = true;
        p.moveValuablesToTop = false;
        p.crystalPvpPreset = false;
        p.potionQuickAccess = true;
        // Slot 0=Sword, 1=Axe, 2=Bow/Crossbow, 3=Potion, 4=Potion, 5=Potion, 6=Food, 7=Blocks, 8=Totem
        p.fixedSlots.put(0, "sword");
        p.fixedSlots.put(1, "axe");
        p.fixedSlots.put(2, "bow");
        p.fixedSlots.put(3, "potion");
        p.fixedSlots.put(4, "potion");
        p.fixedSlots.put(5, "potion");
        p.fixedSlots.put(6, "food");
        p.fixedSlots.put(8, "totem");
        return p;
    }

    public static ProfileConfig miningDefaults() {
        ProfileConfig p = new ProfileConfig();
        p.name = "Mining";
        p.sortMode = SorterConfig.SortMode.ITEM_TYPE;
        p.organizeHotbar = true;
        p.autoCleanJunk = true;
        p.moveValuablesToTop = true;
        p.crystalPvpPreset = false;
        p.potionQuickAccess = false;
        // Slot 0=Pickaxe, 1=Sword, 2=Shovel, 3=Torch, 4=Food, 5=Bucket, 8=Totem
        p.fixedSlots.put(0, "pickaxe");
        p.fixedSlots.put(1, "sword");
        p.fixedSlots.put(2, "shovel");
        p.fixedSlots.put(3, "torch");
        p.fixedSlots.put(4, "food");
        p.fixedSlots.put(8, "totem");
        return p;
    }

    public static ProfileConfig buildingDefaults() {
        ProfileConfig p = new ProfileConfig();
        p.name = "Building";
        p.sortMode = SorterConfig.SortMode.ITEM_TYPE;
        p.organizeHotbar = true;
        p.autoCleanJunk = false;
        p.moveValuablesToTop = false;
        p.crystalPvpPreset = false;
        p.potionQuickAccess = false;
        // Slot 0=Primary Block, 1=Secondary Block, 2=Scaffolding, 3=Axe, 4=Shovel, 5=Pickaxe, 8=Food
        p.fixedSlots.put(0, "building_block");
        p.fixedSlots.put(1, "building_block");
        p.fixedSlots.put(2, "scaffold");
        p.fixedSlots.put(3, "axe");
        p.fixedSlots.put(4, "shovel");
        p.fixedSlots.put(5, "pickaxe");
        p.fixedSlots.put(8, "food");
        return p;
    }
}
