package net.mehvahdjukaar.meteors;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class FireballExplosion extends Explosion {


    public FireballExplosion(Level level, Entity entity, float radius, boolean fire, BlockInteraction blockInteraction) {
        super(level, entity, new DamageSource(Meteors.FIREBALL_EXPLOSION_DAMAGE.getHolder()),
                null, entity.getX(), entity.getY(), entity.getZ(),
                radius, fire, blockInteraction);
    }

    @Override
    public void finalizeExplosion(boolean spawnParticles) {
        if (spawnParticles) {
            if (this.radius >= 2.0F) {
                this.level.addParticle(Meteors.FIREBALL_EMITTER_PARTICLE.get(), this.x, this.y, this.z, radius, 0.0, 0.0);
            } else {
                this.level.addParticle(Meteors.FIREBALL_PARTICLE.get(), this.x, this.y, this.z, 1.0, 0.0, 0.0);
            }
        }
        super.finalizeExplosion(false);
        //spawns particle manually


    }
}
