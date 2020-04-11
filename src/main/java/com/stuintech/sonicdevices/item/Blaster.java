package com.stuintech.sonicdevices.item;

import com.stuintech.sonicdevices.action.blaster.ResetAction;
import com.stuintech.sonicdevices.action.blaster.ShiftAction;

/*
 * Created by Stuart Irwin on 4/9/2020.
 */

public class Blaster extends Device {
    Blaster() {
        super(false, 2, "blaster");

        addAction(1, new ResetAction());
        addAction(1, new ShiftAction());
    }
}
