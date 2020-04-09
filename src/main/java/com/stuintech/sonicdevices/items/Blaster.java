package com.stuintech.sonicdevices.items;

import com.stuintech.sonicdevices.actions.blaster.ShiftAction;

public class Blaster extends Device {
    Blaster() {
        super(false, 2);

        addAction(1, new ShiftAction());
    }
}
