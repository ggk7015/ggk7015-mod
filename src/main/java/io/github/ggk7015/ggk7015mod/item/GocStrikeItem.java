package io.github.ggk7015.ggk7015mod.item;

import io.github.ggk7015.ggk7015mod.effect.ModEffects;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class GocStrikeItem extends Item {
    public GocStrikeItem() {
        super(new Item.Properties().sword(ToolMaterial.DIAMOND, 3.0F, -2.4F).stacksTo(1));
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.hurtEnemy(stack, target, attacker);
        target.addEffect(new MobEffectInstance(ModEffects.ANOMALY_SUPPRESSION, 100, 0), attacker);
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0), attacker);
        if (target.level() instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, target.blockPosition(), SoundEvents.BLAZE_SHOOT,
                    SoundSource.PLAYERS, 0.8F, 1.7F);
            serverLevel.sendParticles(ParticleTypes.END_ROD,
                    target.getX(), target.getY() + target.getBbHeight() * 0.6, target.getZ(),
                    24, 0.4, 0.4, 0.4, 0.05);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.accept(Component.translatable("tooltip.ggk7015-mod.goc_strike.line1"));
        tooltip.accept(Component.translatable("tooltip.ggk7015-mod.goc_strike.line2"));
    }
}