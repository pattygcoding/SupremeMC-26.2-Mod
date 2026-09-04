package com.suprememc

import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class IcicleDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val thicknesses = listOf("base", "frustum", "middle", "tip", "tip_merge")

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, blockState(), resourcePath("blockstates/icicle.json"))
        thicknesses.forEach { thickness ->
            listOf("down", "up").forEach { direction ->
                val id = "icicle_${direction}_$thickness"
                writes += save(cache, model(id), resourcePath("models/block/$id.json"))
            }
        }
        writes += save(cache, model("icicle", "minecraft:item/generated", "$namespace:block/icicle_down_tip"), resourcePath("models/item/icicle.json"))
        writes += save(cache, itemModelDefinition("$namespace:item/icicle"), resourcePath("items/icicle.json"))
        writes += save(cache, valuesTag("$namespace:icicle"), minecraftDataPath("tags/block/speleothems.json"))
        writes += save(cache, selfDropLootTable("icicle"), dataPath("loot_table/blocks/icicle.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun blockState() = obj {
        add("variants", obj {
            thicknesses.forEach { thickness ->
                listOf("down", "up").forEach { direction ->
                    add("thickness=$thickness,vertical_direction=$direction", obj {
                        addProperty("model", "$namespace:block/icicle_${direction}_$thickness")
                    })
                }
            }
        })
    }

    private fun model(id: String): JsonObject = model(id, "minecraft:block/pointed_dripstone", "$namespace:block/$id")

    private fun model(id: String, parent: String, texture: String): JsonObject = obj {
        addProperty("parent", parent)
        add("textures", obj { addProperty(if (parent == "minecraft:item/generated") "layer0" else "cross", texture) })
    }

    override fun getName() = "SupremeMC icicle resources"
}