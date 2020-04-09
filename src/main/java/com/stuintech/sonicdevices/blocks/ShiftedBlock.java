package com.stuintech.sonicdevices.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ShiftedBlock extends Block {
    ShiftedBlock(Block.Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        for(int i = 0; i < 4; ++i) {
            double d = (double)pos.getX() + (double)random.nextFloat();
            double e = (double)pos.getY() + (double)random.nextFloat();
            double f = (double)pos.getZ() + (double)random.nextFloat();
            double g = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double h = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double j = ((double)random.nextFloat() - 0.5D) * 0.5D;

            world.addParticle(ParticleTypes.PORTAL, d, e, f, g, h, j);
        }
    }
}
