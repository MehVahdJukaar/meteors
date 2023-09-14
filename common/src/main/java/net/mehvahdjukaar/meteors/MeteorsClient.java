package net.mehvahdjukaar.meteors;

import net.mehvahdjukaar.meteors.client.FireballExplosionEmitterParticle;
import net.mehvahdjukaar.meteors.client.FireballExplosionParticle;
import net.mehvahdjukaar.meteors.client.FireballTrailParticle;
import net.mehvahdjukaar.meteors.client.MeteorRenderer;
import net.mehvahdjukaar.moonlight.api.misc.EventCalled;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class MeteorsClient {
    public static final ModelLayerLocation METEOR_MODEL = loc("clock_hands");

    public static void init() {
        ClientHelper.addModelLayerRegistration(MeteorsClient::registerModelLayers);
        ClientHelper.addEntityRenderersRegistration(MeteorsClient::registerEntityRenderers);
        ClientHelper.addParticleRegistration(MeteorsClient::registerParticles);
    }

    private static void registerParticles(ClientHelper.ParticleEvent event) {
        event.register(Meteors.FIREBALL_EMITTER_PARTICLE.get(), FireballExplosionEmitterParticle.Factory::new);
        event.register(Meteors.FIREBALL_PARTICLE.get(), FireballExplosionParticle.Factory::new);
        event.register(Meteors.FIREBALL_TRAIL_PARTICLE.get(), FireballTrailParticle.Factory::new);
    }

    private static void registerEntityRenderers(ClientHelper.EntityRendererEvent event) {
        event.register(Meteors.METEOR_ENTITY.get(),MeteorRenderer::new);
    }


    @EventCalled
    private static void registerModelLayers(ClientHelper.ModelLayerEvent event) {
        event.register(METEOR_MODEL, MeteorRenderer::createMesh);
    }

    private static ModelLayerLocation loc(String name) {
        return new ModelLayerLocation(Meteors.res(name), name);
    }
}
