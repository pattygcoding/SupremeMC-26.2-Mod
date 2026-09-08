package com.suprememc.content.items;

import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public final class MilkBucketPlacement {
    private MilkBucketPlacement() {
    }

    public static boolean place(LivingEntity user, Level level, BlockHitResult hit) {
        BlockPos pos = hit.getBlockPos();
        if (level.getBlockState(pos).is(Blocks.CAULDRON)) {
            if (!level.isClientSide()) {
                level.setBlockAndUpdate(pos, ModContent.MILK_CAULDRON.defaultBlockState()
                    .setValue(net.minecraft.world.level.block.LayeredCauldronBlock.LEVEL,
                        net.minecraft.world.level.block.LayeredCauldronBlock.MAX_FILL_LEVEL));
            }
            return true;
        }
        BlockPos placementPos = pos.relative(hit.getDirection());
        BlockState placementState = level.getBlockState(placementPos);
        if (!placementState.canBeReplaced(ModFluids.MILK)) {
            return false;
        }
        if (!level.isClientSide()) {
            level.setBlockAndUpdate(placementPos, ModContent.MILK_FLUID_BLOCK.defaultBlockState());
            if (user instanceof net.minecraft.world.entity.player.Player player && !player.getAbilities().instabuild) {
                player.setItemInHand(player.getUsedItemHand(), ItemUtils.createFilledResult(
                    player.getItemInHand(player.getUsedItemHand()), player, new net.minecraft.world.item.ItemStack(Items.BUCKET)));
            }
        }
        return true;
    }
}