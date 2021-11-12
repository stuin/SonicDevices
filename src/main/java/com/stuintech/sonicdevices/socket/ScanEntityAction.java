package com.stuintech.sonicdevices.socket;

import com.stuintech.socketwrench.socket.Socket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.Collection;

/*
 * Created by Stuart Irwin on 4/8/2020.
 */

public class ScanEntityAction extends Socket.EntityActionSocket {
    @Override
    public boolean onFasten(PlayerEntity player, LivingEntity entity) {
        //Run scan
        if(!player.getEntityWorld().isClient) {
            //Scan mob
            Style top = Style.EMPTY.withColor(Formatting.BLUE);
            player.sendMessage(new TranslatableText(entity.getType().getTranslationKey()).setStyle(top), false);
            player.sendMessage(new LiteralText("  Health: "
                    + entity.getHealth() + " / " + entity.getMaxHealth()), false);
            
            //Zombie
            if(entity instanceof ZombieEntity) {
                if(((ZombieEntity) entity).canPickUpLoot())
                    player.sendMessage(new LiteralText("  Can pick up loot."), false);
                else
                    player.sendMessage(new LiteralText("  Cannot pick up loot."), false);
            }

            //Display horse stats
            if(entity instanceof HorseBaseEntity) {
                int speed = (int)Math.round(entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getValue() * 50);
                double jump = ((HorseBaseEntity) entity).getJumpStrength();
                jump = (int)((-0.1817584952 * Math.pow(jump, 3) + 3.689713992 * Math.pow(jump, 2) + 2.128599134 * jump - 0.343930367) * 10);
                player.sendMessage(new LiteralText("  Speed: " + speed + " blocks/sec"), false);
                player.sendMessage(new LiteralText("  Jump: " + jump / 10f + " blocks"), false);
            }
            
            //Tamable
            if(entity instanceof TameableEntity) {
                if(((TameableEntity) entity).isTamed())
                    player.sendMessage(new LiteralText("  Tamed"), false);
                else
                    player.sendMessage(new LiteralText("  Not Tamed."), false);
            }

            //Equipped
            if(entity.getMainHandStack().getItem() != Items.AIR) {
                MutableText holding = new LiteralText("  Holding: ");
                holding.append(entity.getMainHandStack().toHoverableText());
                player.sendMessage(holding, false);
            }

            //Check for armor
            if(entity.getArmor() > 0) {
                player.sendMessage(new LiteralText("  Armor: " + entity.getArmor()), false);
                entity.getArmorItems().forEach(itemStack -> {
                    if(itemStack.getItem() != Items.AIR) {
                        MutableText armor = new LiteralText("    ");
                        armor.append(itemStack.toHoverableText());
                        player.sendMessage(armor, false);
                    }
                });
            }
            
            //Check for potion effects
            if(entity.getStatusEffects().size() > 0) {
                Collection<StatusEffectInstance> effects = entity.getStatusEffects();
                player.sendMessage(new LiteralText("  Potion Effects:"), false);

                //List effects
                for(StatusEffectInstance effect : effects) {
                    //Get time
                    int seconds = effect.getDuration() / 20;

                    //Display status
                    String message = "    " + new TranslatableText(effect.getTranslationKey()).asString();
                    message += " " + (effect.getAmplifier() + 1) + " " + seconds + 's';
                    player.sendMessage(new LiteralText(message), false);
                }
            }
        }
        return true;
    }
}
