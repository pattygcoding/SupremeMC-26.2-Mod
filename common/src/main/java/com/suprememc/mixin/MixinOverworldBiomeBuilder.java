package com.suprememc.mixin;

import com.mojang.datafixers.util.Pair;
import com.suprememc.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

// Vanilla places mangrove_swamp across the entire humidity range of the hottest, flattest, most-inland
// climate slot (see OverworldBiomeBuilder#addMidSlice/#addLowSlice). We split that slot in half by
// humidity so the drier side spawns Florida Plains instead, leaving mangrove swamps on the humid side.
//
// Vanilla also places mushroom_fields across the ENTIRE temperature range of its isolated, ultra-rare
// "mushroom_fields_continentalness" island slice (see OverworldBiomeBuilder#addOffCoastBiomes). We split
// that slot by temperature so the warm/lukewarm-ocean half spawns Cays instead, leaving mushroom fields
// on the cooler half. This keeps Cays' island-in-the-ocean spawn mechanic identical to mushroom fields.
//
// Vanilla also places dripstone_caves across the ENTIRE temperature range of its underground continentalness
// slot (see OverworldBiomeBuilder#addUndergroundBiomes). We split that slot by temperature so the frozen
// (cold-biome) half spawns Ice Caves instead, leaving dripstone_caves on the rest of the temperature range.
@Mixin(OverworldBiomeBuilder.class)
public abstract class MixinOverworldBiomeBuilder {
    private static final ResourceKey<Biome> FLORIDA_PLAINS =
        ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "florida_plains"));
    private static final ResourceKey<Biome> CAYS =
        ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "cays"));
    private static final ResourceKey<Biome> ICE_CAVES =
        ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "ice_caves"));

    @Invoker("addSurfaceBiome")
    abstract void suprememc$addSurfaceBiome(
        Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes,
        Climate.Parameter temperature,
        Climate.Parameter humidity,
        Climate.Parameter continentalness,
        Climate.Parameter erosion,
        Climate.Parameter weirdness,
        float offset,
        ResourceKey<Biome> biome
    );

    @Invoker("addUndergroundBiome")
    abstract void suprememc$addUndergroundBiome(
        Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes,
        Climate.Parameter temperature,
        Climate.Parameter humidity,
        Climate.Parameter continentalness,
        Climate.Parameter erosion,
        Climate.Parameter weirdness,
        float offset,
        ResourceKey<Biome> biome
    );

    @Accessor("FROZEN_RANGE")
    abstract Climate.Parameter suprememc$frozenRange();

    @Accessor("UNFROZEN_RANGE")
    abstract Climate.Parameter suprememc$unfrozenRange();

    @Redirect(
        method = {"addMidSlice", "addLowSlice"},
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addSurfaceBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V",
            ordinal = 2
        )
    )
    private void suprememc$splitMangroveSwampSlotWithFloridaPlains(
        OverworldBiomeBuilder self,
        Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes,
        Climate.Parameter temperature,
        Climate.Parameter humidity,
        Climate.Parameter continentalness,
        Climate.Parameter erosion,
        Climate.Parameter weirdness,
        float offset,
        ResourceKey<Biome> biome
    ) {
        MixinOverworldBiomeBuilder accessor = (MixinOverworldBiomeBuilder) (Object) self;
        accessor.suprememc$addSurfaceBiome(biomes, temperature, Climate.Parameter.span(-1.0F, 0.0F), continentalness, erosion, weirdness, offset, FLORIDA_PLAINS);
        accessor.suprememc$addSurfaceBiome(biomes, temperature, Climate.Parameter.span(0.0F, 1.0F), continentalness, erosion, weirdness, offset, biome);
    }
    // The mushroom_fields isolated-island slot is registered once with FULL_RANGE temperature (see
    // addOffCoastBiomes); this is its only addSurfaceBiome call site, so ordinal 0 targets it.
    @Redirect(
        method = "addOffCoastBiomes",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addSurfaceBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V",
            ordinal = 0
        )
    )
    private void suprememc$splitMushroomFieldsSlotWithCays(
        OverworldBiomeBuilder self,
        Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes,
        Climate.Parameter temperature,
        Climate.Parameter humidity,
        Climate.Parameter continentalness,
        Climate.Parameter erosion,
        Climate.Parameter weirdness,
        float offset,
        ResourceKey<Biome> biome
    ) {
        MixinOverworldBiomeBuilder accessor = (MixinOverworldBiomeBuilder) (Object) self;
        // Cold/temperate half (< 0.2, matching vanilla's frozen/cool/temperate temperature bands) keeps mushroom fields.
        accessor.suprememc$addSurfaceBiome(biomes, Climate.Parameter.span(-1.0F, 0.2F), humidity, continentalness, erosion, weirdness, offset, biome);
        // Warm/hot half (>= 0.2, matching vanilla's warm/hot temperature bands used by lukewarm/warm ocean) spawns Cays.
        accessor.suprememc$addSurfaceBiome(biomes, Climate.Parameter.span(0.2F, 1.0F), humidity, continentalness, erosion, weirdness, offset, CAYS);
    }

    // dripstone_caves is registered once with FULL_RANGE temperature (see addUndergroundBiomes); this is
    // its only addUndergroundBiome call site, so ordinal 0 targets it.
    @Redirect(
        method = "addUndergroundBiomes",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addUndergroundBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V",
            ordinal = 0
        )
    )
    private void suprememc$splitDripstoneCavesSlotWithIceCaves(
        OverworldBiomeBuilder self,
        Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes,
        Climate.Parameter temperature,
        Climate.Parameter humidity,
        Climate.Parameter continentalness,
        Climate.Parameter erosion,
        Climate.Parameter weirdness,
        float offset,
        ResourceKey<Biome> biome
    ) {
        MixinOverworldBiomeBuilder accessor = (MixinOverworldBiomeBuilder) (Object) self;
        // Frozen half (vanilla's coldest temperature band) spawns Ice Caves instead of dripstone_caves.
        accessor.suprememc$addUndergroundBiome(biomes, accessor.suprememc$frozenRange(), humidity, continentalness, erosion, weirdness, offset, ICE_CAVES);
        // The rest of the temperature range keeps dripstone_caves.
        accessor.suprememc$addUndergroundBiome(biomes, accessor.suprememc$unfrozenRange(), humidity, continentalness, erosion, weirdness, offset, biome);
    }
}
