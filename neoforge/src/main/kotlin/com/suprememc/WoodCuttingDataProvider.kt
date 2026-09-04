package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class WoodCuttingDataProvider(output: PackOutput) : EcosystemDataProvider(output) {

	private enum class Kind { LOG_WOOD, STEM_HYPHAE, BAMBOO }

	private data class WoodType(val name: String, val modded: Boolean, val kind: Kind, val boatName: String?)

	private val woodTypes = listOf(
		WoodType("oak", false, Kind.LOG_WOOD, "boat"),
		WoodType("spruce", false, Kind.LOG_WOOD, "boat"),
		WoodType("birch", false, Kind.LOG_WOOD, "boat"),
		WoodType("jungle", false, Kind.LOG_WOOD, "boat"),
		WoodType("acacia", false, Kind.LOG_WOOD, "boat"),
		WoodType("dark_oak", false, Kind.LOG_WOOD, "boat"),
		WoodType("mangrove", false, Kind.LOG_WOOD, "boat"),
		WoodType("cherry", false, Kind.LOG_WOOD, "boat"),
		WoodType("pale_oak", false, Kind.LOG_WOOD, "boat"),
		WoodType("palm", true, Kind.LOG_WOOD, "boat"),
		WoodType("crimson", false, Kind.STEM_HYPHAE, null),
		WoodType("warped", false, Kind.STEM_HYPHAE, null),
		WoodType("bamboo", false, Kind.BAMBOO, "raft"),
	)

	// Matches vanilla's recipe-book category per result item (see ZReference advancement/recipes/**).
	private val categoryByResultSuffix = mapOf(
		"stairs" to "building_blocks",
		"slab" to "building_blocks",
		"fence" to "decorations",
		"fence_gate" to "redstone",
		"sign" to "decorations",
		"crafting_table" to "decorations",
		"chest" to "decorations",
		"door" to "redstone",
		"trapdoor" to "redstone",
		"pressure_plate" to "redstone",
		"button" to "redstone",
		"boat" to "transportation",
		"raft" to "transportation",
		"bowl" to "misc",
	)

	override fun run(cache: CachedOutput): CompletableFuture<*> {
		val writes = mutableListOf<CompletableFuture<*>>()

		woodTypes.forEach { wood ->
			val planksResultSuffixes = listOf(
				"fence_gate" to 1,
				"fence" to 1,
				"pressure_plate" to 1,
				"button" to 1,
				"stairs" to 1,
				"slab" to 2,
				"door" to 1,
				"trapdoor" to 1,
				"sign" to 1,
				"bowl" to 2,
				"chest" to 1,
				"crafting_table" to 1,
			) + (wood.boatName?.let { listOf(it to 1) } ?: emptyList())

			planksResultSuffixes.forEach { (resultSuffix, count) ->
				val result = if (resultSuffix in listOf("bowl", "chest", "crafting_table")) resultSuffix else "${wood.name}_$resultSuffix"
				writes += stonecuttingRecipeWithAdvancement(
					cache, "${wood.name}_planks", result, count,
					resultModded = wood.modded && resultSuffix !in listOf("bowl", "chest", "crafting_table"),
					ingredientModded = wood.modded,
					category = categoryByResultSuffix.getValue(resultSuffix)
				)
			}

			blockConversions(wood).forEach { (ingredient, results) ->
				results.forEach { (result, count) ->
					writes += stonecuttingRecipeWithAdvancement(
						cache, ingredient, result, count,
						resultModded = wood.modded, ingredientModded = wood.modded,
						category = "building_blocks"
					)
				}
			}
		}

		// Bamboo mosaic is a distinct block family layered on top of bamboo planks.
		writes += stonecuttingRecipeWithAdvancement(cache, "bamboo_planks", "bamboo_mosaic", 1, false, false, "decorations")
		writes += stonecuttingRecipeWithAdvancement(cache, "bamboo_planks", "bamboo_mosaic_stairs", 1, false, false, "building_blocks")
		writes += stonecuttingRecipeWithAdvancement(cache, "bamboo_planks", "bamboo_mosaic_slab", 2, false, false, "building_blocks")
		writes += stonecuttingRecipeWithAdvancement(cache, "bamboo_mosaic", "bamboo_mosaic_stairs", 1, false, false, "building_blocks")
		writes += stonecuttingRecipeWithAdvancement(cache, "bamboo_mosaic", "bamboo_mosaic_slab", 2, false, false, "building_blocks")

		return CompletableFuture.allOf(*writes.toTypedArray())
	}

	// log/wood <-> planks for ordinary trees; stem/hyphae <-> planks for nether fungi (no boat); a single
	// bamboo_block for bamboo (no "wood" counterpart).
	private fun blockConversions(wood: WoodType): List<Pair<String, List<Pair<String, Int>>>> = when (wood.kind) {
		Kind.LOG_WOOD -> listOf(
			"${wood.name}_log" to listOf("stripped_${wood.name}_log" to 1, "${wood.name}_planks" to 4),
			"${wood.name}_wood" to listOf("stripped_${wood.name}_wood" to 1, "${wood.name}_planks" to 4),
			"stripped_${wood.name}_log" to listOf("${wood.name}_planks" to 4),
			"stripped_${wood.name}_wood" to listOf("${wood.name}_planks" to 4),
		)
		Kind.STEM_HYPHAE -> listOf(
			"${wood.name}_stem" to listOf("stripped_${wood.name}_stem" to 1, "${wood.name}_planks" to 4),
			"${wood.name}_hyphae" to listOf("stripped_${wood.name}_hyphae" to 1, "${wood.name}_planks" to 4),
			"stripped_${wood.name}_stem" to listOf("${wood.name}_planks" to 4),
			"stripped_${wood.name}_hyphae" to listOf("${wood.name}_planks" to 4),
		)
		Kind.BAMBOO -> listOf(
			"bamboo_block" to listOf("stripped_bamboo_block" to 1, "bamboo_planks" to 2),
			"stripped_bamboo_block" to listOf("bamboo_planks" to 2),
		)
	}

	private fun stonecuttingRecipeWithAdvancement(
		cache: CachedOutput, ingredient: String, result: String, count: Int,
		resultModded: Boolean, ingredientModded: Boolean, category: String
	): CompletableFuture<*> {
		val recipeName = "${result}_from_${ingredient}_stonecutting"
		val recipeId = "$namespace:$recipeName"
		val ingredientId = if (ingredientModded) this.ingredient(ingredient) else minecraftIngredient(ingredient)
		val recipeWrite = save(cache, stonecuttingRecipe(ingredientId, result, count, resultModded), dataPath("recipe/$recipeName.json"))
		val advancementWrite = save(
			cache, recipeAdvancement(recipeId, ingredientId), dataPath("advancement/recipes/$category/$recipeName.json")
		)
		return CompletableFuture.allOf(recipeWrite, advancementWrite)
	}

	private fun stonecuttingRecipe(ingredientId: String, result: String, count: Int, resultModded: Boolean) = obj {
		addProperty("type", "minecraft:stonecutting")
		addProperty("ingredient", ingredientId)
		add("result", if (resultModded) itemResult(result, count) else minecraftItemResult(result, count))
	}

	// Mirrors vanilla's stonecutting recipe-unlock advancements (see ZReference andesite_stairs_from_andesite_stonecutting.json).
	private fun recipeAdvancement(recipeId: String, ingredientId: String) = obj {
		addProperty("parent", "minecraft:recipes/root")
		val ingredientName = ingredientId.substringAfter(':')
		add("criteria", obj {
			add("has_$ingredientName", obj {
				addProperty("trigger", "minecraft:inventory_changed")
				add("conditions", obj { add("items", JsonArray().also { it.add(obj { addProperty("items", ingredientId) }) }) })
			})
			add("has_the_recipe", obj {
				addProperty("trigger", "minecraft:recipe_unlocked")
				add("conditions", obj { addProperty("recipe", recipeId) })
			})
		})
		add("requirements", JsonArray().also { requirements ->
			requirements.add(JsonArray().also { it.add("has_the_recipe"); it.add("has_$ingredientName") })
		})
		add("rewards", obj { add("recipes", JsonArray().also { it.add(recipeId) }) })
	}

	override fun getName() = "SupremeMC wood stonecutting recipes"
}
