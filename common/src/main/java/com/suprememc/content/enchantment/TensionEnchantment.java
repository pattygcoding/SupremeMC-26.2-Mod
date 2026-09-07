package com.suprememc.content.enchantment;

import com.suprememc.Constants;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/** Runtime behavior for the data-driven {@code suprememc:tension} bow enchantment. */
public final class TensionEnchantment {
    public static final ResourceKey<Enchantment> TENSION = ResourceKey.create(
            Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "tension"));

    public static final int MAX_LEVEL = 3;
    public static final int TICKS_SAVED_PER_LEVEL = 4;

    private TensionEnchantment() {
    }

    /** Read off the stack's component so the same code works on both loaders without a registry lookup. */
    public static int getLevel(ItemStack stack) {
        for (Object2IntMap.Entry<Holder<Enchantment>> entry
                : EnchantmentHelper.getEnchantmentsForCrafting(stack).entrySet()) {
            if (entry.getKey().is(TENSION)) {
                return Math.min(Math.max(entry.getIntValue(), 0), MAX_LEVEL);
            }
        }
        return 0;
    }

    /** Ticks a bow must be held to reach full draw, shortened by 4 ticks per Tension level. */
    public static int getBowDrawTicks(ItemStack bow) {
        return Math.max(1, BowItem.MAX_DRAW_DURATION - getLevel(bow) * TICKS_SAVED_PER_LEVEL);
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
