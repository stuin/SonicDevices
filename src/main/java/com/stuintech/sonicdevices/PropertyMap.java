package com.stuintech.sonicdevices;

import java.util.HashMap;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class PropertyMap {
    private static final HashMap<String, String> properties = new HashMap<String, String>() {{
        put("block.minecraft.iron_door", "open");
        put("block.minecraft.iron_trapdoor", "open");
        put("block.minecraft.redstone_lamp", "lit");
        //put("block.minecraft.piston", "extended");
        //put("block.minecraft.sticky_piston", "extended");
        put("block.minecraft.dropper!", "triggered");
        put("block.minecraft.dispenser!", "triggered");
        put("block.minecraft.repeater", "powered");
        put("block.minecraft.comparator", "powered");
        put("block.minecraft.redstone_wire!", "power");
        put("block.minecraft.redstone_torch", "lit");
        put("block.minecraft.tnt", "unstable");
        put("block.minecraft.powered_rail!", "powered");
        put("block.minecraft.activator_rail!", "powered");
        put("block.minecraft.daylight_detector", "power");
        put("block.minecraft.observer!", "powered");
    }};

    private static final String[] exceptions = new String[]{
            "block.minecraft.dropper",
            "block.minecraft.dispenser",
            "block.minecraft.repeater",
            "block.minecraft.comparator",
            "block.minecraft.daylight_detector"
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
}
