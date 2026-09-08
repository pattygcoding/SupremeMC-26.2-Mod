package com.suprememc.content.fluid;

import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModFluids;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.WaterFluid;

public abstract class MilkFluid extends WaterFluid {
    @Override
    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, net.minecraft.world.level.material.FluidState> builder) {
        super.createFluidStateDefinition(builder);
        builder.add(LEVEL);
    }

    @Override
    public Fluid getFlowing() {
        return ModFluids.FLOWING_MILK;
    }

    @Override
    public Fluid getSource() {
        return ModFluids.MILK;
    }

    @Override
    public Item getBucket() {
        return Items.MILK_BUCKET;
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == ModFluids.MILK || fluid == ModFluids.FLOWING_MILK;
    }

    @Override
    public BlockState createLegacyBlock(net.minecraft.world.level.material.FluidState state) {
        return ModContent.MILK_FLUID_BLOCK.defaultBlockState()
            .setValue(net.minecraft.world.level.block.LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    public static final class Source extends MilkFluid {
        @Override
        public boolean isSource(net.minecraft.world.level.material.FluidState state) {
            return true;
        }

        @Override
        public int getAmount(net.minecraft.world.level.material.FluidState state) {
            return 8;
        }
    }

    public static final class Flowing extends MilkFluid {
        @Override
        public boolean isSource(net.minecraft.world.level.material.FluidState state) {
            return false;
        }

        @Override
        public int getAmount(net.minecraft.world.level.material.FluidState state) {
            return state.getValue(LEVEL);
        }
    }
}