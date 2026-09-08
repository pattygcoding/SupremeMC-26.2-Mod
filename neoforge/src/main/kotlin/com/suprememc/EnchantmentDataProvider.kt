package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class EnchantmentDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, bounty(), dataPath("enchantment/bounty.json"))
        writes += save(cache, venom(), dataPath("enchantment/venom.json"))
        writes += save(cache, decay(), dataPath("enchantment/decay.json"))
        writes += save(cache, wisdom(), dataPath("enchantment/wisdom.json"))
        writes += save(cache, smelting(), dataPath("enchantment/smelting.json"))
        writes += save(cache, tension(), dataPath("enchantment/tension.json"))
        writes += save(cache, superChanneling(), dataPath("enchantment/super_channeling.json"))
        writes += save(cache, curseOfMass(), dataPath("enchantment/curse_of_mass.json"))
        writes += save(cache, curseOfSloth(), dataPath("enchantment/curse_of_sloth.json"))
        writes += save(cache, valuesTag(
            "#minecraft:enchantable/armor",
            "#minecraft:enchantable/weapon",
            "#minecraft:enchantable/mining"
        ), dataPath("tags/item/curse_of_mass_items.json"))
        writes += save(cache, valuesTag(
            "#minecraft:enchantable/armor",
            "#minecraft:enchantable/weapon",
            "#minecraft:enchantable/mining"
        ), dataPath("tags/item/curse_of_sloth_items.json"))
        writes += save(cache, valuesTag(
            "$namespace:burning_diamond_sword",
            "$namespace:burning_netherite_sword",
            "$namespace:aquamarine_sword",
            "$namespace:amber_sword",
            "$namespace:abyssalite_sword",
            "$namespace:emerald_sword"
        ), minecraftDataPath("tags/item/swords.json"))
        writes += save(cache, valuesTag(
            "$namespace:burning_diamond_axe",
            "$namespace:burning_netherite_axe",
            "$namespace:aquamarine_axe",
            "$namespace:amber_axe",
            "$namespace:abyssalite_axe",
            "$namespace:emerald_axe"
        ), minecraftDataPath("tags/item/axes.json"))
        writes += save(cache, valuesTag(
            "$namespace:burning_diamond_pickaxe",
            "$namespace:burning_netherite_pickaxe",
            "$namespace:aquamarine_pickaxe",
            "$namespace:amber_pickaxe",
            "$namespace:abyssalite_pickaxe",
            "$namespace:emerald_pickaxe"
        ), minecraftDataPath("tags/item/pickaxes.json"))
        writes += save(cache, valuesTag(
            "$namespace:burning_diamond_shovel",
            "$namespace:burning_netherite_shovel",
            "$namespace:aquamarine_shovel",
            "$namespace:amber_shovel",
            "$namespace:abyssalite_shovel",
            "$namespace:emerald_shovel"
        ), minecraftDataPath("tags/item/shovels.json"))
        writes += save(cache, valuesTag(
            "$namespace:burning_diamond_hoe",
            "$namespace:burning_netherite_hoe",
            "$namespace:aquamarine_hoe",
            "$namespace:amber_hoe",
            "$namespace:abyssalite_hoe",
            "$namespace:emerald_hoe"
        ), minecraftDataPath("tags/item/hoes.json"))
        writes += save(cache, valuesTag(
            "$namespace:cotton_helmet",
            "$namespace:aquamarine_helmet",
            "$namespace:burning_diamond_helmet",
            "$namespace:burning_netherite_helmet",
            "$namespace:amber_helmet",
            "$namespace:abyssalite_helmet",
            "$namespace:emerald_helmet"
        ), minecraftDataPath("tags/item/head_armor.json"))
        writes += save(cache, valuesTag(
            "$namespace:cotton_chestplate",
            "$namespace:aquamarine_chestplate",
            "$namespace:burning_diamond_chestplate",
            "$namespace:burning_netherite_chestplate",
            "$namespace:amber_chestplate",
            "$namespace:abyssalite_chestplate",
            "$namespace:emerald_chestplate"
        ), minecraftDataPath("tags/item/chest_armor.json"))
        writes += save(cache, valuesTag(
            "$namespace:cotton_leggings",
            "$namespace:aquamarine_leggings",
            "$namespace:burning_diamond_leggings",
            "$namespace:burning_netherite_leggings",
            "$namespace:amber_leggings",
            "$namespace:abyssalite_leggings",
            "$namespace:emerald_leggings"
        ), minecraftDataPath("tags/item/leg_armor.json"))
        writes += save(cache, valuesTag(
            "$namespace:cotton_boots",
            "$namespace:aquamarine_boots",
            "$namespace:burning_diamond_boots",
            "$namespace:burning_netherite_boots",
            "$namespace:amber_boots",
            "$namespace:abyssalite_boots",
            "$namespace:emerald_boots"
        ), minecraftDataPath("tags/item/foot_armor.json"))
        writes += save(cache, valuesTag("$namespace:abyssalite_trident"), minecraftDataPath("tags/item/enchantable/trident.json"))
        writes += save(cache, valuesTag("minecraft:looting", "$namespace:bounty"), dataPath("tags/enchantment/exclusive_set/bounty.json"))
        writes += save(cache, valuesTag("minecraft:fire_aspect", "$namespace:venom", "$namespace:decay"), dataPath("tags/enchantment/exclusive_set/status_damage.json"))
        writes += save(cache, valuesTag("minecraft:thorns", "$namespace:wisdom"), dataPath("tags/enchantment/exclusive_set/xp_armor.json"))
        writes += save(cache, valuesTag("minecraft:silk_touch", "$namespace:smelting"), dataPath("tags/enchantment/exclusive_set/smelting.json"))
        writes += save(cache, valuesTag("minecraft:punch", "$namespace:tension"), dataPath("tags/enchantment/exclusive_set/tension.json"))
        writes += save(cache, valuesTag("minecraft:channeling", "minecraft:riptide", "$namespace:super_channeling"), dataPath("tags/enchantment/exclusive_set/super_channeling.json"))
        writes += save(cache, valuesTag("#minecraft:enchantable/trident", "$namespace:abyssalite_trident"), dataPath("tags/item/super_channeling_items.json"))
        writes += save(cache, valuesTag("minecraft:trident", "$namespace:abyssalite_trident"), dataPath("tags/entity_type/super_channeling_tridents.json"))
        writes += save(cache, valuesTag("$namespace:curse_of_mass", "$namespace:curse_of_sloth"), minecraftDataPath("tags/enchantment/curse.json"))
        writes += save(cache, valuesTag("$namespace:curse_of_mass", "$namespace:curse_of_sloth", "$namespace:super_channeling"), minecraftDataPath("tags/enchantment/treasure.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun base(description: String, anvilCost: Int, maxLevel: Int, weight: Int, supported: String, minBase: Int, minPerLevel: Int, maxBase: Int, maxPerLevel: Int) = obj {
        addProperty("anvil_cost", anvilCost)
        add("description", obj { addProperty("translate", "enchantment.$namespace.$description") })
        addProperty("max_level", maxLevel)
        add("min_cost", cost(minBase, minPerLevel))
        add("max_cost", cost(maxBase, maxPerLevel))
        addProperty("supported_items", supported)
        add("slots", JsonArray().also { it.add("mainhand") })
        addProperty("weight", weight)
    }

    private fun cost(base: Int, perLevel: Int) = obj {
        addProperty("base", base)
        addProperty("per_level_above_first", perLevel)
    }

    private fun bounty() = base("bounty", 4, 3, 2, "#minecraft:enchantable/weapon", 15, 9, 65, 9).apply {
        addProperty("exclusive_set", "#$namespace:exclusive_set/bounty")
    }

    private fun venom() = base("venom", 2, 2, 5, "#minecraft:enchantable/weapon", 5, 8, 25, 8).apply {
        addProperty("exclusive_set", "#$namespace:exclusive_set/status_damage")
        add("effects", obj {
            add("minecraft:post_attack", JsonArray().also { it.add(statusEffect("minecraft:poison", 60, 120)) })
        })
    }

    private fun decay() = base("decay", 4, 2, 1, "#minecraft:enchantable/weapon", 15, 9, 65, 9).apply {
        addProperty("exclusive_set", "#$namespace:exclusive_set/status_damage")
        add("effects", obj {
            add("minecraft:post_attack", JsonArray().also { it.add(statusEffect("minecraft:wither", 40, 80)) })
        })
    }

    private fun statusEffect(effect: String, firstDuration: Int, secondDuration: Int) = obj {
        addProperty("affected", "victim")
        add("effect", obj {
            addProperty("type", "minecraft:apply_mob_effect")
            addProperty("to_apply", effect)
            addProperty("min_amplifier", 0)
            addProperty("max_amplifier", 0)
            addProperty("min_duration", firstDuration / 20.0)
            add("max_duration", obj {
                addProperty("type", "minecraft:linear")
                addProperty("base", firstDuration / 20.0)
                addProperty("per_level_above_first", (secondDuration - firstDuration) / 20.0)
            })
        })
        addProperty("enchanted", "attacker")
        add("requirements", obj {
            addProperty("condition", "minecraft:damage_source_properties")
            add("predicate", obj { addProperty("is_direct", true) })
        })
    }

    private fun wisdom() = base("wisdom", 4, 3, 2, "#minecraft:enchantable/leg_armor", 15, 9, 65, 9).apply {
        addProperty("exclusive_set", "#$namespace:exclusive_set/xp_armor")
        add("slots", JsonArray().also { it.add("legs") })
    }

    private fun smelting() = base("smelting", 4, 1, 2, "#minecraft:enchantable/mining", 15, 9, 65, 9).apply {
        addProperty("exclusive_set", "#$namespace:exclusive_set/smelting")
    }

    // Mirrors Quick Charge's costs; supported_items is bow-only so crossbows keep using Quick Charge.
    private fun tension() = base("tension", 2, 3, 5, "minecraft:bow", 12, 20, 50, 0).apply {
        addProperty("exclusive_set", "#$namespace:exclusive_set/tension")
    }

    private fun superChanneling() = base(
        "super_channeling", 8, 1, 1, "#$namespace:super_channeling_items", 25, 0, 50, 0
    ).apply {
        addProperty("exclusive_set", "#$namespace:exclusive_set/super_channeling")
        add("effects", obj {
            add("minecraft:hit_block", JsonArray().also {
                it.add(lightningEffect(hitBlockRequirements()))
            })
            add("minecraft:post_attack", JsonArray().also {
                it.add(lightningEffect(postAttackRequirements()).apply {
                    addProperty("affected", "victim")
                    addProperty("enchanted", "attacker")
                })
            })
        })
    }

    private fun lightningEffect(requirements: JsonObject) = obj {
        add("effect", obj {
            addProperty("type", "minecraft:all_of")
            add("effects", JsonArray().also {
                it.add(obj {
                    addProperty("type", "minecraft:summon_entity")
                    addProperty("entity", "minecraft:lightning_bolt")
                })
                it.add(obj {
                    addProperty("type", "minecraft:play_sound")
                    addProperty("pitch", 1.0)
                    addProperty("sound", "minecraft:item.trident.thunder")
                    addProperty("volume", 5.0)
                })
            })
        })
        add("requirements", requirements)
    }

    private fun hitBlockRequirements() = obj {
        addProperty("condition", "minecraft:all_of")
        add("terms", JsonArray().also {
            it.add(entityTypeRequirement("this", "#$namespace:super_channeling_tridents"))
            it.add(obj {
                addProperty("condition", "minecraft:location_check")
                add("predicate", obj {
                    add("block", obj { addProperty("blocks", "#minecraft:lightning_rods") })
                    addProperty("can_see_sky", true)
                })
            })
        })
    }

    private fun postAttackRequirements() = obj {
        addProperty("condition", "minecraft:all_of")
        add("terms", JsonArray().also {
            it.add(obj {
                addProperty("condition", "minecraft:entity_properties")
                addProperty("entity", "this")
                add("predicate", obj {
                    add("minecraft:location", obj { addProperty("can_see_sky", true) })
                })
            })
            it.add(entityTypeRequirement("direct_attacker", "#$namespace:super_channeling_tridents"))
        })
    }

    private fun entityTypeRequirement(entity: String, type: String) = obj {
        addProperty("condition", "minecraft:entity_properties")
        addProperty("entity", entity)
        add("predicate", obj { addProperty("minecraft:entity_type", type) })
    }

    private fun curseOfMass() = base(
        "curse_of_mass", 8, 1, 1, "#$namespace:curse_of_mass_items", 25, 0, 25, 0
    ).apply {
        add("slots", JsonArray().also { it.add("head"); it.add("chest"); it.add("legs"); it.add("feet"); it.add("mainhand"); it.add("offhand") })
    }

    private fun curseOfSloth() = base(
        "curse_of_sloth", 8, 1, 1, "#$namespace:curse_of_sloth_items", 25, 0, 25, 0
    ).apply {
        add("slots", JsonArray().also { it.add("head"); it.add("chest"); it.add("legs"); it.add("feet"); it.add("mainhand"); it.add("offhand") })
    }

    override fun getName() = "SupremeMC enchantments"
}