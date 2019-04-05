package com.stuintech.sonicdevices;

import com.stuintech.sonicdevices.items.Device;
import com.stuintech.sonicdevices.items.Screwdriver;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class ModItems {
    public static final Item.Settings SETTINGS = new Item.Settings().stackSize(1).itemGroup(SonicDevices.SONIC_GROUP);

    //Device casings
    public static final Item mark1case = new Item(SETTINGS);

    //Sonic Screwdrivers
    public static final Device mark1b = new Screwdriver();
    public static final Device mark1g = new Screwdriver();

    //Register items rendering
    public static void register() {
        registerItem("mark1case", mark1case);
        registerItem("mark1b", mark1b);
        registerItem("mark1g", mark1g);
    }

    private static void registerItem(String name, Item item) {
        Registry.register(Registry.ITEM, SonicDevices.MODID + ":" + name, item);
    }
}
