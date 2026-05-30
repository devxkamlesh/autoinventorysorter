package com.devxkamlesh.autoinventorysorter.sorting;

import net.minecraft.item.*;

public enum ItemCategory {
    SWORD(0),
    AXE(1),
    PICKAXE(2),
    SHOVEL(3),
    HOE(4),
    BOW(5),
    CROSSBOW(6),
    TRIDENT(7),
    ARMOR(8),
    TOTEM(9),
    POTION(10),
    FOOD(11),
    VALUABLE(12),
    ORE(13),
    BUILDING_BLOCK(14),
    WOOD(15),
    STONE(16),
    SAND(17),
    REDSTONE(18),
    ARROW(19),
    TORCH(20),
    BUCKET(21),
    TOOL_MISC(22),
    MISC(100);

    public final int priority;

    ItemCategory(int priority) {
        this.priority = priority;
    }

    public static ItemCategory of(ItemStack stack) {
        if (stack.isEmpty()) return MISC;
        Item item = stack.getItem();
        String id = net.minecraft.registry.Registries.ITEM.getId(item).toString();

        // Check by ID patterns for tools and weapons
        if (id.contains("_sword")) return SWORD;
        if (id.contains("_axe")) return AXE;
        if (id.contains("_pickaxe")) return PICKAXE;
        if (id.contains("_shovel")) return SHOVEL;
        if (id.contains("_hoe")) return HOE;
        if (id.equals("minecraft:bow")) return BOW;
        if (id.equals("minecraft:crossbow")) return CROSSBOW;
        if (id.equals("minecraft:trident")) return TRIDENT;
        
        // Check for armor
        if (id.contains("_helmet") || id.contains("_chestplate") || id.contains("_leggings") || id.contains("_boots")) return ARMOR;

        if (id.equals("minecraft:totem_of_undying")) return TOTEM;
        if (id.contains("potion")) return POTION;
        if (item.getComponents().contains(net.minecraft.component.DataComponentTypes.FOOD)) return FOOD;
        if (id.contains("arrow")) return ARROW;

        if (isValuable(id)) return VALUABLE;
        if (isOre(id)) return ORE;
        if (isBuildingBlock(item, id)) return BUILDING_BLOCK;

        if (id.contains("torch")) return TORCH;
        if (id.contains("bucket")) return BUCKET;
        if (id.contains("log") || id.contains("plank") || id.contains("wood")) return WOOD;
        if (id.contains("stone") || id.contains("cobblestone") || id.contains("andesite")
                || id.contains("diorite") || id.contains("granite")) return STONE;
        if (id.contains("sand") || id.contains("gravel")) return SAND;
        if (id.contains("redstone") || id.contains("repeater") || id.contains("comparator")
                || id.contains("piston") || id.contains("lever") || id.contains("button")) return REDSTONE;

        return MISC;
    }

    private static boolean isValuable(String id) {
        return id.contains("diamond") || id.contains("emerald") || id.contains("netherite")
                || id.contains("ancient_debris") || id.contains("gold_ingot")
                || id.contains("iron_ingot") || id.contains("gold_block")
                || id.contains("diamond_block") || id.contains("emerald_block");
    }

    private static boolean isOre(String id) {
        return id.contains("_ore") || id.contains("raw_iron") || id.contains("raw_gold")
                || id.contains("raw_copper");
    }

    private static boolean isBuildingBlock(Item item, String id) {
        return item instanceof BlockItem
                && !id.contains("sapling")
                && !id.contains("seed")
                && !id.contains("torch");
    }
}
