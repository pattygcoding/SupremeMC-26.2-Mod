package com.suprememc.content.fluid;

import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModFluids;
import com.suprememc.content.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.InsideBlockEffectType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;

public abstract class LiquidNitrogenFluid extends LavaFluid {
    @Override
    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
        super.createFluidStateDefinition(builder);
        builder.add(LEVEL);
    }

    @Override
    protected void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) {
        effectApplier.apply(InsideBlockEffectType.EXTINGUISH);
        // applied twice per tick so entities freeze twice as fast as powdered snow
        effectApplier.apply(InsideBlockEffectType.FREEZE);
        effectApplier.apply(InsideBlockEffectType.FREEZE);
        if (level instanceof ServerLevel serverLevel && entity instanceof LivingEntity living
                && living.canFreeze() && living.isFullyFrozen() && living.tickCount % 40 == 0) {
            // stacked on top of the vanilla freeze-damage tick, making it twice as painful
            living.hurtServer(serverLevel, living.damageSources().freeze(), 1.0F);
        }
    }

    @Override
    public void randomTick(ServerLevel level, BlockPos pos, FluidState state, RandomSource random) {
        // liquid nitrogen freezes entities instead of igniting flammable neighbours
    }

    @Override
    public void animateTick(Level level, BlockPos pos, FluidState fluidState, RandomSource random) {
        BlockPos above = pos.above();
        if (level.getBlockState(above).isAir() && !level.getBlockState(above).isSolidRender() && random.nextInt(20) == 0) {
            double xx = pos.getX() + random.nextDouble();
            double yy = pos.getY() + 1.0;
            double zz = pos.getZ() + random.nextDouble();
            level.addParticle(ParticleTypes.SNOWFLAKE, xx, yy, zz, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public Fluid getFlowing() {
        return ModFluids.FLOWING_LIQUID_NITROGEN;
    }

    @Override
    public Fluid getSource() {
        return ModFluids.LIQUID_NITROGEN;
    }

    @Override
    public Item getBucket() {
        return ModItems.LIQUID_NITROGEN_BUCKET;
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == ModFluids.LIQUID_NITROGEN || fluid == ModFluids.FLOWING_LIQUID_NITROGEN;
    }

    @Override
    public BlockState createLegacyBlock(FluidState state) {
        return ModContent.LIQUID_NITROGEN_FLUID_BLOCK.defaultBlockState()
            .setValue(net.minecraft.world.level.block.LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    public static final class Source extends LiquidNitrogenFluid {
        @Override
        public boolean isSource(FluidState state) {
            return true;
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }
    }

    public static final class Flowing extends LiquidNitrogenFluid {
        @Override
        public boolean isSource(FluidState state) {
            return false;
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }
    }
}