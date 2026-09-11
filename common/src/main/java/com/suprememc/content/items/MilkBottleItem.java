package com.suprememc.content.items;

import com.suprememc.content.ModContent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class MilkBottleItem extends Item {
    public MilkBottleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return MilkBottlePlacement.fill(context.getPlayer(), context.getHand(), context.getLevel(), context.getClickedPos());
    }
}