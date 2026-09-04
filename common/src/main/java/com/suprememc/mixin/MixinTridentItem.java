package com.suprememc.mixin;

import com.suprememc.content.ModContent;
import com.suprememc.content.entity.AbyssaliteTridentEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// Vanilla releaseUsing builds the projectile through a private factory lambda, bypassing
// ProjectileItem.asProjectile, so a custom trident item can never spawn a custom entity.
@Mixin(TridentItem.class)
public abstract class MixinTridentItem {
    @Redirect(method = "releaseUsing", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectileFromRotation(Lnet/minecraft/world/entity/projectile/Projectile$ProjectileFactory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;FFF)Lnet/minecraft/world/entity/projectile/Projectile;"))
    private Projectile suprememc$spawnAbyssaliteTrident(Projectile.ProjectileFactory<ThrownTrident> factory,
            ServerLevel level, ItemStack stack, LivingEntity shooter, float xRot, float power, float inaccuracy) {
        if (stack.is(ModContent.ABYSSALITE_TRIDENT)) {
            return Projectile.spawnProjectileFromRotation(AbyssaliteTridentEntity::new, level, stack, shooter,
                    xRot, power, inaccuracy);
        }
        return Projectile.spawnProjectileFromRotation(factory, level, stack, shooter, xRot, power, inaccuracy);
    }
}
