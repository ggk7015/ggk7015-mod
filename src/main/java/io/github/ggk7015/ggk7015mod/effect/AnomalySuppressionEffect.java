package io.github.ggk7015.ggk7015mod.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class AnomalySuppressionEffect extends MobEffect {

    public AnomalySuppressionEffect(MobEffectCategory category, int color) {
        super(category, color);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, Identifier.withDefaultNamespace("effect.ggk7015.anomaly_speed"), -0.15,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, Identifier.withDefaultNamespace("effect.ggk7015.anomaly_attack"), -0.2,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
        return true;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
        if (mob.tickCount % 12 == 0) {
            level.sendParticles(ParticleTypes.END_ROD,
                    mob.getX() + 0.3, mob.getY() + mob.getBbHeight() * 0.75, mob.getZ(),
                    2, 0.1, 0.1, 0.1, 0.01);
        }
        return true;
    }
}