package com.suprememc

import com.google.gson.JsonArray
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
        writes += save(cache, valuesTag("minecraft:looting", "$namespace:bounty"), dataPath("tags/enchantment/exclusive_set/bounty.json"))
        writes += save(cache, valuesTag("minecraft:fire_aspect", "$namespace:venom", "$namespace:decay"), dataPath("tags/enchantment/exclusive_set/status_damage.json"))
        writes += save(cache, valuesTag("minecraft:thorns", "$namespace:wisdom"), dataPath("tags/enchantment/exclusive_set/xp_armor.json"))
        writes += save(cache, valuesTag("minecraft:silk_touch", "$namespace:smelting"), dataPath("tags/enchantment/exclusive_set/smelting.json"))
        writes += save(cache, valuesTag("minecraft:punch", "$namespace:tension"), dataPath("tags/enchantment/exclusive_set/tension.json"))
        writes += save(cache, valuesTag("$namespace:curse_of_mass", "$namespace:curse_of_sloth"), minecraftDataPath("tags/enchantment/curse.json"))
        writes += save(cache, valuesTag("$namespace:curse_of_mass", "$namespace:curse_of_sloth"), minecraftDataPath("tags/enchantment/treasure.json"))
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