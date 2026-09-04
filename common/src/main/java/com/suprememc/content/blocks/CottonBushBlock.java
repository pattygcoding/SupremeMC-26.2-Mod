package com.suprememc.content.blocks;

import com.suprememc.Constants;
import com.suprememc.content.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;

public final class CottonBushBlock extends SweetBerryBushBlock {
    private static final ResourceKey<LootTable> HARVEST_COTTON_BUSH = ResourceKey.create(
        Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "harvest/cotton_bush"));

    public CottonBushBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(ModContent.COTTON);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (state.getValue(AGE) > 1 && level instanceof ServerLevel serverLevel) {
            Block.dropFromBlockInteractLootTable(serverLevel, HARVEST_COTTON_BUSH, state, level.getBlockEntity(pos), null, player,
                (lootLevel, stack) -> popResource(lootLevel, pos, stack));
            serverLevel.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F,
                0.8F + serverLevel.getRandom().nextFloat() * 0.4F);
            BlockState harvestedState = state.setValue(AGE, 1);
            serverLevel.setBlock(pos, harvestedState, 2);
            serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, harvestedState));
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
}