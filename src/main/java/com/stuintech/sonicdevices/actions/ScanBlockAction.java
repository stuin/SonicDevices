package com.stuintech.sonicdevices.actions;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ScanBlockAction extends IAction.IBlockAction {
    @Override
    public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
        //Scan Block
        if(player.getEntityWorld().isClient) {
            BlockState blockState = world.getBlockState(pos.offset(dir.getOpposite()));
            player.addChatMessage(new TranslatableText(blockState.getBlock().getTranslationKey()), false);
            return true;
        }
        return false;
    }
}
