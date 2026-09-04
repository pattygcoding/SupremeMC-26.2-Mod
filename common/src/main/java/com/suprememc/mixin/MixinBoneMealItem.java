package com.suprememc.mixin;

import com.suprememc.Constants;
import com.suprememc.content.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public abstract class MixinBoneMealItem {
    private static final ResourceKey<Biome> CAYS =
        ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "cays"));

    @Inject(method = "growCrop", at = @At("HEAD"), cancellable = true)
    private static void suprememc$growBeachGrass(ItemStack stack, Level level, BlockPos pos,
            CallbackInfoReturnable<Boolean> callback) {
        if (!level.getBlockState(pos).is(Blocks.SAND) || !suprememc$isBeachBiome(level, pos)) {
            return;
        }

        if (level instanceof ServerLevel serverLevel) {
            suprememc$spreadBeachGrass(serverLevel, pos);
        }
        callback.setReturnValue(true);
    }

    private static void suprememc$spreadBeachGrass(ServerLevel level, BlockPos pos) {
        RandomSource random = level.getRandom();
        for (int attempt = 0; attempt < 128; attempt++) {
            BlockPos grassPos = pos.above();
            for (int step = 0; step < attempt / 16; step++) {
                grassPos = grassPos.offset(
                    random.nextInt(3) - 1,
                    (random.nextInt(3) - 1) * random.nextInt(3) / 2,
                    random.nextInt(3) - 1
                );
            }

            if (level.getBlockState(grassPos.below()).is(Blocks.SAND)
                    && level.isEmptyBlock(grassPos)
                    && level.isInsideBuildHeight(grassPos)
                    && suprememc$isBeachBiome(level, grassPos)) {
                level.setBlockAndUpdate(grassPos, ModContent.BEACH_GRASS.defaultBlockState());
            }
        }
    }

    private static boolean suprememc$isBeachBiome(Level level, BlockPos pos) {
        return level.getBiome(pos).is(BiomeTags.IS_BEACH) || level.getBiome(pos).is(CAYS);
    }
}