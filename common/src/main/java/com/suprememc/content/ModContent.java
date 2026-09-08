package com.suprememc.content;

import com.suprememc.Constants;
import com.suprememc.content.init.ModBlocks;
import com.suprememc.content.init.ModBlockSetTypes;
import com.suprememc.content.init.ModBlockItems;
import com.suprememc.content.init.ModCompostables;
import com.suprememc.content.init.ModEntities;
import com.suprememc.content.init.ModFlammables;
import com.suprememc.content.init.ModFluids;
import com.suprememc.content.init.ModFoods;
import com.suprememc.content.init.ModItems;
import com.suprememc.content.init.ModPotions;
import com.suprememc.content.init.ModFeatures;
import com.suprememc.content.init.ModFoliagePlacerTypes;
import com.suprememc.content.init.ModPlacementModifiers;
import com.suprememc.content.init.ModStrippables;
import com.suprememc.content.init.ModStructureTypes;
import com.suprememc.content.init.ModTreeDecorators;
import com.suprememc.content.init.ModTreeGrowers;
import com.suprememc.content.init.ModTrunkPlacerTypes;
import com.suprememc.content.init.ModWoodTypes;
import com.suprememc.tabs.CreativeTab;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.List;

public final class ModContent extends ModBlocks {

    private static boolean registered = false;

    // Loader-agnostic list, shared by fabric/neoforge to populate the SupremeMC creative tab.
    public static final List<ItemLike> CREATIVE_TAB_ITEMS = new java.util.ArrayList<>();

    public static void bootstrap() {
        if (registered) {
            return;
        }
        registered = true;

        ModFluids.bootstrap();
        ModItems.bootstrapInitial();
        ModTreeGrowers.bootstrap();
        ModFoliagePlacerTypes.bootstrap();
        ModTrunkPlacerTypes.bootstrap();
        ModPlacementModifiers.bootstrap();
        ModTreeDecorators.bootstrap();
        ModFeatures.bootstrap();
        ModStructureTypes.bootstrap();
        ModBlockSetTypes.bootstrap();
        ModWoodTypes.bootstrap();
        ModBlocks.bootstrap();
        ModEntities.bootstrap();
        ModPotions.bootstrap();

        ModStrippables.bootstrap();
        ModFlammables.bootstrap();

        ModBlockItems.bootstrap();
        ModFoods.bootstrap();

        ModBlockItems.bootstrapSignItems();
        ModItems.bootstrapArmor();
        ModCompostables.bootstrap();
        ModItems.bootstrapRemaining();

        CreativeTab.populate();

        Constants.LOG.info("Registered SupremeMC progression content");
    }

    public static SmithingTemplateItem smithingTemplateItem(String type, String material, String base, String addition) {
        String translationPrefix = "item." + Constants.MOD_ID + ".smithing_template." + type;
        return new SmithingTemplateItem(
            Component.translatable(translationPrefix + ".applies_to"),
            Component.translatable(translationPrefix + ".ingredients"),
            Component.translatable(translationPrefix + ".base_slot_description"),
            Component.translatable(translationPrefix + ".additions_slot_description"),
            List.of(
                Identifier.withDefaultNamespace("item/empty_slot_helmet"),
                Identifier.withDefaultNamespace("item/empty_slot_chestplate"),
                Identifier.withDefaultNamespace("item/empty_slot_leggings"),
                Identifier.withDefaultNamespace("item/empty_slot_boots"),
                Identifier.withDefaultNamespace("item/empty_slot_hoe"),
                Identifier.withDefaultNamespace("item/empty_slot_axe"),
                Identifier.withDefaultNamespace("item/empty_slot_sword"),
                Identifier.withDefaultNamespace("item/empty_slot_shovel"),
                Identifier.withDefaultNamespace("item/empty_slot_pickaxe"),
                Identifier.withDefaultNamespace("item/empty_slot_spear")
            ),
            List.of(Identifier.withDefaultNamespace("item/empty_slot_ingot")),
            itemProperties(material + "_upgrade_smithing_template").stacksTo(64).rarity(Rarity.UNCOMMON)
        );
    }

    public static Item.Properties itemProperties(String id) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, id)));
    }

    public static BlockBehaviour.Properties blockProperties(String id) {
        return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, id)));
    }

}
