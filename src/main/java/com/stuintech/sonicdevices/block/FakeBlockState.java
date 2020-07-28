package com.stuintech.sonicdevices.block;

import com.stuintech.sonicdevices.mixin.StateMixin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public abstract class FakeBlockState extends BlockState {
    private final World world;
    private final BlockState oldState;

    FakeBlockState(World world, BlockState old) {
        super(old.getBlock(), old.getEntries(), ((StateMixin)old).getMapCodec());

        this.world = world;
        this.oldState = old;
    }

    @Override
    public <T extends Comparable<T>> T get(Property<T> property) {
        if(oldState == null)
            return (T)property.getValues().toArray()[0];
        return oldState.get(property);
    }

    @Override
    public <T extends Comparable<T>, V extends T> BlockState with(Property<T> property, V value) {
        return oldState.with(property, value);
    }

    //Reset original blockstate
    public void restore(BlockPos blockPos) {
        world.setBlockState(blockPos, oldState);
    }

    @Override
    public BlockRenderType getRenderType() {
        return oldState.getRenderType();
    }

    @Override
    public void neighborUpdate(World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean bl) {
        oldState.neighborUpdate(world, pos, neighborBlock, neighborPos, bl);
    }
}
