package com.stuintech.sonicdevices.items;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AdvancedScrewdriver extends Screwdriver {
    public AdvancedScrewdriver(boolean cane) { this(cane, 3); }

    //Actual constructor
    public AdvancedScrewdriver(boolean cane, int maxLevel) {
        super(cane, cane ? maxLevel + 1 : maxLevel);
    }

    public boolean interact(int level, World world) {
        return false;
    }

    public boolean interact(int level, World world, BlockPos pos, Direction dir) {
        int used = 0;

        //Activate and deactivate
        if((level == 1 || level == 2)) {
            if(super.interact(level, world, pos, dir)) {
                used++;
            } else if(super.interact(level, world, pos.offset(dir.getOpposite()), dir)){
                used++;
            }
        }

        return used > 0;
    }
}
