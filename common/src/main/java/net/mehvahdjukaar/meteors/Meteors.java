package net.mehvahdjukaar.meteors;

import net.mehvahdjukaar.moonlight.api.misc.DataObjectReference;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.BarrierBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;


public class Meteors {

    public static final String MOD_ID = "meteors";

    public static final Logger LOGGER = LogManager.getLogger("meteors");

    public static ResourceLocation res(String n) {
        return new ResourceLocation(MOD_ID, n);
    }

    public static String str(String n) {
        return MOD_ID + ":" + n;
    }


    public static final Supplier<EntityType<MeteorEntity>> METEOR_ENTITY = RegHelper.registerEntityType(
            res("meteor"), MeteorEntity::new, MobCategory.MISC, 0.5f, 0.5f, 10, 20
    );

    public static final Supplier<Block> VOLCANO_BLOCK = RegHelper.registerBlockWithItem(res("volcano_block"), VolcanoBlock::new);

    public static final Supplier<BlockEntityType<VolcanoBlockTile>> VOLCANO_TILE = RegHelper.registerBlockEntityType(
            res("volcano_block"),
            () -> PlatHelper.newBlockEntityType(VolcanoBlockTile::new, VOLCANO_BLOCK.get()));

    public static final Supplier<SimpleParticleType> FIREBALL_PARTICLE = RegHelper.registerParticle(res("fireball_explosion"));
    public static final Supplier<SimpleParticleType> FIREBALL_TRAIL_PARTICLE = RegHelper.registerParticle(res("fireball_trail"));
    public static final Supplier<SimpleParticleType> FIREBALL_EMITTER_PARTICLE = RegHelper.registerParticle(res("fireball_explosion_emitter"));

    public static final DataObjectReference<DamageType> FIREBALL_EXPLOSION_DAMAGE =
            new DataObjectReference<>(res("fireball_explosion"), Registries.DAMAGE_TYPE);


    public static void init() {
        VolcanoSettings.init();
    }
}
