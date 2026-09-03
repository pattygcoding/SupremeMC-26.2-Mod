package com.suprememc.content.items;

import com.suprememc.content.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;

public class AbyssaliteHoeItem extends HoeItem {
    public AbyssaliteHoeItem() { super(ModContent.ABYSSALITE_TOOL_MATERIAL, 0.0F, -3.0F, ModContent.itemProperties("abyssalite_hoe")); }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if ((state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK)) && !level.isClientSide()) {
            level.setBlock(pos, ModContent.WET_FARMLAND.defaultBlockState().setValue(FarmlandBlock.MOISTURE, 7), 3);
            context.getItemInHand().hurtAndBreak(1, context.getPlayer(), net.minecraft.world.entity.EquipmentSlot.MAINHAND);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
}
