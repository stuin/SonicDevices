package com.stuintech.sonicdevices.blocks;

import com.stuintech.sonicdevices.SonicDevices;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final Block shifted = new ShiftedBlock(Block.Settings.copy(Blocks.AIR));

    public static void register() {
        Registry.register(Registry.BLOCK, SonicDevices.MODID + ":shifted", shifted);
    }
}
