package com.stuintech.sonicdevices.item;

import com.stuintech.sonicdevices.action.BlasterShiftAction;
import com.stuintech.sonicdevicesapi.DeviceList;

/*
 * Created by Stuart Irwin on 4/9/2020.
 */

public class Blaster extends Device {
    Blaster() {
        super(false, "blaster");
    }

    @Override
    public int getType() {
        return DeviceList.BLASTER;
    }

    @Override
    public int[] getTypeList() {
        return new int[] { DeviceList.BLASTER };
    }
    
    static {
        DeviceList.allActions[DeviceList.BLASTER][1].add(new BlasterShiftAction());
    }
}
