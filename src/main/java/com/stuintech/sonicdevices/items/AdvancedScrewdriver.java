package com.stuintech.sonicdevices.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Collection;

public class AdvancedScrewdriver extends Screwdriver {
    public AdvancedScrewdriver(boolean cane) { this(cane, 4); }

    //Actual constructor
    public AdvancedScrewdriver(boolean cane, int maxLevel) {
        super(cane, maxLevel);
    }

    public boolean interact(int level, PlayerEntity player, LivingEntity entity) {
        //Run scan
        if(level == 4 && !player.getEntityWorld().isClient) {
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

    public boolean interact(int level, PlayerEntity player, World world, BlockPos pos, Direction dir) {
        int used = 0;

        //Activate and deactivate
        if((level == 1 || level == 2)) {
            if(super.interact(level, player, world, pos, dir)) {
                used++;
            } else if(super.interact(level, player, world, pos.offset(dir.getOpposite()), dir)){
                used++;
            }
        }

        if(used > 0)
            return true;

        //Scan Block
        if(level == 4 && player.getEntityWorld().isClient) {
            BlockState blockState = world.getBlockState(pos.offset(dir.getOpposite()));
            player.addChatMessage(new TranslatableText(blockState.getBlock().getTranslationKey()), false);
            return true;
        }

        return false;
    }
}
