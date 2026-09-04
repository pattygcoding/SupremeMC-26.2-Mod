package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.suprememc.loot.ModLootInjections
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class CalamariDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val items = arrayOf("calamari", "cooked_calamari", "grapes", "tomato", "corn")

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        modelFiles(cache, writes, items)
        listOf("smelting" to 200, "smoking" to 100, "campfire_cooking" to 600).forEach { (kind, time) ->
            val id = "cooked_calamari_from_${kind}_calamari"
            writes += save(cache, cookingRecipe("minecraft:$kind", "calamari", "cooked_calamari", time, 0.35F), dataPath("recipe/$id.json"))
            writes += save(cache, recipeAdvancement(id, "food", "calamari"), dataPath("advancement/recipes/food/$id.json"))
        }
        writes += save(cache, valuesTag("$namespace:calamari", "$namespace:cooked_calamari"), minecraftDataPath("tags/item/meat.json"))
        writes += save(cache, valuesTag("$namespace:calamari", "$namespace:cooked_calamari"), cDataPath("tags/item/foods.json"))
        writes += save(cache, valuesTag("$namespace:calamari"), cDataPath("tags/item/foods/raw_meat.json"))
        writes += save(cache, valuesTag("$namespace:calamari"), cDataPath("tags/item/foods/fish.json"))
        writes += save(cache, valuesTag("$namespace:cooked_calamari"), cDataPath("tags/item/foods/cooked_meat.json"))
        writes += save(cache, valuesTag("$namespace:cooked_calamari"), cDataPath("tags/item/foods/cooked_fish.json"))
        writes += save(cache, squidLoot(true), dataPath("loot_table/inject/squid_calamari.json"))
        writes += save(cache, squidLoot(false), dataPath("loot_table/inject/glow_squid_calamari.json"))
        ModLootInjections.CALAMARI_INJECTIONS.forEach { injection ->
            writes += save(cache, obj {
                addProperty("type", "neoforge:add_table")
                add("conditions", JsonArray().also { it.add(obj { addProperty("condition", "neoforge:loot_table_id"); addProperty("loot_table_id", injection.targetTable()) }) })
                addProperty("table", injection.injectedTable())
            }, dataPath("loot_modifiers/${injection.name()}.json"))
        }
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun squidLoot(cooksWhenBurning: Boolean) = obj {
        addProperty("type", "minecraft:entity")
        add("pools", JsonArray().also { pools -> pools.add(obj {
            addProperty("rolls", 1)
            add("entries", JsonArray().also { entries ->
                if (cooksWhenBurning) entries.add(obj {
                    addProperty("type", "minecraft:alternatives")
                    add("children", JsonArray().also { children ->
                        children.add(calamariEntry("cooked_calamari", onFireCondition()))
                        children.add(calamariEntry("calamari", obj { addProperty("condition", "minecraft:inverted"); add("term", onFireCondition()) }))
                    })
                }) else entries.add(calamariEntry("calamari"))
            })
        }) })
    }

    private fun onFireCondition() = obj {
        addProperty("condition", "minecraft:entity_properties")
        addProperty("entity", "this")
        add("predicate", obj { add("flags", obj { addProperty("is_on_fire", true) }) })
    }

    private fun calamariEntry(id: String, condition: JsonObject? = null) = itemLootEntry(id).apply {
        if (condition != null) add("conditions", array(condition))
        add("functions", JsonArray().also { functions ->
            functions.add(obj { addProperty("function", "minecraft:set_count"); add("count", obj { addProperty("type", "minecraft:uniform"); addProperty("min", 1); addProperty("max", 2) }) })
            functions.add(obj { addProperty("function", "minecraft:enchanted_count_increase"); addProperty("enchantment", "minecraft:looting"); addProperty("count", 1) })
        })
    }

    override fun getName() = "SupremeMC calamari"
}