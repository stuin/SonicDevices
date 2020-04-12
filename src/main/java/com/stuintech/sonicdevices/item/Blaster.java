package com.stuintech.sonicdevices.item;

import com.stuintech.sonicdevices.action.BlasterShiftAction;

/*
 * Created by Stuart Irwin on 4/9/2020.
 */

public class Blaster extends Device {
    Blaster() {
        super(false, 1, "blaster");

        addAction(1, new BlasterShiftAction());
    }
}
