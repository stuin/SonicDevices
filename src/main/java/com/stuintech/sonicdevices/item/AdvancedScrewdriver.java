package com.stuintech.sonicdevices.item;

import com.stuintech.sonicdevices.action.LongActivateAction;
import com.stuintech.sonicdevices.action.ScanBlockAction;
import com.stuintech.sonicdevices.action.ScanEntityAction;
import com.stuintech.wrenchsystems.DeviceList;

public class AdvancedScrewdriver extends Screwdriver {

    //Actual constructor
    public AdvancedScrewdriver(boolean hidden) {
        super(hidden);
    }

    @Override
    public int getType() {
        return DeviceList.ADVANCEDSCREWDRIVER;
    }

    @Override
    public int[] getTypeList() {
        return new int[] {DeviceList.SCREWDRIVER, DeviceList.ADVANCEDSCREWDRIVER};
    }
    
    static {
        DeviceList.allActions[DeviceList.ADVANCEDSCREWDRIVER][1].add(new LongActivateAction(false));
        DeviceList.allActions[DeviceList.ADVANCEDSCREWDRIVER][2].add(new LongActivateAction(true));
        DeviceList.allActions[DeviceList.ADVANCEDSCREWDRIVER][4].add(new ScanBlockAction());
        DeviceList.allActions[DeviceList.ADVANCEDSCREWDRIVER][4].add(new ScanEntityAction());
    }
}
