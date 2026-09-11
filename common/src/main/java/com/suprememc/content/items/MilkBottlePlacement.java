package com.suprememc.content.items;

import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModFluids;
import com.suprememc.content.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class MilkBottlePlacement {
    private MilkBottlePlacement() {
    }

    public static InteractionResult fill(Player player, InteractionHand hand, Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.is(ModContent.MILK_CAULDRON) && state.getValue(LayeredCauldronBlock.LEVEL) > 0) {
            if (!level.isClientSide()) {
                int levelValue = state.getValue(LayeredCauldronBlock.LEVEL);
                BlockState nextState = levelValue == 1
                    ? net.minecraft.world.level.block.Blocks.CAULDRON.defaultBlockState()
                    : state.setValue(LayeredCauldronBlock.LEVEL, levelValue - 1);
                level.setBlockAndUpdate(pos, nextState);
                player.setItemInHand(hand, ItemUtils.createFilledResult(
                    player.getItemInHand(hand), player, new ItemStack(ModItems.MILK_BOTTLE)));
            }
            return InteractionResult.SUCCESS;
        }

        if (level.getFluidState(pos).isSourceOfType(ModFluids.MILK)) {
            if (!level.isClientSide()) {
                level.setBlockAndUpdate(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
                player.setItemInHand(hand, ItemUtils.createFilledResult(
                    player.getItemInHand(hand), player, new ItemStack(ModItems.MILK_BOTTLE)));
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}