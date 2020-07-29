package com.stuintech.sonicdevices.integration;

import com.stuintech.sonicdevicesapi.IAction;
import com.stuintech.sonicdevicesapi.DeviceList;
import com.stuintech.sonicdevicesapi.ILoader;
import me.benfah.simpledrawers.api.drawer.blockentity.BlockEntityAbstractDrawer;
import me.benfah.simpledrawers.api.drawer.holder.ItemHolder;
import me.benfah.simpledrawers.utils.BlockUtils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SimpleDrawers extends IAction.IBlockAction implements ILoader {
    private final boolean deactivate;

    public SimpleDrawers() {
        deactivate = false;
    }
    public SimpleDrawers(boolean deactivate) {
        this.deactivate = deactivate;
    }

    public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
        return false;
    }

    @Override
    public boolean interact(PlayerEntity player, World world, BlockPos pos, Vec3d hit, Direction dir) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof BlockEntityAbstractDrawer && !world.isClient) {
            BlockEntityAbstractDrawer drawer = (BlockEntityAbstractDrawer)blockEntity;
            Vec2f interactPos = BlockUtils.getCoordinatesFromHitResult(new BlockHitResult(hit, dir, pos, false));

            ItemHolder itemHolder = drawer.getItemHolderAt(interactPos.x, interactPos.y);
            if(deactivate && itemHolder.isLocked()) {
                itemHolder.setLocked(false);
                return true;
            }
            if(!deactivate && !itemHolder.isLocked() && !itemHolder.isEmpty()) {
                itemHolder.setLocked(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onInitialize() {
        DeviceList.allActions[DeviceList.SCREWDRIVER][1].add(new SimpleDrawers(false));
        DeviceList.allActions[DeviceList.SCREWDRIVER][2].add(new SimpleDrawers(true));
    }
}
