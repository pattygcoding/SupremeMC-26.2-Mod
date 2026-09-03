package com.suprememc.content.items;

import com.suprememc.Constants;
import com.suprememc.content.ModContent;
import com.suprememc.content.entity.AbyssaliteTridentEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;

public class AbyssaliteTridentItem extends TridentItem {
    public AbyssaliteTridentItem(Item.Properties properties) {
        super(properties.attributes(ItemAttributeModifiers.builder()
            .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssalite_trident_damage"), 10.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
            // Base player attack speed is 4.0, so -2.8 yields the intended effective 1.2 attack speed.
            .add(Attributes.ATTACK_SPEED, new AttributeModifier(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssalite_trident_speed"), -2.8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
            .build()));
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack stack, Direction direction) {
        return new AbyssaliteTridentEntity(level, position.x(), position.y(), position.z(), stack);
    }
}
