package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

abstract class EcosystemDataProvider(private val output: PackOutput) : DataProvider {
    protected val namespace = Constants.MOD_ID

    protected fun save(cache: CachedOutput, json: JsonObject, path: Path) = DataProvider.saveStable(cache, json, path)

    protected fun resourcePath(relative: String) = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(namespace).resolve(relative)
    protected fun dataPath(relative: String) = output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(namespace).resolve(relative)
    protected fun minecraftDataPath(relative: String) = output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve("minecraft").resolve(relative)

    protected fun itemModelDefinition(model: String) = obj {
        add("model", obj {
            addProperty("type", "minecraft:model")
            addProperty("model", model)
        })
    }

    protected fun itemResult(id: String, count: Int = 1) = obj {
        addProperty("id", "$namespace:$id")
        addProperty("count", count)
    }

    protected fun minecraftItemResult(id: String, count: Int = 1) = obj {
        addProperty("id", "minecraft:$id")
        addProperty("count", count)
    }

    protected fun ingredient(id: String) = "$namespace:$id"
    protected fun minecraftIngredient(id: String) = "minecraft:$id"

    protected fun valuesTag(vararg values: String) = obj {
        add("values", JsonArray().also { values.forEach(it::add) })
    }

    protected fun itemLootEntry(id: String) = obj {
        addProperty("type", "minecraft:item")
        addProperty("name", "$namespace:$id")
    }
        protected fun array(value: JsonObject) = JsonArray().also { it.add(value) }


    protected fun selfDropLootTable(id: String) = obj {
        addProperty("type", "minecraft:block")
        add("pools", JsonArray().also { pools ->
            pools.add(obj {
                addProperty("rolls", 1)
                add("entries", JsonArray().also { it.add(itemLootEntry(id)) })
            })
        })
    }

