package com.suprememc.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class MilkCauldronBlock extends LayeredCauldronBlock {
    public MilkCauldronBlock(BlockBehaviour.Properties properties) {
        super(net.minecraft.world.level.biome.Biome.Precipitation.NONE, new CauldronInteraction.Dispatcher(), properties);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.is(Items.BUCKET) && isFull(state)) {
            if (!level.isClientSide()) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.MILK_BUCKET)));
                level.setBlockAndUpdate(pos, net.minecraft.world.level.block.Blocks.CAULDRON.defaultBlockState());
            }
            return InteractionResult.SUCCESS;
        }
        if (stack.is(Items.MILK_BUCKET) && !isFull(state)) {
            if (!level.isClientSide()) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
                level.setBlockAndUpdate(pos, state.setValue(LEVEL, MAX_FILL_LEVEL));
            }
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }
}