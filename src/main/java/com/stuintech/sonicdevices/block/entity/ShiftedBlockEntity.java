package com.stuintech.sonicdevices.block.entity;

import com.stuintech.sonicdevices.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.tick.OrderedTick;

import java.util.Map;
import java.util.Optional;

public class ShiftedBlockEntity extends BlockEntity {
    private BlockState oldState;
    private int group = -1;
    private boolean done = false;

    public ShiftedBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, true);
    }

    public ShiftedBlockEntity(BlockPos pos, BlockState state, boolean clear) {
        super(ModBlocks.shiftedEntity, pos, state);
        this.oldState = world.getBlockState(pos);
        if(clear)
            world.setBlockState(pos, ModBlocks.clear.getDefaultState());
        else
            world.setBlockState(pos, ModBlocks.shifted.getDefaultState());
        //world.setBlockEntity(pos, this);
        world.getBlockTickScheduler().scheduleTick(new OrderedTick<>(state.getBlock(), pos, 60, 0));
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
    public void readNbt(NbtCompound tag) {
        oldState = Registry.BLOCK.get(Identifier.tryParse(tag.getString("blockID"))).getDefaultState();
        done = true;
        /*group = tag.getInt("groupID");
        if(group > 0)
            ResetAction.add(pos, group);*/
        for(Property<?> prop : oldState.getEntries().keySet())
            addProp(tag, prop);

        super.readNbt(tag);
    }

    private <T extends Comparable<T>, V extends T> void addProp(NbtCompound tag, Property<T> prop) {
        Optional<T> value = prop.parse(tag.getString(prop.getName()));
        if(value.isPresent() && prop.getType() == value.get().getClass())
            oldState = oldState.with(prop, value.get());
    }



    @Override
    public void writeNbt(NbtCompound tag) {
        if(oldState != null) {
            tag.putString("blockID", Registry.BLOCK.getId(oldState.getBlock()).toString());
            for(Map.Entry<Property<?>, Comparable<?>> prop : oldState.getEntries().entrySet())
                tag.putString(prop.getKey().getName(), prop.getValue().toString());
        }
        tag.putInt("groupID", group);

        super.writeNbt(tag);
    }
}
