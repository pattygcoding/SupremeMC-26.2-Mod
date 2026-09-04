package com.suprememc.mixin;

import com.suprememc.content.blocks.ColoredSlimeBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Colored slime blocks must behave like vanilla slime for piston stickiness, but only stick to
// slime blocks sharing the same dye color (not vanilla slime, honey, or other colors).
@Mixin(PistonStructureResolver.class)
public abstract class MixinPistonStructureResolver {

    @Inject(method = "isSticky", at = @At("HEAD"), cancellable = true)
    private static void suprememc$coloredSlimeIsSticky(BlockState state, CallbackInfoReturnable<Boolean> callback) {
        if (state.getBlock() instanceof ColoredSlimeBlock) {
            callback.setReturnValue(true);
        }
    }

    @Inject(method = "canStickToEachOther", at = @At("HEAD"), cancellable = true)
    private static void suprememc$coloredSlimeCanStickToEachOther(BlockState state1, BlockState state2,
            CallbackInfoReturnable<Boolean> callback) {
        boolean colored1 = state1.getBlock() instanceof ColoredSlimeBlock;
        boolean colored2 = state2.getBlock() instanceof ColoredSlimeBlock;
        if (!colored1 && !colored2) {
            return;
        }
        // Only restrict sticking when the other side is also slime/honey/colored-slime; plain
        // blocks should still stick to colored slime the same way they do to vanilla slime.
        BlockState other = colored1 ? state2 : state1;
        boolean otherIsStickyFamily = colored2 || other.is(Blocks.SLIME_BLOCK) || other.is(Blocks.HONEY_BLOCK);
        if (!otherIsStickyFamily) {
            return;
        }
        boolean sameColor = colored1 && colored2
            && ((ColoredSlimeBlock) state1.getBlock()).getColor() == ((ColoredSlimeBlock) state2.getBlock()).getColor();
        callback.setReturnValue(sameColor);
    }
}
