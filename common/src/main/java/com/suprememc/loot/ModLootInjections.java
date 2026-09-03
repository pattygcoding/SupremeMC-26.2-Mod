package com.suprememc.loot;

import com.suprememc.Constants;

import java.util.List;

/**
 * Shared description of the extra loot the mod appends to vanilla chest loot tables.
 * NeoForge consumes this through generated global loot modifiers, Fabric through {@code LootTableEvents.MODIFY}.
 */
public final class ModLootInjections {

    public record Injection(String name, String targetTable, String injectedTable) {
    }

    public static final String GUARANTEED_ABYSSALITE_TEMPLATE = Constants.MOD_ID + ":inject/abyssalite_template_guaranteed";
    public static final String RARE_ABYSSALITE_TEMPLATE = Constants.MOD_ID + ":inject/abyssalite_template_rare";

    public static final float RARE_ABYSSALITE_TEMPLATE_CHANCE = 0.1F;

    public static final List<Injection> ABYSSALITE_TEMPLATE_INJECTIONS = List.of(
            new Injection("add_abyssalite_template_buried_treasure", "minecraft:chests/buried_treasure", GUARANTEED_ABYSSALITE_TEMPLATE),
            new Injection("add_abyssalite_template_underwater_ruin_small", "minecraft:chests/underwater_ruin_small", RARE_ABYSSALITE_TEMPLATE),
            new Injection("add_abyssalite_template_underwater_ruin_big", "minecraft:chests/underwater_ruin_big", RARE_ABYSSALITE_TEMPLATE),
            new Injection("add_abyssalite_template_shipwreck_treasure", "minecraft:chests/shipwreck_treasure", RARE_ABYSSALITE_TEMPLATE));

    private ModLootInjections() {
    }
}
