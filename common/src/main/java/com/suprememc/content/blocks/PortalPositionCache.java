package com.suprememc.content.blocks;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.List;
import java.util.Optional;

/**
 * Remembers where a dimension's exit portals were placed so later arrivals can be
 * linked to an existing nearby portal instead of always carving a brand-new one.
 */
public final class PortalPositionCache extends SavedData {
    private final LongList positions;

    public PortalPositionCache() {
        this(List.of());
    }

    private PortalPositionCache(List<Long> positions) {
        this.positions = new LongArrayList(positions);
    }

    public static SavedDataType<PortalPositionCache> type(String id) {
        Codec<PortalPositionCache> codec = Codec.LONG.listOf().xmap(PortalPositionCache::new, cache -> new LongArrayList(cache.positions));
        return new SavedDataType<>(Identifier.fromNamespaceAndPath("suprememc", id), PortalPositionCache::new, codec, null);
    }

    public void record(BlockPos pos) {
        long packed = pos.asLong();
        if (!positions.contains(packed)) {
            positions.add(packed);
            setDirty();
        }
    }

    /**
     * Finds the closest still-valid recorded portal block of the given type within radius blocks of origin,
     * discarding any stale entries whose block no longer matches.
     */
    public Optional<BlockPos> findNearby(ServerLevel level, Block portalBlock, BlockPos origin, int radius) {
        double radiusSqr = (double) radius * radius;
        BlockPos best = null;
        double bestDistSqr = -1.0D;
        LongIterator iterator = positions.iterator();
        LongArrayList stale = new LongArrayList();
        while (iterator.hasNext()) {
            long packed = iterator.nextLong();
            BlockPos pos = BlockPos.of(packed);
            if (!level.getBlockState(pos).is(portalBlock)) {
                stale.add(packed);
                continue;
            }
            double distSqr = pos.distSqr(origin);
            if (distSqr > radiusSqr) continue;
            if (best == null || distSqr < bestDistSqr) {
                best = pos;
                bestDistSqr = distSqr;
            }
        }
        if (!stale.isEmpty()) {
            positions.removeAll(stale);
            setDirty();
        }
        return Optional.ofNullable(best);
    }
}
