package com.suprememc.mixin;

import com.suprememc.content.init.ModBlocks;
import com.suprememc.content.worldgen.EndMineshaftContext;
import com.suprememc.content.worldgen.NetherMineshaftContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Nether/End mineshafts (NetherMineshaftStructure/EndMineshaftStructure) reuse vanilla's NORMAL
// mineshaft type as-is, but while one is actively placing blocks (see MixinStructureStart) its
// oak planks/fence become warped or lavender planks/fence. MESA-type mineshafts never run with
// either flag set, so no type check needed.
@Mixin(MineshaftStructure.Type.class)
public abstract class MixinMineshaftStructureType {

	@Inject(method = "getPlanksState", at = @At("HEAD"), cancellable = true)
	private void suprememc$netherPlanks(CallbackInfoReturnable<BlockState> callback) {
		if (NetherMineshaftContext.ACTIVE.get()) {
			callback.setReturnValue(Blocks.WARPED_PLANKS.defaultBlockState());
		} else if (EndMineshaftContext.ACTIVE.get()) {
			callback.setReturnValue(ModBlocks.LAVENDER_PLANKS.defaultBlockState());
		}
	}

	@Inject(method = "getFenceState", at = @At("HEAD"), cancellable = true)
	private void suprememc$netherFence(CallbackInfoReturnable<BlockState> callback) {
		if (NetherMineshaftContext.ACTIVE.get()) {
			callback.setReturnValue(Blocks.WARPED_FENCE.defaultBlockState());
		} else if (EndMineshaftContext.ACTIVE.get()) {
			callback.setReturnValue(ModBlocks.LAVENDER_FENCE.defaultBlockState());
		}
	}
}
