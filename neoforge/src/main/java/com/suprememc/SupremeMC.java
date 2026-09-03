package com.suprememc;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import com.suprememc.content.ModContent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent.Client;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.core.registries.Registries;

@Mod(Constants.MOD_ID)
public class SupremeMC {

    public SupremeMC(IEventBus eventBus) {

        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        Constants.LOG.info("Hello NeoForge world!");
        eventBus.addListener(SupremeMC::registerContent);
        eventBus.addListener(SupremeMC::gatherData);
        eventBus.addListener(SupremeMC::addCreativeItems);

    }

    private static void registerContent(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.BLOCK || event.getRegistryKey() == Registries.ITEM) {
            com.suprememc.content.ModContent.bootstrap();
        }
    }

    private static void gatherData(Client event) {
        event.addProvider(new SupremeMCDataProvider(event.getGenerator().getPackOutput()));
    }

    private static void addCreativeItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != CreativeModeTabs.INGREDIENTS) {
            return;
        }
        event.accept(ModContent.AQUAMARINE);
        event.accept(ModContent.AQUAMARINE_ORE);
        event.accept(ModContent.DEEPSLATE_AQUAMARINE_ORE);
        event.accept(ModContent.AQUAMARINE_BLOCK);
        event.accept(ModContent.WET_FARMLAND);
        event.accept(ModContent.AQUAMARINE_PICKAXE);
        event.accept(ModContent.AQUAMARINE_AXE);
        event.accept(ModContent.AQUAMARINE_SHOVEL);
        event.accept(ModContent.AQUAMARINE_HOE);
        event.accept(ModContent.AQUAMARINE_SWORD);
        event.accept(ModContent.AQUAMARINE_HELMET);
        event.accept(ModContent.AQUAMARINE_CHESTPLATE);
        event.accept(ModContent.AQUAMARINE_LEGGINGS);
        event.accept(ModContent.AQUAMARINE_BOOTS);
    }
}
