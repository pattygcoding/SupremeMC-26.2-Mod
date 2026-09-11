package com.suprememc.content.worldgen;

// Thread-local flag toggled by MixinStructureStart while an EndMineshaftStructure places its
// pieces, letting MixinMineshaftStructureType swap its oak planks/fence for lavender equivalents.
public final class EndMineshaftContext {

    public static final ThreadLocal<Boolean> ACTIVE = ThreadLocal.withInitial(() -> false);

    private EndMineshaftContext() {
    }
}
