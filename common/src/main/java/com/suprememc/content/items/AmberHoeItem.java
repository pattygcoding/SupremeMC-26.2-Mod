package com.suprememc.content.items;

import com.suprememc.content.ModContent;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ToolMaterial;

public class AmberHoeItem extends HoeItem {
    public AmberHoeItem(String id) { super(ToolMaterial.DIAMOND, 0.0F, 0.0F, ModContent.itemProperties(id)); }
}