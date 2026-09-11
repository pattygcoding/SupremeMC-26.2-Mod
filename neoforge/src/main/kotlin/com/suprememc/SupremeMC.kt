package com.suprememc

import com.suprememc.content.ModContent
import com.suprememc.content.init.ModItems
import com.suprememc.content.init.ModEntities
import com.suprememc.content.init.ModPotions
import com.suprememc.tabs.CreativeTab
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityTypes
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.registries.RegisterEvent

@Mod(Constants.MOD_ID)
class SupremeMC(eventBus: IEventBus) {
    init {
        Constants.LOG.info("Hello NeoForge world!")
        eventBus.addListener(SupremeMC::registerContent)
        eventBus.addListener(SupremeMC::addSignBlockEntityBlocks)
        eventBus.addListener(SupremeMC::registerAttributes)
        NeoForge.EVENT_BUS.addListener(SupremeMC::registerBrewingRecipes)
            NeoForge.EVENT_BUS.addListener(SupremeMC::registerFuelValues)
        eventBus.addListener(SupremeMC::gatherData)
    }

    companion object {
        // Vanilla's sign block entities only accept their hardcoded block list, so opt the palm signs in.
        private fun addSignBlockEntityBlocks(event: BlockEntityTypeAddBlocksEvent) {
            event.modify(BlockEntityTypes.SIGN, ModContent.PALM_SIGN, ModContent.PALM_WALL_SIGN)
            event.modify(BlockEntityTypes.HANGING_SIGN, ModContent.PALM_HANGING_SIGN, ModContent.PALM_WALL_HANGING_SIGN)
            event.modify(BlockEntityTypes.FURNACE, ModContent.BLACKSTONE_FURNACE, ModContent.DEEPSLATE_FURNACE)
        }
        private fun registerAttributes(event: EntityAttributeCreationEvent) {
            event.put(ModEntities.GRIZZLY_BEAR_ENTITY, net.minecraft.world.entity.animal.polarbear.PolarBear.createAttributes().build())
            event.put(ModEntities.ENDER_SPIDER_ENTITY, com.suprememc.content.entity.EnderSpider.createAttributes().build())
            event.put(ModEntities.FIRE_CREEPER_ENTITY, net.minecraft.world.entity.monster.Creeper.createAttributes().build())
            event.put(ModEntities.SNOW_CREEPER_ENTITY, net.minecraft.world.entity.monster.Creeper.createAttributes().build())
        }
        private fun registerFuelValues(event: FurnaceFuelBurnTimeEvent) {
            when (event.itemStack.item) {
                ModItems.ANTHRACITE -> event.setBurnTime(1600)
                ModContent.ANTHRACITE_BLOCK.asItem() -> event.setBurnTime(16000)
            }
        }
        private fun registerBrewingRecipes(event: RegisterBrewingRecipesEvent) {
            val builder = event.builder
            builder.addMix(net.minecraft.world.item.alchemy.Potions.AWKWARD, ModContent.CLOVER.asItem(), ModPotions.LUCK_POTION)
            builder.addMix(ModPotions.LUCK_POTION, net.minecraft.world.item.Items.REDSTONE, ModPotions.LONG_LUCK_POTION)
            builder.addMix(ModPotions.LUCK_POTION, net.minecraft.world.item.Items.GLOWSTONE_DUST, ModPotions.STRONG_LUCK_POTION)
            builder.addMix(ModPotions.LUCK_POTION, net.minecraft.world.item.Items.FERMENTED_SPIDER_EYE, ModPotions.BAD_LUCK_POTION)
            builder.addMix(ModPotions.LONG_LUCK_POTION, net.minecraft.world.item.Items.FERMENTED_SPIDER_EYE, ModPotions.LONG_BAD_LUCK_POTION)
            builder.addMix(ModPotions.STRONG_LUCK_POTION, net.minecraft.world.item.Items.FERMENTED_SPIDER_EYE, ModPotions.STRONG_BAD_LUCK_POTION)
            builder.addMix(net.minecraft.world.item.alchemy.Potions.AWKWARD, net.minecraft.world.item.Items.ROTTEN_FLESH, ModPotions.HUNGER_POTION)
            builder.addMix(ModPotions.HUNGER_POTION, net.minecraft.world.item.Items.REDSTONE, ModPotions.LONG_HUNGER_POTION)
            builder.addMix(ModPotions.HUNGER_POTION, net.minecraft.world.item.Items.GLOWSTONE_DUST, ModPotions.STRONG_HUNGER_POTION)
            builder.addMix(net.minecraft.world.item.alchemy.Potions.AWKWARD, net.minecraft.world.item.Items.WITHER_ROSE, ModPotions.DECAY_POTION)
            builder.addMix(ModPotions.DECAY_POTION, net.minecraft.world.item.Items.REDSTONE, ModPotions.LONG_DECAY_POTION)
            builder.addMix(ModPotions.DECAY_POTION, net.minecraft.world.item.Items.GLOWSTONE_DUST, ModPotions.STRONG_DECAY_POTION)
            builder.addContainerRecipe(net.minecraft.world.item.Items.SPLASH_POTION, ModItems.EXPERIENCE_DUST, net.minecraft.world.item.Items.SPLASH_POTION)
        }
        private fun registerContent(event: RegisterEvent) {
            if (event.registryKey == Registries.BLOCK || event.registryKey == Registries.ITEM) {
                ModContent.bootstrap()
            } else if (event.registryKey == Registries.CREATIVE_MODE_TAB) {
                val tab = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup.${Constants.MOD_ID}.main"))
                    .icon { ItemStack(ModContent.SUPREME_MC_LOGO_BLOCK) }
                    .displayItems { params, output ->
                        ModContent.CREATIVE_TAB_ITEMS.forEach(output::accept)
                        CreativeTab.enchantedBooks(params.holders()).forEach(output::accept)
                        CreativeTab.potions().forEach(output::accept)
                    }
                    .build()
                Registry.register(
                    BuiltInRegistries.CREATIVE_MODE_TAB,
                    ResourceKey.create(
                        Registries.CREATIVE_MODE_TAB,
                        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "main")
                    ),
                    tab
                )
            }
        }

