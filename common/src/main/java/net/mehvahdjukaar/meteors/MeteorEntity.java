package net.mehvahdjukaar.meteors;

import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MeteorEntity extends ImprovedProjectileEntity {

    private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.INT);

    private int particleCooldown;
    private boolean damages = false;
    private float size = 0.5f;
    private float explosionSize = 2;

    private static final int MAX_AGE = 30*20;

    protected MeteorEntity(EntityType<? extends ThrowableItemProjectile> type, Level world) {
        super(type, world);
        this.maxAge = MAX_AGE;
    }

    protected MeteorEntity(double x, double y, double z, Level world) {
        super(Meteors.METEOR_ENTITY.get(), x, y, z, world);
        this.maxAge = MAX_AGE;

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_SIZE, 1);
    }

    public void setNoDamages() {
        this.damages = false;
    }

    @Override
    public void tick() {
        // this.setNoGravity(true);
        //   float d = this.tickCount/40f;
        // this.setDeltaMovement(Mth.sin(d)/10f, 0, Mth.cos(d)/10f);
        super.tick();
    }

    @Override
    protected Item getDefaultItem() {
        return Items.FIRE_CHARGE;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        this.explode();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (result.getType() == HitResult.Type.BLOCK) {
            if (this.level().getBlockState(result.getBlockPos()).isSolid()) {
                //sync better with clients
                level().broadcastEntityEvent(this, (byte) 17);
                this.explode();
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 17) this.explode();
    }

    private void explode() {
        var e = new FireballExplosion(level(), this, 5, damages,
                damages ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.KEEP);
        e.explode();
        e.finalizeExplosion(true);
        this.discard();
    }

    @Override
    public void spawnTrailParticles(Vec3 currentPos, Vec3 newPos) {
        if (!this.isNoPhysics()) {
            double d = this.getDeltaMovement().length();
            if (this.tickCount > 1 && d * this.tickCount > 1.5) {
                if (false) {

                    Vec3 rot = new Vec3(0.325, 0, 0).yRot(this.tickCount * 0.32f);

                    Vec3 movement = this.getDeltaMovement();
                    Vec3 offset = MthUtils.changeBasisN(movement, rot);

                    double px = newPos.x + offset.x;
                    double py = newPos.y + offset.y; //+ this.getBbHeight() / 2d;
                    double pz = newPos.z + offset.z;

                    movement = movement.scale(0.25);
                    this.level().addParticle(ParticleTypes.LARGE_SMOKE, px, py, pz, movement.x, movement.y, movement.z);
                } else {
                    double interval = 4 / (d * 0.95 + 0.05);
                    if (true || this.particleCooldown > interval) {
                        this.particleCooldown -= interval;
                        double x = currentPos.x;
                        double y = currentPos.y;//+ this.getBbHeight() / 2d;
                        double z = currentPos.z;
                        Vec3 movement = this.getDeltaMovement();
                        movement = movement.scale(1);

                        this.level().addAlwaysVisibleParticle(Meteors.FIREBALL_TRAIL_PARTICLE.get(), true,
                                x, y, z, 1, 0.01, 0);
                        this.level().addAlwaysVisibleParticle(Meteors.FIREBALL_TRAIL_PARTICLE.get(), true,
                                x - movement.x, y - movement.y, z - movement.z, 1, 0.01, 0);

                        this.level().addAlwaysVisibleParticle(Meteors.FIREBALL_TRAIL_PARTICLE.get(), true,
                                x - movement.x / 2f, y - movement.y / 2f, z - movement.z / 2f, 1, 0.01, 0);

                        this.level().addAlwaysVisibleParticle(Meteors.FIREBALL_TRAIL_PARTICLE.get(), true,
                                x - movement.x / 4f, y - movement.y / 4f, z - movement.z / 4f, 1, 0.01, 0);


                        this.level().addAlwaysVisibleParticle(Meteors.FIREBALL_TRAIL_PARTICLE.get(), true,
                                x - movement.x * 3 / 4f, y - movement.y * 3 / 4f, z - movement.z * 3 / 4f, 1, 0.01, 0);
                    }
                }
            }
        }
    }
}
