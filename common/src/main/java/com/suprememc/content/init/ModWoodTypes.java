package com.suprememc.content.init;

import net.minecraft.world.level.block.state.properties.WoodType;

public final class ModWoodTypes {
    public static final WoodType PALM = new WoodType("palm", ModBlockSetTypes.PALM);
    public static final WoodType LAVENDER = new WoodType("lavender", ModBlockSetTypes.LAVENDER);

    private ModWoodTypes() {
    }

    public static void bootstrap() {
    }
}