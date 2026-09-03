package com.suprememc

import com.google.gson.JsonArray
import com.suprememc.loot.ModLootInjections
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class AbyssaliteDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val items = arrayOf("abyssalite_scrap", "abyssalite_ingot", "abyssalite_upgrade_smithing_template", "abyssalite_pickaxe", "abyssalite_axe", "abyssalite_shovel", "abyssalite_hoe", "abyssalite_sword", "abyssalite_helmet", "abyssalite_chestplate", "abyssalite_leggings", "abyssalite_boots", "abyssalite_trident")

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writeBlock(cache, writes, "atlantis_debris")
        writeBlock(cache, writes, "abyssalite_block")
        writes += save(cache, configuredFeature(), dataPath("worldgen/configured_feature/atlantis_debris.json"))
        writes += save(cache, placedFeature(), dataPath("worldgen/placed_feature/atlantis_debris.json"))
        writes += save(cache, obj { addProperty("type", "neoforge:add_features"); addProperty("biomes", "#minecraft:is_ocean"); addProperty("features", "$namespace:atlantis_debris"); addProperty("step", "underground_ores") }, dataPath("neoforge/biome_modifier/add_atlantis_debris.json"))
        writes += save(cache, selfDropLootTable("atlantis_debris"), dataPath("loot_table/blocks/atlantis_debris.json"))
        writes += save(cache, obj { addProperty("type", "minecraft:crafting_shapeless"); addProperty("category", "misc"); add("ingredients", JsonArray().apply { repeat(4) { add(ingredient("abyssalite_scrap")); add("minecraft:prismarine_crystals") } }); add("result", itemResult("abyssalite_ingot")) }, dataPath("recipe/abyssalite_ingot.json"))
        writes += save(cache, recipeAdvancement("abyssalite_ingot", "misc", "abyssalite_scrap"), dataPath("advancement/recipes/misc/abyssalite_ingot.json"))
        writes += save(cache, cookingRecipe("minecraft:smelting", "atlantis_debris", "abyssalite_scrap", 200, 2.0f), dataPath("recipe/abyssalite_scrap_from_smelting_atlantis_debris.json"))
        writes += save(cache, recipeAdvancement("abyssalite_scrap_from_smelting_atlantis_debris", "misc", "atlantis_debris"), dataPath("advancement/recipes/misc/abyssalite_scrap_from_smelting_atlantis_debris.json"))
        writes += save(cache, cookingRecipe("minecraft:blasting", "atlantis_debris", "abyssalite_scrap", 100, 2.0f), dataPath("recipe/abyssalite_scrap_from_blasting_atlantis_debris.json"))
        writes += save(cache, recipeAdvancement("abyssalite_scrap_from_blasting_atlantis_debris", "misc", "atlantis_debris"), dataPath("advancement/recipes/misc/abyssalite_scrap_from_blasting_atlantis_debris.json"))
        writes += save(cache, shapedRecipe("abyssalite_block", "building", arrayOf("AAA", "AAA", "AAA"), "A", "abyssalite_ingot"), dataPath("recipe/abyssalite_block.json"))
        writes += save(cache, shapelessRecipe("abyssalite_ingot", 9, "abyssalite_block"), dataPath("recipe/abyssalite_ingot_from_block.json"))
        writes += save(cache, obj { addProperty("type", "minecraft:crafting_shapeless"); addProperty("category", "misc"); add("ingredients", JsonArray().apply { add(ingredient("abyssalite_upgrade_smithing_template")); repeat(7) { add("minecraft:diamond") }; add("minecraft:prismarine") }); add("result", itemResult("abyssalite_upgrade_smithing_template", 2)) }, dataPath("recipe/abyssalite_upgrade_smithing_template.json"))
        listOf("helmet", "chestplate", "leggings", "boots", "pickaxe", "axe", "shovel", "hoe", "sword").forEach { id ->
            writes += save(cache, smithing("abyssalite_$id", "aquamarine_$id"), dataPath("recipe/abyssalite_${id}_smithing.json"))
            writes += save(cache, recipeAdvancement("abyssalite_${id}_smithing", "misc", "abyssalite_ingot"), dataPath("advancement/recipes/misc/abyssalite_${id}_smithing.json"))
        }
        writes += save(cache, smithing("abyssalite_trident", "minecraft:trident"), dataPath("recipe/abyssalite_trident_smithing.json"))
        modelFiles(cache, writes, items.filter { it != "abyssalite_trident" }.toTypedArray())
        writes += save(cache, tridentModel(), resourcePath("models/item/abyssalite_trident.json"))
        writes += save(cache, tridentDefinition(), resourcePath("items/abyssalite_trident.json"))
        writes += save(cache, equipmentAsset(), resourcePath("equipment/abyssalite.json"))
        writes += save(cache, valuesTag("$namespace:abyssalite_ingot"), dataPath("tags/item/abyssalite_repair_items.json"))
        writes += save(cache, valuesTag("$namespace:atlantis_debris", "$namespace:abyssalite_block"), minecraftDataPath("tags/block/needs_diamond_tool.json"))
        writeInjections(cache, writes)
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun writeBlock(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>, id: String) {
        writes += save(cache, obj { add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) }) }, resourcePath("blockstates/$id.json"))
        val model = if (id == "atlantis_debris") obj {
            addProperty("parent", "minecraft:block/cube_bottom_top")
            add("textures", obj { addProperty("side", "$namespace:block/atlantis_debris_side"); addProperty("top", "$namespace:block/atlantis_debris_top"); addProperty("bottom", "$namespace:block/atlantis_debris_top") })
        } else obj { addProperty("parent", "minecraft:block/cube_all"); add("textures", obj { addProperty("all", "$namespace:block/$id") }) }
        writes += save(cache, model, resourcePath("models/block/$id.json"))
        writes += save(cache, obj { addProperty("parent", "$namespace:block/$id") }, resourcePath("models/item/$id.json"))
        writes += save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json"))
        if (id == "atlantis_debris") writes += save(cache, selfDropLootTable(id), dataPath("loot_table/blocks/$id.json"))
    }

    private fun smithing(result: String, base: String) = obj { addProperty("type", "minecraft:smithing_transform"); addProperty("template", ingredient("abyssalite_upgrade_smithing_template")); addProperty("base", if (base.startsWith("minecraft:")) base else ingredient(base)); addProperty("addition", ingredient("abyssalite_ingot")); add("result", itemResult(result)) }
    private fun configuredFeature() = obj { addProperty("type", "minecraft:ore"); add("config", obj { addProperty("discard_chance_on_air_exposure", 0.5f); addProperty("size", 2); add("targets", JsonArray().also { listOf("minecraft:stone_ore_replaceables", "minecraft:deepslate_ore_replaceables").forEach { tag -> it.add(oreTarget("$namespace:atlantis_debris", tag, true)) }; it.add(oreTarget("$namespace:atlantis_debris", "minecraft:tuff", false)); it.add(oreTarget("$namespace:atlantis_debris", "minecraft:dripstone_block", false)) }) }) }
    private fun oreTarget(block: String, value: String, tag: Boolean) = obj { add("state", obj { addProperty("Name", block) }); add("target", obj { addProperty("predicate_type", if (tag) "minecraft:tag_match" else "minecraft:block_match"); addProperty(if (tag) "tag" else "block", value) }) }
    private fun placedFeature() = obj { addProperty("feature", "$namespace:atlantis_debris"); add("placement", JsonArray().also { it.add(obj { addProperty("type", "minecraft:count"); addProperty("count", 2) }); it.add(obj { addProperty("type", "minecraft:in_square") }); it.add(obj { addProperty("type", "minecraft:height_range"); add("height", obj { addProperty("type", "minecraft:uniform"); add("min_inclusive", obj { addProperty("absolute", -64) }); add("max_inclusive", obj { addProperty("absolute", -49) }) }) }); it.add(obj { addProperty("type", "minecraft:biome") }) }) }
    private fun tridentModel() = obj { addProperty("parent", "minecraft:item/generated"); add("textures", obj { addProperty("layer0", "$namespace:item/abyssalite_trident") }) }
    private fun floats(vararg values: Float) = JsonArray().also { arr -> values.forEach { arr.add(it) } }

    // Mirrors vanilla items/trident.json: flat model for gui/ground/fixed/on_shelf, the registered
    // suprememc:abyssalite_trident special model in hand, and the throwing pose while using the item.
    private fun tridentDefinition() = obj {
        add("model", obj {
            addProperty("type", "minecraft:select")
            add("cases", JsonArray().also {
                it.add(obj {
                    add("model", obj { addProperty("type", "minecraft:model"); addProperty("model", "$namespace:item/abyssalite_trident") })
                    add("when", array("gui", "ground", "fixed", "on_shelf"))
                })
            })
            add("fallback", obj {
                addProperty("type", "minecraft:condition")
                addProperty("property", "minecraft:using_item")
                add("on_false", specialTrident("minecraft:item/trident_in_hand"))
                add("on_true", specialTrident("minecraft:item/trident_throwing"))
                add("transformation", obj {
                    add("left_rotation", floats(0f, 0f, 0f, 1f))
                    add("right_rotation", floats(0f, 0f, 0f, 1f))
                    add("scale", floats(1f, -1f, -1f))
                    add("translation", floats(0f, 0f, 0f))
                })
            })
            addProperty("property", "minecraft:display_context")
        })
    }
    private fun specialTrident(base: String) = obj {
        addProperty("type", "minecraft:special")
        addProperty("base", base)
        add("model", obj { addProperty("type", "$namespace:abyssalite_trident") })
    }
    private fun equipmentAsset() = obj { add("layers", obj { add("humanoid", layer()); add("humanoid_baby", layer()); add("humanoid_leggings", layer()) }) }
    private fun layer() = JsonArray().also { it.add(obj { addProperty("texture", "$namespace:abyssalite") }) }

    private fun writeInjections(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>) {
        writes += save(cache, templateLoot(null), dataPath("loot_table/inject/abyssalite_template_guaranteed.json"))
        writes += save(cache, templateLoot(ModLootInjections.RARE_ABYSSALITE_TEMPLATE_CHANCE), dataPath("loot_table/inject/abyssalite_template_rare.json"))
        ModLootInjections.ABYSSALITE_TEMPLATE_INJECTIONS.forEach { injection ->
            writes += save(cache, obj { addProperty("type", "neoforge:add_table"); add("conditions", JsonArray().also { it.add(obj { addProperty("condition", "neoforge:loot_table_id"); addProperty("loot_table_id", injection.targetTable()) }) }); addProperty("table", injection.injectedTable()) }, dataPath("loot_modifiers/${injection.name()}.json"))
        }
    }
    private fun templateLoot(chance: Float?) = obj { addProperty("type", "minecraft:chest"); add("pools", JsonArray().also { it.add(obj { addProperty("rolls", 1); add("entries", JsonArray().also { entries -> entries.add(itemLootEntry("abyssalite_upgrade_smithing_template").apply { if (chance != null) add("conditions", JsonArray().also { it.add(obj { addProperty("condition", "minecraft:random_chance"); addProperty("chance", chance) }) }) }) }) }) }) }
    override fun getName() = "SupremeMC abyssalite resources"
}
