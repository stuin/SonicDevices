package com.stuintech.sonicdevices.items;

import com.stuintech.sonicdevices.ModSounds;
import com.stuintech.sonicdevices.PropertyMap;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.time.format.TextStyle;
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

    public Device(boolean cane, int maxLevel) {
        super(ModItems.SETTINGS);

        //Set device stats
        this.maxLevel = maxLevel;
        this.offset = cane ? 0 : 1;

        //Set animation variables
        this.addProperty(new Identifier("level"), (itemStack, world, livingEntity) -> itemStack.getOrCreateTag().getInt("level") + 1);
        this.addProperty(new Identifier("on"), (itemStack, world, livingEntity) -> itemStack.getOrCreateTag().getInt("on"));
    }

    //Run sound and light
    private void activate(ItemStack itemStack, World world, PlayerEntity playerEntity) {
        itemStack.getOrCreateTag().putInt("on", 1);
        world.playSoundFromEntity(null, playerEntity, ModSounds.sonicSound, SoundCategory.PLAYERS, 1, 0);
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
            activate(itemStack, world, playerEntity);

            //Activate use
            interact(itemStack.getOrCreateTag().getInt("level") + offset, playerEntity, world);
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
        Direction dir = context.getFacing();

        //Override sneaking for interactive blocks
        boolean override = false;
        String blockName = world.getBlockState(pos).getBlock().getTranslationKey();
        if(PropertyMap.isOverride(blockName))
            override = true;

        //Change level or run
        if(player != null && (override || !setLevel(player, itemStack))) {
            //Enable item
            activate(itemStack, world, player);

            //Activate use
            interact(itemStack.getOrCreateTag().getInt("level") + offset, context.getWorld(), pos, dir);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public boolean interactWithEntity(ItemStack itemStack, PlayerEntity playerEntity, LivingEntity livingEntity, Hand hand) {
        //Change level or run
        if(!setLevel(playerEntity, itemStack)) {
            //Enable item
            activate(itemStack, playerEntity.getEntityWorld(), playerEntity);

            //Activate use
            return interact(itemStack.getOrCreateTag().getInt("level") + offset, playerEntity, livingEntity);
        }
        return false;
    }

    //Set level of screwdriver
    private boolean setLevel(PlayerEntity player, ItemStack itemStack) {
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

    @Override
    public void buildTooltip(ItemStack itemStack, World world, List<TextComponent> list, TooltipContext tooltipContext) {
        super.buildTooltip(itemStack, world, list, tooltipContext);
        System.out.println(itemStack.getTranslationKey());

        //Get text features
        String[] key = itemStack.getTranslationKey().replace('.', ',').split(",");
        String color = key[3].toUpperCase().substring(0, 1) + key[3].substring(1);
        String model = new TranslatableTextComponent(itemStack.getTranslationKey().replace(key[3], "model")).getText();

        //Add text to item
        list.add(new StringTextComponent(color + " " + model));
    }

    @Override
    public void onEntityTick(ItemStack itemStack, World world, Entity entity, int i, boolean bool) {
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

    //Overridable functions for specific device use
    public abstract boolean interact(int level, PlayerEntity player, World world);
    public abstract boolean interact(int level, PlayerEntity player, LivingEntity entity);
    public abstract boolean interact(int level, World world, BlockPos pos, Direction dir);
}
