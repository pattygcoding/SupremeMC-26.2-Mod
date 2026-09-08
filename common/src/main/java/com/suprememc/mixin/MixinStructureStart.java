package com.suprememc.mixin;

import com.suprememc.content.worldgen.NetherMineshaftContext;
import com.suprememc.content.worldgen.NetherMineshaftStructure;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Flags NetherMineshaftContext.ACTIVE while this start places its pieces, so
// MixinMineshaftStructureType can swap in warped planks/fence for that structure only.
@Mixin(StructureStart.class)
public abstract class MixinStructureStart {

    @Shadow
    @Final
    private Structure structure;

    @Inject(method = "placeInChunk", at = @At("HEAD"))
    private void suprememc$beginNetherMineshaft(CallbackInfo ci) {
        if (this.structure instanceof NetherMineshaftStructure) {
            NetherMineshaftContext.ACTIVE.set(true);
        }
    }

    @Inject(method = "placeInChunk", at = @At("RETURN"))
    private void suprememc$endNetherMineshaft(CallbackInfo ci) {
        NetherMineshaftContext.ACTIVE.set(false);
    }
}
