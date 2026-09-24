package io.github.ggk7015.ggk7015mod;

import io.github.ggk7015.ggk7015mod.effect.ModEffects;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
    @Override
    public void onInitialize() {
        ModEffects.register();
        ModItems.register();
    }
}