package com.suprememc

import com.suprememc.content.ModContent
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
import net.neoforged.neoforge.registries.RegisterEvent

@Mod(Constants.MOD_ID)
class SupremeMC(eventBus: IEventBus) {
    init {
        Constants.LOG.info("Hello NeoForge world!")
        eventBus.addListener(SupremeMC::registerContent)
        eventBus.addListener(SupremeMC::addSignBlockEntityBlocks)
        eventBus.addListener(SupremeMC::registerAttributes)
        eventBus.addListener(SupremeMC::gatherData)
    }

    companion object {
        // Vanilla's sign block entities only accept their hardcoded block list, so opt the palm signs in.
        private fun addSignBlockEntityBlocks(event: BlockEntityTypeAddBlocksEvent) {
            event.modify(BlockEntityTypes.SIGN, ModContent.PALM_SIGN, ModContent.PALM_WALL_SIGN)
            event.modify(BlockEntityTypes.HANGING_SIGN, ModContent.PALM_HANGING_SIGN, ModContent.PALM_WALL_HANGING_SIGN)
        }
        private fun registerAttributes(event: EntityAttributeCreationEvent) {
            event.put(ModContent.GRIZZLY_BEAR_ENTITY, net.minecraft.world.entity.animal.polarbear.PolarBear.createAttributes().build())
            event.put(ModContent.FIRE_CREEPER_ENTITY, net.minecraft.world.entity.monster.Creeper.createAttributes().build())
        }
        private fun registerContent(event: RegisterEvent) {
            if (event.registryKey == Registries.BLOCK || event.registryKey == Registries.ITEM) {
                ModContent.bootstrap()
            } else if (event.registryKey == Registries.CREATIVE_MODE_TAB) {
                val tab = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup.${Constants.MOD_ID}.main"))
                    .icon { ItemStack(ModContent.AQUAMARINE) }
                    .displayItems { _, output -> ModContent.CREATIVE_TAB_ITEMS.forEach(output::accept) }
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
            event.addProvider(MaterialBlocksDataProvider(output))
            event.addProvider(EmeraldDataProvider(output))
            event.addProvider(AbyssaliteDataProvider(output))
            event.addProvider(IcicleDataProvider(output))
            event.addProvider(PalmDataProvider(output))
            event.addProvider(CottonDataProvider(output))
            event.addProvider(CornDataProvider(output))
            event.addProvider(GrapeVineDataProvider(output))
            event.addProvider(TomatoDataProvider(output))
            event.addProvider(BeachGrassDataProvider(output))
            event.addProvider(CalamariDataProvider(output))
                event.addProvider(DrownedDataProvider(output))
               event.addProvider(EnchantmentDataProvider(output))
            event.addProvider(FloridaPlainsDataProvider(output))
            event.addProvider(CaysDataProvider(output))
            event.addProvider(IceCavesDataProvider(output))
            event.addProvider(WoodCuttingDataProvider(output))
            event.addProvider(GlowSlimeDataProvider(output))
            event.addProvider(GrizzlyBearDataProvider(output))
            event.addProvider(FireCreeperDataProvider(output))
            event.addProvider(SupremeMCLanguageProvider(output))
        }
    }
}