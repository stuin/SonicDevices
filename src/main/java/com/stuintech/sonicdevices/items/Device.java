package com.stuintech.sonicdevices.items;

import com.stuintech.sonicdevices.ModItems;
import com.stuintech.sonicdevices.PropertyMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class Device extends Item {
    private final int maxLevel;

    public Device(int maxLevel) {
        super(ModItems.SETTINGS);

        //Set screwdriver type
        this.maxLevel = maxLevel;

        //Set animation variables
        this.addProperty(new Identifier("level"), (itemStack, world, livingEntity) -> itemStack.getOrCreateTag().getInt("level") + 1);
        this.addProperty(new Identifier("on"), (itemStack, world, livingEntity) -> (itemStack.getOrCreateTag().getInt("on") == 1 && livingEntity.isUsingItem()) ? 1 : 0);
    }

    @Override
    public UseAction getUseAction(ItemStack itemStack_1) {
        return UseAction.NONE;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack = playerEntity.getStackInHand(hand);

        //Change level or run
        if(!setLevel(playerEntity, itemStack)) {
            //Enable item
            itemStack.getOrCreateTag().putInt("on", 1);

            //Activate use
            interact(itemStack.getOrCreateTag().getInt("level") + 1, world);
        }

        return super.use(world, playerEntity, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        //Get item properties
        ItemStack itemStack = context.getItemStack();
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();

        //Override sneaking for interactive blocks
        boolean override = false;
        String blockName = world.getBlockState(pos).getBlock().getTranslationKey();
        if(PropertyMap.isOverride(blockName))
            override = true;

        //Change level or run
        if(player != null && (override || !setLevel(player, itemStack))) {
            //Enable item
            itemStack.getOrCreateTag().putInt("on", 1);

            //Activate use
            interact(itemStack.getOrCreateTag().getInt("level") + 1, context.getWorld(), pos);
        }

        return ActionResult.SUCCESS;
    }

    //Set level of screwdriver
    public boolean setLevel(PlayerEntity player, ItemStack itemStack) {
        if(player.isSneaking()) {
            int level = itemStack.getOrCreateTag().getInt("level");

            //Basic level loop
            level++;
            if(level >= maxLevel) {
                level = 0;
            }

            //Update item variables
            itemStack.getOrCreateTag().putInt("level", level);
            itemStack.getOrCreateTag().putInt("on", 0);
        }

        return player.isSneaking();
    }

    //Overridable functions for specific device use
    public void interact(int level, World world) {

    }

    public void interact(int level, World world, BlockPos pos) {

    }
}
