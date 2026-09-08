package com.suprememc.content.blocks;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.block.Block;

public class WoodCraftingMenu extends CraftingMenu {
    private final ContainerLevelAccess access;
    private final Block craftingTable;

    public WoodCraftingMenu(int containerId, Inventory inventory, ContainerLevelAccess access, Block craftingTable) {
        super(containerId, inventory, access);
        this.access = access;
        this.craftingTable = craftingTable;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, craftingTable);
    }
}