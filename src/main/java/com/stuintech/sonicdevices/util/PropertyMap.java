package com.stuintech.sonicdevices.util;

import com.stuintech.sonicdevices.block.ShiftedBlock;
import net.minecraft.block.*;

import java.util.HashMap;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class PropertyMap {
    private static final HashMap<String, String> properties = new HashMap<String, String>() {{
        put("block.minecraft.iron_door", "open");
        put("block.minecraft.iron_trapdoor", "open");
        put("block.minecraft.redstone_lamp", "lit");
        put("block.minecraft.repeater", "powered");
        put("block.minecraft.comparator", "powered");
        put("block.minecraft.redstone_torch", "lit");
        put("block.minecraft.redstone_wall_torch", "lit");
        put("block.minecraft.tnt", "unstable");
        put("block.minecraft.daylight_detector", "power");
    }};

    private static final String[] exceptions = new String[] {
            "block.minecraft.repeater",
            "block.minecraft.comparator",
            "block.minecraft.daylight_detector",
            "block.minecraft.loom",
            "block.minecraft.grindstone",
            "block.minecraft.stonecutter",
            "block.minecraft.anvil",
    };

    public static String getCode(String block) {
        return properties.getOrDefault(block, "");
    }

    public static boolean isOverride(String block) {
        for(String s : exceptions) {
            if(block.contains(s))
                return true;
        }
        return false;
    }

    public static boolean canShift(BlockState blockState) {
        Block block = blockState.getBlock();
        return !(blockState.isAir() ||
                block instanceof ShiftedBlock ||
                block instanceof BlockEntityProvider ||
                block instanceof BedrockBlock ||
                block instanceof BarrierBlock ||
                (block instanceof PistonBlock && blockState.get(PistonBlock.EXTENDED)) ||
                block instanceof PistonHeadBlock ||
                block instanceof NetherPortalBlock ||
                block instanceof EndPortalFrameBlock ||
                block == Blocks.OBSIDIAN
        );
    }

}