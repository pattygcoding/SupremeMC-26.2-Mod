package com.suprememc.mixin;

import com.suprememc.content.init.ModEntities;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;

// adds a concrete canBoatInFluid override to AbstractBoat, taking precedence over IAbstractBoatExtension's
// default (state.supportsBoating(boat)) so crimson/warped boats also float on lava
@Mixin(AbstractBoat.class)
public class MixinAbstractBoat {

	public boolean canBoatInFluid(FluidState state) {
		AbstractBoat self = (AbstractBoat) (Object) this;
		EntityType<?> type = self.getType();
		if ((type == ModEntities.CRIMSON_BOAT_ENTITY || type == ModEntities.CRIMSON_CHEST_BOAT_ENTITY
			|| type == ModEntities.WARPED_BOAT_ENTITY || type == ModEntities.WARPED_CHEST_BOAT_ENTITY)
			&& state.is(FluidTags.LAVA)) {
			return true;
		}
		return state.supportsBoating(self);
	}
}
