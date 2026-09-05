package com.suprememc.content.items;

import com.suprememc.content.ModContent;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;

public class AmberShovelItem extends ShovelItem {
    public AmberShovelItem(String id) { super(ToolMaterial.DIAMOND, 1.5F, -3.0F, ModContent.itemProperties(id)); }
}