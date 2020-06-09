package com.stuintech.sonicdevices.action;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Collection;
import java.util.function.Consumer;

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
            player.addChatMessage(new LiteralText("  Health: "
                    + entity.getHealth() + " / " + entity.getMaximumHealth()), false);
            
            //Zombie
            if(entity instanceof ZombieEntity) {
                if(((ZombieEntity) entity).canPickUpLoot())
                    player.addChatMessage(new LiteralText("  Can pick up loot."), false);
                else
                    player.addChatMessage(new LiteralText("  Cannot pick up loot."), false);
            }

            //Display horse stats
            if(entity instanceof HorseBaseEntity) {
                int speed = (int)Math.round(entity.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).getValue() * 50);
                double jump = ((HorseBaseEntity) entity).getJumpStrength();
                jump = (int)((-0.1817584952 * Math.pow(jump, 3) + 3.689713992 * Math.pow(jump, 2) + 2.128599134 * jump - 0.343930367) * 10);
                player.addChatMessage(new LiteralText("  Speed: " + speed + " blocks/sec"), false);
                player.addChatMessage(new LiteralText("  Jump: " + jump / 10f + " blocks"), false);
            }
            
            //Tamable
            if(entity instanceof TameableEntity) {
                if(((TameableEntity) entity).isTamed())
                    player.addChatMessage(new LiteralText("  Tamed"), false);
                else
                    player.addChatMessage(new LiteralText("  Not Tamed."), false);
            }

            //Equipped
            if(entity.getMainHandStack().getItem() != Items.AIR) {
                Text holding = new LiteralText("  Holding: ");
                holding.append(entity.getMainHandStack().toHoverableText());
                player.addChatMessage(holding, false);
            }

            //Check for armor
            if(entity.getArmor() > 0) {
                player.addChatMessage(new LiteralText("  Armor: " + entity.getArmor()), false);
                entity.getArmorItems().forEach(itemStack -> {
                    if(itemStack.getItem() != Items.AIR) {
                        Text armor = new LiteralText("    ");
                        armor.append(itemStack.toHoverableText());
                        player.addChatMessage(armor, false);
                    }
                });
            }
            
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
