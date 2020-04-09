package com.stuintech.sonicdevices.extensions;

import com.stuintech.sonicdevices.items.Screwdriver;
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
        if(var1.getItem() instanceof Screwdriver)
            return ((Screwdriver) var1.getItem()).getLevel(var1) == 3;
        return false;
    }

    public boolean handleTool(ItemStack var1, BlockPos var2, World var3, PlayerEntity var4, Direction var5, boolean var6) {
        if(var1.getItem() instanceof Screwdriver) {
            Screwdriver s = ((Screwdriver) var1.getItem());
            if(s.getLevel(var1) == 3) {
                s.activate(var1, var3, var4);
                return true;
            }
        }
        return false;
    }

    public void onInitialize() {
        ToolManager.INSTANCE.customToolHandlerList.add(this);
    }
}
