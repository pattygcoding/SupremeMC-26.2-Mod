package com.suprememc

import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class NetherBoatDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val boatTypes = listOf("crimson", "warped")

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()

        boatTypes.forEach { woodType ->
            val boat = "${woodType}_boat"
            val chestBoat = "${woodType}_chest_boat"
            writes += save(cache, itemModel(boat), resourcePath("models/item/$boat.json"))
            writes += save(cache, itemModel(chestBoat), resourcePath("models/item/$chestBoat.json"))
            writes += save(cache, itemModelDefinition("$namespace:item/$boat"), resourcePath("items/$boat.json"))
            writes += save(cache, itemModelDefinition("$namespace:item/$chestBoat"), resourcePath("items/$chestBoat.json"))

            writes += save(cache, shapedRecipe(boat, "misc", arrayOf("# #", "###"), "#", "${woodType}_planks").apply {
                getAsJsonObject("key").addProperty("#", "minecraft:${woodType}_planks")
            }, dataPath("recipe/$boat.json"))
            writes += save(cache, recipeAdvancement(boat, "misc", "minecraft:${woodType}_planks"), dataPath("advancement/recipes/misc/$boat.json"))
            writes += save(cache, shapelessRecipe(chestBoat, 1, boat).apply {
                getAsJsonArray("ingredients").add("minecraft:chest")
            }, dataPath("recipe/$chestBoat.json"))
            writes += save(cache, recipeAdvancement(chestBoat, "misc", boat), dataPath("advancement/recipes/misc/$chestBoat.json"))

        }

        writes += save(cache, valuesTag(*boatTypes.map { "$namespace:${it}_boat" }.toTypedArray()), minecraftDataPath("tags/item/boats.json"))
        writes += save(cache, valuesTag(*boatTypes.map { "$namespace:${it}_chest_boat" }.toTypedArray()), minecraftDataPath("tags/item/chest_boats.json"))

        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun itemModel(id: String): JsonObject = obj {
        addProperty("parent", "minecraft:item/generated")
        add("textures", obj { addProperty("layer0", "$namespace:item/$id") })
    }

    override fun getName() = "SupremeMC crimson and warped boat recipes"
}
