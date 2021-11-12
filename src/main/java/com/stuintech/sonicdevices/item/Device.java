package com.stuintech.sonicdevices.item;

import com.stuintech.socketwrench.item.ModeWrenchItem;
import com.stuintech.sonicdevices.ModSounds;
import com.stuintech.sonicdevices.SonicDevices;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class Device extends ModeWrenchItem {
    private final SyncedItemTimer timer = new SyncedItemTimer("onTime", 25);

    public Device(List<Identifier> modes) {
        super(ModItems.SETTINGS, modes);
    }

    @Override
    public void postFasten(ItemStack stack, PlayerEntity player) {
        activate(stack, player.world, player);
    }

    @Override
    public void onModeChange(ItemStack stack, PlayerEntity player, int mode) {
        activate(stack, player.world, player);
    }

    //Run sound and light
    public void activate(ItemStack itemStack, World world, PlayerEntity playerEntity) {
        itemStack.getOrCreateNbt().putInt("on", 1);
        timer.start(itemStack);

        if(!world.isClient)
            world.playSound(null, playerEntity.getBlockPos(), ModSounds.sonicSound,
                    SoundCategory.PLAYERS, 0.6f, getMode(itemStack) * 0.25f + 0.5f);
    }

    @Override
    public UseAction getUseAction(ItemStack itemStack_1) {
        return UseAction.NONE;
    }

    public void appendTooltip(ItemStack itemStack, World world, List<Text> list, TooltipContext tooltipContext) {
        //Get text features
        String color = itemStack.getTranslationKey().replace('.', ',').split(",")[3];
        TranslatableText model = new TranslatableText(itemStack.getTranslationKey().replace(color, "model"));
        color = color.toUpperCase().charAt(0) + color.substring(1);

        //Add text to item
        list.add(new LiteralText(color + " ").append(model));
        list.add(getModeName(itemStack));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int i, boolean bool) {
        //Check for end of timer
        if(itemStack.getOrCreateNbt().getInt("on") == 1) {
            if(timer.tick(itemStack)) {
                itemStack.getOrCreateNbt().putInt("on", 0);
            }
        }
    }

}
