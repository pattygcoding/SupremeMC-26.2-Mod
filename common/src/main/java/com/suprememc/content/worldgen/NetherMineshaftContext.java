package com.suprememc.content.worldgen;

// Thread-local flag toggled by MixinStructureStart while a NetherMineshaftStructure places its
// pieces, letting MixinMineshaftStructureType swap its oak planks/fence for warped equivalents.
public final class NetherMineshaftContext {

    public static final ThreadLocal<Boolean> ACTIVE = ThreadLocal.withInitial(() -> false);

    private NetherMineshaftContext() {
    }
}
