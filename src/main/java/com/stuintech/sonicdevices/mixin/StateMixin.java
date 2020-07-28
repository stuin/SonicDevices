package com.stuintech.sonicdevices.mixin;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.state.State;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(State.class)
public interface StateMixin {
    @Accessor("field_24740")
    MapCodec<BlockState> getMapCodec();
}
