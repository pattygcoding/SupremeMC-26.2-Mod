package com.suprememc.content.init;

import com.suprememc.Constants;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/** Lookup helpers for the mod's data-driven enchantments that also need runtime behavior. */
public final class ModEnchantments {
    public static final ResourceKey<Enchantment> CURSE_OF_MASS = ResourceKey.create(
            Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "curse_of_mass"));

    public static final ResourceKey<Enchantment> CURSE_OF_SLOTH = ResourceKey.create(
            Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "curse_of_sloth"));

    public static final ResourceKey<Enchantment> TENSION = ResourceKey.create(
            Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "tension"));

    public static final int TENSION_MAX_LEVEL = 3;
    public static final int TENSION_TICKS_PER_LEVEL = 4;

    private ModEnchantments() {
    }

    /**
     * Enchantment levels are read off the stack's component instead of the registry so the same
     * code path works on both loaders without holding a {@link Holder} to a datapack entry.
     */
    public static int getLevel(ResourceKey<Enchantment> enchantment, ItemStack stack) {
        for (Object2IntMap.Entry<Holder<Enchantment>> entry
                : EnchantmentHelper.getEnchantmentsForCrafting(stack).entrySet()) {
            if (entry.getKey().is(enchantment)) {
                return entry.getIntValue();
            }
        }
        return 0;
    }

    /** Returns the Slowness amplifier required by the cursed equipment currently worn or held. */
    public static int getCurseOfMassAmplifier(Player player) {
        int cursedArmorPieces = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmor() && getLevel(CURSE_OF_MASS, player.getItemBySlot(slot)) > 0) {
                cursedArmorPieces++;
            }
        }

        boolean cursedItemHeld = getLevel(CURSE_OF_MASS, player.getMainHandItem()) > 0
                || getLevel(CURSE_OF_MASS, player.getOffhandItem()) > 0;
        int affectedPieces = Math.max(cursedArmorPieces, cursedItemHeld ? 1 : 0);
        return affectedPieces == 0 ? -1 : Math.min(affectedPieces - 1, 2);
    }

    /** Returns the Weakness amplifier required by the cursed equipment currently worn or held. */
    public static int getCurseOfSlothAmplifier(Player player) {
        int cursedArmorPieces = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmor() && getLevel(CURSE_OF_SLOTH, player.getItemBySlot(slot)) > 0) {
                cursedArmorPieces++;
            }
        }

        boolean cursedItemHeld = getLevel(CURSE_OF_SLOTH, player.getMainHandItem()) > 0
                || getLevel(CURSE_OF_SLOTH, player.getOffhandItem()) > 0;
        int affectedPieces = Math.max(cursedArmorPieces, cursedItemHeld ? 1 : 0);
        return affectedPieces == 0 ? -1 : Math.min(affectedPieces - 1, 2);
    }

    /** Ticks a bow must be held to reach full draw, shortened by 4 ticks per Tension level. */
    public static int getBowDrawTicks(ItemStack bow) {
        int level = Math.min(Math.max(getLevel(TENSION, bow), 0), TENSION_MAX_LEVEL);
        return Math.max(1, BowItem.MAX_DRAW_DURATION - level * TENSION_TICKS_PER_LEVEL);
    }

    /**
     * Vanilla's {@link BowItem#getPowerForTime(int)} curve rescaled to the Tension-adjusted draw
     * time, so releasing at full draw still returns exactly 1.0F (full velocity and a crit arrow).
     */
    public static float getBowPowerForTime(ItemStack bow, int timeHeld) {
        float charge = Math.min(timeHeld / (float) getBowDrawTicks(bow), 1.0F);
        return (charge * charge + charge * 2.0F) / 3.0F;
    }
}