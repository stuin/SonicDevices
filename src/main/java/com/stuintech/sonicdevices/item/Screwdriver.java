package com.stuintech.sonicdevices.item;

import com.stuintech.sonicdevices.action.*;
import com.stuintech.sonicdevicesapi.DeviceList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class Screwdriver extends Device {
    private SonicWrench wrench = null;

    //Actual constructor
    public Screwdriver(boolean hidden) {
        super(hidden, "screwdriver");
    }

    @Override
    public int getType() {
        return DeviceList.SCREWDRIVER;
    }

    @Override
    public int[] getTypeList() {
        return new int[] { DeviceList.SCREWDRIVER };
    }

    @Override
    public Item getAlt() {
        if(wrench == null)
            wrench = new SonicWrench(this);
        return wrench;
    }

    @Override
    protected boolean setLevel(PlayerEntity player, Hand hand, ItemStack itemStack, boolean air) {
        boolean changed = super.setLevel(player, hand, itemStack, air);
        if(changed && wrench != null) {
            int level = getLevel(itemStack);
            if(level == 3)
                player.setStackInHand(hand, getAlternate(player, itemStack));
        }
        return changed;
    }

    private ItemStack getAlternate(PlayerEntity player, ItemStack oldStack) {
        ItemStack itemStack = new ItemStack(wrench);
        itemStack.setCount(1);
        itemStack.getOrCreateTag().putInt("level", oldStack.getOrCreateTag().getInt("level"));
        if(oldStack.hasCustomName())
            itemStack.setCustomName(oldStack.getName());
        activate(itemStack, player.world, player);
        return itemStack;
    }

    static {
        DeviceList.allActions[DeviceList.SCREWDRIVER][1].add(new ActivateAction(false));
        DeviceList.allActions[DeviceList.SCREWDRIVER][2].add(new ActivateAction(true));
    }
}
