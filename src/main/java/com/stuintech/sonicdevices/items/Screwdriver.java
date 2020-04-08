package com.stuintech.sonicdevices.items;

import com.stuintech.sonicdevices.actions.*;

/*
 * Created by Stuart Irwin on 4/4/2019.
 * Parts copied and adapted from minecraft debug stick and piston
 */

public class Screwdriver extends Device {
    public Screwdriver(boolean hidden) { this(hidden, 3); }

    //Actual constructor
    public Screwdriver(boolean hidden, int maxLevel) {
        super(hidden, maxLevel);
        actions[1].add(new ActivateAction(false));
        actions[2].add(new ActivateAction(true));
        actions[3].add(new RotateAction());
    }


}
