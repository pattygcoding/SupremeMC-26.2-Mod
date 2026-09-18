package com.suprememc

import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class NetherReactorCoreDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val id = "nether_reactor_core"
        val writes = listOf(
            save(cache, blockState(), resourcePath("blockstates/$id.json")),
            save(cache, cubeModel(id), resourcePath("models/block/$id.json")),
            save(cache, cubeModel("${id}_active"), resourcePath("models/block/${id}_active.json")),
            save(cache, cubeModel("${id}_inactive"), resourcePath("models/block/${id}_inactive.json")),
            save(cache, itemBlockModel(id), resourcePath("models/item/$id.json")),
            save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json")),
            save(cache, selfDropLootTable(id), dataPath("loot_table/blocks/$id.json")),
            save(cache, recipe(id), dataPath("recipe/$id.json"))
        )
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun blockState() = obj {
        add("variants", obj {
            add("reactor_state=unused", obj { addProperty("model", "$namespace:block/nether_reactor_core") })
            add("reactor_state=active", obj { addProperty("model", "$namespace:block/nether_reactor_core_active") })
            add("reactor_state=used", obj { addProperty("model", "$namespace:block/nether_reactor_core_inactive") })
        })
    }

    private fun cubeModel(textureId: String) = obj {
        addProperty("parent", "minecraft:block/cube_all")
        add("textures", obj { addProperty("all", "$namespace:block/$textureId") })
    }

    private fun itemBlockModel(id: String) = obj {
        addProperty("parent", "$namespace:block/$id")
    }

    private fun recipe(id: String) = obj {
        addProperty("type", "minecraft:crafting_shaped")
        addProperty("category", "misc")
        add("pattern", array("IDI", "IDI", "IDI"))
        add("key", obj {
            addProperty("I", "minecraft:iron_ingot")
            addProperty("D", "minecraft:diamond")
        })
        add("result", itemResult(id))
    }

    override fun getName() = "Nether Reactor Core"
}
