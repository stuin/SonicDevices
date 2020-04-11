package com.stuintech.sonicdevices.block.entity;

import com.stuintech.sonicdevices.action.blaster.ResetAction;
import com.stuintech.sonicdevices.block.ModBlocks;
import com.stuintech.sonicdevices.block.ShiftedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class ShiftedBlockEntity extends BlockEntity {
    private BlockState oldState = null;
    private int group = -1;
    private boolean done = false;

    public ShiftedBlockEntity() {
        super(ModBlocks.shiftedEntity);
    }

    public ShiftedBlockEntity(World world, BlockPos pos, boolean clear) {
        super(ModBlocks.shiftedEntity);
        this.oldState = world.getBlockState(pos);
        if(clear)
            world.setBlockState(pos, ModBlocks.clear.getDefaultState());
        else
            world.setBlockState(pos, ModBlocks.shifted.getDefaultState());
        world.setBlockEntity(pos, this);
    }

    public void restore() {
        if(world != null && done) {
            if(oldState != null)
                world.setBlockState(pos, oldState);
            else
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
            markRemoved();
        }
    }

    public void done(int group) {
        this.group = group;
        this.done = true;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        oldState = Registry.BLOCK.get(Identifier.tryParse(tag.getString("block"))).getDefaultState();
        done = true;
        group = tag.getInt("group");
        if(group > 0)
            ResetAction.add(pos, group);
        super.fromTag(tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        if(oldState != null)
            tag.putString("block", Registry.BLOCK.getId(oldState.getBlock()).toString());
        tag.putInt("group", group);
        return super.toTag(tag);
    }
}
