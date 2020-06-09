package com.stuintech.sonicdevices.mixin;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class StateMixin extends State<Block, BlockState> implements CodecSupplier {

    protected StateMixin(Block owner, ImmutableMap<Property<?>, Comparable<?>> entries, MapCodec<BlockState> mapCodec) {
        super(owner, entries, mapCodec);
    }

    public MapCodec<BlockState> getCodec() {
        return field_24740;
    }
}
