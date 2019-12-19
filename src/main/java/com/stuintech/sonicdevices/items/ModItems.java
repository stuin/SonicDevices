package com.stuintech.sonicdevices.items;

import com.stuintech.sonicdevices.SonicDevices;
import com.stuintech.sonicdevices.items.Device;
import com.stuintech.sonicdevices.items.Screwdriver;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class ModItems {
    public static final Item.Settings SETTINGS = new Item.Settings().maxCount(1).group(SonicDevices.SONIC_GROUP);

    //Device parts
    public static final Item slide_circuit = new Item(SETTINGS);
    public static final Item blue_crystal = new Item(SETTINGS);
    public static final Item green_crystal = new Item(SETTINGS);
    public static final Item red_crystal = new Item(SETTINGS);

    //Device casings
    public static final Item cane_case = new Item(SETTINGS);
    public static final Item mark1_case = new Item(SETTINGS);
    public static final Item mark5_case = new Item(SETTINGS);
    public static final Item mark7_case = new Item(SETTINGS);
    public static final Item river_case = new Item(SETTINGS);

    //Sonic Screwdrivers
    public static final Device[] cane = initializeDevice(true);
    public static final Device[] mark1 = initializeDevice(false);
    public static final Device[] mark5 = initializeDevice(false);
    public static final Device[] mark7 = initializeDevice(false);
    public static final Device[] river = initializeDevice(false);

    private static Device[] initializeDevice(boolean cane) {
        Device[] array = {new Screwdriver(cane), new Screwdriver(cane), new AdvancedScrewdriver(cane)};
        return array;
    }

    //Register items rendering
    public static void register() {
        registerItem("slide_circuit", slide_circuit);
        registerItem("blue_crystal", blue_crystal);
        registerItem("green_crystal", green_crystal);
        registerItem("red_crystal", red_crystal);

        registerItem("cane/casing", cane_case);
        registerItem("mark1/casing", mark1_case);
        registerItem("mark5/casing", mark5_case);
        registerItem("mark7/casing", mark7_case);
        registerItem("river/casing", river_case);

        registerDevice("cane", cane);
        registerDevice("mark1", mark1);
        registerDevice("mark5", mark5);
        registerDevice("mark7", mark7);
        registerDevice("river", river);

    }

    private static void registerDevice(String name, Item[] items) {
        registerItem(name + "/blue", items[0]);
        registerItem(name + "/green", items[1]);
        registerItem(name + "/red", items[2]);
    }

    private static void registerItem(String name, Item item) {
        Registry.register(Registry.ITEM, SonicDevices.MODID + ":" + name, item);
    }
}
