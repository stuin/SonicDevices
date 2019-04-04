package com.stuintech.sonicdevices.items;

import com.stuintech.sonicdevices.SonicDevices;
import net.minecraft.item.Item;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class Screwdriver extends Item {

    private final int version;

    public Screwdriver(int version) {
        super(new Item.Settings().stackSize(1).itemGroup(SonicDevices.SONIC_GROUP));

        //Set screwdriver type
        this.version = version;
    }


}
