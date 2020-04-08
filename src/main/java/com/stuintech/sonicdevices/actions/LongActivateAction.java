package com.stuintech.sonicdevices.actions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class LongActivateAction extends ActivateAction {
    public LongActivateAction(boolean deactivate) {
        super(deactivate);
    }

    @Override
    public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
        return super.interact(player, world, pos.offset(dir.getOpposite()), dir);
    }
}
