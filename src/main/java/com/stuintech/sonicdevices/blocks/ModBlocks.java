package com.stuintech.sonicdevices.blocks;

import com.stuintech.sonicdevices.SonicDevices;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.registry.Registry;

/*
 * Created by Stuart Irwin on 4/9/2020.
 */

public class ModBlocks {
    public static final Block shifted = new ShiftedBlock(Block.Settings.of(Material.PORTAL).noCollision());
    public static final Block clear = new ShiftedBlock(Block.Settings.of(Material.PORTAL));

    public static void register() {
        Registry.register(Registry.BLOCK, SonicDevices.MODID + ":shifted", shifted);
        Registry.register(Registry.BLOCK, SonicDevices.MODID + ":clear", clear);
    }
}
