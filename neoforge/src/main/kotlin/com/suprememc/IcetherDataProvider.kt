package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class IcetherDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, obj {
            add("variants", obj {
                add("axis=x", obj { addProperty("model", "$namespace:block/icether_portal_ns") })
                add("axis=z", obj { addProperty("model", "$namespace:block/icether_portal_ew") })
            })
        }, resourcePath("blockstates/icether_portal.json"))
        writes += save(cache, portalModel("north", "south", 0, 6, 16, 10), resourcePath("models/block/icether_portal_ns.json"))
        writes += save(cache, portalModel("east", "west", 6, 0, 10, 16), resourcePath("models/block/icether_portal_ew.json"))
        writes += save(cache, itemModelDefinition("$namespace:block/icether_portal_ns"), resourcePath("items/icether_portal.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun portalModel(firstFace: String, secondFace: String, fromX: Int, fromZ: Int, toX: Int, toZ: Int): JsonObject = obj {
        add("textures", obj { addProperty("particle", "$namespace:block/icether_portal"); addProperty("portal", "$namespace:block/icether_portal") })
        add("elements", JsonArray().also { elements -> elements.add(obj {
            add("from", JsonArray().also { it.add(fromX); it.add(0); it.add(fromZ) })
            add("to", JsonArray().also { it.add(toX); it.add(16); it.add(toZ) })
            add("faces", obj { add(firstFace, obj { addProperty("texture", "#portal") }); add(secondFace, obj { addProperty("texture", "#portal") }) })
        }) })
    }

    override fun getName() = "SupremeMC Icether portal"
}