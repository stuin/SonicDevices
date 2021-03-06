package com.stuintech.sonicdevices.item;

import com.stuintech.sonicdevices.ModSounds;
import com.stuintech.sonicdevices.util.PropertyMap;
import com.stuintech.sonicdevices.util.SyncedList;
import com.stuintech.sonicdevicesapi.CancelActionException;
import com.stuintech.sonicdevicesapi.DeviceList;
import com.stuintech.sonicdevicesapi.IAction;
import com.stuintech.sonicdevicesapi.IDevice;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.client.item.ModelPredicateProvider;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public abstract class Device extends Item implements IDevice {
    private final int MAXTIME = 25;
    private final int offset;
    private final String langCode;

    protected final int maxLevel;
    protected final boolean hidden;

    //Included lists
    private static final SyncedList<Integer> timers = new SyncedList<>(0);

    public Device(boolean hidden, String langCode) {
        this(hidden, langCode, ModItems.SETTINGS, -1);
    }

    public Device(boolean hidden, String langCode, Settings settings, int type) {
        super(settings);

        if(type == -1)
            type = getType();

        //Set device stats
        this.maxLevel = hidden ? DeviceList.maxLevel[type] + 1 : DeviceList.maxLevel[type];
        this.offset = hidden ? 0 : 1;
        this.hidden = hidden;
        this.langCode = langCode;

        DeviceList.allDevices[type].add(this);
    }

    //Run sound and light
    public void activate(ItemStack itemStack, World world, PlayerEntity playerEntity) {
        itemStack.getOrCreateTag().putInt("on", 1);
        itemStack.getOrCreateTag().putInt("timer", timers.addNext(MAXTIME));
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
        setLevel(playerEntity, hand, itemStack, true);

        return TypedActionResult.success(itemStack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        //Get item properties
        ItemStack itemStack = context.getStack();
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        Direction dir = context.getSide();
        Vec3d hit = context.getHitPos();

        //Override sneaking for interactive blocks
        boolean override = false;
        Block block = world.getBlockState(pos).getBlock();
        String blockName = block.getTranslationKey();
        if(PropertyMap.isOverride(blockName) || block instanceof BlockWithEntity)
            override = true;

        //Change level or run
        if(player != null && (override || !setLevel(player, hand, itemStack,false))) {
            //Activate use
            if(interact(getLevel(itemStack), player, context.getWorld(), pos, hit, dir)) {
                activate(itemStack, world, player);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.FAIL;
    }

    @Override
    public ActionResult useOnEntity(ItemStack itemStack, PlayerEntity playerEntity, LivingEntity livingEntity, Hand hand) {
        //Change level or run
        if(!setLevel(playerEntity, hand, itemStack, false)) {
            //Activate use
            if(interact(getLevel(itemStack), playerEntity, livingEntity)) {
                activate(itemStack, playerEntity.getEntityWorld(), playerEntity);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.FAIL;
    }

    public boolean interact(int level, PlayerEntity player, LivingEntity entity) {
        try {
            for(int i : getTypeList())
                for(IAction action : DeviceList.allActions[i][level])
                    if(action.interact(player, entity))
                        return true;
        } catch (CancelActionException e) {
            //No action should be taken
        }
        return false;
    }

    public boolean interact(int level, PlayerEntity player, World world, BlockPos pos, Vec3d hit, Direction dir) {
        try {
            for(int i : getTypeList())
                for(IAction action : DeviceList.allActions[i][level])
                    if(action.interact(player, world, pos, hit, dir))
                        return true;
        } catch (CancelActionException e) {
            //No action should be taken
        }
        return false;
    }

    public int getLevel(ItemStack itemStack) {
        return itemStack.getOrCreateTag().getInt("level") + offset;
    }

    //Set level of screwdriver
    protected boolean setLevel(PlayerEntity player, Hand hand, ItemStack itemStack, boolean air) {
        if(player.isSneaking() || air) {
            int level = itemStack.getOrCreateTag().getInt("level");

            //Basic level loop
            level++;
            if(level >= maxLevel) {
                level = 0;
            }

            //Update item variables
            itemStack.getOrCreateTag().putInt("level", level);
            activate(itemStack, player.world, player);
            return true;
        }
        return false;
    }

    public void appendTooltip(ItemStack itemStack, World world, List<Text> list, TooltipContext tooltipContext) {
        //Get text features
        String[] key = itemStack.getTranslationKey().replace('.', ',').split(",");
        if(key[3].equals("alt"))
            key[3] = key[4];
        String color = key[3].toUpperCase().substring(0, 1) + key[3].substring(1);
        String model = new TranslatableText(itemStack.getTranslationKey().replace(key[3], "model")).asString();

        //Add text to item
        list.add(new LiteralText(color + " " + model));
        list.add(new TranslatableText("item.sonicdevices." + langCode + ".mode." + getLevel(itemStack)));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int i, boolean bool) {
        //Get countdown clock
        if(itemStack.getOrCreateTag().getInt("on") == 1) {
            int timer = itemStack.getOrCreateTag().getInt("timer") - 1;
            if(timer == -1 || timer >= timers.size()) {
                itemStack.getOrCreateTag().putInt("timer", timers.addNext(MAXTIME));
            } else {
                //Check existing timer
                int time = timers.get(timer);
                if(time > 0) {
                    timers.set(timer, time - 1);
                } else {
                    //End timer
                    itemStack.getOrCreateTag().putInt("timer", 0);
                    itemStack.getOrCreateTag().putInt("on", 0);
                    timers.clear(timer);
                }
            }
        }
    }

    public Item getAlt() {
        return this;
    }

    public static final ModelPredicateProvider levelPredicate = 
            (itemStack, world, livingEntity) -> itemStack.getOrCreateTag().getInt("level") + 1;
    public static final ModelPredicateProvider onPredicate =
            (itemStack, world, livingEntity) -> itemStack.getOrCreateTag().getInt("on");

}
