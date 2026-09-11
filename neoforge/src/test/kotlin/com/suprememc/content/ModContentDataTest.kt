package com.suprememc.content

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ModContentDataTest {
    @Test
    fun generatesMossyStoneBrickRecipesAndModels() {
        listOf("andesite", "diorite", "granite").forEach { stone ->
            val id = "mossy_${stone}_bricks"
            assertResourceExists("assets/suprememc/models/block/$id.json")
            assertResourceExists("assets/suprememc/items/$id.json")
            assertResourceExists("data/suprememc/loot_table/blocks/$id.json")
            assertResourceExists("data/suprememc/advancement/recipes/building_blocks/${id}_from_vine.json")

            val recipe = readJson("data/suprememc/recipe/${id}_from_vine.json")
            assertEquals("minecraft:crafting_shapeless", recipe.get("type").asString)
            assertEquals(
                setOf("suprememc:${stone}_bricks", "minecraft:vine"),
                recipe.getAsJsonArray("ingredients").map { it.asString }.toSet()
            )
            assertEquals("suprememc:$id", recipe.getAsJsonObject("result").get("id").asString)

            listOf("brick_stairs", "brick_slab", "brick_wall").forEach { shape ->
                val shapeId = "mossy_${stone}_$shape"
                assertResourceExists("assets/suprememc/blockstates/$shapeId.json")
                assertResourceExists("assets/suprememc/items/$shapeId.json")
                assertResourceExists("data/suprememc/loot_table/blocks/$shapeId.json")
                val stonecutting = readJson("data/suprememc/recipe/${shapeId}_from_${id}_stonecutting.json")
                assertEquals("minecraft:stonecutting", stonecutting.get("type").asString)
                assertEquals(1, stonecutting.getAsJsonObject("result").get("count").asInt)
            }

            val slabLoot = readJson("data/suprememc/loot_table/blocks/mossy_${stone}_brick_slab.json")
            assertTrue(slabLoot.toString().contains("\"count\":2"))
        }
    }

    @Test
    fun generatesCrackedBrickSmeltingRecipesAndModels() {
        mapOf(
            "andesite" to "suprememc:andesite_bricks",
            "diorite" to "suprememc:diorite_bricks",
            "granite" to "suprememc:granite_bricks",
            "end_stone" to "minecraft:end_stone_bricks",
            "quartz" to "minecraft:quartz_bricks"
        ).forEach { (stone, ingredient) ->
            val id = "cracked_${stone}_bricks"
            assertResourceExists("assets/suprememc/blockstates/$id.json")
            assertResourceExists("assets/suprememc/models/block/$id.json")
            assertResourceExists("assets/suprememc/items/$id.json")
            assertResourceExists("data/suprememc/loot_table/blocks/$id.json")
            assertResourceExists("data/suprememc/advancement/recipes/building_blocks/$id.json")

            val recipe = readJson("data/suprememc/recipe/$id.json")
            assertEquals("minecraft:smelting", recipe.get("type").asString)
            assertEquals("blocks", recipe.get("category").asString)
            assertEquals(ingredient, recipe.get("ingredient").asString)
            assertEquals("suprememc:$id", recipe.getAsJsonObject("result").get("id").asString)
        }
    }

    @Test
    fun generatesCottonArmorResources() {
        listOf("cotton_helmet", "cotton_chestplate", "cotton_leggings", "cotton_boots").forEach { id ->
            listOf(
                "assets/suprememc/models/item/$id.json",
                "assets/suprememc/items/$id.json",
                "data/suprememc/recipe/$id.json",
                "data/suprememc/advancement/recipes/equipment/$id.json"
            ).forEach(::assertResourceExists)
        }
        assertResourceExists("assets/suprememc/equipment/cotton.json")
        assertResourceExists("data/suprememc/tags/item/cotton_repair_items.json")

        val helmet = readJson("data/suprememc/recipe/cotton_helmet.json")
        assertEquals(listOf("CCC", "C C"), helmet.getAsJsonArray("pattern").map { it.asString })
        assertEquals("suprememc:cotton", helmet.getAsJsonObject("key").get("C").asString)
    }

    @Test
    fun upgradesBurningDiamondEquipmentToBurningNetherite() {
        listOf("pickaxe", "axe", "shovel", "hoe", "sword", "helmet", "chestplate", "leggings", "boots").forEach { id ->
            val recipe = readJson("data/suprememc/recipe/burning_netherite_$id.json")
            assertEquals("minecraft:smithing_transform", recipe.get("type").asString)
            assertEquals("minecraft:netherite_upgrade_smithing_template", recipe.get("template").asString)
            assertEquals("suprememc:burning_diamond_$id", recipe.get("base").asString)
            assertEquals("minecraft:netherite_ingot", recipe.get("addition").asString)
            assertEquals("suprememc:burning_netherite_$id", recipe.getAsJsonObject("result").get("id").asString)
        }
        assertFalse(Files.exists(generatedResources.resolve("data/suprememc/recipe/burning_netherite.json")))
    }

    @Test
    fun generatesSupremeMCLogoBlockRecipeAdvancementAndModels() {
        val recipe = readJson("data/suprememc/recipe/suprememc_logo_block.json")
        assertEquals("minecraft:crafting_shapeless", recipe.get("type").asString)
        assertEquals(
            setOf("minecraft:grass_block", "minecraft:magenta_dye", "minecraft:pink_dye"),
            recipe.getAsJsonArray("ingredients").map { it.asString }.toSet()
        )
        assertEquals("suprememc:suprememc_logo_block", recipe.getAsJsonObject("result").get("id").asString)

        val advancement = readJson("data/suprememc/advancement/recipes/building_blocks/suprememc_logo_block.json")
        val grassCriterion = advancement.getAsJsonObject("criteria").getAsJsonObject("has_grass_block")
        assertEquals("minecraft:inventory_changed", grassCriterion.get("trigger").asString)
        assertEquals(
            "minecraft:grass_block",
            grassCriterion.getAsJsonObject("conditions").getAsJsonArray("items").get(0).asJsonObject.get("items").asString
        )

        val model = readJson("assets/suprememc/models/block/suprememc_logo_block.json")
        assertEquals("suprememc:block/suprememc_logo_block_top", model.getAsJsonObject("textures").get("top").asString)
        assertEquals("suprememc:block/suprememc_logo_block_side", model.getAsJsonObject("textures").get("side").asString)
        assertEquals("suprememc:block/suprememc_logo_block_bottom", model.getAsJsonObject("textures").get("bottom").asString)
    }

    @Test
    fun generatesEnderSpiderSpawnEggAndLootTable() {
        listOf(
            "assets/suprememc/models/item/ender_spider_spawn_egg.json",
            "assets/suprememc/items/ender_spider_spawn_egg.json",
            "data/suprememc/loot_table/entities/ender_spider.json"
        ).forEach(::assertResourceExists)

        val model = readJson("assets/suprememc/models/item/ender_spider_spawn_egg.json")
        assertEquals("suprememc:item/ender_spider_spawn_egg", model.getAsJsonObject("textures").get("layer0").asString)

        val lootTable = readJson("data/suprememc/loot_table/entities/ender_spider.json")
        val entries = lootTable.getAsJsonArray("pools").flatMap { pool ->
            pool.asJsonObject.getAsJsonArray("entries").map { it.asJsonObject.get("name").asString }
        }
        assertEquals(setOf("minecraft:string", "minecraft:spider_eye", "minecraft:ender_pearl"), entries.toSet())
    }

    @Test
    fun generatesColoredSlimeBlockDyeingRecipes() {
        val colors = listOf("white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black")
        val slimeTag = readJson("data/suprememc/tags/item/slime_blocks.json")
        assertEquals(
            setOf("minecraft:slime_block") + colors.map { "suprememc:${it}_slime_block" },
            slimeTag.getAsJsonArray("values").map { it.asString }.toSet()
        )

        val recipe = readJson("data/suprememc/recipe/red_slime_block.json")
        assertEquals("minecraft:crafting_shapeless", recipe.get("type").asString)
        assertEquals(setOf("#suprememc:slime_blocks", "minecraft:red_dye"), recipe.getAsJsonArray("ingredients").map { it.asString }.toSet())
        assertEquals("suprememc:red_slime_block", recipe.getAsJsonObject("result").get("id").asString)
        assertResourceExists("data/suprememc/advancement/recipes/misc/red_slime_block.json")
    }

    @Test
    fun generatesAbyssaliteProgressionResources() {
        listOf(
            "assets/suprememc/models/item/abyssalite_scrap.json",
            "assets/suprememc/models/item/abyssalite_upgrade_smithing_template.json",
            "assets/suprememc/models/block/atlantis_debris.json",
            "assets/suprememc/equipment/abyssalite.json",
            "assets/suprememc/items/abyssalite_ingot.json",
            "data/suprememc/worldgen/configured_feature/atlantis_debris.json",
            "data/suprememc/worldgen/placed_feature/atlantis_debris.json",
            "data/suprememc/recipe/abyssalite_upgrade_smithing_template.json",
            "data/suprememc/recipe/abyssalite_helmet_smithing.json",
            "data/suprememc/recipe/abyssalite_scrap_from_smelting_atlantis_debris.json",
            "data/suprememc/recipe/abyssalite_scrap_from_blasting_atlantis_debris.json"
        ).forEach(::assertResourceExists)

        val feature = readJson("data/suprememc/worldgen/configured_feature/atlantis_debris.json")
            .getAsJsonObject("config")
        assertEquals(2, feature.get("size").asInt)
        assertEquals(0.5f, feature.get("discard_chance_on_air_exposure").asFloat)
        assertEquals(4, feature.getAsJsonArray("targets").size())

        val placed = readJson("data/suprememc/worldgen/placed_feature/atlantis_debris.json")
        val height = placed.getAsJsonArray("placement").map { it.asJsonObject }
            .first { it.get("type").asString == "minecraft:height_range" }
            .getAsJsonObject("height")
        assertEquals(-64, height.getAsJsonObject("min_inclusive").get("absolute").asInt)
        assertEquals(-49, height.getAsJsonObject("max_inclusive").get("absolute").asInt)
    }

    @Test
    fun injectsAbyssaliteTemplateIntoOceanStructureLoot() {
        val guaranteed = readJson("data/suprememc/loot_table/inject/abyssalite_template_guaranteed.json")
        val guaranteedEntry = guaranteed.getAsJsonArray("pools").single().asJsonObject
            .getAsJsonArray("entries").single().asJsonObject
        assertEquals("suprememc:abyssalite_upgrade_smithing_template", guaranteedEntry.get("name").asString)
        assertFalse(guaranteedEntry.has("conditions"))

        val rare = readJson("data/suprememc/loot_table/inject/abyssalite_template_rare.json")
        val rareCondition = rare.getAsJsonArray("pools").single().asJsonObject
            .getAsJsonArray("entries").single().asJsonObject
            .getAsJsonArray("conditions").single().asJsonObject
        assertEquals("minecraft:random_chance", rareCondition.get("condition").asString)
        assertEquals(0.1f, rareCondition.get("chance").asFloat)

        mapOf(
            "add_abyssalite_template_buried_treasure" to
                ("minecraft:chests/buried_treasure" to "suprememc:inject/abyssalite_template_guaranteed"),
            "add_abyssalite_template_underwater_ruin_small" to
                ("minecraft:chests/underwater_ruin_small" to "suprememc:inject/abyssalite_template_rare"),
            "add_abyssalite_template_underwater_ruin_big" to
                ("minecraft:chests/underwater_ruin_big" to "suprememc:inject/abyssalite_template_rare"),
            "add_abyssalite_template_shipwreck_treasure" to
                ("minecraft:chests/shipwreck_treasure" to "suprememc:inject/abyssalite_template_rare")
        ).forEach { (name, expected) ->
            val modifier = readJson("data/suprememc/loot_modifiers/$name.json")
            assertEquals("neoforge:add_table", modifier.get("type").asString)
            assertEquals(expected.second, modifier.get("table").asString)
            val condition = modifier.getAsJsonArray("conditions").single().asJsonObject
            assertEquals("neoforge:loot_table_id", condition.get("condition").asString)
            assertEquals(expected.first, condition.get("loot_table_id").asString)
        }
    }

    @Test
    fun generatesAbyssaliteRecipesAndCookingValues() {        val ingot = readJson("data/suprememc/recipe/abyssalite_ingot.json")
        assertEquals("minecraft:crafting_shapeless", ingot.get("type").asString)
        assertEquals(8, ingot.getAsJsonArray("ingredients").size())
        assertEquals("suprememc:abyssalite_ingot", ingot.getAsJsonObject("result").get("id").asString)

        val template = readJson("data/suprememc/recipe/abyssalite_upgrade_smithing_template.json")
        assertEquals("minecraft:crafting_shapeless", template.get("type").asString)
        assertEquals(2, template.getAsJsonObject("result").get("count").asInt)
        assertEquals(7, template.getAsJsonArray("ingredients").count { it.asString == "minecraft:diamond" })
        assertEquals(1, template.getAsJsonArray("ingredients").count { it.asString == "minecraft:prismarine" })

        val smithing = readJson("data/suprememc/recipe/abyssalite_helmet_smithing.json")
        assertEquals("minecraft:smithing_transform", smithing.get("type").asString)
        assertEquals("suprememc:abyssalite_upgrade_smithing_template", smithing.get("template").asString)
        assertEquals("suprememc:aquamarine_helmet", smithing.get("base").asString)
        assertEquals("suprememc:abyssalite_ingot", smithing.get("addition").asString)
        assertEquals("suprememc:abyssalite_helmet", smithing.getAsJsonObject("result").get("id").asString)

        listOf("smelting" to 200, "blasting" to 100).forEach { (kind, time) ->
            val recipe = readJson("data/suprememc/recipe/abyssalite_scrap_from_${kind}_atlantis_debris.json")
            assertEquals("minecraft:$kind", recipe.get("type").asString)
            assertEquals("suprememc:abyssalite_scrap", recipe.getAsJsonObject("result").get("id").asString)
            assertEquals(2.0f, recipe.get("experience").asFloat)
            assertEquals(time, recipe.get("cookingtime").asInt)
        }
    }

    @Test
    fun generatesRequiredAquamarineProgressionResources() {
        listOf(
            "assets/suprememc/models/item/aquamarine.json",
            "assets/suprememc/models/block/aquamarine_ore.json",
            "assets/suprememc/blockstates/aquamarine_ore.json",
            "assets/suprememc/equipment/aquamarine.json",
            "assets/suprememc/items/aquamarine.json",
            "assets/suprememc/items/aquamarine_ore.json",
            "data/suprememc/loot_table/blocks/aquamarine_ore.json",
            "data/suprememc/recipe/aquamarine_block.json",
            "data/suprememc/recipe/aquamarine_from_aquamarine_block.json",
            "data/suprememc/worldgen/configured_feature/aquamarine_ore.json",
            "data/suprememc/worldgen/placed_feature/aquamarine_ore.json",
            "data/suprememc/neoforge/biome_modifier/add_aquamarine_ore.json",
            "data/minecraft/tags/block/needs_iron_tool.json",
            "data/minecraft/tags/item/beacon_payment_items.json"
        ).forEach(::assertResourceExists)
    }

    @Test
    fun materialBlockRecipesAndModelsUseSupremeMCNamespace() {
        val slabRecipe = readJson("data/suprememc/recipe/aquamarine_slab.json")
        assertEquals("minecraft:crafting_shaped", slabRecipe.get("type").asString)
        assertEquals("suprememc:aquamarine_block", slabRecipe.getAsJsonObject("key").get("#").asString)

        val slabAdvancement = readJson("data/suprememc/advancement/recipes/building_blocks/aquamarine_slab.json")
        val unlockItems = slabAdvancement.getAsJsonObject("criteria")
            .getAsJsonObject("has_aquamarine_block")
            .getAsJsonObject("conditions")
            .getAsJsonArray("items")
            .get(0).asJsonObject
        assertEquals("suprememc:aquamarine_block", unlockItems.get("items").asString)

        val slabModel = readJson("assets/suprememc/models/block/aquamarine_slab.json")
        assertEquals("suprememc:block/aquamarine_block", slabModel.getAsJsonObject("textures").get("bottom").asString)

        val abyssaliteRecipe = readJson("data/suprememc/recipe/abyssalite_slab.json")
        assertEquals("suprememc:abyssalite_block", abyssaliteRecipe.getAsJsonObject("key").get("#").asString)
    }

    @Test
    fun generatesPrismarineOreDropsAndResources() {
        listOf("prismarine_ore", "deepslate_prismarine_ore").forEach { id ->
            listOf(
                "assets/suprememc/blockstates/$id.json",
                "assets/suprememc/models/block/$id.json",
                "assets/suprememc/models/item/$id.json",
                "assets/suprememc/items/$id.json",
                "data/suprememc/loot_table/blocks/$id.json"
            ).forEach(::assertResourceExists)

            val pools = readJson("data/suprememc/loot_table/blocks/$id.json")
                .getAsJsonArray("pools").map { it.asJsonObject }
            assertEquals(3, pools.size)
            val entries = pools.map { it.getAsJsonArray("entries").single().asJsonObject }
            assertTrue(entries.any { it.get("name").asString == "suprememc:$id" && it.has("conditions") })
            assertTrue(entries.any { it.get("name").asString == "minecraft:prismarine_crystals" && it.has("functions") })
            val shards = entries.single { it.get("name").asString == "minecraft:prismarine_shard" }
            val count = shards.getAsJsonArray("functions")
                .first { it.asJsonObject.get("function").asString == "minecraft:set_count" }
                .asJsonObject.getAsJsonObject("count")
            assertEquals(2, count.get("min").asInt)
            assertEquals(3, count.get("max").asInt)
            assertTrue(entries.filter { it.has("functions") }.all { entry ->
                entry.getAsJsonArray("functions").any { it.asJsonObject.get("function").asString == "minecraft:apply_bonus" }
            })
        }

        val configuredOre = readJson("data/suprememc/worldgen/configured_feature/prismarine_ore.json")
        val configuredSmallOre = readJson("data/suprememc/worldgen/configured_feature/prismarine_ore_small.json")
        assertEquals(9, configuredOre.getAsJsonObject("config").get("size").asInt)
        assertEquals(4, configuredSmallOre.getAsJsonObject("config").get("size").asInt)

        val orePlacement = readJson("data/suprememc/worldgen/placed_feature/prismarine_ore.json").getAsJsonArray("placement")
        val smallOrePlacement = readJson("data/suprememc/worldgen/placed_feature/prismarine_ore_small.json").getAsJsonArray("placement")
        assertEquals(10, orePlacement[0].asJsonObject.get("count").asInt)
        assertEquals(10, smallOrePlacement[0].asJsonObject.get("count").asInt)
        assertEquals("minecraft:trapezoid", orePlacement[2].asJsonObject.getAsJsonObject("height").get("type").asString)
        assertEquals("minecraft:uniform", smallOrePlacement[2].asJsonObject.getAsJsonObject("height").get("type").asString)

        val biomeModifier = readJson("data/suprememc/neoforge/biome_modifier/add_prismarine_ore.json")
        assertEquals("#minecraft:is_ocean", biomeModifier.getAsJsonArray("biomes").single().asString)
        assertEquals(listOf("suprememc:prismarine_ore", "suprememc:prismarine_ore_small"), biomeModifier.getAsJsonArray("features").map { it.asString })
    }

    @Test
    fun generatesPalmWoodsetResources() {
        listOf(
            "assets/suprememc/blockstates/palm_log.json",
            "assets/suprememc/models/block/palm_log.json",
            "assets/suprememc/models/block/palm_planks.json",
            "assets/suprememc/models/block/stripped_palm_log.json",
            "assets/suprememc/models/item/palm_planks.json",
            "assets/suprememc/models/item/palm_boat.json",
            "assets/suprememc/models/item/palm_sign.json",
            "assets/suprememc/items/palm_planks.json",
            "assets/suprememc/items/palm_sign.json",
            "data/suprememc/recipe/palm_planks.json",
            "data/suprememc/recipe/palm_log_to_wood.json",
            "data/suprememc/recipe/palm_sign.json",
            "data/suprememc/recipe/palm_boat.json",
            "data/suprememc/loot_table/blocks/palm_leaves.json",
            "data/minecraft/tags/block/logs.json",
            "data/minecraft/tags/item/logs.json"
        ).forEach(::assertResourceExists)

        val logState = readJson("assets/suprememc/blockstates/palm_log.json")
        assertTrue(logState.has("variants") || logState.has("multipart"))

        val signRecipe = readJson("data/suprememc/recipe/palm_sign.json")
        assertEquals("minecraft:crafting_shaped", signRecipe.get("type").asString)
        assertEquals("suprememc:palm_sign", signRecipe.getAsJsonObject("result").get("id").asString)
    }

    @Test
    fun generatesCustomEnchantmentsAndExclusiveSets() {
        mapOf(
            "bounty" to Triple(3, 2, "#minecraft:enchantable/weapon"),
            "venom" to Triple(2, 5, "#minecraft:enchantable/weapon"),
            "decay" to Triple(2, 1, "#minecraft:enchantable/weapon"),
            "wisdom" to Triple(3, 2, "#minecraft:enchantable/leg_armor"),
            "smelting" to Triple(1, 2, "#minecraft:enchantable/mining"),
            "tension" to Triple(3, 5, "minecraft:bow")
            ,"super_channeling" to Triple(1, 1, "#suprememc:super_channeling_items")
            ,"curse_of_mass" to Triple(1, 1, "#suprememc:curse_of_mass_items")
            ,"curse_of_sloth" to Triple(1, 1, "#suprememc:curse_of_sloth_items")
        ).forEach { (id, expected) ->
            val enchantment = readJson("data/suprememc/enchantment/$id.json")
            assertEquals(expected.first, enchantment.get("max_level").asInt)
            assertEquals(expected.second, enchantment.get("weight").asInt)
            assertEquals(expected.third, enchantment.get("supported_items").asString)
            assertEquals("enchantment.suprememc.$id", enchantment.getAsJsonObject("description").get("translate").asString)
        }
        val language = readJson("assets/suprememc/lang/en_us.json")
        listOf(
            "bounty", "venom", "decay", "wisdom", "smelting", "tension",
            "super_channeling", "curse_of_mass", "curse_of_sloth"
        ).forEach { id ->
            assertTrue(language.has("enchantment.suprememc.$id.description"))
        }
        listOf("aquamarine", "amber", "burning_diamond", "burning_netherite", "abyssalite", "experience")
            .forEach { id ->
                assertTrue(language.has("item.suprememc.armor.$id.full_set_bonus"))
            }

        val venom = readJson("data/suprememc/enchantment/venom.json").getAsJsonObject("effects")
            .getAsJsonArray("minecraft:post_attack").single().asJsonObject
        assertEquals("minecraft:poison", venom.getAsJsonObject("effect").get("to_apply").asString)
        assertEquals(3.0, venom.getAsJsonObject("effect").get("min_duration").asDouble)
        assertEquals(3.0, venom.getAsJsonObject("effect").getAsJsonObject("max_duration").get("per_level_above_first").asDouble)

        val decay = readJson("data/suprememc/enchantment/decay.json").getAsJsonObject("effects")
            .getAsJsonArray("minecraft:post_attack").single().asJsonObject
        assertEquals("minecraft:wither", decay.getAsJsonObject("effect").get("to_apply").asString)
        assertEquals(2.0, decay.getAsJsonObject("effect").getAsJsonObject("max_duration").get("per_level_above_first").asDouble)

        assertTagContains("data/suprememc/tags/enchantment/exclusive_set/bounty.json", "minecraft:looting", "suprememc:bounty")
        assertTagContains("data/suprememc/tags/enchantment/exclusive_set/status_damage.json", "minecraft:fire_aspect", "suprememc:venom", "suprememc:decay")
        assertTagContains("data/suprememc/tags/enchantment/exclusive_set/xp_armor.json", "minecraft:thorns", "suprememc:wisdom")
        assertTagContains("data/suprememc/tags/enchantment/exclusive_set/smelting.json", "minecraft:silk_touch", "suprememc:smelting")
        assertTagContains("data/suprememc/tags/enchantment/exclusive_set/tension.json", "minecraft:punch", "suprememc:tension")
        assertTagContains("data/suprememc/tags/enchantment/exclusive_set/super_channeling.json", "minecraft:channeling", "minecraft:riptide", "suprememc:super_channeling")
        assertTagContains("data/suprememc/tags/item/super_channeling_items.json", "#minecraft:enchantable/trident", "suprememc:abyssalite_trident")
        assertTagContains("data/suprememc/tags/entity_type/super_channeling_tridents.json", "minecraft:trident", "suprememc:abyssalite_trident")
        assertTagContains("data/minecraft/tags/enchantment/curse.json", "suprememc:curse_of_mass", "suprememc:curse_of_sloth")
        assertTagContains("data/minecraft/tags/enchantment/treasure.json", "suprememc:curse_of_mass", "suprememc:curse_of_sloth", "suprememc:super_channeling")

        val superChanneling = readJson("data/suprememc/enchantment/super_channeling.json")
            .getAsJsonObject("effects")
        listOf("minecraft:hit_block", "minecraft:post_attack").forEach { effectType ->
            val requirements = superChanneling.getAsJsonArray(effectType).single().asJsonObject
                .getAsJsonObject("requirements")
            assertFalse(requirements.getAsJsonArray("terms").any { it.asJsonObject.get("condition")?.asString == "minecraft:weather_check" })
        }
        assertTagContains(
            "data/suprememc/tags/item/curse_of_mass_items.json",
            "#minecraft:enchantable/armor",
            "#minecraft:enchantable/weapon",
            "#minecraft:enchantable/mining"
        )
        assertTagContains(
            "data/suprememc/tags/item/curse_of_sloth_items.json",
            "#minecraft:enchantable/armor",
            "#minecraft:enchantable/weapon",
            "#minecraft:enchantable/mining"
        )
        assertTagContains(
            "data/minecraft/tags/item/swords.json",
            "suprememc:abyssalite_sword",
            "suprememc:emerald_sword"
        )
        assertTagContains(
            "data/minecraft/tags/item/pickaxes.json",
            "suprememc:abyssalite_pickaxe",
            "suprememc:emerald_pickaxe"
        )
        assertTagContains(
            "data/minecraft/tags/item/head_armor.json",
            "suprememc:cotton_helmet",
            "suprememc:abyssalite_helmet"
        )
        assertTagContains(
            "data/minecraft/tags/item/enchantable/trident.json",
            "suprememc:abyssalite_trident"
        )
    }

    @Test
    fun generatesRecipeUnlockAdvancementsForAllRecipes() {
        val expected = listOf(
            Triple("aquamarine_block", "building", "aquamarine"),
            Triple("aquamarine_from_aquamarine_block", "misc", "aquamarine_block"),
            Triple("aquamarine_pickaxe", "equipment", "aquamarine"),
            Triple("aquamarine_axe", "equipment", "aquamarine"),
            Triple("aquamarine_shovel", "equipment", "aquamarine"),
            Triple("aquamarine_hoe", "equipment", "aquamarine"),
            Triple("aquamarine_sword", "equipment", "aquamarine"),
            Triple("aquamarine_helmet", "equipment", "aquamarine"),
            Triple("aquamarine_chestplate", "equipment", "aquamarine"),
            Triple("aquamarine_leggings", "equipment", "aquamarine"),
            Triple("aquamarine_boots", "equipment", "aquamarine"),
            Triple("aquamarine_from_smelting_aquamarine_ore", "misc", "aquamarine_ore"),
            Triple("aquamarine_from_blasting_aquamarine_ore", "misc", "aquamarine_ore"),
            Triple("aquamarine_from_smelting_deepslate_aquamarine_ore", "misc", "deepslate_aquamarine_ore"),
            Triple("aquamarine_from_blasting_deepslate_aquamarine_ore", "misc", "deepslate_aquamarine_ore")
        )

        expected.forEach { (recipeId, category, unlockItemId) ->
            val advancement = readJson("data/suprememc/advancement/recipes/$category/$recipeId.json")
            assertEquals("minecraft:recipes/root", advancement.get("parent").asString)

            val criteria = advancement.getAsJsonObject("criteria")
            assertTrue("Missing has_$unlockItemId criterion for $recipeId") { criteria.has("has_$unlockItemId") }
            val unlockCriterion = criteria.getAsJsonObject("has_$unlockItemId")
            assertEquals("minecraft:inventory_changed", unlockCriterion.get("trigger").asString)
            val unlockItems = unlockCriterion.getAsJsonObject("conditions").getAsJsonArray("items")
                .get(0).asJsonObject.getAsJsonArray("items")
            assertEquals("suprememc:$unlockItemId", unlockItems.get(0).asString)

            val hasRecipe = criteria.getAsJsonObject("has_the_recipe")
            assertEquals("minecraft:recipe_unlocked", hasRecipe.get("trigger").asString)
            assertEquals("suprememc:$recipeId", hasRecipe.getAsJsonObject("conditions").get("recipe").asString)

            val requirements = advancement.getAsJsonArray("requirements")
            assertTrue("Expected at least one requirement group for $recipeId") { requirements.size() > 0 }
            val requirementGroup = requirements.get(0).asJsonArray.map { it.asString }
            assertTrue(requirementGroup.contains("has_$unlockItemId"))
            assertTrue(requirementGroup.contains("has_the_recipe"))

            val rewardedRecipes = advancement.getAsJsonObject("rewards").getAsJsonArray("recipes")
            assertEquals("suprememc:$recipeId", rewardedRecipes.get(0).asString)
        }
    }

    @Test
    fun anthraciteCraftsEightTorches() {
        val recipe = readJson("data/suprememc/recipe/anthracite_torch.json")
        assertEquals(listOf("A", "S"), recipe.getAsJsonArray("pattern").map { it.asString })
        assertEquals("suprememc:anthracite", recipe.getAsJsonObject("key").get("A").asString)
        assertEquals("minecraft:stick", recipe.getAsJsonObject("key").get("S").asString)
        assertEquals("minecraft:torch", recipe.getAsJsonObject("result").get("id").asString)
        assertEquals(8, recipe.getAsJsonObject("result").get("count").asInt)
    }

    @Test
    fun addsBellRecipeAndGoldBlockUnlock() {
        val recipe = readJson("data/minecraft/recipe/bell.json")
        assertEquals("minecraft:crafting_shaped", recipe.get("type").asString)
        assertEquals(listOf("ISI", "IGI"), recipe.getAsJsonArray("pattern").map { it.asString })
        assertEquals("minecraft:iron_ingot", recipe.getAsJsonObject("key").get("I").asString)
        assertEquals("minecraft:stick", recipe.getAsJsonObject("key").get("S").asString)
        assertEquals("minecraft:gold_block", recipe.getAsJsonObject("key").get("G").asString)
        assertEquals("minecraft:bell", recipe.getAsJsonObject("result").get("id").asString)

        val advancement = readJson("data/suprememc/advancement/recipes/bell.json")
        val goldCriterion = advancement.getAsJsonObject("criteria").getAsJsonObject("has_gold_block")
        assertEquals("minecraft:inventory_changed", goldCriterion.get("trigger").asString)
        assertEquals(
            "minecraft:gold_block",
            goldCriterion.getAsJsonObject("conditions").getAsJsonArray("items")
                .get(0).asJsonObject.getAsJsonArray("items").get(0).asString
        )
        val recipeCriterion = advancement.getAsJsonObject("criteria").getAsJsonObject("has_the_recipe")
        assertEquals("minecraft:bell", recipeCriterion.getAsJsonObject("conditions").get("recipe").asString)
        assertEquals("minecraft:bell", advancement.getAsJsonObject("rewards").getAsJsonArray("recipes").get(0).asString)
    }

    @Test
    fun allCraftingRecipesUseAValidCraftingBookCategory() {
        val validCategories = setOf("building", "redstone", "equipment", "misc")
        val recipesDir = generatedResources.resolve("data/suprememc/recipe")
        Files.list(recipesDir).use { paths ->
            paths.filter { it.toString().endsWith(".json") }.forEach { path ->
                val recipe = JsonParser.parseString(Files.readString(path)).asJsonObject
                if (recipe.get("type").asString != "minecraft:crafting_shaped" && recipe.get("type").asString != "minecraft:crafting_shapeless") {
                    return@forEach
                }
                val category = recipe.get("category").asString
                assertTrue("Recipe ${path.fileName} has invalid crafting category '$category'") { category in validCategories }
            }
        }
    }

    @Test
    fun generatesMoistFarmlandAndAquamarineEquipmentModels() {
        val farmlandModel = readJson("assets/suprememc/models/block/wet_farmland.json")
        assertEquals("minecraft:block/farmland_moist", farmlandModel.get("parent").asString)
        assertFalse(farmlandModel.has("textures"))

        val layers = readJson("assets/suprememc/equipment/aquamarine.json").getAsJsonObject("layers")
        assertTrue(layers.has("humanoid"))
        assertTrue(layers.has("humanoid_baby"))
        assertTrue(layers.has("humanoid_leggings"))
        assertEquals("suprememc:aquamarine", layers.getAsJsonArray("humanoid").get(0).asJsonObject.get("texture").asString)
    }

    @Test
    fun generatesOceanOnlyAquamarineOrePlacement() {
        val config = readJson("data/suprememc/worldgen/configured_feature/aquamarine_ore.json").getAsJsonObject("config")
        assertEquals(4, config.get("size").asInt)
        assertEquals(0.5f, config.get("discard_chance_on_air_exposure").asFloat)
        assertEquals(3, config.getAsJsonArray("targets").size())

        val modifier = readJson("data/suprememc/neoforge/biome_modifier/add_aquamarine_ore.json")
        assertEquals("neoforge:add_features", modifier.get("type").asString)
        assertEquals(listOf("#minecraft:is_ocean"), modifier.getAsJsonArray("biomes").map { it.asString })
        assertEquals("suprememc:aquamarine_ore", modifier.get("features").asString)
        assertEquals("underground_ores", modifier.get("step").asString)
    }

    @Test
    fun generatesCorrectProgressionRecipesLootAndTags() {
        val compacting = readJson("data/suprememc/recipe/aquamarine_block.json")
        assertEquals("minecraft:crafting_shaped", compacting.get("type").asString)
        assertEquals("AAA", compacting.getAsJsonArray("pattern").get(0).asString)
        assertEquals("suprememc:aquamarine", compacting.getAsJsonObject("key").get("A").asString)
        assertEquals("suprememc:aquamarine_block", compacting.getAsJsonObject("result").get("id").asString)

        val decompacting = readJson("data/suprememc/recipe/aquamarine_from_aquamarine_block.json")
        assertEquals("minecraft:crafting_shapeless", decompacting.get("type").asString)
        assertEquals("suprememc:aquamarine_block", decompacting.getAsJsonArray("ingredients").get(0).asString)
        assertEquals(9, decompacting.getAsJsonObject("result").get("count").asInt)

        listOf("aquamarine_ore", "deepslate_aquamarine_ore").forEach { oreId ->
            val lootJson = readJson("data/suprememc/loot_table/blocks/$oreId.json").toString()
            assertTrue("minecraft:silk_touch" in lootJson)
            assertTrue("minecraft:fortune" in lootJson)
            assertTrue("minecraft:ore_drops" in lootJson)
            assertTrue("suprememc:aquamarine" in lootJson)
            assertTrue("suprememc:$oreId" in lootJson)

            assertCookingRecipe("aquamarine_from_smelting_$oreId", "minecraft:smelting", oreId)
            assertCookingRecipe("aquamarine_from_blasting_$oreId", "minecraft:blasting", oreId)
        }

        assertTagContains("data/minecraft/tags/block/needs_iron_tool.json", "suprememc:aquamarine_ore", "suprememc:deepslate_aquamarine_ore", "suprememc:aquamarine_block")
        assertTagContains("data/minecraft/tags/block/mineable/pickaxe.json", "suprememc:aquamarine_ore", "suprememc:deepslate_aquamarine_ore", "suprememc:aquamarine_block")
        assertTagContains("data/minecraft/tags/block/beacon_base_blocks.json", "suprememc:aquamarine_block")
        assertTagContains("data/minecraft/tags/item/beacon_payment_items.json", "suprememc:aquamarine")
    }

    @Test
    fun generatedResourcesAreValidAndBackedByRequiredTextures() {
        Files.walk(generatedResources).use { paths ->
            paths.filter { it.toString().endsWith(".json") }.forEach { path ->
                assertTrue("Empty JSON file: $path") { Files.size(path) > 0 }
            }
        }

        val textures = Path.of("..", "common", "src", "main", "resources", "assets", "suprememc", "textures")
        listOf(
            "block/aquamarine_ore.png", "block/deepslate_aquamarine_ore.png", "block/aquamarine_block.png",
            "item/aquamarine.png", "item/aquamarine_pickaxe.png", "item/aquamarine_axe.png", "item/aquamarine_shovel.png", "item/aquamarine_hoe.png", "item/aquamarine_sword.png",
            "item/aquamarine_helmet.png", "item/aquamarine_chestplate.png", "item/aquamarine_leggings.png", "item/aquamarine_boots.png",
            "entity/equipment/humanoid/aquamarine.png", "entity/equipment/humanoid_baby/aquamarine.png", "entity/equipment/humanoid_leggings/aquamarine.png"
        ).forEach { texture ->
            val texturePath = textures.resolve(texture)
            assertTrue("Missing required texture: $texture") { Files.isRegularFile(texturePath) }
            assertTrue("Empty texture: $texture") { Files.size(texturePath) > 0 }
        }
    }

    private fun assertCookingRecipe(recipeId: String, type: String, ingredient: String) {
        val recipe = readJson("data/suprememc/recipe/$recipeId.json")
        assertEquals(type, recipe.get("type").asString)
        assertEquals("suprememc:$ingredient", recipe.get("ingredient").asString)
        assertEquals("suprememc:aquamarine", recipe.getAsJsonObject("result").get("id").asString)
        assertEquals(1.0f, recipe.get("experience").asFloat)
    }

    private fun assertTagContains(relativePath: String, vararg expectedValues: String) {
        val values = readJson(relativePath).getAsJsonArray("values").toString()
        expectedValues.forEach { expectedValue ->
            assertTrue("Missing tag value $expectedValue in $relativePath") { expectedValue in values }
        }
    }

    private fun assertResourceExists(relativePath: String) {
        assertTrue("Missing generated resource: $relativePath") { Files.isRegularFile(generatedResources.resolve(relativePath)) }
    }

    private fun readJson(relativePath: String): JsonObject {
        val path = generatedResources.resolve(relativePath)
        assertResourceExists(relativePath)
        return JsonParser.parseString(Files.readString(path)).asJsonObject
    }

    private companion object {
        val generatedResources: Path = Path.of("src", "generated", "resources")
    }
}