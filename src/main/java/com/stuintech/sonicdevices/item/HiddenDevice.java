package com.stuintech.sonicdevices.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.List;

public class HiddenDevice extends Device {

    public HiddenDevice(List<Identifier> modes) {
        super(modes);
    }

    public boolean isHidden(ItemStack stack) {
        return getModeName(stack).getKey().contains("hidden");
    }

    @Override
    public void onModeChange(ItemStack stack, PlayerEntity player, int mode) {
        if(!isHidden(stack))
            super.onModeChange(stack, player, mode);
    }
}
