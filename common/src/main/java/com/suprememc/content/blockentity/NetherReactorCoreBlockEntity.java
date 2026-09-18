package com.suprememc.content.blockentity;

import com.suprememc.Constants;
import com.suprememc.content.blocks.NetherReactorCoreBlock;
import com.suprememc.content.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class NetherReactorCoreBlockEntity extends BlockEntity {
    public static final ResourceKey<LootTable> DROP_TABLE = ResourceKey.create(Registries.LOOT_TABLE,
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "gameplay/nether_reactor_drops"));

    private static final int ACTIVE_DURATION_TICKS = 900;
    private static final int LOOT_INTERVAL_TICKS = 40;
    private static final int SPAWN_INTERVAL_TICKS = 100;
    private static final int DECAY_DURATION_TICKS = 2400;
    private static final int DECAY_ATTEMPTS_PER_TICK = 2;
    private static final int MIN_SPAWN_RADIUS = 3;
    private static final int MAX_SPAWN_RADIUS = 5;

    /** Ticks elapsed in the active phase, or -1 when not running. */
    private int activeTicks = -1;
    /** Ticks elapsed in the post-burnout decay phase, or -1 when not decaying. */
    private int decayTicks = -1;
    /** Exact positions turned to glowing obsidian on activation, reverted on burnout. */
    private final List<BlockPos> obsidianPositions = new ArrayList<>();

    public NetherReactorCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NETHER_REACTOR_CORE, pos, state);
    }

    public void startActivePhase(List<BlockPos> obsidian) {
        this.activeTicks = 0;
        this.decayTicks = -1;
        this.obsidianPositions.clear();
        this.obsidianPositions.addAll(obsidian);
        setChanged();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, NetherReactorCoreBlockEntity reactor) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        if (reactor.activeTicks >= 0) {
            reactor.tickActive(serverLevel, pos, state);
        } else if (reactor.decayTicks >= 0) {
            reactor.tickDecay(serverLevel, pos);
        }
    }

    private void tickActive(ServerLevel level, BlockPos pos, BlockState state) {
        this.activeTicks++;
        if (this.activeTicks % LOOT_INTERVAL_TICKS == 0) {
            dropLoot(level, pos);
        }
        if (this.activeTicks % SPAWN_INTERVAL_TICKS == 0) {
            spawnPiglin(level, pos);
        }
        if (this.activeTicks >= ACTIVE_DURATION_TICKS) {
            burnOut(level, pos, state);
        }
        setChanged();
    }

    private void dropLoot(ServerLevel level, BlockPos pos) {
        LootTable table = level.getServer().reloadableRegistries().getLootTable(DROP_TABLE);
        BlockPos drop = randomFloorPos(level, pos);
        LootParams params = new LootParams.Builder(level)
            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(drop))
            .create(LootContextParamSets.CHEST);
        for (ItemStack stack : table.getRandomItems(params)) {
            if (stack.isEmpty()) {
                continue;
            }
            ItemEntity item = new ItemEntity(level, drop.getX() + 0.5D, drop.getY() + 0.5D, drop.getZ() + 0.5D, stack);
            item.setDeltaMovement(0.0D, 0.05D, 0.0D);
            level.addFreshEntity(item);
        }
    }

    /** A block-space position standing on the spire floor, 3-5 blocks out from the core. */
    private static BlockPos randomFloorPos(ServerLevel level, BlockPos core) {
        RandomSource random = level.getRandom();
        int span = MAX_SPAWN_RADIUS * 2 + 1;
        for (int attempt = 0; attempt < 16; attempt++) {
            int dx = random.nextInt(span) - MAX_SPAWN_RADIUS;
            int dz = random.nextInt(span) - MAX_SPAWN_RADIUS;
            if (Math.max(Math.abs(dx), Math.abs(dz)) < MIN_SPAWN_RADIUS) {
                continue;
            }
            return new BlockPos(core.getX() + dx, NetherReactorCoreBlock.floorY(core) + 1, core.getZ() + dz);
        }
        return new BlockPos(core.getX() + MIN_SPAWN_RADIUS, NetherReactorCoreBlock.floorY(core) + 1, core.getZ());
    }

    private void spawnPiglin(ServerLevel level, BlockPos pos) {
        RandomSource random = level.getRandom();
        for (int attempt = 0; attempt < 8; attempt++) {
            BlockPos spawnPos = randomFloorPos(level, pos);
            if (!level.getBlockState(spawnPos).isAir() || !level.getBlockState(spawnPos.above()).isAir()) {
                continue;
            }
            ZombifiedPiglin piglin = EntityTypes.ZOMBIFIED_PIGLIN.create(level, EntitySpawnReason.TRIGGERED);
            if (piglin == null) {
                return;
            }
            piglin.snapTo(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, random.nextFloat() * 360.0F, 0.0F);
            DifficultyInstance difficulty = level.getCurrentDifficultyAt(spawnPos);
            piglin.finalizeSpawn(level, difficulty, EntitySpawnReason.TRIGGERED, null);
            level.addFreshEntity(piglin);

            Player target = level.getNearestPlayer(piglin, 24.0D);
            if (target != null) {
                piglin.setTarget(target);
                piglin.startPersistentAngerTimer();
            }
            return;
        }
    }

    private void burnOut(ServerLevel level, BlockPos pos, BlockState state) {
        level.setBlockAndUpdate(pos, state.setValue(NetherReactorCoreBlock.STATE, NetherReactorCoreBlock.ReactorState.USED));
        List<BlockPos> tracked = this.obsidianPositions.isEmpty()
            ? NetherReactorCoreBlock.structurePositions(pos)
            : this.obsidianPositions;
        NetherReactorCoreBlock.extinguishGlowingObsidian(level, tracked);
        this.obsidianPositions.clear();
        this.activeTicks = -1;
        this.decayTicks = 0;
    }

    /** Randomly punches holes in the spire, mimicking the Pocket Edition decay. */
    private void tickDecay(ServerLevel level, BlockPos pos) {
        this.decayTicks++;
        RandomSource random = level.getRandom();
        int span = NetherReactorCoreBlock.SPIRE_RADIUS * 2 + 1;
        int floorY = NetherReactorCoreBlock.floorY(pos);
        for (int attempt = 0; attempt < DECAY_ATTEMPTS_PER_TICK; attempt++) {
            BlockPos target = new BlockPos(
                pos.getX() + random.nextInt(span) - NetherReactorCoreBlock.SPIRE_RADIUS,
                floorY + random.nextInt(NetherReactorCoreBlock.SPIRE_HEIGHT + 1),
                pos.getZ() + random.nextInt(span) - NetherReactorCoreBlock.SPIRE_RADIUS);
            if (level.getBlockState(target).is(Blocks.NETHERRACK)) {
                level.removeBlock(target, false);
            }
        }
        if (this.decayTicks >= DECAY_DURATION_TICKS) {
            this.decayTicks = -1;
        }
        setChanged();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("ActiveTicks", this.activeTicks);
        output.putInt("DecayTicks", this.decayTicks);
        int[] packed = new int[this.obsidianPositions.size() * 3];
        for (int i = 0; i < this.obsidianPositions.size(); i++) {
            BlockPos pos = this.obsidianPositions.get(i);
            packed[i * 3] = pos.getX();
            packed[i * 3 + 1] = pos.getY();
            packed[i * 3 + 2] = pos.getZ();
        }
        output.putIntArray("Obsidian", packed);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.activeTicks = input.getIntOr("ActiveTicks", -1);
        this.decayTicks = input.getIntOr("DecayTicks", -1);
        this.obsidianPositions.clear();
        input.getIntArray("Obsidian").ifPresent(packed -> {
            for (int i = 0; i + 2 < packed.length; i += 3) {
                this.obsidianPositions.add(new BlockPos(packed[i], packed[i + 1], packed[i + 2]));
            }
        });
    }
}
