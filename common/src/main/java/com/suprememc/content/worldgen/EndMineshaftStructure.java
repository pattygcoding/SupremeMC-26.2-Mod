package com.suprememc.content.worldgen;

import com.mojang.serialization.MapCodec;
import com.suprememc.content.init.ModStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;

import java.util.Optional;

// A mineshaft that generates inside the solid end stone of the End's outer islands (never the
// center island, since suprememc:end_mineshaft's biome list omits minecraft:the_end). Reuses
// vanilla's "normal" mineshaft pieces as-is; MixinMineshaftStructureType swaps its oak
// planks/fence for lavender equivalents only while MixinStructureStart marks this specific
// structure as actively placing blocks.
public class EndMineshaftStructure extends MineshaftStructure {

    // End islands vary wildly in height/thickness, unlike the Nether's flat lava floor, so we
    // sample each column's own surface instead of assuming a fixed world Y.
    private static final int MIN_DEPTH_BELOW_SURFACE = 8;
    private static final int MAX_DEPTH_BELOW_SURFACE = 24;

    public static final MapCodec<EndMineshaftStructure> CODEC =
        Structure.simpleCodec(EndMineshaftStructure::new);

    public EndMineshaftStructure(Structure.StructureSettings settings) {
        super(settings, MineshaftStructure.Type.NORMAL);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        RandomSource random = context.random();
        int surfaceY = context.chunkGenerator().getBaseHeight(
            chunkPos.getMiddleBlockX(), chunkPos.getMiddleBlockZ(),
            Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        int depth = MIN_DEPTH_BELOW_SURFACE + random.nextInt(MAX_DEPTH_BELOW_SURFACE - MIN_DEPTH_BELOW_SURFACE + 1);
        int roomY = surfaceY - depth;
        BlockPos position = new BlockPos(chunkPos.getMiddleBlockX(), roomY, chunkPos.getMinBlockZ());
        return Optional.of(new Structure.GenerationStub(position, piecesBuilder -> {
            MineshaftPieces.MineShaftRoom start = new MineshaftPieces.MineShaftRoom(
                0, random, chunkPos.getBlockX(2), chunkPos.getBlockZ(2), MineshaftStructure.Type.NORMAL);
            piecesBuilder.addPiece(start);
            start.addChildren(start, piecesBuilder, random);
            // MineShaftRoom's ctor bakes minY=50 into its BoundingBox; recenter onto roomY.
            piecesBuilder.offsetPiecesVertically(roomY - 50);
        }));
    }

    @Override
    public StructureType<?> type() {
        return ModStructureTypes.END_MINESHAFT;
    }
}
