package com.stuintech.sonicdevices.item;

import com.stuintech.sonicdevices.action.LongActivateAction;
import com.stuintech.sonicdevices.action.ScanBlockAction;
import com.stuintech.sonicdevices.action.ScanEntityAction;

public class AdvancedScrewdriver extends Screwdriver {
    public AdvancedScrewdriver(boolean hidden) { this(hidden, 4); }

    //Actual constructor
    public AdvancedScrewdriver(boolean hidden, int maxLevel) {
        super(hidden, maxLevel);

        //Additional actions
        actions[1].add(new LongActivateAction(false));
        actions[2].add(new LongActivateAction(true));
        actions[4].add(new ScanBlockAction());
        actions[4].add(new ScanEntityAction());
    }
}
