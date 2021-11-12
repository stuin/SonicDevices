package com.stuintech.sonicdevices.item;

import net.minecraft.item.ItemStack;

public class SyncedItemTimer extends SyncedItemData<Integer> {
    private final int max;

    public SyncedItemTimer(String key, int max) {
        super(key, -1);
        this.max = max;
    }

    public boolean tick(ItemStack stack) {
        int time = getData(stack);
        if(time > 0) {
            setData(stack, time - 1);
            return false;
        }

        return true;
    }

    public void start(ItemStack stack) {
        setData(stack, max);
    }
}
