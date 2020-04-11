package com.stuintech.sonicdevices.action;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
/*
 * Created by Stuart Irwin on 4/8/2020.
 */


public class LongActivateAction extends ActivateAction {
    public LongActivateAction(boolean deactivate) {
        super(deactivate);
    }

    @Override
    public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
        return super.interact(player, world, pos.offset(dir.getOpposite()), dir);
    }
}
