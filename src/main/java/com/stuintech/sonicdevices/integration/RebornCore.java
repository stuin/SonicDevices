package com.stuintech.sonicdevices.integration;

import com.stuintech.sonicdevicesapi.ILoader;
import com.zundrel.wrenchable.wrench.Wrench;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import reborncore.api.ICustomToolHandler;
import reborncore.api.ToolManager;

/*
 * Created by Stuart Irwin on 4/7/2020.
 */

public class RebornCore implements ICustomToolHandler, ILoader {
    public boolean canHandleTool(ItemStack var1) {
        return var1.getItem() instanceof Wrench;
    }

    public boolean handleTool(ItemStack var1, BlockPos var2, World var3, PlayerEntity var4, Direction var5, boolean var6) {
       return var1.getItem() instanceof Wrench;
    }

    public void onInitialize() {
        ToolManager.INSTANCE.customToolHandlerList.add(this);
    }
}
