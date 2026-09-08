package com.suprememc.content.items;

import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModToolMaterials;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class AbyssaliteShovelItem extends ShovelItem {
    public AbyssaliteShovelItem() { super(ModToolMaterials.ABYSSALITE, 1.5F, -3.0F, ModContent.itemProperties("abyssalite_shovel")); }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if ((state.is(Blocks.DIRT) || state.is(Blocks.COARSE_DIRT) || state.is(Blocks.ROOTED_DIRT)) && !level.isClientSide()) {
            level.setBlock(pos, Blocks.MUD.defaultBlockState(), 3);
            context.getItemInHand().hurtAndBreak(1, context.getPlayer(), net.minecraft.world.entity.EquipmentSlot.MAINHAND);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
}
