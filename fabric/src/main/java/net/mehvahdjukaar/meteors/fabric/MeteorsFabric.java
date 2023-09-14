package net.mehvahdjukaar.meteors.fabric;

import net.fabricmc.api.ModInitializer;
import net.mehvahdjukaar.meteors.Meteors;
import net.mehvahdjukaar.meteors.MeteorsClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;

public class MeteorsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Meteors.init();
        if (PlatHelper.getPhysicalSide().isClient()) {
            MeteorsClient.init();
        }
    }


}
