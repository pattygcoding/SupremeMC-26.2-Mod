package com.suprememc.content.init;

import com.suprememc.Constants;
import com.suprememc.content.fluid.LiquidNitrogenFluid;
import com.suprememc.content.fluid.MilkFluid;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.FlowingFluid;

public final class ModFluids {
    public static FlowingFluid MILK;
    public static FlowingFluid FLOWING_MILK;
    public static FlowingFluid LIQUID_NITROGEN;
    public static FlowingFluid FLOWING_LIQUID_NITROGEN;

    private static boolean registered;

    private ModFluids() {
    }

    public static void bootstrap() {
        if (registered) return;
        registered = true;
        MILK = register("milk", new MilkFluid.Source());
        FLOWING_MILK = register("flowing_milk", new MilkFluid.Flowing());
        LIQUID_NITROGEN = register("liquid_nitrogen", new LiquidNitrogenFluid.Source());
        FLOWING_LIQUID_NITROGEN = register("flowing_liquid_nitrogen", new LiquidNitrogenFluid.Flowing());
    }

    private static <T extends FlowingFluid> T register(String id, T fluid) {
        Registry.register(BuiltInRegistries.FLUID, Identifier.fromNamespaceAndPath(Constants.MOD_ID, id), fluid);
        return fluid;
    }
}