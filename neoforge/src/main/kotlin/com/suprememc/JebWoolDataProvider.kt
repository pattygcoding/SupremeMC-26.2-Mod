package com.suprememc

import com.google.gson.JsonArray
import com.suprememc.loot.ModLootInjections
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class JebWoolDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, obj {
            add("variants", obj { add("", obj { addProperty("model", "$namespace:block/jeb_wool") }) })
        }, resourcePath("blockstates/jeb_wool.json"))
        writes += save(cache, obj {
            addProperty("parent", "minecraft:block/cube_all")
            add("textures", obj { addProperty("all", "$namespace:block/jeb_wool") })
        }, resourcePath("models/block/jeb_wool.json"))
        writes += save(cache, obj {
            addProperty("parent", "$namespace:block/jeb_wool")
        }, resourcePath("models/item/jeb_wool.json"))
        writes += save(cache, itemModelDefinition("$namespace:block/jeb_wool"), resourcePath("items/jeb_wool.json"))
        writes += save(cache, selfDropLootTable("jeb_wool"), dataPath("loot_table/blocks/jeb_wool.json"))
        writes += save(cache, jebWoolLoot(), dataPath("loot_table/inject/jeb_wool.json"))
        ModLootInjections.JEB_WOOL_INJECTIONS.forEach { injection ->
            writes += save(cache, obj {
                addProperty("type", "neoforge:add_table")
                add("conditions", JsonArray().also { conditions ->
                    conditions.add(obj {
                        addProperty("condition", "neoforge:loot_table_id")
                        addProperty("loot_table_id", injection.targetTable())
                    })
                })
                addProperty("table", injection.injectedTable())
            }, dataPath("loot_modifiers/${injection.name()}.json"))
        }
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun jebWoolLoot() = obj {
        addProperty("type", "minecraft:entity")
        add("pools", JsonArray().also { pools ->
            pools.add(obj {
                addProperty("rolls", 1)
                add("entries", JsonArray().also { entries ->
                    entries.add(itemLootEntry("jeb_wool").apply {
                        add("conditions", JsonArray().also { conditions ->
                            conditions.add(obj {
                                addProperty("condition", "minecraft:entity_properties")
                                addProperty("entity", "this")
                                add("predicate", obj {
                                    add("minecraft:components", obj {
                                        addProperty("minecraft:custom_name", "{\"text\":\"jeb_\"}")
                                    })
                                })
                            })
                        })
                    })
                })
            })
        })
    }

    override fun getName() = "SupremeMC jeb wool"
}