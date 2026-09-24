package io.github.ggk7015.ggk7015mod.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ModEffects {

    public static final Holder<MobEffect> ANOMALY_SUPPRESSION = Registry.registerForHolder(
            BuiltInRegistries.MOB_EFFECT,
            Identifier.fromNamespaceAndPath("ggk7015-mod", "anomaly_suppression"),
            new AnomalySuppressionEffect(MobEffectCategory.HARMFUL, 0x7FE9FF));

    public static void register() {
    }
}