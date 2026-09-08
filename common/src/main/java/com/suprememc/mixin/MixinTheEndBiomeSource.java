package com.suprememc.mixin;

import com.suprememc.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.QuartPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Mixin(TheEndBiomeSource.class)
public abstract class MixinTheEndBiomeSource {
    @Unique
    private static final ResourceKey<Biome> suprememc$LAVENDER_HIGHLANDS =
        ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "lavender_highlands"));
    @Unique
    private static final ResourceKey<Biome> suprememc$LAVENDER_MIDLANDS =
        ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "lavender_midlands"));
    @Unique
    private static final ResourceKey<Biome> suprememc$LAVENDER_BARRENS =
        ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "lavender_barrens"));

    @Unique
    private Holder<Biome> suprememc$lavenderHighlands;
    @Unique
    private Holder<Biome> suprememc$lavenderMidlands;
    @Unique
    private Holder<Biome> suprememc$lavenderBarrens;

    @Inject(method = "create", at = @At("RETURN"))
    private static void suprememc$onCreate(HolderGetter<Biome> holderGetter, CallbackInfoReturnable<TheEndBiomeSource> cir) {
        TheEndBiomeSource source = cir.getReturnValue();
        MixinTheEndBiomeSource mixinSource = (MixinTheEndBiomeSource) (Object) source;
        holderGetter.get(suprememc$LAVENDER_HIGHLANDS).ifPresent(h -> mixinSource.suprememc$lavenderHighlands = h);
        holderGetter.get(suprememc$LAVENDER_MIDLANDS).ifPresent(h -> mixinSource.suprememc$lavenderMidlands = h);
        holderGetter.get(suprememc$LAVENDER_BARRENS).ifPresent(h -> mixinSource.suprememc$lavenderBarrens = h);
    }

    @Inject(method = "getNoiseBiome", at = @At("RETURN"), cancellable = true)
    private void suprememc$swapOuterIslandBiomes(
        int x, int y, int z, Climate.Sampler sampler, CallbackInfoReturnable<Holder<Biome>> cir
    ) {
        Holder<Biome> current = cir.getReturnValue();
        if (current.is(Biomes.THE_END) || current.is(Biomes.SMALL_END_ISLANDS)) {
            return;
        }

        int blockX = QuartPos.toBlock(x);
        int sectionX = SectionPos.blockToSectionCoord(blockX);
        int blockZ = QuartPos.toBlock(z);
        int sectionZ = SectionPos.blockToSectionCoord(blockZ);
        if ((long) sectionX * (long) sectionX + (long) sectionZ * (long) sectionZ <= 4096L) {
            return;
        }

        int blockY = QuartPos.toBlock(y);
        double temp = sampler.temperature().compute(new DensityFunction.SinglePointContext(blockX, blockY, blockZ));
        if (temp >= 0.2) {
            if (current.is(Biomes.END_HIGHLANDS) && this.suprememc$lavenderHighlands != null) {
                cir.setReturnValue(this.suprememc$lavenderHighlands);
            } else if (current.is(Biomes.END_MIDLANDS) && this.suprememc$lavenderMidlands != null) {
                cir.setReturnValue(this.suprememc$lavenderMidlands);
            } else if (current.is(Biomes.END_BARRENS) && this.suprememc$lavenderBarrens != null) {
                cir.setReturnValue(this.suprememc$lavenderBarrens);
            }
        }
    }

    @Inject(method = "collectPossibleBiomes", at = @At("RETURN"), cancellable = true)
    private void suprememc$addLavenderBiomesToPossibleBiomes(CallbackInfoReturnable<Stream<Holder<Biome>>> cir) {
        if (this.suprememc$lavenderHighlands != null) {
            cir.setReturnValue(Stream.concat(cir.getReturnValue(), Stream.of(
                this.suprememc$lavenderHighlands,
                this.suprememc$lavenderMidlands,
                this.suprememc$lavenderBarrens
            )));
        }
    }
}
