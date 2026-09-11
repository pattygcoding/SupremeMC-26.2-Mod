package com.suprememc.mixin;

import com.suprememc.content.init.ModEntities;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// vanilla hardcodes FluidTags.WATER at these call sites when checking whether the boat can float;
// redirect them so crimson/warped boats also count lava as a floatable fluid
@Mixin(AbstractBoat.class)
public abstract class MixinAbstractBoat {

	@Redirect(method = {"checkInWater", "isUnderwater", "getWaterLevelAbove", "checkFallDamage"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
	private boolean suprememc$allowLavaBoating(FluidState state, TagKey<Fluid> tag) {
		if (state.is(tag)) {
			return true;
		}
		if (tag != FluidTags.WATER) {
			return false;
		}
		EntityType<?> type = ((AbstractBoat) (Object) this).getType();
		return (type == ModEntities.CRIMSON_BOAT_ENTITY || type == ModEntities.CRIMSON_CHEST_BOAT_ENTITY
			|| type == ModEntities.WARPED_BOAT_ENTITY || type == ModEntities.WARPED_CHEST_BOAT_ENTITY)
			&& state.is(FluidTags.LAVA);
	}
}