    /** Vanilla-style leaves loot table: shears/silk touch drops the leaves, otherwise a fortune-scaled chance of a sapling plus a separate fortune-scaled stick pool (see e.g. ZReference oak_leaves.json). */
    protected fun leavesLootTable(leafId: String, saplingId: String, saplingChances: List<Double>, stickChances: List<Double>) = obj {
        val shearsOrSilkTouch = obj {
            addProperty("condition", "minecraft:any_of")
            add("terms", JsonArray().also { terms ->
                terms.add(obj {
                    addProperty("condition", "minecraft:match_tool")
                    add("predicate", obj { addProperty("items", "minecraft:shears") })
                })
                terms.add(obj {
                    addProperty("condition", "minecraft:match_tool")
                    add("predicate", obj {
                        add("predicates", obj {
                            add("minecraft:enchantments", JsonArray().also {
                                it.add(obj {
                                    addProperty("enchantments", "minecraft:silk_touch")
                                    add("levels", obj { addProperty("min", 1) })
                                })
                            })
                        })
                    })
                })
            })
        }
        addProperty("type", "minecraft:block")
        add("pools", JsonArray().also { pools ->
            pools.add(obj {
                addProperty("rolls", 1.0)
                add("entries", JsonArray().also { entries ->
                    entries.add(obj {
                        addProperty("type", "minecraft:alternatives")
                        add("children", JsonArray().also { children ->
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                add("conditions", JsonArray().also { it.add(shearsOrSilkTouch) })
                                addProperty("name", "$namespace:$leafId")
                            })
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                add("conditions", JsonArray().also {
                                    it.add(obj { addProperty("condition", "minecraft:survives_explosion") })
                                    it.add(obj {
                                        addProperty("condition", "minecraft:table_bonus")
                                        addProperty("enchantment", "minecraft:fortune")
                                        add("chances", JsonArray().also { c -> saplingChances.forEach(c::add) })
                                    })
                                })
                                addProperty("name", "$namespace:$saplingId")
                            })
                        })
                    })
                })
            })
            pools.add(obj {
                addProperty("rolls", 1.0)
                add("conditions", JsonArray().also {
                    it.add(obj {
                        addProperty("condition", "minecraft:inverted")
                        add("term", shearsOrSilkTouch)
                    })
                })
                add("entries", JsonArray().also { entries ->
                    entries.add(obj {
                        addProperty("type", "minecraft:item")
                        addProperty("name", "minecraft:stick")
                        add("conditions", JsonArray().also {
                            it.add(obj {
                                addProperty("condition", "minecraft:table_bonus")
                                addProperty("enchantment", "minecraft:fortune")
                                add("chances", JsonArray().also { c -> stickChances.forEach(c::add) })
                            })
                        })
                        add("functions", JsonArray().also { fns ->
                            fns.add(obj {
                                addProperty("function", "minecraft:set_count")
                                add("count", obj {
                                    addProperty("type", "minecraft:uniform")
                                    addProperty("min", 1.0)
                                    addProperty("max", 2.0)
                                })
                            })
                            fns.add(obj { addProperty("function", "minecraft:explosion_decay") })
                        })
                    })
                })
            })
        })
        addProperty("random_sequence", "$namespace:blocks/$leafId")
    }

    protected fun cookingRecipe(type: String, ingredient: String, result: String = "aquamarine", time: Int = 200, experience: Float = 1.0f) = obj {
        addProperty("type", type)
        addProperty("group", if (result.startsWith("abyssalite")) "abyssalite" else "aquamarine")
        addProperty("ingredient", "$namespace:$ingredient")
        add("result", itemResult(result))
        addProperty("experience", experience)
        addProperty("cookingtime", time)
    }

    protected fun shapedRecipe(result: String, category: String, pattern: Array<String>, key: String, ingredient: String) = obj {
        addProperty("type", "minecraft:crafting_shaped")
        addProperty("category", category)
        add("pattern", JsonArray().also { pattern.forEach(it::add) })
        add("key", obj { addProperty(key, "$namespace:$ingredient") })
        add("result", itemResult(result))
    }

    protected fun shapelessRecipe(result: String, count: Int, ingredient: String) = obj {
        addProperty("type", "minecraft:crafting_shapeless")
        addProperty("category", "misc")
        add("ingredients", JsonArray().also { it.add("$namespace:$ingredient") })
        add("result", itemResult(result, count))
    }

    protected fun recipeAdvancement(recipe: String, category: String, unlock: String) = obj {
        addProperty("parent", "minecraft:recipes/root")
        add("criteria", obj {
            add("has_$unlock", obj { addProperty("trigger", "minecraft:inventory_changed"); add("conditions", obj { add("items", JsonArray().also { it.add(obj { add("items", JsonArray().also { ids -> ids.add("$namespace:$unlock") }) }) }) }) })
            add("has_the_recipe", obj { addProperty("trigger", "minecraft:recipe_unlocked"); add("conditions", obj { addProperty("recipe", "$namespace:$recipe") }) })
        })
        add("requirements", JsonArray().also { it.add(JsonArray().also { group -> group.add("has_$unlock"); group.add("has_the_recipe") }) })
        add("rewards", obj { add("recipes", JsonArray().also { it.add("$namespace:$recipe") }) })
    }

    protected fun obj(block: JsonObject.() -> Unit): JsonObject = JsonObject().apply(block)
    protected fun array(vararg values: String) = JsonArray().also { values.forEach(it::add) }

    protected fun modelFiles(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>, ids: Array<String>, block: Boolean = false) {
        ids.forEach { id ->
            writes += save(cache, obj {
                addProperty("parent", "minecraft:item/generated")
                add("textures", obj { addProperty("layer0", "$namespace:item/$id") })
            }, resourcePath("models/item/$id.json"))
            writes += save(cache, itemModelDefinition("$namespace:${if (block) "block" else "item"}/$id"), resourcePath("items/$id.json"))
        }
    }

    protected fun displayName(id: String) = id.split('_').joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
}
