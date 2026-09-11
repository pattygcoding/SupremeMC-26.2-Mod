package com.suprememc

import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class SupremeMCLanguageProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val items = arrayOf(
            "burning_diamond_ore",
            "burning_diamond_block",
            "burning_diamond_slab",
            "burning_diamond_stairs",
            "burning_diamond",
            "burning_diamond_pickaxe",
            "burning_diamond_axe",
            "burning_diamond_shovel",
            "burning_diamond_hoe",
            "burning_diamond_sword",
            "burning_diamond_helmet",
            "burning_diamond_chestplate",
            "burning_diamond_leggings",
            "burning_diamond_boots",
            "burning_netherite_pickaxe",
            "burning_netherite_axe",
            "burning_netherite_shovel",
            "burning_netherite_hoe",
            "burning_netherite_sword",
            "burning_netherite_helmet",
            "burning_netherite_chestplate",
            "burning_netherite_leggings",
            "burning_netherite_boots",
            "aquamarine",
            "amber",
            "anthracite",
            "experience_dust",
            "experience_ingot",
            "experience_upgrade_smithing_template",
            "xylium_dust",
            "glendstone_dust",
            "milk_bottle",
            "anthracite_block",
            "nether_anthracite_ore",
            "aquamarine_block",
            "aquamarine_ore",
            "aquamarine_slab",
            "aquamarine_stairs",
            "aquamarine_pickaxe",
            "aquamarine_axe",
            "aquamarine_shovel",
            "aquamarine_hoe",
            "aquamarine_sword",
            "aquamarine_helmet",
            "aquamarine_chestplate",
            "aquamarine_leggings",
            "aquamarine_boots",
            "amber_block",
            "amber_ore",
            "deepslate_amber_ore",
            "prismarine_ore",
            "deepslate_prismarine_ore",
            "xylium_ore",
            "xylium_block",
            "amber_slab",
            "amber_stairs",
            "amber_pickaxe",
            "amber_axe",
            "amber_shovel",
            "amber_hoe",
            "amber_sword",
            "amber_helmet",
            "amber_chestplate",
            "amber_leggings",
            "amber_boots",
            "emerald_pickaxe",
            "emerald_axe",
            "emerald_shovel",
            "emerald_hoe",
            "emerald_sword",
            "emerald_helmet",
            "emerald_chestplate",
            "emerald_leggings",
            "emerald_boots",
            "experience_pickaxe",
            "experience_axe",
            "experience_shovel",
            "experience_hoe",
            "experience_sword",
            "experience_helmet",
            "experience_chestplate",
            "experience_leggings",
            "experience_boots",
            "abyssalite_block",
            "abyssalite_slab",
            "abyssalite_stairs",
            "abyssalite_scrap",
            "abyssalite_ingot",
            "abyssalite_upgrade_smithing_template",
            "abyssalite_pickaxe",
            "abyssalite_axe",
            "abyssalite_shovel",
            "abyssalite_hoe",
            "abyssalite_sword",
            "abyssalite_helmet",
            "abyssalite_chestplate",
            "abyssalite_leggings",
            "abyssalite_boots",
            "abyssalite_trident",
            "palm_sign",
            "palm_hanging_sign",
            "palm_wall_sign",
            "palm_wall_hanging_sign",
            "lavender_stem",
            "lavender_endspar",
            "lavender_roots",
            "lavender_fungus",
            "lavender_wart_block",
            "stripped_lavender_stem",
            "lavender_hyphae",
            "stripped_lavender_hyphae",
            "lavender_sign",
            "lavender_hanging_sign",
            "lavender_wall_sign",
            "lavender_wall_hanging_sign",
            "lavender_boat",
            "lavender_chest_boat",
            "palm_boat",
            "palm_chest_boat",
            "crimson_boat",
            "crimson_chest_boat",
            "warped_boat",
            "warped_chest_boat",
            "coconut",
            "coconut_seeds",
            "cotton",
            "cotton_helmet",
            "cotton_chestplate",
            "cotton_leggings",
            "cotton_boots",
            "beach_grass",
            "tall_beach_grass",
            "buttercup",
            "clover",
            "wet_farmland",
            "suprememc_logo_block",
			"glendstone",
            "calamari",
            "cooked_calamari",
            "grapes",
            "tomato",
            "corn",
            "icicle",
            "grizzly_bear_spawn_egg",
            "ender_spider_spawn_egg",
            "fire_creeper_spawn_egg",
            "snow_creeper_spawn_egg",
            "potted_palm_sapling",
            "iron_slab",
            "iron_stairs",
            "lapis_slab",
            "lapis_stairs",
            "gold_slab",
            "gold_stairs",
            "diamond_slab",
            "diamond_stairs",
            "emerald_slab",
            "emerald_stairs",
            "coal_slab",
            "coal_stairs",
            "obsidian_slab",
            "obsidian_stairs",
            "netherite_slab",
            "netherite_stairs",
            "polished_granite_wall",
            "polished_diorite_wall",
            "polished_andesite_wall"
            , "andesite_bricks", "andesite_brick_stairs", "andesite_brick_slab", "andesite_brick_wall"
            , "mossy_andesite_bricks"
            , "mossy_andesite_brick_stairs", "mossy_andesite_brick_slab", "mossy_andesite_brick_wall"
            , "cracked_andesite_bricks"
            , "diorite_bricks", "diorite_brick_stairs", "diorite_brick_slab", "diorite_brick_wall"
            , "mossy_diorite_bricks"
            , "mossy_diorite_brick_stairs", "mossy_diorite_brick_slab", "mossy_diorite_brick_wall"
            , "cracked_diorite_bricks"
            , "granite_bricks", "granite_brick_stairs", "granite_brick_slab", "granite_brick_wall"
            , "mossy_granite_bricks"
            , "mossy_granite_brick_stairs", "mossy_granite_brick_slab", "mossy_granite_brick_wall"
            , "cracked_granite_bricks", "cracked_end_stone_bricks", "cracked_quartz_bricks"
        )
        val blocks = arrayOf(
            "aquamarine_ore",
            "deepslate_aquamarine_ore",
            "aquamarine_block",
            "burning_diamond_ore",
            "burning_diamond_block",
            "burning_diamond_slab",
            "burning_diamond_stairs",
            "nether_anthracite_ore",
            "anthracite_block",
            "aquamarine_slab",
            "aquamarine_stairs",
            "amber_ore",
            "deepslate_amber_ore",
            "prismarine_ore",
            "deepslate_prismarine_ore",
            "xylium_ore",
            "xylium_block",
            "amber_block",
            "amber_slab",
            "amber_stairs",
            "wet_farmland",
            "suprememc_logo_block",
            "atlantis_debris",
            "abyssalite_block",
            "abyssalite_slab",
            "abyssalite_stairs",
            "iron_stairs",
            "iron_slab",
            "lapis_stairs",
            "lapis_slab",
            "gold_stairs",
            "gold_slab",
            "diamond_stairs",
            "diamond_slab",
            "emerald_stairs",
            "emerald_slab",
            "coal_stairs",
            "coal_slab",
            "obsidian_stairs",
            "obsidian_slab",
            "netherite_stairs",
            "netherite_slab",
            "polished_granite_wall",
            "polished_diorite_wall",
            "polished_andesite_wall",
            "andesite_bricks", "andesite_brick_stairs", "andesite_brick_slab", "andesite_brick_wall",
            "mossy_andesite_bricks",
            "cracked_andesite_bricks",
            "diorite_bricks", "diorite_brick_stairs", "diorite_brick_slab", "diorite_brick_wall",
            "mossy_diorite_bricks",
            "cracked_diorite_bricks",
            "granite_bricks", "granite_brick_stairs", "granite_brick_slab", "granite_brick_wall",
            "mossy_granite_bricks",
            "cracked_granite_bricks", "cracked_end_stone_bricks", "cracked_quartz_bricks",
            "glendstone",
            "palm_log",
            "stripped_palm_log",
            "palm_wood",
            "stripped_palm_wood",
            "palm_planks",
            "palm_slab",
            "palm_stairs",
            "palm_fence",
            "palm_fence_gate",
            "palm_door",
            "palm_trapdoor",
            "palm_pressure_plate",
            "palm_button",
            "palm_sign",
            "palm_wall_sign",
            "palm_hanging_sign",
            "palm_wall_hanging_sign",
            "lavender_stem",
            "lavender_endspar",
            "lavender_roots",
            "lavender_fungus",
            "lavender_wart_block",
            "stripped_lavender_stem",
            "lavender_hyphae",
            "stripped_lavender_hyphae",
            "lavender_wood",
            "stripped_lavender_wood",
            "lavender_planks",
            "lavender_slab",
            "lavender_stairs",
            "lavender_fence",
            "lavender_fence_gate",
            "lavender_door",
            "lavender_trapdoor",
            "lavender_pressure_plate",
            "lavender_button",
            "lavender_sign",
            "lavender_wall_sign",
            "lavender_hanging_sign",
            "lavender_wall_hanging_sign",
            "palm_leaves",
            "palm_sapling",
            "potted_palm_sapling",
            "coconut",
            "cotton_bush",
            "tomato_bush",
            "beach_grass",
            "tall_beach_grass",
            "buttercup",
            "clover",
            "corn_stalk",
            "corn_stalk_plant",
            "grape_vine",
            "grape_vine_plant",
            "icicle",
            "luck",
            "long_luck",
            "strong_luck",
            "bad_luck",
            "long_bad_luck",
            "strong_bad_luck"
        )
        val dyeColors = arrayOf(
            "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
            "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"
        )
        val glowSlimeBlocks = dyeColors.flatMap { listOf("${it}_glowblock", "${it}_slime_block") }
        val bookshelfWoods = listOf(
            "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry",
            "pale_oak", "bamboo", "crimson", "warped", "palm", "lavender"
        )
        val craftingTableWoods = bookshelfWoods
        val coloredSandstoneBlocks = listOf("white", "black", "pink").flatMap { color ->
            listOf(
                "${color}_sand", "${color}_sandstone", "${color}_sandstone_stairs", "${color}_sandstone_slab", "${color}_sandstone_wall",
                "smooth_${color}_sandstone", "smooth_${color}_sandstone_stairs", "smooth_${color}_sandstone_slab",
                "cut_${color}_sandstone", "cut_${color}_sandstone_slab"
            )
        }
        val enchantments = arrayOf(
            "bounty", 
            "venom", 
            "decay", 
            "wisdom",
            "smelting",
            "tension"
            ,"super_channeling"
            ,"curse_of_mass"
            ,"curse_of_sloth"
        )
        val json = obj {
            addProperty("itemGroup.$namespace.main", "SupremeMC")
            addProperty("block.minecraft.bookshelf", "Oak Bookshelf")
            addProperty("block.minecraft.crafting_table", "Oak Crafting Table")
            addProperty("block.minecraft.furnace", "Cobblestone Furnace")
            items.forEach { addProperty("item.$namespace.$it", displayName(it)) }
            coloredSandstoneBlocks.forEach { addProperty("item.$namespace.$it", displayName(it)) }
            bookshelfWoods.forEach { addProperty("item.$namespace.${it}_bookshelf", "${it.split('_').joinToString(" ") { part -> part.replaceFirstChar { c -> c.uppercase() } }} Bookshelf") }
            craftingTableWoods.forEach { addProperty("item.$namespace.${it}_crafting_table", "${it.split('_').joinToString(" ") { part -> part.replaceFirstChar { c -> c.uppercase() } }} Crafting Table") }
            addProperty("item.$namespace.blackstone_furnace", "Blackstone Furnace")
            addProperty("item.$namespace.deepslate_furnace", "Deepslate Furnace")
            val customPotions = mapOf(
                "luck" to ("Potion of Luck" to "Luck"),
                "long_luck" to ("Long Potion of Luck" to "Long Luck"),
                "strong_luck" to ("Strong Potion of Luck" to "Strong Luck"),
                "bad_luck" to ("Potion of Bad Luck" to "Bad Luck"),
                "long_bad_luck" to ("Long Potion of Bad Luck" to "Long Bad Luck"),
                "strong_bad_luck" to ("Strong Potion of Bad Luck" to "Strong Bad Luck"),
                "hunger" to ("Potion of Hunger" to "Hunger"),
                "long_hunger" to ("Long Potion of Hunger" to "Long Hunger"),
                "strong_hunger" to ("Strong Potion of Hunger" to "Strong Hunger"),
                "decay" to ("Potion of Decay" to "Decay"),
                "long_decay" to ("Long Potion of Decay" to "Long Decay"),
                "strong_decay" to ("Strong Potion of Decay" to "Strong Decay")
            )
            customPotions.forEach { (id, names) ->
                val (potionName, arrowName) = names
                addProperty("potion.$namespace.$id", potionName)
                addProperty("item.minecraft.potion.effect.$id", potionName)
                addProperty("item.minecraft.splash_potion.effect.$id", "Splash $potionName")
                addProperty("item.minecraft.lingering_potion.effect.$id", "Lingering $potionName")
                addProperty("item.minecraft.tipped_arrow.effect.$id", "Arrow of $arrowName")
            }
            blocks.forEach { addProperty("block.$namespace.$it", displayName(it)) }
            coloredSandstoneBlocks.forEach { addProperty("block.$namespace.$it", displayName(it)) }
            bookshelfWoods.forEach { addProperty("block.$namespace.${it}_bookshelf", "${it.split('_').joinToString(" ") { part -> part.replaceFirstChar { c -> c.uppercase() } }} Bookshelf") }
            craftingTableWoods.forEach { addProperty("block.$namespace.${it}_crafting_table", "${it.split('_').joinToString(" ") { part -> part.replaceFirstChar { c -> c.uppercase() } }} Crafting Table") }
            addProperty("block.$namespace.blackstone_furnace", "Blackstone Furnace")
            addProperty("block.$namespace.deepslate_furnace", "Deepslate Furnace")
            glowSlimeBlocks.forEach { addProperty("block.$namespace.$it", displayName(it)) }
            addProperty("item.$namespace.experience_upgrade_smithing_template", "Experience Upgrade")
            addProperty("block.$namespace.milk", "Milk")
            addProperty("block.$namespace.milk_cauldron", "Milk Cauldron")
            addProperty("item.$namespace.abyssalite_upgrade_smithing_template", "Abyssalite Upgrade")
            addProperty("item.$namespace.abyssalite_upgrade_smithing_template.upgrade_description", "Upgrade to Abyssalite")
            addSmithingTemplateTranslations("experience_upgrade", "Emerald Equipment", "Experience Ingot", "Add emerald armor, weapon, or tool", "Add Experience Ingot")
            addSmithingTemplateTranslations("abyssalite_upgrade", "Aquamarine Equipment", "Abyssalite Ingot", "Add aquamarine armor, weapon, or tool", "Add Abyssalite Ingot")
            enchantments.forEach { addProperty("enchantment.$namespace.$it", displayName(it)) }
            mapOf(
                "aquamarine" to "Full set grants Water Breathing.",
                "amber" to "Full set grants Night Vision.",
                "burning_diamond" to "Full set grants Fire Resistance.",
                "burning_netherite" to "Full set grants Fire Resistance.",
                "abyssalite" to "Full set grants Conduit Power and Dolphin's Grace.",
                "experience" to "Full set grants Hero of the Village."
            ).forEach { (id, description) ->
                addProperty("item.$namespace.armor.$id.full_set_bonus", description)
            }
            mapOf(
                "bounty" to "Increases mob drops.",
                "venom" to "Applies Poison after a direct melee hit.",
                "decay" to "Applies Wither after a direct melee hit.",
                "wisdom" to "Increases experience gained.",
                "smelting" to "Automatically smelts mined drops.",
                "tension" to "Reduces bow draw time.",
                "super_channeling" to "Calls lightning on valid targets and lightning rods.",
                "curse_of_mass" to "Increases Slowness while cursed equipment is worn or held.",
                "curse_of_sloth" to "Applies escalating Weakness while cursed equipment is worn or held."
            ).forEach { (id, description) ->
                addProperty("enchantment.$namespace.$id.description", description)
            }
            addProperty("enchantment.$namespace.curse_of_mass", "Curse of Mass")
            addProperty("enchantment.$namespace.curse_of_sloth", "Curse of Sloth")
            addProperty("entity.$namespace.grizzly_bear", "Grizzly Bear")
            addProperty("entity.$namespace.fire_creeper", "Fire Creeper")
            addProperty("entity.$namespace.snow_creeper", "Snow Creeper")
            addProperty("block.$namespace.snow_tnt", "Snow TNT")
            addProperty("block.$namespace.fire_tnt", "Fire TNT")
            addProperty("biome.$namespace.florida_plains", "Florida Plains")
            addProperty("biome.$namespace.cays", "Cays")
            addProperty("biome.$namespace.ice_caves", "Ice Caves")
        }
        return save(cache, json, resourcePath("lang/en_us.json"))
    }

    private fun JsonObject.addSmithingTemplateTranslations(type: String, appliesTo: String, ingredients: String, baseDescription: String, additionsDescription: String) {
        val prefix = "item.$namespace.smithing_template.$type"
        addProperty("$prefix.applies_to", appliesTo)
        addProperty("$prefix.ingredients", ingredients)
        addProperty("$prefix.base_slot_description", baseDescription)
        addProperty("$prefix.additions_slot_description", additionsDescription)
    }

    override fun getName() = "SupremeMC language"
}