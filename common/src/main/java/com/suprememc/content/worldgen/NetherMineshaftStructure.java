package com.suprememc.content.worldgen;

import com.mojang.serialization.MapCodec;
import com.suprememc.content.init.ModStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;

import java.util.Optional;

// A mineshaft that can generate in any Nether biome. Reuses vanilla's "normal" mineshaft pieces
// as-is; MixinMineshaftStructureType swaps its oak planks/fence for warped equivalents only while
// MixinStructureStart marks this specific structure as actively placing blocks.
public class NetherMineshaftStructure extends MineshaftStructure {

    // Matches the fixed minY vanilla's MineShaftRoom constructor bakes into its BoundingBox.
    private static final int ROOM_BASE_Y = 50;
    private static final int VERTICAL_SPREAD = 20;

    public static final MapCodec<NetherMineshaftStructure> CODEC =
        Structure.simpleCodec(NetherMineshaftStructure::new);

    public NetherMineshaftStructure(Structure.StructureSettings settings) {
        super(settings, MineshaftStructure.Type.NORMAL);
    }

    // Vanilla's own findGenerationPoint (inherited private generatePiecesAndAdjust) shoves NORMAL
    // mineshafts down to just below ChunkGenerator.getSeaLevel(); in the Nether that's ~y=32, i.e.
    // inside the lava ocean, so every piece failed its liquid check and placed zero blocks even
    // though the structure start was still registered/locatable. Rebuild the room+corridors the
    // same way vanilla does, but keep it in the solid mid-height netherrack band instead.
    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        RandomSource random = context.random();
        int verticalOffset = random.nextInt(VERTICAL_SPREAD);
        BlockPos position = new BlockPos(chunkPos.getMiddleBlockX(), ROOM_BASE_Y + verticalOffset, chunkPos.getMinBlockZ());
        return Optional.of(new Structure.GenerationStub(position, piecesBuilder -> {
            MineshaftPieces.MineShaftRoom start = new MineshaftPieces.MineShaftRoom(
                0, random, chunkPos.getBlockX(2), chunkPos.getBlockZ(2), MineshaftStructure.Type.NORMAL);
            piecesBuilder.addPiece(start);
            start.addChildren(start, piecesBuilder, random);
            piecesBuilder.offsetPiecesVertically(verticalOffset);
        }));
    }

    @Override
    public StructureType<?> type() {
        return ModStructureTypes.NETHER_MINESHAFT;
    }
}
