package com.suprememc.client;

import com.mojang.serialization.MapCodec;
import com.suprememc.Constants;
import com.suprememc.content.enchantment.TensionEnchantment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Bow draw progress normalized against the Tension-adjusted draw time, replacing vanilla's
 * {@code minecraft:use_duration} with its hardcoded 0.05 (1/20 tick) scale.
 */
public class BowPullProperty implements RangeSelectItemModelProperty {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "bow_pull");
    public static final MapCodec<BowPullProperty> MAP_CODEC = MapCodec.unit(new BowPullProperty());

    @Override
    public float get(ItemStack itemStack, ClientLevel level, ItemOwner owner, int seed) {
        LivingEntity entity = owner == null ? null : owner.asLivingEntity();
        if (entity == null || entity.getUseItem() != itemStack) {
            return 0.0F;
        }
        return UseDuration.useDuration(itemStack, entity) / (float) TensionEnchantment.getBowDrawTicks(itemStack);
    }

    @Override
    public MapCodec<BowPullProperty> type() {
        return MAP_CODEC;
    }
}