        private fun gatherData(event: GatherDataEvent.Client) {
            val output = event.generator.packOutput
            event.addProvider(AquamarineDataProvider(output))
            event.addProvider(BurningDiamondDataProvider(output))
            event.addProvider(AmberDataProvider(output))
            event.addProvider(AnthraciteDataProvider(output))
            event.addProvider(XyliumDataProvider(output))
            event.addProvider(DustDataProvider(output))
            event.addProvider(PrismarineDataProvider(output))
            event.addProvider(MaterialBlocksDataProvider(output))
            event.addProvider(SupremeMCLogoBlockDataProvider(output))
            event.addProvider(PolishedStoneWallsDataProvider(output))
            event.addProvider(StoneBrickDataProvider(output))
            event.addProvider(EmeraldDataProvider(output))
            event.addProvider(ExperienceDataProvider(output))
            event.addProvider(AbyssaliteDataProvider(output))
            event.addProvider(IcicleDataProvider(output))
            event.addProvider(PalmDataProvider(output))
            event.addProvider(LavenderDataProvider(output))
            event.addProvider(CottonDataProvider(output))
            event.addProvider(CornDataProvider(output))
            event.addProvider(GrapeVineDataProvider(output))
            event.addProvider(TomatoDataProvider(output))
            event.addProvider(ButtercupCloverDataProvider(output))
            event.addProvider(BeachGrassDataProvider(output))
            event.addProvider(CalamariDataProvider(output))
                event.addProvider(DrownedDataProvider(output))
               event.addProvider(EnchantmentDataProvider(output))
            event.addProvider(SmeltingDataProvider(output))
            event.addProvider(FloridaPlainsDataProvider(output))
            event.addProvider(CaysDataProvider(output))
            event.addProvider(IceCavesDataProvider(output))
            event.addProvider(WoodCuttingDataProvider(output))
			event.addProvider(NetherBoatDataProvider(output))
            event.addProvider(GlowSlimeDataProvider(output))
            event.addProvider(GrizzlyBearDataProvider(output))
            event.addProvider(EnderSpiderDataProvider(output))
            event.addProvider(FireCreeperDataProvider(output))
            event.addProvider(SnowCreeperDataProvider(output))
            event.addProvider(SnowTntDataProvider(output))
            event.addProvider(FireTntDataProvider(output))
			event.addProvider(GlendstoneDataProvider(output))
            event.addProvider(BellDataProvider(output))
            event.addProvider(CakeDataProvider(output))
            event.addProvider(MilkDataProvider(output))
            event.addProvider(BookshelfDataProvider(output))
            event.addProvider(CraftingTableDataProvider(output))
            event.addProvider(FurnaceDataProvider(output))
            event.addProvider(MineralTagsDataProvider(output))
            event.addProvider(ColoredSandstoneDataProvider(output))
            event.addProvider(SupremeMCLanguageProvider(output))
        }
    }
}