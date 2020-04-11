package com.stuintech.sonicdevices.item;

import com.stuintech.sonicdevices.action.*;
import com.stuintech.sonicdevices.integration.Wrenchable;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public class Screwdriver extends Device {
    public Screwdriver(boolean hidden) { this(hidden, 3); }

    //Actual constructor
    public Screwdriver(boolean hidden, int maxLevel) {
        super(hidden, maxLevel, "screwdriver");

        //Base actions
        actions[1].add(new ActivateAction(false));
        actions[2].add(new ActivateAction(true));
        actions[3].add(new RotateAction());
        actions[3].add(new Wrenchable());
    }
}
