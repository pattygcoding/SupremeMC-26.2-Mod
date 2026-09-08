package com.suprememc.content.items;

import com.suprememc.Constants;
import com.suprememc.content.entity.AbyssaliteTridentEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
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
    // The player base attack speed is 4.0, so the -2.8 modifier yields the intended effective 1.2 attack speed.
    // The raw modifier would render as "-2.8 Attack Speed"; override the display to show the effective +1.2.
    private static final Component ATTACK_SPEED_TOOLTIP = Component.translatable("attribute.modifier.plus.0", "1.6",
            Component.translatable(Attributes.ATTACK_SPEED.value().getDescriptionId())).withStyle(ChatFormatting.BLUE);

    public AbyssaliteTridentItem(Item.Properties properties) {
        super(properties.attributes(ItemAttributeModifiers.builder()
            .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssalite_trident_damage"), 10.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
            .add(Attributes.ATTACK_SPEED, new AttributeModifier(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssalite_trident_speed"), -2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND, ItemAttributeModifiers.Display.override(ATTACK_SPEED_TOOLTIP))
            .build()));
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack stack, Direction direction) {
        return new AbyssaliteTridentEntity(level, position.x(), position.y(), position.z(), stack);
    }
}
