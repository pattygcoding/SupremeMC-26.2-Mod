package com.suprememc.content.items;

import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModArmorMaterials;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;

public class ExperienceArmorItem extends Item {
    public ExperienceArmorItem(String id, ArmorType type) {
        super(ModContent.itemProperties(id).humanoidArmor(ModArmorMaterials.EXPERIENCE, type));
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (entity instanceof Player player && isFullSet(player)) {
            player.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 25, 0, true, false, true));
        }
    }

    public static boolean isFullSet(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ExperienceArmorItem
            && player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ExperienceArmorItem
            && player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ExperienceArmorItem
            && player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ExperienceArmorItem;
    }
}
