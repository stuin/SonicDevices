package com.stuintech.sonicdevices.items;

import com.stuintech.sonicdevices.ModSounds;
import com.stuintech.sonicdevices.PropertyMap;
import com.stuintech.sonicdevices.actions.IAction;
import com.zundrel.wrenchable.wrench.Wrench;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public abstract class Device extends Item {
    private final int MAXTIME = 25;
    private final int maxLevel;
    private final int offset;

    //Timer system
    private static int nextTimer = -1;
    private static ArrayList<Integer> timers = new ArrayList<>();

    //Action list
    protected ArrayList<IAction>[] actions;

    public Device(boolean hidden, int maxLevel) {
        super(ModItems.SETTINGS);

        //Set device stats
        this.maxLevel = hidden ? maxLevel + 1 : maxLevel;
        this.offset = hidden ? 0 : 1;

        //Make action lists for each level
        actions = new ArrayList[this.maxLevel + 1];
        for(int i = 0; i < this.maxLevel; i++) {
            actions[i + 1] = new ArrayList<>();
        }

        //Set animation variables
        this.addPropertyGetter(new Identifier("level"), (itemStack, world, livingEntity) -> itemStack.getOrCreateTag().getInt("level") + 1);
        this.addPropertyGetter(new Identifier("on"), (itemStack, world, livingEntity) -> itemStack.getOrCreateTag().getInt("on"));
    }

    //Run sound and light
    public void activate(ItemStack itemStack, World world, PlayerEntity playerEntity) {
        itemStack.getOrCreateTag().putInt("on", 1);
        if(!world.isClient)
            world.playSound(null, playerEntity.getBlockPos(), ModSounds.sonicSound,
                    SoundCategory.PLAYERS, 0.6f, getLevel(itemStack) * 0.25f + 0.5f);
    }

    @Override
    public UseAction getUseAction(ItemStack itemStack_1) {
        return UseAction.NONE;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack = playerEntity.getStackInHand(hand);

        //Change level
        setLevel(playerEntity, itemStack,true);

        return super.use(world, playerEntity, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        //Get item properties
        ItemStack itemStack = context.getStack();
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        Direction dir = context.getSide();

        //Override sneaking for interactive blocks
        boolean override = false;
        Block block = world.getBlockState(pos).getBlock();
        String blockName = block.getTranslationKey();
        if(PropertyMap.isOverride(blockName) || block instanceof BlockWithEntity)
            override = true;

        //Change level or run
        if(player != null && (override || !setLevel(player, itemStack,false))) {
            //Activate use
            if(interact(getLevel(itemStack), player, context.getWorld(), pos, dir))
                activate(itemStack, world, player);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public boolean useOnEntity(ItemStack itemStack, PlayerEntity playerEntity, LivingEntity livingEntity, Hand hand) {
        //Change level or run
        if(!setLevel(playerEntity, itemStack, false)) {
            //Activate use
            if(interact(getLevel(itemStack), playerEntity, livingEntity)) {
                activate(itemStack, playerEntity.getEntityWorld(), playerEntity);
                return true;
            }
        }
        return false;
    }

    public boolean interact(int level, PlayerEntity player, LivingEntity entity) {
        for(IAction action : actions[level]) {
            if(action.interact(player, entity))
                return true;
        }
        return false;
    }
    public boolean interact(int level, PlayerEntity player, World world, BlockPos pos, Direction dir) {
        for(IAction action : actions[level]) {
            if(action.interact(player, world, pos, dir))
                return true;
        }
        return false;
    }

    public int getLevel(ItemStack itemStack) {
        return itemStack.getOrCreateTag().getInt("level") + offset;
    }

    //Set level of screwdriver
    protected boolean setLevel(PlayerEntity player, ItemStack itemStack, boolean air) {
        if(player.isSneaking() || air) {
            int level = itemStack.getOrCreateTag().getInt("level");

            //Basic level loop
            level++;
            if(level >= maxLevel) {
                level = 0;
            }

            //Update item variables
            itemStack.getOrCreateTag().putInt("level", level);
            itemStack.getOrCreateTag().putInt("on", 0);
            activate(itemStack, player.world, player);
        }

        return player.isSneaking();
    }

    public void appendTooltip(ItemStack itemStack, World world, List<Text> list, TooltipContext tooltipContext) {

        //Get text features
        String[] key = itemStack.getTranslationKey().replace('.', ',').split(",");
        String color = key[3].toUpperCase().substring(0, 1) + key[3].substring(1);
        String model = new TranslatableText(itemStack.getTranslationKey().replace(key[3], "model")).asString();

        //Add text to item
        list.add(new LiteralText(color + " " + model));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int i, boolean bool) {
        //Get countdown clock
        if (itemStack.getOrCreateTag().getInt("on") == 1) {
            int timer = itemStack.getOrCreateTag().getInt("timer") - 1;
            if (timer == -1 || timer >= timers.size()) {
                //Add new timer
                if(nextTimer > -1) {
                    itemStack.getOrCreateTag().putInt("timer", nextTimer + 1);
                    timers.set(nextTimer, MAXTIME);
                    nextTimer = -1;
                } else {
                    //Locate timer location
                    nextTimer = 0;
                    while(nextTimer < timers.size() && timers.get(nextTimer) != -1)
                        nextTimer++;

                    //Set new timer
                    itemStack.getOrCreateTag().putInt("timer", nextTimer);
                    if(nextTimer == timers.size())
                        timers.add(MAXTIME);
                    else
                        timers.set(nextTimer, MAXTIME);
                    nextTimer = -1;
                }
            } else {
                //Check existing timer
                int time = timers.get(timer);
                if (time > 0) {
                    timers.set(timer, time - 1);
                } else {
                    //End timer
                    itemStack.getOrCreateTag().putInt("timer", 0);
                    itemStack.getOrCreateTag().putInt("on", 0);
                    timers.set(timer, -1);
                    nextTimer = timer;
                }
            }
        }
    }
}
