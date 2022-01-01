package com.stuintech.sonicdevices.item;

import com.stuintech.sonicdevices.SonicDevices;
import com.stuintech.sonicdevices.socket.SonicSocketSet;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class ModItems {
    public static final Item.Settings SETTINGS = new Item.Settings().maxCount(1).group(SonicDevices.SONIC_GROUP);
    public static final ArrayList<Device> ALL_DEVICES = new ArrayList<>();

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
    public static final Device[] cane = {
            new HiddenDevice(SonicSocketSet.BASE_MODES_HIDDEN),
            new HiddenDevice(SonicSocketSet.BASE_MODES_HIDDEN),
            new HiddenDevice(SonicSocketSet.ADV_MODES_HIDDEN)};

    public static final Device[] mark1 = initializeDevice();
    public static final Device[] mark5 = initializeDevice();
    public static final Device[] mark7 = initializeDevice();
    public static final Device[] river = initializeDevice();

    public static final Device[] blaster = {
            new Device(SonicSocketSet.BLASTER_MODES),
            new Device(SonicSocketSet.BLASTER_MODES),
            new Device(SonicSocketSet.BLASTER_MODES)};

    private static Device[] initializeDevice() {
        return new Device[]{
                new Device(SonicSocketSet.BASE_MODES),
                new Device(SonicSocketSet.BASE_MODES),
                new Device(SonicSocketSet.ADV_MODES)};
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
        registerDevice("blaster", blaster);
    }

    private static void registerDevice(String name, Device[] items) {
        ALL_DEVICES.add((Device) registerItem(name + "/blue", items[0]));
        ALL_DEVICES.add((Device) registerItem(name + "/green", items[1]));
        ALL_DEVICES.add((Device) registerItem(name + "/red", items[2]));
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, SonicDevices.MODID + ":" + name, item);
    }
}
