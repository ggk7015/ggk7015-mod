package io.github.ggk7015.ggk7015mod;

import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
    @Override
    public void onInitialize() {
        ModItems.register();
    }
}