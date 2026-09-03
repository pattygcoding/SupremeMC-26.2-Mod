package com.suprememc;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import com.suprememc.content.ModContent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent.Client;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

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

    }

    private static void registerContent(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.BLOCK || event.getRegistryKey() == Registries.ITEM) {
            ModContent.bootstrap();
        } else if (event.getRegistryKey() == Registries.CREATIVE_MODE_TAB) {
            CreativeModeTab tab = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup." + Constants.MOD_ID + ".main"))
                    .icon(() -> new ItemStack(ModContent.AQUAMARINE))
                    .displayItems((params, output) -> ModContent.CREATIVE_TAB_ITEMS.forEach(output::accept))
                    .build();
            Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                    ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "main")), tab);
        }
    }

    private static void gatherData(Client event) {
        event.addProvider(new SupremeMCDataProvider(event.getGenerator().getPackOutput()));
    }
}
