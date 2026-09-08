package com.suprememc.content.items;

import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;

public class EmeraldArmorItem extends Item {
    public EmeraldArmorItem(String id, ArmorType type) {
        super(ModContent.itemProperties(id).humanoidArmor(ModArmorMaterials.EMERALD, type));
    }
}
