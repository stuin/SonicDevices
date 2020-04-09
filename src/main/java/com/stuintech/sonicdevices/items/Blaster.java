package com.stuintech.sonicdevices.items;

import com.stuintech.sonicdevices.actions.blaster.ShiftAction;

/*
 * Created by Stuart Irwin on 4/9/2020.
 */

public class Blaster extends Device {
    Blaster() {
        super(false, 2);

        addAction(1, new ShiftAction());
    }
}
