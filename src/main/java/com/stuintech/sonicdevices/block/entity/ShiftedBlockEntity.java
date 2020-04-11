package com.stuintech.sonicdevices.block.entity;

import com.stuintech.sonicdevices.block.ModBlocks;
import com.stuintech.sonicdevices.block.ShiftedBlock;
import net.minecraft.block.Block;
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
        if(getWorld() != null) {
            if(oldState != null)
                getWorld().setBlockState(getPos(), oldState);
            else
                getWorld().setBlockState(getPos(), Blocks.AIR.getDefaultState());
            markRemoved();
        }
    }

    public void done() {
        if(getWorld() != null)
            getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).with(ShiftedBlock.SETUP, true));
    }

    @Override
    public void fromTag(CompoundTag tag) {
        Block block = Registry.BLOCK.get(Identifier.tryParse(tag.getString("blockID")));
        oldState = block.getDefaultState();
        super.fromTag(tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putString("blockID", Registry.BLOCK.getId(oldState.getBlock()).toString());
        return super.toTag(tag);
    }
}
