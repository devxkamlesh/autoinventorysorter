package com.devxkamlesh.autoinventorysorter.gui;

import com.devxkamlesh.autoinventorysorter.AutoInventorySorter;
import com.devxkamlesh.autoinventorysorter.config.SorterConfig;
import com.devxkamlesh.autoinventorysorter.profile.ProfileManager;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class SorterConfigScreen {

    private final Screen parent;
    private final SorterConfig config;

    public SorterConfigScreen(Screen parent, SorterConfig config) {
        this.parent = parent;
        this.config = config;
    }

    public Screen build() {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Auto Inventory Sorter"))
                .setSavingRunnable(config::save);

        ConfigEntryBuilder eb = builder.entryBuilder();

        // ─── General ──────────────────────────────────────────────────
        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));

        general.addEntry(eb.startBooleanToggle(Text.literal("Enabled"), config.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.enabled = v)
                .build());

        general.addEntry(eb.startBooleanToggle(Text.literal("Auto Sort on Inventory Open"), config.autoSortOnOpen)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.autoSortOnOpen = v)
                .build());

        general.addEntry(eb.startBooleanToggle(Text.literal("Show Sort Button in Inventory"), config.showSortButton)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.showSortButton = v)
                .build());

        general.addEntry(eb.startEnumSelector(Text.literal("Sort Mode"), SorterConfig.SortMode.class, config.sortMode)
                .setDefaultValue(SorterConfig.SortMode.ITEM_TYPE)
                .setSaveConsumer(v -> config.sortMode = v)
                .build());

        // ─── Hotbar ───────────────────────────────────────────────────
        ConfigCategory hotbar = builder.getOrCreateCategory(Text.literal("Hotbar"));

        hotbar.addEntry(eb.startBooleanToggle(Text.literal("Organize Hotbar"), config.organizeHotbar)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.organizeHotbar = v)
                .build());

        hotbar.addEntry(eb.startBooleanToggle(Text.literal("Lock Sword to Slot"), config.lockSword)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.lockSword = v)
                .build());

        hotbar.addEntry(eb.startIntSlider(Text.literal("Sword Slot"), config.swordSlot, 0, 8)
                .setDefaultValue(0)
                .setSaveConsumer(v -> config.swordSlot = v)
                .build());

        hotbar.addEntry(eb.startBooleanToggle(Text.literal("Lock Pickaxe to Slot"), config.lockPickaxe)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.lockPickaxe = v)
                .build());

        hotbar.addEntry(eb.startIntSlider(Text.literal("Pickaxe Slot"), config.pickaxeSlot, 0, 8)
                .setDefaultValue(1)
                .setSaveConsumer(v -> config.pickaxeSlot = v)
                .build());

        hotbar.addEntry(eb.startBooleanToggle(Text.literal("Lock Axe to Slot"), config.lockAxe)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.lockAxe = v)
                .build());

        hotbar.addEntry(eb.startIntSlider(Text.literal("Axe Slot"), config.axeSlot, 0, 8)
                .setDefaultValue(2)
                .setSaveConsumer(v -> config.axeSlot = v)
                .build());

        hotbar.addEntry(eb.startBooleanToggle(Text.literal("Lock Totem Slot"), config.lockTotem)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.lockTotem = v)
                .build());

        hotbar.addEntry(eb.startIntSlider(Text.literal("Totem Slot"), config.totemSlot, 0, 8)
                .setDefaultValue(8)
                .setSaveConsumer(v -> config.totemSlot = v)
                .build());

        // ─── Smart Features ───────────────────────────────────────────
        ConfigCategory smart = builder.getOrCreateCategory(Text.literal("Smart Features"));

        smart.addEntry(eb.startBooleanToggle(Text.literal("Auto Stack"), config.autoStack)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.autoStack = v)
                .build());

        smart.addEntry(eb.startBooleanToggle(Text.literal("Auto Merge Stacks"), config.autoMergeStacks)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.autoMergeStacks = v)
                .build());

        smart.addEntry(eb.startBooleanToggle(Text.literal("Compact Empty Slots"), config.compactEmptySlots)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.compactEmptySlots = v)
                .build());

        smart.addEntry(eb.startBooleanToggle(Text.literal("Auto Clean Junk"), config.autoCleanJunk)
                .setDefaultValue(false)
                .setSaveConsumer(v -> config.autoCleanJunk = v)
                .build());

        // ─── Survival ─────────────────────────────────────────────────
        ConfigCategory survival = builder.getOrCreateCategory(Text.literal("Survival"));

        survival.addEntry(eb.startBooleanToggle(Text.literal("Move Food Together"), config.moveFoodTogether)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.moveFoodTogether = v)
                .build());

        survival.addEntry(eb.startBooleanToggle(Text.literal("Move Ores to Slots"), config.moveOresToSlots)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.moveOresToSlots = v)
                .build());

        survival.addEntry(eb.startBooleanToggle(Text.literal("Move Potions Together"), config.movePotionsTogether)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.movePotionsTogether = v)
                .build());

        survival.addEntry(eb.startBooleanToggle(Text.literal("Move Arrows Together"), config.moveArrowsTogether)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.moveArrowsTogether = v)
                .build());

        survival.addEntry(eb.startBooleanToggle(Text.literal("Move Valuables to Top"), config.moveValuablesToTop)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.moveValuablesToTop = v)
                .build());

        // ─── PvP ──────────────────────────────────────────────────────
        ConfigCategory pvp = builder.getOrCreateCategory(Text.literal("PvP"));

        pvp.addEntry(eb.startBooleanToggle(Text.literal("Potion Quick Access Layout"), config.potionQuickAccess)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.potionQuickAccess = v)
                .build());

        pvp.addEntry(eb.startBooleanToggle(Text.literal("Crystal PvP Preset"), config.crystalPvpPreset)
                .setDefaultValue(false)
                .setSaveConsumer(v -> config.crystalPvpPreset = v)
                .build());

        // ─── Chest/Shulker ────────────────────────────────────────────
        ConfigCategory chests = builder.getOrCreateCategory(Text.literal("Chests & Shulkers"));

        chests.addEntry(eb.startBooleanToggle(Text.literal("Smart Chest Organization"), config.smartChestOrganization)
                .setDefaultValue(false)
                .setSaveConsumer(v -> config.smartChestOrganization = v)
                .build());

        chests.addEntry(eb.startBooleanToggle(Text.literal("Smart Shulker Organization"), config.smartShulkerOrganization)
                .setDefaultValue(false)
                .setSaveConsumer(v -> config.smartShulkerOrganization = v)
                .build());

        return builder.build();
    }
}
