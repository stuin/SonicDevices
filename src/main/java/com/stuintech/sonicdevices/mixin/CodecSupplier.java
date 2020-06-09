package com.stuintech.sonicdevices.mixin;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;

public interface CodecSupplier {
    MapCodec<BlockState> getCodec();
}
