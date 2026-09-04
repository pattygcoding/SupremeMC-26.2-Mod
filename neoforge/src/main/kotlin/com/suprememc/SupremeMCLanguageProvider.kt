package com.suprememc

import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class SupremeMCLanguageProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val items = arrayOf("aquamarine", "aquamarine_pickaxe", "aquamarine_axe", "aquamarine_shovel", "aquamarine_hoe", "aquamarine_sword", "aquamarine_helmet", "aquamarine_chestplate", "aquamarine_leggings", "aquamarine_boots", "emerald_pickaxe", "emerald_axe", "emerald_shovel", "emerald_hoe", "emerald_sword", "emerald_helmet", "emerald_chestplate", "emerald_leggings", "emerald_boots", "abyssalite_scrap", "abyssalite_ingot", "abyssalite_upgrade_smithing_template", "abyssalite_pickaxe", "abyssalite_axe", "abyssalite_shovel", "abyssalite_hoe", "abyssalite_sword", "abyssalite_helmet", "abyssalite_chestplate", "abyssalite_leggings", "abyssalite_boots", "abyssalite_trident", "palm_sign", "palm_hanging_sign", "palm_boat", "palm_chest_boat", "coconut", "coconut_seeds", "cotton", "calamari", "cooked_calamari")
        val blocks = arrayOf("aquamarine_ore", "deepslate_aquamarine_ore", "aquamarine_block", "wet_farmland", "atlantis_debris", "abyssalite_block", "palm_log", "stripped_palm_log", "palm_wood", "stripped_palm_wood", "palm_planks", "palm_slab", "palm_stairs", "palm_fence", "palm_fence_gate", "palm_door", "palm_trapdoor", "palm_pressure_plate", "palm_button", "palm_sign", "palm_wall_sign", "palm_hanging_sign", "palm_wall_hanging_sign", "palm_leaves", "palm_sapling", "potted_palm_sapling", "coconut", "cotton_bush")
        val enchantments = arrayOf("bounty", "venom", "decay", "wisdom")
        val json = obj {
            addProperty("itemGroup.$namespace.main", "SupremeMC")
            items.forEach { addProperty("item.$namespace.$it", displayName(it)) }
            blocks.forEach { addProperty("block.$namespace.$it", displayName(it)) }
            addProperty("item.$namespace.abyssalite_upgrade_smithing_template.upgrade_description", "Upgrade to Abyssalite")
            enchantments.forEach { addProperty("enchantment.$namespace.$it", displayName(it)) }
        }
        return save(cache, json, resourcePath("lang/en_us.json"))
    }

    override fun getName() = "SupremeMC language"
}