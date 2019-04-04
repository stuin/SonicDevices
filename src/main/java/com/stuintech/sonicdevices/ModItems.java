package com.stuintech.sonicdevices;

import com.stuintech.sonicdevices.items.Screwdriver;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class ModItems {
    //Basic items
    public static final Screwdriver mark1;

    //Initialize full list
    static {
        mark1 = new Screwdriver(1);
    }

    //Register items rendering
    public static void register() {
        registerItem("mark1", mark1);
    }

    private static void registerItem(String name, Item item) {
        Registry.register(Registry.ITEM, SonicDevices.MODID + ":" + name, item);
    }
}
