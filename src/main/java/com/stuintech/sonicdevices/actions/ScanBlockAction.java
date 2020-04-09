package com.stuintech.sonicdevices.actions;

import com.stuintech.sonicdevices.blocks.ShiftedState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/*
 * Created by Stuart Irwin on 4/8/2020.
 */

public class ScanBlockAction extends IAction.IBlockAction {
    @Override
    public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
        if(!world.getBlockState(pos).isAir()) {
            world.setBlockState(pos, new ShiftedState(world, pos, true));
            return true;
        }

        //Scan Block
        if(player.getEntityWorld().isClient) {
            BlockState blockState = world.getBlockState(pos.offset(dir.getOpposite()));
            player.addChatMessage(new TranslatableText(blockState.getBlock().getTranslationKey()), false);
            return true;
        }
        return false;
    }
}
