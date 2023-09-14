package net.mehvahdjukaar.meteors.forge;

import net.mehvahdjukaar.meteors.Meteors;
import net.mehvahdjukaar.meteors.MeteorsClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.world.level.block.BarrierBlock;
import net.minecraftforge.fml.common.Mod;

/**
 * Author: MehVahdJukaar
 */
@Mod(Meteors.MOD_ID)
public class MeteorsForge {

    //basically fireball reword. also for fun adding some meteors with physics and stuff

    public MeteorsForge() {
        Meteors.init();
        if (PlatHelper.getPhysicalSide().isClient()) {
            MeteorsClient.init();
        }
    }


}
