package net.mehvahdjukaar.meteors.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class FireballTrailParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected FireballTrailParticle(
            ClientLevel clientLevel,
            double x, double y, double z,  double size, SpriteSet sprites
    ) {
        super(clientLevel, x, y, z, 0.0, 0.0, 0.0);
        this.friction = 0.96F;
        this.gravity = gravity;
        this.speedUpWhenYMotionIsBlocked = true;
        this.sprites = sprites;
        this.xd *= (double)xd;
        this.yd *= (double)yd;
        this.zd *= (double)zd;
        this.xd += 0;
        this.yd += 0;
        this.zd += 0;
        float q = clientLevel.random.nextFloat() * 1;
        this.rCol = 1;
        this.gCol = 1;
        this.bCol = 1;
        this.quadSize *= 0.75F * 3;
        this.lifetime = 20;
        this.lifetime = Math.max(this.lifetime, 1);
        this.setSpriteFromAge(sprites);
        this.roll =  Mth.PI*clientLevel.random.nextFloat();
        this.oRoll = roll;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public int getLightColor(float partialTick) {
        int block = (int) Mth.map(this.age+partialTick,0, this.lifetime,  15, 0);
        int light =super. getLightColor(partialTick);
        block = Math.max(LightTexture.block(light), block);

        return  LightTexture.pack(block, LightTexture.sky(light));
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return this.quadSize * Mth.clamp(((float)this.age + scaleFactor) / (float)this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Factory(SpriteSet sprite) {
            this.sprites = sprite;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                                       double size, double ySpeed, double zSpeed) {
            return new FireballTrailParticle(level, x, y, z, size, sprites);
        }
    }
}
