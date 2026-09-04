package com.suprememc.content

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Guards against the two regressions this project has hit before: generated models pointing at
 * textures that were never supplied, and registered content missing its `en_us` translation
 * (including the SupremeMC creative tab title, which previously had no tab registered to use it).
 */
class TextureAndLangCoverageTest {

    @Test
    fun everyGeneratedModelTextureReferenceHasABackingPngFile() {
        val modelDirs = listOf("assets/suprememc/models/item", "assets/suprememc/models/block")
        var checked = 0
        for (dir in modelDirs) {
            val dirPath = generatedResources.resolve(dir)
            if (!Files.isDirectory(dirPath)) continue
            Files.list(dirPath).use { paths ->
                paths.filter { it.toString().endsWith(".json") }.forEach { modelPath ->
                    val model = JsonParser.parseString(Files.readString(modelPath)).asJsonObject
                    val textures = model.getAsJsonObject("textures") ?: return@forEach
                    textures.entrySet().forEach { (layer, value) ->
                        val reference = value.asString
                        if (!reference.startsWith("suprememc:")) return@forEach
                        val texturePath = textures().resolve(reference.removePrefix("suprememc:") + ".png")
                        assertTrue("Model ${modelPath.name} references missing texture '$reference' (layer $layer)") {
                            Files.isRegularFile(texturePath)
                        }
                        assertTrue("Texture referenced by ${modelPath.name} is empty: $texturePath") {
                            Files.size(texturePath) > 0
                        }
                        checked++
                    }
                }
            }
        }
        assertTrue("Expected to validate at least one model texture reference") { checked > 0 }
    }

    @Test
    fun equipmentAssetLayersHaveBackingArmorTextures() {
        val layers = readJson("assets/suprememc/equipment/aquamarine.json").getAsJsonObject("layers")
        layers.entrySet().forEach { (layerName, layerArray) ->
            layerArray.asJsonArray.forEach { layerEntry ->
                val reference = layerEntry.asJsonObject.get("texture").asString.removePrefix("suprememc:")
                val texturePath = textures().resolve("entity/equipment/$layerName/$reference.png")
                assertTrue("Equipment layer '$layerName' references missing texture: $texturePath") {
                    Files.isRegularFile(texturePath)
                }
            }
        }
    }

    @Test
    fun everyGeneratedItemModelHasAnEnglishTranslation() {
        val lang = readJson("assets/suprememc/lang/en_us.json")
        val blockIds = idsFromDirectory("assets/suprememc/blockstates")
        val itemIds = idsFromDirectory("assets/suprememc/models/item")

        assertTrue("Expected registered blocks to be discovered from generated blockstates") { blockIds.isNotEmpty() }
        assertTrue("Expected registered items to be discovered from generated item models") { itemIds.isNotEmpty() }

        blockIds.forEach { id ->
            assertTrue("Missing block translation for '$id' in en_us.json") { lang.has("block.suprememc.$id") }
        }
        itemIds.forEach { id ->
            val hasItemTranslation = lang.has("item.suprememc.$id")
            val isBlockItem = id in blockIds
            assertTrue("Missing item translation for '$id' in en_us.json") { hasItemTranslation || isBlockItem }
        }
    }

    @Test
    fun creativeTabTitleTranslationIsPresent() {
        val lang = readJson("assets/suprememc/lang/en_us.json")
        assertTrue("Missing itemGroup.suprememc.main translation used by the SupremeMC creative tab title") {
            lang.has("itemGroup.suprememc.main")
        }
    }

    @Test
    fun everyRegisteredItemHasAResolvableItemModelDefinition() {
        // MC's item model rework requires assets/<ns>/items/<id>.json to point at a model; without it
        // the item renders as the missing-texture placeholder even though the model/texture files exist.
        val blockIds = idsFromDirectory("assets/suprememc/blockstates")
        val plainItemIds = idsFromDirectory("assets/suprememc/models/item")
        // Blocks whose item form is registered under a different item id need no same-id items/<id>.json
        // (vanilla precedent: sweet_berry_bush is planted by the sweet_berries item; cotton_bush by cotton).
        val blockIdsWithDifferentlyNamedItems = setOf("cotton_bush", "tomato_bush")
        val allItemIds = (blockIds - blockIdsWithDifferentlyNamedItems) + plainItemIds
        assertTrue("Expected to discover at least one registered item") { allItemIds.isNotEmpty() }

        allItemIds.forEach { id ->
            val definition = readJson("assets/suprememc/items/$id.json")
            val model = definition.getAsJsonObject("model")
            // Composite definitions (e.g. the trident's select/special tree) resolve their own models.
            if (model.get("type").asString != "minecraft:model") return@forEach
            val modelId = model.get("model").asString
            assertTrue("Item model definition for '$id' references a namespace outside suprememc: $modelId") {
                modelId.startsWith("suprememc:")
            }

            val modelJsonPath = generatedResources.resolve("assets/suprememc/models/${modelId.removePrefix("suprememc:")}.json")
            assertTrue("Item '$id' references missing model file: $modelJsonPath") { Files.isRegularFile(modelJsonPath) }
        }
    }

    private fun idsFromDirectory(relativeDir: String): Set<String> {
        val dirPath = generatedResources.resolve(relativeDir)
        if (!Files.isDirectory(dirPath)) return emptySet()
        Files.list(dirPath).use { paths ->
            return paths.filter { it.toString().endsWith(".json") }
                .map { it.nameWithoutExtension }
                .toList()
                .toSet()
        }
    }

    private fun readJson(relativePath: String): JsonObject {
        val path = generatedResources.resolve(relativePath)
        assertTrue("Missing generated resource: $relativePath") { Files.isRegularFile(path) }
        return JsonParser.parseString(Files.readString(path)).asJsonObject
    }

    private fun textures(): Path = Path.of("..", "common", "src", "main", "resources", "assets", "suprememc", "textures")

    private companion object {
        val generatedResources: Path = Path.of("src", "generated", "resources")
    }
}
