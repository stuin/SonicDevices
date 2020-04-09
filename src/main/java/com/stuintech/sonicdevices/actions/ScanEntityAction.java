package com.stuintech.sonicdevices.actions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.Collection;

/*
 * Created by Stuart Irwin on 4/8/2020.
 */

public class ScanEntityAction extends IAction.IEntityAction {
    @Override
    public boolean interact(PlayerEntity player, LivingEntity entity) {
        //Run scan
        if(!player.getEntityWorld().isClient) {
            //Scan mob
            player.addChatMessage(new TranslatableText(entity.getType().getTranslationKey()), false);
            player.addChatMessage(new LiteralText("  Health: " + entity.getHealth() + " / " + entity.getMaximumHealth()), false);

            //Check for potion effects
            if(entity.getStatusEffects().size() > 0) {
                Collection<StatusEffectInstance> effects = entity.getStatusEffects();
                player.addChatMessage(new LiteralText("  Potion Effects:"), false);

                //List effects
                for(StatusEffectInstance effect : effects) {
                    //Get time
                    int seconds = effect.getDuration() / 20;

                    //Display status
                    String message = "    " + new TranslatableText(effect.getTranslationKey()).asString();
                    message += " " + (effect.getAmplifier() + 1) + " " + seconds + 's';
                    player.addChatMessage(new LiteralText(message), false);
                }
            }
            return true;
        }
        return false;
    }
}
