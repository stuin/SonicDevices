package com.stuintech.sonicdevices.item;

import com.zundrel.wrenchable.wrench.Wrench;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class SonicWrench extends Device implements Wrench {
    private Screwdriver base = null;

    public SonicWrench(Screwdriver base) {
        super(base.hidden, base.hidden ? base.maxLevel - 1 : base.maxLevel, "screwdriver", ModItems.ALT_SETTINGS);
        this.base = base;
    }

    @Override
    protected boolean setLevel(PlayerEntity player, Hand hand, ItemStack itemStack, boolean air) {
        boolean changed = super.setLevel(player, hand, itemStack, air);
        if(changed && base != null) {
            player.setStackInHand(hand, getAlternate(player, itemStack));
        }
        return changed;
    }

    private ItemStack getAlternate(PlayerEntity player, ItemStack oldStack) {
        ItemStack itemStack = new ItemStack(base);
        itemStack.setCount(1);
        itemStack.getOrCreateTag().putInt("level", oldStack.getOrCreateTag().getInt("level"));
        activate(itemStack, player.world, player);
        return itemStack;
    }

    @Override
    public void onBlockWrenched(World world, ItemStack itemStack, PlayerEntity player, Hand hand, BlockHitResult result) {
        activate(itemStack, world, player);
    }

    @Override
    public void onBlockEntityWrenched(World world, ItemStack itemStack, PlayerEntity player, Hand hand, BlockEntity blockEntity, BlockHitResult result) {
        activate(itemStack, world, player);
    }

    @Override
    public void onEntityWrenched(World world, ItemStack itemStack, PlayerEntity player, Hand hand, EntityHitResult result) {
        activate(itemStack, world, player);
    }
}
