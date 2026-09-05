package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class CakeDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, cakeLoot(), minecraftDataPath("loot_table/blocks/cake.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    /** Overrides vanilla's empty cake loot table: a whole (uneaten) cake drops itself when broken with a Silk Touch tool. */
    private fun cakeLoot() = obj {
        addProperty("type", "minecraft:block")
        addProperty("random_sequence", "minecraft:blocks/cake")
        add("pools", JsonArray().also { pools ->
            pools.add(obj {
                addProperty("rolls", 1)
                add("entries", JsonArray().also { entries ->
                    entries.add(obj {
                        addProperty("type", "minecraft:item")
                        addProperty("name", "minecraft:cake")
                        add("conditions", JsonArray().also {
                            it.add(silkTouchCondition())
                            it.add(wholeCakeCondition())
                        })
                    })
                })
            })
        })
    }

    private fun silkTouchCondition() = obj { addProperty("condition", "minecraft:match_tool"); add("predicate", obj { add("predicates", obj { add("minecraft:enchantments", JsonArray().also { it.add(obj { addProperty("enchantments", "minecraft:silk_touch"); add("levels", obj { addProperty("min", 1) }) }) }) }) }) }

    private fun wholeCakeCondition() = obj {
        addProperty("condition", "minecraft:block_state_property")
        addProperty("block", "minecraft:cake")
        add("properties", obj { addProperty("bites", "0") })
    }

    override fun getName() = "SupremeMC cake loot"
}
