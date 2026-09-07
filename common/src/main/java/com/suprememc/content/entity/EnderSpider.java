package com.suprememc.content.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class EnderSpider extends Spider {
    private int teleportTowardsTargetTime;

    public EnderSpider(EntityType<? extends Spider> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 24.0)
            .add(Attributes.MOVEMENT_SPEED, 0.3F)
            .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    public void aiStep() {
        if (this.level().isClientSide()) {
            for (int i = 0; i < 2; i++) {
                this.level().addParticle(
                    ParticleTypes.PORTAL,
                    this.getRandomX(0.5),
                    this.getRandomY() - 0.25,
                    this.getRandomZ(0.5),
                    (this.random.nextDouble() - 0.5) * 2.0,
                    -this.random.nextDouble(),
                    (this.random.nextDouble() - 0.5) * 2.0);
            }
        }

        super.aiStep();
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        LivingEntity target = this.getTarget();
        if (target != null && !this.isPassenger()) {
            if (target.distanceToSqr(this) > 256.0 && this.teleportTowardsTargetTime++ >= 30
                && this.teleportTowards(target)) {
                this.teleportTowardsTargetTime = 0;
            }
        } else {
            this.teleportTowardsTargetTime = 0;
        }

        super.customServerAiStep(level);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (this.isInvulnerableTo(level, source)) {
            return false;
        }

        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            for (int i = 0; i < 64; i++) {
                if (this.teleport()) {
                    return true;
                }
            }
        }

        boolean result = super.hurtServer(level, source, damage);
        if (result && this.random.nextInt(10) != 0) {
            this.teleport();
        }
        return result;
    }

    protected boolean teleport() {
        if (!this.level().isClientSide() && this.isAlive()) {
            double x = this.getX() + (this.random.nextDouble() - 0.5) * 64.0;
            double y = this.getY() + (this.random.nextInt(64) - 32);
            double z = this.getZ() + (this.random.nextDouble() - 0.5) * 64.0;
            return this.teleport(x, y, z);
        }
        return false;
    }

    private boolean teleportTowards(Entity entity) {
        Vec3 direction = new Vec3(this.getX() - entity.getX(), this.getY(0.5) - entity.getEyeY(), this.getZ() - entity.getZ()).normalize();
        double x = this.getX() + (this.random.nextDouble() - 0.5) * 8.0 - direction.x * 16.0;
        double y = this.getY() + (this.random.nextInt(16) - 8) - direction.y * 16.0;
        double z = this.getZ() + (this.random.nextDouble() - 0.5) * 8.0 - direction.z * 16.0;
        return this.teleport(x, y, z);
    }

    private boolean teleport(double x, double y, double z) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y, z);
        while (pos.getY() > this.level().getMinY() && !this.level().getBlockState(pos).blocksMotion()) {
            pos.move(Direction.DOWN);
        }

        BlockState blockState = this.level().getBlockState(pos);
        if (blockState.blocksMotion() && !blockState.getFluidState().is(FluidTags.WATER)) {
            Vec3 oldPos = this.position();
            boolean result = this.randomTeleport(x, y, z, true);
            if (result) {
                this.level().gameEvent(GameEvent.TELEPORT, oldPos, GameEvent.Context.of(this));
                if (this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.PORTAL, oldPos.x, oldPos.y + this.getBbHeight() * 0.5, oldPos.z,
                        32, this.getBbWidth() * 0.5, this.getBbHeight() * 0.5, this.getBbWidth() * 0.5, 0.15);
                    serverLevel.sendParticles(ParticleTypes.PORTAL, this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(),
                        32, this.getBbWidth() * 0.5, this.getBbHeight() * 0.5, this.getBbWidth() * 0.5, 0.15);
                }
                if (!this.isSilent()) {
                    this.level().playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                    this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }
            return result;
        }
        return false;
    }
}